package com.me.canteen.service.impl;

import com.me.canteen.common.BusinessException;
import com.me.canteen.common.ErrorCode;
import com.me.canteen.dto.*;
import com.me.canteen.entity.*;
import com.me.canteen.mapper.*;
import com.me.canteen.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private PickupQrCodeMapper pickupQrCodeMapper;

    @Autowired
    private QrScanLogMapper qrScanLogMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private ReservationMapper reservationMapper;

    @Autowired
    private CanteenMapper canteenMapper;

    @Autowired
    private CanteenTimeSlotMapper canteenTimeSlotMapper;

    @Autowired
    private SeatMapper seatMapper;

    @Override
    @Transactional
    public ScanResponseDTO scanQrCode(String qrToken, Long operatorId) {
        // 验证operatorId
        if (operatorId == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "Operator ID is required");
        }

        // 验证操作员是否存在
        User operator = userMapper.findById(operatorId);
        if (operator == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND, "Operator not found");
        }

        PickupQrCode qrCode = pickupQrCodeMapper.findByToken(qrToken);

        if (qrCode == null) {
            throw new BusinessException(ErrorCode.QR_CODE_INVALID);
        }

        if ("USED".equals(qrCode.getQrStatus())) {
            logScan(qrCode.getId(), operatorId, "REPEATED");
            throw new BusinessException(ErrorCode.QR_CODE_USED);
        }

        if (qrCode.getExpiresAt().isBefore(LocalDateTime.now())) {
            logScan(qrCode.getId(), operatorId, "EXPIRED");
            throw new BusinessException(ErrorCode.QR_CODE_EXPIRED);
        }

        // Mark as used
        pickupQrCodeMapper.updateStatus(qrCode.getId(), "USED", LocalDateTime.now());
        logScan(qrCode.getId(), operatorId, "SUCCESS");

        // 更新预约状态为已核销/已到店
        reservationMapper.updateStatus(qrCode.getReservationId(), "CHECKED_IN");
        // 更新订单状态为已完成
        orderMapper.updateOrderStatus(qrCode.getOrderId(), "COMPLETED");

        // Gather details - 添加空值检查防止NPE
        ScanResponseDTO response = new ScanResponseDTO();
        
        User user = userMapper.findById(qrCode.getUserId());
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND, "User not found");
        }

        Order order = orderMapper.findById(qrCode.getOrderId());
        Reservation reservation = reservationMapper.findById(qrCode.getReservationId());

        response.setVerified(true);
        response.setMessage("Scan successful");
        response.setUserId(user.getId());
        response.setUserName(user.getUserName());

        if (order != null) {
            response.setOrderNo(order.getOrderNo());
            response.setTotalAmount(order.getTotalAmount());
            response.setPaidAt(order.getPaidAt());
            response.setItems(orderItemMapper.findByOrderId(order.getId()));
        }

        if (reservation != null) {
            Canteen canteen = canteenMapper.findById(reservation.getCanteenId());
            CanteenTimeSlot timeSlot = canteenTimeSlotMapper.findById(reservation.getTimeSlotId());
            Seat seat = seatMapper.findById(reservation.getSeatId());

            if (canteen != null) {
                response.setCanteenName(canteen.getCanteenName());
            }
            if (timeSlot != null) {
                response.setTimeSlot(timeSlot.getStartTime() + "-" + timeSlot.getEndTime());
            }
            if (seat != null) {
                response.setSeatNo(seat.getSeatNo());
            }
        }

        return response;
    }

    private void logScan(Long qrCodeId, Long operatorId, String result) {
        QrScanLog log = new QrScanLog();
        log.setQrCodeId(qrCodeId);
        log.setOperatorId(operatorId);
        log.setScanResult(result);
        qrScanLogMapper.insert(log);
    }

    @Override
    public List<RealtimeOccupancyDTO> getRealtimeOccupancy(Long canteenId) {
        List<Canteen> canteens = new ArrayList<>();
        if (canteenId != null) {
            canteens.add(canteenMapper.findById(canteenId));
        } else {
            canteens = canteenMapper.findAll();
        }

        List<RealtimeOccupancyDTO> result = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        for (Canteen canteen : canteens) {
            if (canteen == null) continue;
            
            CanteenTimeSlot currentSlot = canteenTimeSlotMapper.findCurrentSlot(canteen.getId(), today, now);
            Long slotId = currentSlot != null ? currentSlot.getId() : null;

            RealtimeOccupancyDTO dto = new RealtimeOccupancyDTO();
            dto.setCanteenId(canteen.getId());
            dto.setCanteenName(canteen.getCanteenName());
            dto.setCapacity(canteen.getCapacity());
            
            // CHECKED_IN = Current dining
            dto.setCurrentDiningCount(reservationMapper.countByStatus(canteen.getId(), slotId, "CHECKED_IN"));
            // RESERVED = Reserved but not checked in
            dto.setReservedCountInCurrentSlot(reservationMapper.countByStatus(canteen.getId(), slotId, "RESERVED"));
            
            int totalOccupied = dto.getCurrentDiningCount() + dto.getReservedCountInCurrentSlot();
            if (canteen.getCapacity() > 0) {
                dto.setOccupancyRate(new BigDecimal(totalOccupied)
                    .divide(new BigDecimal(canteen.getCapacity()), 4, RoundingMode.HALF_UP));
            } else {
                dto.setOccupancyRate(BigDecimal.ZERO);
            }
            
            result.add(dto);
        }
        return result;
    }

    @Override
    public List<DiningStatisticsDTO> getDiningStatistics(Long canteenId, LocalDate startDate, LocalDate endDate) {
        return reservationMapper.statisticsByDateRange(canteenId, startDate, endDate);
    }

    @Override
    public List<FoodSalesStatisticsDTO> getFoodSalesStatistics(Long canteenId, LocalDate startDate, LocalDate endDate) {
        return orderItemMapper.foodSalesByDateRange(canteenId, startDate, endDate);
    }
}

package com.me.canteen.service.impl;

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
        ScanResponseDTO response = new ScanResponseDTO();
        PickupQrCode qrCode = pickupQrCodeMapper.findByToken(qrToken);

        if (qrCode == null) {
            response.setVerified(false);
            response.setMessage("Invalid QR Code");
            return response;
        }

        if ("USED".equals(qrCode.getQrStatus())) {
            logScan(qrCode.getId(), operatorId, "REPEATED");
            response.setVerified(false);
            response.setMessage("QR Code already used");
            return response;
        }

        if (qrCode.getExpiresAt().isBefore(LocalDateTime.now())) {
            logScan(qrCode.getId(), operatorId, "EXPIRED");
            response.setVerified(false);
            response.setMessage("QR Code expired");
            return response;
        }

        // Mark as used
        pickupQrCodeMapper.updateStatus(qrCode.getId(), "USED", LocalDateTime.now());
        logScan(qrCode.getId(), operatorId, "SUCCESS");

        // Gather details
        User user = userMapper.findById(qrCode.getUserId());
        Order order = orderMapper.findById(qrCode.getOrderId());
        Reservation reservation = reservationMapper.findById(qrCode.getReservationId());
        Canteen canteen = canteenMapper.findById(reservation.getCanteenId());
        CanteenTimeSlot timeSlot = canteenTimeSlotMapper.findById(reservation.getTimeSlotId());
        Seat seat = seatMapper.findById(reservation.getSeatId());

        response.setVerified(true);
        response.setMessage("Scan successful");
        response.setUserId(user.getId());
        response.setUserName(user.getUserName());
        response.setOrderNo(order.getOrderNo());
        response.setTotalAmount(order.getTotalAmount());
        response.setPaidAt(order.getPaidAt());
        response.setItems(orderItemMapper.findByOrderId(order.getId()));
        
        response.setCanteenName(canteen.getCanteenName());
        response.setTimeSlot(timeSlot.getStartTime() + "-" + timeSlot.getEndTime());
        response.setSeatNo(seat.getSeatNo());

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

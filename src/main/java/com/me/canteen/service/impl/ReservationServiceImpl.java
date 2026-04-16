package com.me.canteen.service.impl;

import com.me.canteen.entity.Order;
import com.me.canteen.entity.Reservation;
import com.me.canteen.mapper.CanteenTimeSlotMapper;
import com.me.canteen.mapper.OrderMapper;
import com.me.canteen.mapper.ReservationMapper;
import com.me.canteen.mapper.SeatMapper;
import com.me.canteen.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationMapper reservationMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private CanteenTimeSlotMapper canteenTimeSlotMapper;

    @Autowired
    private SeatMapper seatMapper;

    @Override
    @Transactional
    public Reservation reserve(Long userId, Long orderId, Long canteenId, Long timeSlotId, Long seatId) {
        // 验证订单是否存在且为已支付状态
        Order order = orderMapper.findById(orderId);
        if (order == null) {
            throw new RuntimeException("Order not found");
        }
        if (!"PAID".equals(order.getOrderStatus())) {
            throw new RuntimeException("Only paid orders can be reserved");
        }

        // 验证订单归属权
        if (!userId.equals(order.getUserId())) {
            throw new RuntimeException("Order does not belong to current user");
        }

        // 验证时段是否存在
        if (canteenTimeSlotMapper.findById(timeSlotId) == null) {
            throw new RuntimeException("Time slot not found");
        }

        // 验证座位是否存在
        if (seatMapper.findById(seatId) == null) {
            throw new RuntimeException("Seat not found");
        }

        // 检查座位是否已被预约（同一时段同一座位不能重复）
        Reservation existing = reservationMapper.findBySlotAndSeat(timeSlotId, seatId);
        if (existing != null) {
            throw new RuntimeException("Seat already reserved for this time slot");
        }

        // 检查用户是否已在此时段有预约（约束：同一用户同一时段只能有一个预约）
        Reservation userExistingReservation = reservationMapper.findByUserAndSlot(userId, timeSlotId);
        if (userExistingReservation != null) {
            throw new RuntimeException("You already have a reservation for this time slot");
        }

        Reservation reservation = new Reservation();
        reservation.setReservationCode("RES" + System.currentTimeMillis());
        reservation.setUserId(userId);
        reservation.setOrderId(orderId);
        reservation.setCanteenId(canteenId);
        reservation.setTimeSlotId(timeSlotId);
        reservation.setSeatId(seatId);
        reservation.setReservationStatus("RESERVED");

        reservationMapper.insert(reservation);
        return reservation;
    }
}

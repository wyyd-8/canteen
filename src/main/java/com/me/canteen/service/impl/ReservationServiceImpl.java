package com.me.canteen.service.impl;

import com.me.canteen.entity.Order;
import com.me.canteen.entity.Reservation;
import com.me.canteen.mapper.OrderMapper;
import com.me.canteen.mapper.ReservationMapper;
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

    @Override
    @Transactional
    public Reservation reserve(Long userId, Long orderId, Long canteenId, Long timeSlotId, Long seatId) {
        Order order = orderMapper.findById(orderId);
        if (order == null || !"PAID".equals(order.getOrderStatus())) {
            throw new RuntimeException("Only paid orders can be reserved");
        }

        Reservation existing = reservationMapper.findBySlotAndSeat(timeSlotId, seatId);
        if (existing != null) {
            throw new RuntimeException("Seat already reserved for this time slot");
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

package com.me.canteen.service;

import com.me.canteen.entity.Reservation;

public interface ReservationService {
    Reservation reserve(Long userId, Long orderId, Long canteenId, Long timeSlotId, Long seatId);
}

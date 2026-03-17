package com.me.canteen.controller;

import com.me.canteen.common.Result;
import com.me.canteen.context.BaseContext;
import com.me.canteen.dto.ReservationRequest;
import com.me.canteen.entity.Reservation;
import com.me.canteen.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user/reservations")
public class UserReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping
    public Result<Reservation> reserve(@RequestBody ReservationRequest request) {
        Reservation reservation = reservationService.reserve(
            BaseContext.getCurrentId(), request.getOrderId(), request.getCanteenId(), request.getTimeSlotId(), request.getSeatId());
        return Result.success(reservation);
    }
}

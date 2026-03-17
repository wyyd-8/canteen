package com.me.canteen.dto;

import lombok.Data;

@Data
public class ReservationRequest {
    private Long userId;
    private Long orderId;
    private Long canteenId;
    private Long timeSlotId;
    private Long seatId;
}

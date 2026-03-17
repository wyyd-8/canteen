package com.me.canteen.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Reservation {
    private Long id;
    private String reservationCode;
    private Long userId;
    private Long orderId;
    private Long canteenId;
    private Long timeSlotId;
    private Long seatId;
    private String reservationStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

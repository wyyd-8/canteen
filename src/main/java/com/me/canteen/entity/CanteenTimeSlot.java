package com.me.canteen.entity;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Data
public class CanteenTimeSlot {
    private Long id;
    private Long canteenId;
    private LocalDate slotDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer reservationLimit;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

package com.me.canteen.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Seat {
    private Long id;
    private Long canteenId;
    private String seatNo;
    private String seatArea;
    private Integer isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

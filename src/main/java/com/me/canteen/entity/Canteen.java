package com.me.canteen.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Canteen {
    private Long id;
    private String canteenCode;
    private String canteenName;
    private Integer capacity;
    private Integer isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

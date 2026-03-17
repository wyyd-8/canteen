package com.me.canteen.dto;

import lombok.Data;

@Data
public class CanteenResponseDTO {
    private Long canteenId;
    private String canteenName;
    private Integer currentDiningCount;
    private Integer reservedCount;
    private Integer capacity;
}

package com.me.canteen.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class RealtimeOccupancyDTO {
    private Long canteenId;
    private Integer currentDiningCount;
    private Integer reservedCountInCurrentSlot;
    private Integer capacity;
    private BigDecimal occupancyRate;
}

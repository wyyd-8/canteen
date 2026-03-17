package com.me.canteen.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class FoodSalesStatisticsDTO {
    private Long foodId;
    private String foodName;
    private Integer soldQuantity;
    private BigDecimal soldAmount;
}

package com.me.canteen.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class FoodItem {
    private Long id;
    private Long canteenId;
    private String foodName;
    private String category;
    private BigDecimal price;
    private Integer stock;
    private Integer isOnSale;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

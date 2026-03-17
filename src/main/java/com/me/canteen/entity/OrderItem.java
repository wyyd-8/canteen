package com.me.canteen.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderItem {
    private Long id;
    private Long orderId;
    private Long foodId;
    private String foodNameSnapshot;
    private BigDecimal unitPriceSnapshot;
    private Integer quantity;
    private BigDecimal subtotalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

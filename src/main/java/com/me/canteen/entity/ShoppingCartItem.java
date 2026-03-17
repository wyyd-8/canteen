package com.me.canteen.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ShoppingCartItem {
    private Long id;
    private Long userId;
    private Long foodId;
    private Integer quantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

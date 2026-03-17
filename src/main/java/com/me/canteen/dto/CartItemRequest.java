package com.me.canteen.dto;

import lombok.Data;

@Data
public class CartItemRequest {
    private Long userId;
    private Long foodId;
    private Integer quantity;
}

package com.me.canteen.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class CartDTO {
    private Long userId;
    private List<CartItemDTO> items;
    private BigDecimal totalAmount;

    @Data
    public static class CartItemDTO {
        private Long foodId;
        private String foodName;
        private BigDecimal price;
        private Integer quantity;
        private BigDecimal subtotalAmount;
    }
}

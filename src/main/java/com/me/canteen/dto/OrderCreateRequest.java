package com.me.canteen.dto;

import lombok.Data;

@Data
public class OrderCreateRequest {
    private Long userId;
    private Long canteenId;
    private String paymentMethod;
}

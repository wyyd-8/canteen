package com.me.canteen.dto;

import lombok.Data;

@Data
public class QrcodeRequest {
    private Long userId;
    private Long orderId;
    private Long reservationId;
}

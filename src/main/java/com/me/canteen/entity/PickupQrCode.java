package com.me.canteen.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PickupQrCode {
    private Long id;
    private String qrToken;
    private Long userId;
    private Long orderId;
    private Long reservationId;
    private String qrStatus;
    private LocalDateTime expiresAt;
    private LocalDateTime usedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

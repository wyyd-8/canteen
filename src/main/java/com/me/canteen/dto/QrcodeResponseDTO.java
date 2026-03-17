package com.me.canteen.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class QrcodeResponseDTO {
    private Long id;
    private String qrToken;
    private LocalDateTime expiresAt;
    private String qrImageUrl;
}

package com.me.canteen.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class QrScanLog {
    private Long id;
    private Long qrCodeId;
    private Long operatorId;
    private String scanResult;
    private LocalDateTime createdAt;
}

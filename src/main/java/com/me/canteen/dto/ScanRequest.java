package com.me.canteen.dto;

import lombok.Data;

@Data
public class ScanRequest {
    private String qrToken;
    private Long operatorId;
}

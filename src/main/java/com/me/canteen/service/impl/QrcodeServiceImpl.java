package com.me.canteen.service.impl;

import com.me.canteen.dto.QrcodeResponseDTO;
import com.me.canteen.entity.PickupQrCode;
import com.me.canteen.mapper.PickupQrCodeMapper;
import com.me.canteen.service.QrcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class QrcodeServiceImpl implements QrcodeService {

    @Autowired
    private PickupQrCodeMapper pickupQrCodeMapper;

    @Override
    public QrcodeResponseDTO generate(Long userId, Long orderId, Long reservationId) {
        String token = UUID.randomUUID().toString().replace("-", "");
        
        PickupQrCode qrCode = new PickupQrCode();
        qrCode.setQrToken(token);
        qrCode.setUserId(userId);
        qrCode.setOrderId(orderId);
        qrCode.setReservationId(reservationId);
        qrCode.setQrStatus("ACTIVE");
        qrCode.setExpiresAt(LocalDateTime.now().plusHours(1));
        
        pickupQrCodeMapper.insert(qrCode);
        
        QrcodeResponseDTO response = new QrcodeResponseDTO();
        response.setId(qrCode.getId());
        response.setQrToken(token);
        response.setExpiresAt(qrCode.getExpiresAt());
        // 占位图片 URL
        response.setQrImageUrl("https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=" + token);
        
        return response;
    }
}

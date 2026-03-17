package com.me.canteen.service;

import com.me.canteen.dto.QrcodeResponseDTO;

public interface QrcodeService {
    QrcodeResponseDTO generate(Long userId, Long orderId, Long reservationId);
}

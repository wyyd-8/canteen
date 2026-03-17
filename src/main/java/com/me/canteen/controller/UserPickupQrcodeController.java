package com.me.canteen.controller;

import com.me.canteen.common.Result;
import com.me.canteen.context.BaseContext;
import com.me.canteen.dto.QrcodeRequest;
import com.me.canteen.dto.QrcodeResponseDTO;
import com.me.canteen.service.QrcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user/pickup-qrcodes")
public class UserPickupQrcodeController {

    @Autowired
    private QrcodeService qrcodeService;

    @PostMapping
    public Result<QrcodeResponseDTO> generate(@RequestBody QrcodeRequest request) {
        QrcodeResponseDTO response = qrcodeService.generate(BaseContext.getCurrentId(), request.getOrderId(), request.getReservationId());
        return Result.success(response);
    }
}

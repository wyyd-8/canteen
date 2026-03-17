package com.me.canteen.controller;

import com.me.canteen.common.Result;
import com.me.canteen.dto.ScanRequest;
import com.me.canteen.dto.ScanResponseDTO;
import com.me.canteen.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/pickup-qrcodes")
public class AdminPickupQrcodeController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/scan")
    public Result<ScanResponseDTO> scan(@RequestBody ScanRequest request) {
        return Result.success(adminService.scanQrCode(request.getQrToken(), request.getOperatorId()));
    }
}

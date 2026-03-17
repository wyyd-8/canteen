package com.me.canteen.controller;

import com.me.canteen.common.Result;
import com.me.canteen.dto.RealtimeOccupancyDTO;
import com.me.canteen.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/canteens")
public class AdminDashboardController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/realtime-occupancy")
    public Result<List<RealtimeOccupancyDTO>> getRealtimeOccupancy(@RequestParam(required = false) Long canteenId) {
        return Result.success(adminService.getRealtimeOccupancy(canteenId));
    }
}

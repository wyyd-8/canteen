package com.me.canteen.controller;

import com.me.canteen.common.Result;
import com.me.canteen.dto.DiningStatisticsDTO;
import com.me.canteen.dto.FoodSalesStatisticsDTO;
import com.me.canteen.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/statistics")
public class AdminStatisticsController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/dining")
    public Result<List<DiningStatisticsDTO>> getDiningStatistics(
            @RequestParam(required = false) Long canteenId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return Result.success(adminService.getDiningStatistics(canteenId, startDate, endDate));
    }

    @GetMapping("/food-sales")
    public Result<List<FoodSalesStatisticsDTO>> getFoodSalesStatistics(
            @RequestParam(required = false) Long canteenId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return Result.success(adminService.getFoodSalesStatistics(canteenId, startDate, endDate));
    }
}

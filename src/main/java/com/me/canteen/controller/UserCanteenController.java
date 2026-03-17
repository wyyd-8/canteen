package com.me.canteen.controller;

import com.me.canteen.common.Result;
import com.me.canteen.dto.CanteenResponseDTO;
import com.me.canteen.entity.FoodItem;
import com.me.canteen.service.CanteenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user/canteens")
public class UserCanteenController {

    @Autowired
    private CanteenService canteenService;

    @GetMapping
    public Result<List<CanteenResponseDTO>> listCanteens(@RequestParam(required = false) Long timeSlotId) {
        return Result.success(canteenService.listCanteens(timeSlotId));
    }

    @GetMapping("/{canteenId}/foods")
    public Result<List<FoodItem>> listFoods(
            @PathVariable Long canteenId,
            @RequestParam(defaultValue = "true") boolean onSaleOnly) {
        return Result.success(canteenService.listFoods(canteenId, onSaleOnly));
    }
}

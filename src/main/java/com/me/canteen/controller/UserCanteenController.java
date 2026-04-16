package com.me.canteen.controller;

import com.me.canteen.common.Result;
import com.me.canteen.dto.CanteenResponseDTO;
import com.me.canteen.entity.CanteenTimeSlot;
import com.me.canteen.entity.FoodItem;
import com.me.canteen.entity.Seat;
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

    @GetMapping("/{canteenId}/time-slots")
    public Result<List<CanteenTimeSlot>> listTimeSlots(@PathVariable Long canteenId) {
        return Result.success(canteenService.listTimeSlots(canteenId));
    }

    @GetMapping("/{canteenId}/available-seats")
    public Result<List<Seat>> listAvailableSeats(
            @PathVariable Long canteenId,
            @RequestParam Long timeSlotId) {
        return Result.success(canteenService.listAvailableSeats(canteenId, timeSlotId));
    }
}

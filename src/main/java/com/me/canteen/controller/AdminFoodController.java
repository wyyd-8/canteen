package com.me.canteen.controller;

import com.me.canteen.common.Result;
import com.me.canteen.entity.FoodItem;
import com.me.canteen.service.CanteenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/canteens/{canteenId}/foods")
public class AdminFoodController {

    @Autowired
    private CanteenService canteenService;

    @GetMapping
    public Result<List<FoodItem>> listFoods(
            @PathVariable Long canteenId,
            @RequestParam(defaultValue = "false") boolean onSaleOnly) {
        return Result.success(canteenService.listFoods(canteenId, onSaleOnly));
    }

    @PostMapping
    public Result<Void> addFood(@PathVariable Long canteenId, @RequestBody FoodItem food) {
        food.setCanteenId(canteenId);
        canteenService.addFood(food);
        return Result.success();
    }

    @PutMapping("/{foodId}")
    public Result<Void> updateFood(@PathVariable Long foodId, @RequestBody FoodItem food) {
        food.setId(foodId);
        canteenService.updateFood(food);
        return Result.success();
    }

    @DeleteMapping("/{foodId}")
    public Result<Void> deleteFood(@PathVariable Long foodId) {
        canteenService.deleteFood(foodId);
        return Result.success();
    }
}

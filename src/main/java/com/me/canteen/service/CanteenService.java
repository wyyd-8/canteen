package com.me.canteen.service;

import com.me.canteen.dto.CanteenResponseDTO;
import com.me.canteen.entity.Canteen;
import com.me.canteen.entity.FoodItem;
import java.util.List;

public interface CanteenService {
    List<CanteenResponseDTO> listCanteens(Long timeSlotId);
    List<FoodItem> listFoods(Long canteenId, boolean onSaleOnly);
    void addFood(FoodItem food);
    void updateFood(FoodItem food);
    void deleteFood(Long foodId);
}

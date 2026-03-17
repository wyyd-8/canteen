package com.me.canteen.service.impl;

import com.me.canteen.dto.CanteenResponseDTO;
import com.me.canteen.entity.Canteen;
import com.me.canteen.entity.FoodItem;
import com.me.canteen.mapper.CanteenMapper;
import com.me.canteen.mapper.FoodItemMapper;
import com.me.canteen.mapper.ReservationMapper;
import com.me.canteen.service.CanteenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class CanteenServiceImpl implements CanteenService {

    @Autowired
    private CanteenMapper canteenMapper;

    @Autowired
    private FoodItemMapper foodItemMapper;

    @Autowired
    private ReservationMapper reservationMapper;

    @Override
    public List<CanteenResponseDTO> listCanteens(Long timeSlotId) {
        List<Canteen> canteens = canteenMapper.findAll();
        List<CanteenResponseDTO> dtos = new ArrayList<>();
        for (Canteen canteen : canteens) {
            CanteenResponseDTO dto = new CanteenResponseDTO();
            dto.setCanteenId(canteen.getId());
            dto.setCanteenName(canteen.getCanteenName());
            dto.setCapacity(canteen.getCapacity());
            // TODO: currentDiningCount can be calculated if we have current slot or real-time tracking
            // For now, use reservedCount as a proxy or just zero
            dto.setCurrentDiningCount(0); 
            dto.setReservedCount(reservationMapper.countReserved(canteen.getId(), timeSlotId));
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public List<FoodItem> listFoods(Long canteenId, boolean onSaleOnly) {
        return foodItemMapper.findByCanteenId(canteenId, onSaleOnly);
    }

    @Override
    public void addFood(FoodItem food) {
        foodItemMapper.insert(food);
    }

    @Override
    public void updateFood(FoodItem food) {
        foodItemMapper.update(food);
    }

    @Override
    public void deleteFood(Long foodId) {
        foodItemMapper.delete(foodId);
    }
}

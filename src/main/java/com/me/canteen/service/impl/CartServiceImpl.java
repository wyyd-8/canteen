package com.me.canteen.service.impl;

import com.me.canteen.dto.CartDTO;
import com.me.canteen.entity.FoodItem;
import com.me.canteen.entity.ShoppingCartItem;
import com.me.canteen.mapper.FoodItemMapper;
import com.me.canteen.mapper.ShoppingCartMapper;
import com.me.canteen.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private FoodItemMapper foodItemMapper;

    @Override
    public CartDTO getCart(Long userId) {
        List<ShoppingCartItem> cartItems = shoppingCartMapper.findByUserId(userId);
        CartDTO cartDTO = new CartDTO();
        cartDTO.setUserId(userId);
        
        List<CartDTO.CartItemDTO> itemDTOs = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (ShoppingCartItem item : cartItems) {
            FoodItem food = foodItemMapper.findById(item.getFoodId());
            if (food != null) {
                CartDTO.CartItemDTO itemDTO = new CartDTO.CartItemDTO();
                itemDTO.setFoodId(food.getId());
                itemDTO.setFoodName(food.getFoodName());
                itemDTO.setPrice(food.getPrice());
                itemDTO.setQuantity(item.getQuantity());
                
                BigDecimal subtotal = food.getPrice().multiply(new BigDecimal(item.getQuantity()));
                itemDTO.setSubtotalAmount(subtotal);
                
                itemDTOs.add(itemDTO);
                totalAmount = totalAmount.add(subtotal);
            }
        }
        
        cartDTO.setItems(itemDTOs);
        cartDTO.setTotalAmount(totalAmount);
        return cartDTO;
    }

    @Override
    public void addItem(Long userId, Long foodId, Integer quantity) {
        ShoppingCartItem existing = shoppingCartMapper.findByUserIdAndFoodId(userId, foodId);
        if (existing != null) {
            shoppingCartMapper.updateQuantity(userId, foodId, existing.getQuantity() + quantity);
        } else {
            ShoppingCartItem item = new ShoppingCartItem();
            item.setUserId(userId);
            item.setFoodId(foodId);
            item.setQuantity(quantity);
            shoppingCartMapper.insert(item);
        }
    }

    @Override
    public void updateItem(Long userId, Long foodId, Integer quantity) {
        shoppingCartMapper.updateQuantity(userId, foodId, quantity);
    }

    @Override
    public void removeItem(Long userId, Long foodId) {
        shoppingCartMapper.deleteByUserIdAndFoodId(userId, foodId);
    }
}

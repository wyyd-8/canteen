package com.me.canteen.service;

import com.me.canteen.dto.CartDTO;

public interface CartService {
    CartDTO getCart(Long userId);
    void addItem(Long userId, Long foodId, Integer quantity);
    void updateItem(Long userId, Long foodId, Integer quantity);
    void removeItem(Long userId, Long foodId);
}

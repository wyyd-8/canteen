package com.me.canteen.service;

import com.me.canteen.dto.OrderDetailsDTO;
import com.me.canteen.entity.Order;

public interface OrderService {
    Order createOrder(Long userId, Long canteenId, String paymentMethod);
    void handlePayment(Long orderId);
    OrderDetailsDTO getOrderDetails(Long orderId);
}

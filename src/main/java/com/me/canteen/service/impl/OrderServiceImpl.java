package com.me.canteen.service.impl;

import com.me.canteen.dto.OrderDetailsDTO;
import com.me.canteen.entity.FoodItem;
import com.me.canteen.entity.Order;
import com.me.canteen.entity.OrderItem;
import com.me.canteen.entity.ShoppingCartItem;
import com.me.canteen.mapper.FoodItemMapper;
import com.me.canteen.mapper.OrderItemMapper;
import com.me.canteen.mapper.OrderMapper;
import com.me.canteen.mapper.ShoppingCartMapper;
import com.me.canteen.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private FoodItemMapper foodItemMapper;

    @Override
    @Transactional
    public Order createOrder(Long userId, Long canteenId, String paymentMethod) {
        List<ShoppingCartItem> cartItems = shoppingCartMapper.findByUserId(userId);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setOrderNo("ORD" + System.currentTimeMillis());
        order.setUserId(userId);
        order.setCanteenId(canteenId);
        order.setPaymentMethod(paymentMethod);
        order.setOrderStatus("CREATED");
        
        BigDecimal totalAmount = BigDecimal.ZERO;
        // Pre-calculate total and validate foods
        for (ShoppingCartItem cartItem : cartItems) {
            FoodItem food = foodItemMapper.findById(cartItem.getFoodId());
            if (food != null) {
                totalAmount = totalAmount.add(food.getPrice().multiply(new BigDecimal(cartItem.getQuantity())));
            }
        }
        order.setTotalAmount(totalAmount);
        
        orderMapper.insert(order);

        for (ShoppingCartItem cartItem : cartItems) {
            FoodItem food = foodItemMapper.findById(cartItem.getFoodId());
            if (food != null) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrderId(order.getId());
                orderItem.setFoodId(food.getId());
                orderItem.setFoodNameSnapshot(food.getFoodName());
                orderItem.setUnitPriceSnapshot(food.getPrice());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setSubtotalAmount(food.getPrice().multiply(new BigDecimal(cartItem.getQuantity())));
                orderItemMapper.insert(orderItem);
            }
        }

        shoppingCartMapper.clearByUserId(userId);
        return order;
    }

    @Override
    public void handlePayment(Long orderId) {
        orderMapper.updateStatus(orderId, "PAID", LocalDateTime.now());
    }

    @Override
    public OrderDetailsDTO getOrderDetails(Long orderId) {
        Order order = orderMapper.findById(orderId);
        if (order == null) return null;
        
        List<OrderItem> items = orderItemMapper.findByOrderId(orderId);
        OrderDetailsDTO dto = new OrderDetailsDTO();
        dto.setOrder(order);
        dto.setItems(items);
        return dto;
    }
}

package com.me.canteen.controller;

import com.me.canteen.common.Result;
import com.me.canteen.context.BaseContext;
import com.me.canteen.dto.OrderCreateRequest;
import com.me.canteen.dto.OrderDetailsDTO;
import com.me.canteen.dto.PaymentCallbackRequest;
import com.me.canteen.entity.Order;
import com.me.canteen.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user/orders")
public class UserOrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public Result<Order> createOrder(@RequestBody OrderCreateRequest request) {
        Order order = orderService.createOrder(BaseContext.getCurrentId(), request.getCanteenId(), request.getPaymentMethod());
        return Result.success(order);
    }

    @PostMapping("/payment-callback")
    public Result<Void> paymentCallback(@RequestBody PaymentCallbackRequest request) {
        orderService.handlePayment(request.getOrderId());
        return Result.success();
    }

    @GetMapping("/{orderId}")
    public Result<OrderDetailsDTO> getOrderDetails(@PathVariable Long orderId) {
        return Result.success(orderService.getOrderDetails(orderId));
    }
}

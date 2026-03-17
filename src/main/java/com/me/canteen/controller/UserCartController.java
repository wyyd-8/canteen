package com.me.canteen.controller;

import com.me.canteen.common.Result;
import com.me.canteen.context.BaseContext;
import com.me.canteen.dto.CartDTO;
import com.me.canteen.dto.CartItemRequest;
import com.me.canteen.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user/cart")
public class UserCartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public Result<CartDTO> getCart() {
        return Result.success(cartService.getCart(BaseContext.getCurrentId()));
    }

    @PostMapping("/items")
    public Result<Void> addItem(@RequestBody CartItemRequest request) {
        cartService.addItem(BaseContext.getCurrentId(), request.getFoodId(), request.getQuantity());
        return Result.success();
    }

    @PutMapping("/items/{foodId}")
    public Result<Void> updateItem(@PathVariable Long foodId, @RequestBody CartItemRequest request) {
        cartService.updateItem(BaseContext.getCurrentId(), foodId, request.getQuantity());
        return Result.success();
    }

    @DeleteMapping("/items/{foodId}")
    public Result<Void> removeItem(@PathVariable Long foodId) {
        cartService.removeItem(BaseContext.getCurrentId(), foodId);
        return Result.success();
    }
}

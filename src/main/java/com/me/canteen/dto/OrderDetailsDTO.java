package com.me.canteen.dto;

import com.me.canteen.entity.Order;
import com.me.canteen.entity.OrderItem;
import lombok.Data;
import java.util.List;

@Data
public class OrderDetailsDTO {
    private Order order;
    private List<OrderItem> items;
}

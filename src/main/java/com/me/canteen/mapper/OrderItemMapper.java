package com.me.canteen.mapper;

import com.me.canteen.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface OrderItemMapper {
    int insert(OrderItem item);
    List<OrderItem> findByOrderId(@Param("orderId") Long orderId);
    List<com.me.canteen.dto.FoodSalesStatisticsDTO> foodSalesByDateRange(@Param("canteenId") Long canteenId, @Param("startDate") java.time.LocalDate startDate, @Param("endDate") java.time.LocalDate endDate);
}

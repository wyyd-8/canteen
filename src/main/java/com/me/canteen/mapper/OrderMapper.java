package com.me.canteen.mapper;

import com.me.canteen.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderMapper {
    int insert(Order order);
    int updateStatus(@Param("id") Long id, @Param("status") String status, @Param("paidAt") java.time.LocalDateTime paidAt);
    Order findById(@Param("id") Long id);
}

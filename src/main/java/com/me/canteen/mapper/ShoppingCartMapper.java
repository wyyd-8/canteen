package com.me.canteen.mapper;

import com.me.canteen.entity.ShoppingCartItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    int insert(ShoppingCartItem item);
    int updateQuantity(@Param("userId") Long userId, @Param("foodId") Long foodId, @Param("quantity") Integer quantity);
    int deleteByUserIdAndFoodId(@Param("userId") Long userId, @Param("foodId") Long foodId);
    List<ShoppingCartItem> findByUserId(@Param("userId") Long userId);
    ShoppingCartItem findByUserIdAndFoodId(@Param("userId") Long userId, @Param("foodId") Long foodId);
    int clearByUserId(@Param("userId") Long userId);
}

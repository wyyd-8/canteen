package com.me.canteen.mapper;

import com.me.canteen.entity.FoodItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface FoodItemMapper {
    List<FoodItem> findByCanteenId(@Param("canteenId") Long canteenId, @Param("onSaleOnly") boolean onSaleOnly);
    FoodItem findById(@Param("id") Long id);
    int insert(FoodItem food);
    int update(FoodItem food);
    int delete(@Param("id") Long id);
}

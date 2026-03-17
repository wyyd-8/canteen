package com.me.canteen.mapper;

import com.me.canteen.entity.Seat;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SeatMapper {
    Seat findById(@Param("id") Long id);
}

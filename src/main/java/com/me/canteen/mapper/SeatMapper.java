package com.me.canteen.mapper;

import com.me.canteen.entity.Seat;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SeatMapper {
    Seat findById(@Param("id") Long id);
    List<Seat> findByCanteenId(@Param("canteenId") Long canteenId);
}

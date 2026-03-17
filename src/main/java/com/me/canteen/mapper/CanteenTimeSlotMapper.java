package com.me.canteen.mapper;

import com.me.canteen.entity.CanteenTimeSlot;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CanteenTimeSlotMapper {
    CanteenTimeSlot findById(@Param("id") Long id);
    CanteenTimeSlot findCurrentSlot(@Param("canteenId") Long canteenId, @Param("date") java.time.LocalDate date, @Param("time") java.time.LocalTime time);
}

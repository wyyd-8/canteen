package com.me.canteen.mapper;

import com.me.canteen.entity.Reservation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ReservationMapper {
    int insert(Reservation reservation);
    Reservation findById(@Param("id") Long id);
    Reservation findBySlotAndSeat(@Param("timeSlotId") Long timeSlotId, @Param("seatId") Long seatId);
    int countReserved(@Param("canteenId") Long canteenId, @Param("timeSlotId") Long timeSlotId);
    int countByStatus(@Param("canteenId") Long canteenId, @Param("timeSlotId") Long timeSlotId, @Param("status") String status);
    List<com.me.canteen.dto.DiningStatisticsDTO> statisticsByDateRange(@Param("canteenId") Long canteenId, @Param("startDate") java.time.LocalDate startDate, @Param("endDate") java.time.LocalDate endDate);
}

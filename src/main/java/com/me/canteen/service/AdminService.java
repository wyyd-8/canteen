package com.me.canteen.service;

import com.me.canteen.dto.*;
import java.time.LocalDate;
import java.util.List;

public interface AdminService {
    ScanResponseDTO scanQrCode(String qrToken, Long operatorId);
    List<RealtimeOccupancyDTO> getRealtimeOccupancy(Long canteenId);
    List<DiningStatisticsDTO> getDiningStatistics(Long canteenId, LocalDate startDate, LocalDate endDate);
    List<FoodSalesStatisticsDTO> getFoodSalesStatistics(Long canteenId, LocalDate startDate, LocalDate endDate);
}

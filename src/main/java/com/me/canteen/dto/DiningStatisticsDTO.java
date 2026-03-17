package com.me.canteen.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class DiningStatisticsDTO {
    private LocalDate date;
    private Integer diningCount;
    private Integer reservationCount;
}

package com.me.canteen.dto;

import com.me.canteen.entity.OrderItem;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ScanResponseDTO {
    // 用户信息
    private Long userId;
    private String userName;
    
    // 订单信息
    private String orderNo;
    private BigDecimal totalAmount;
    private LocalDateTime paidAt;
    
    // 菜品明细
    private List<OrderItem> items;
    
    // 预约信息
    private String canteenName;
    private String timeSlot; // "11:00-12:00"
    private String seatNo;
    
    // 核销状态
    private boolean verified;
    private String message;
}

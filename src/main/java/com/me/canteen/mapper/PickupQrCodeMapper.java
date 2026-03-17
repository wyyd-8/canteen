package com.me.canteen.mapper;

import com.me.canteen.entity.PickupQrCode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PickupQrCodeMapper {
    int insert(PickupQrCode qrCode);
    PickupQrCode findByToken(@Param("token") String token);
    int updateStatus(@Param("id") Long id, @Param("status") String status, @Param("usedAt") java.time.LocalDateTime usedAt);
}

package com.me.canteen.mapper;

import com.me.canteen.entity.QrScanLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QrScanLogMapper {
    int insert(QrScanLog log);
}

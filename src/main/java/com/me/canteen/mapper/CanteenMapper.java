package com.me.canteen.mapper;

import com.me.canteen.entity.Canteen;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface CanteenMapper {
    List<Canteen> findAll();
    Canteen findById(@Param("id") Long id);
}

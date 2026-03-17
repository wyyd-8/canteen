package com.me.canteen.mapper;

import com.me.canteen.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    int insert(User user);
    User findById(@Param("id") Long id);
    User findByUserNo(@Param("userNo") String userNo);
    User findByPhone(@Param("phone") String phone);
}

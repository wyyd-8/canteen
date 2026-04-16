package com.me.canteen.service.impl;

import com.me.canteen.common.BusinessException;
import com.me.canteen.common.ErrorCode;
import com.me.canteen.entity.User;
import com.me.canteen.mapper.UserMapper;
import com.me.canteen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public User register(User user) {
        // 检查手机号是否已注册
        User existingUser = userMapper.findByPhone(user.getPhone());
        if (existingUser != null) {
            throw new BusinessException(ErrorCode.USER_PHONE_EXISTS);
        }

        // 生成唯一的userNo（带随机后缀避免并发重复）
        user.setUserNo("U" + System.currentTimeMillis() + ThreadLocalRandom.current().nextInt(1000, 9999));
        user.setRole("USER");
        try {
            userMapper.insert(user);
        } catch (DuplicateKeyException e) {
            throw new BusinessException("Registration failed, please try again");
        }
        return user;
    }

    @Override
    public User login(String phone, String password) {
        // 参数校验
        if (!org.springframework.util.StringUtils.hasText(phone) || !org.springframework.util.StringUtils.hasText(password)) {
            throw new BusinessException(ErrorCode.PARAM_ERROR);
        }
        
        User user = userMapper.findByPhone(phone);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        if (!user.getPassword().equals(password)) {
            throw new BusinessException(ErrorCode.USER_PASSWORD_ERROR);
        }
        return user;
    }
}

package com.me.canteen.service.impl;

import com.me.canteen.entity.User;
import com.me.canteen.mapper.UserMapper;
import com.me.canteen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User register(User user) {
        // 简单生成一个 userNo
        user.setUserNo("U" + System.currentTimeMillis());
        user.setRole("USER");
        userMapper.insert(user);
        return user;
    }

    @Override
    public User login(String phone, String password) {
        User user = userMapper.findByPhone(phone);
        if (user == null || !user.getPassword().equals(password)) {
            throw new RuntimeException("Phone or password incorrect");
        }
        return user;
    }
}

package com.me.canteen.service;

import com.me.canteen.entity.User;

public interface UserService {
    User register(User user);
    User login(String phone, String password);
}

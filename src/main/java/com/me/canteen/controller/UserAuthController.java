package com.me.canteen.controller;

import com.me.canteen.common.Result;
import com.me.canteen.dto.UserLoginRequest;
import com.me.canteen.dto.UserLoginVO;
import com.me.canteen.entity.User;
import com.me.canteen.properties.JwtProperties;
import com.me.canteen.service.UserService;
import com.me.canteen.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
public class UserAuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("/register")
    public Result<User> register(@RequestBody User user) {
        User registeredUser = userService.register(user);
        return Result.success(registeredUser);
    }

    @PostMapping("/login")
    public Result<UserLoginVO> login(@RequestBody UserLoginRequest loginRequest) {
        User user = userService.login(loginRequest.getPhone(), loginRequest.getPassword());

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getUserSecretKey(),
                jwtProperties.getUserTtl(),
                claims);

        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .token(token)
                .build();

        return Result.success(userLoginVO);
    }
}

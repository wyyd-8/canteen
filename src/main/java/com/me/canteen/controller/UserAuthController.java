package com.me.canteen.controller;

import com.me.canteen.common.Result;
import com.me.canteen.dto.UserLoginRequest;
import com.me.canteen.dto.UserLoginVO;
import com.me.canteen.entity.User;
import com.me.canteen.properties.JwtProperties;
import com.me.canteen.service.UserService;
import com.me.canteen.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/v1/user")
public class UserAuthController {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("/register")
    public Result<User> register(@RequestBody User user) {
        // 参数校验：用户名非空
        if (!StringUtils.hasText(user.getUserName())) {
            return Result.error("Username cannot be empty");
        }
        // 参数校验：手机号格式
        if (!StringUtils.hasText(user.getPhone()) || !PHONE_PATTERN.matcher(user.getPhone()).matches()) {
            return Result.error("Invalid phone number format");
        }
        // 参数校验：密码非空
        if (!StringUtils.hasText(user.getPassword())) {
            return Result.error("Password cannot be empty");
        }

        User registeredUser = userService.register(user);
        // 脱敏：不返回密码
        registeredUser.setPassword(null);
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
                .role(user.getRole())
                .build();

        return Result.success(userLoginVO);
    }
}

package com.me.canteen.dto;

import lombok.Data;

@Data
public class UserLoginRequest {
    private String phone;
    private String password;
}

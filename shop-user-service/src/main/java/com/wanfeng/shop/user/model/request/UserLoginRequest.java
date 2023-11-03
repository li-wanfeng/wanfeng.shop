package com.wanfeng.shop.user.model.request;

import lombok.Data;

@Data
public class UserLoginRequest {
    private String mail;
    private String pwd;
}

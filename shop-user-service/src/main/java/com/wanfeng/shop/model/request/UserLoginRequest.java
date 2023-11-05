package com.wanfeng.shop.model.request;

import lombok.Data;

@Data
public class UserLoginRequest {
    private String mail;
    private String pwd;
}

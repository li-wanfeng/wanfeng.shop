package com.wanfeng.shop.model.request;

import lombok.Data;

/**
 * 用户注册实体
 */
@Data
public class UserRegisterRequest {

    private String name;
    private String mail;
    private String pwd;
    private String slogan;
    private Integer sex;
    private String code;

}

package com.wanfeng.shop.model.vo;

import lombok.Data;

import java.util.Date;

@Data
public class UserVO {
    private Long id;

    /**
     * 昵称
     */
    private String name;


    /**
     * 头像
     */
    private String headImg;

    /**
     * 用户签名
     */
    private String slogan;

    /**
     * 0表示女，1表示男
     */
    private Integer sex;

    /**
     * 积分
     */
    private Integer points;



    /**
     * 邮箱
     */
    private String mail;
    
}

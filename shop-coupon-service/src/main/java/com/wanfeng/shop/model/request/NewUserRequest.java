package com.wanfeng.shop.model.request;

import lombok.Data;

@Data
public class NewUserRequest {
    private Long userId;
    private String name;
}

package com.wanfeng.shop.user.service;

import com.wanfeng.shop.enums.SendCodeEnum;
import com.wanfeng.shop.util.JsonData;

public interface NotifyService {

    JsonData sendCode(SendCodeEnum sendCodeEnum, String to);
}

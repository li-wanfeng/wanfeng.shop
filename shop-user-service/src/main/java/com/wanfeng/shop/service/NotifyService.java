package com.wanfeng.shop.service;

import com.wanfeng.shop.enums.SendCodeEnum;
import com.wanfeng.shop.util.JsonData;

public interface NotifyService {

    JsonData sendCode(SendCodeEnum sendCodeEnum, String to);

    boolean checkCode(SendCodeEnum sendCodeEnum, String code, String to);
}

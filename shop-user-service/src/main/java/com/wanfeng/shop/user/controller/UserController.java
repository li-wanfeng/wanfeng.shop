package com.wanfeng.shop.user.controller;

import com.wanfeng.shop.exception.BizException;
import com.wanfeng.shop.user.model.entity.UserDO;
import com.wanfeng.shop.user.model.request.UserLoginRequest;
import com.wanfeng.shop.user.model.request.UserRegisterRequest;
import com.wanfeng.shop.user.service.UserService;
import com.wanfeng.shop.util.JsonData;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/user/v1")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/register")
    public JsonData userRegister(@RequestBody UserRegisterRequest registerRequest) {
        JsonData res = userService.userRegister(registerRequest);
        return res;
    }
    @PostMapping("/login")
    public JsonData userLogin(@RequestBody UserLoginRequest loginRequest) {
        JsonData res = userService.userLogin(loginRequest);
        return res;
    }
}

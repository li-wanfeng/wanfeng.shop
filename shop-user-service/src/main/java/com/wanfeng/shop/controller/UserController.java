package com.wanfeng.shop.controller;

import com.wanfeng.shop.model.request.UserLoginRequest;
import com.wanfeng.shop.model.request.UserRegisterRequest;
import com.wanfeng.shop.model.vo.UserVO;
import com.wanfeng.shop.service.UserService;
import com.wanfeng.shop.util.JsonData;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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

    /**
     * 个人信息查询
     * @return
     */
    @GetMapping("detail")
    public JsonData detail() {
        UserVO res = userService.userDetail();
        return JsonData.buildSuccess(res);
    }
}

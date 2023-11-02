package com.wanfeng.shop.user.controller;

import com.wanfeng.shop.user.model.entity.UserDO;
import com.wanfeng.shop.user.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/user/v1")
public class UserController {
    @Resource
    private UserService userService;
    @GetMapping
    public List getUser(){
        List<UserDO> list = userService.list();

        return list;
    }
}

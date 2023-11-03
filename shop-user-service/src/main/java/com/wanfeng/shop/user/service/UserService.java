package com.wanfeng.shop.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.wanfeng.shop.user.model.entity.UserDO;
import com.wanfeng.shop.user.model.request.UserRegisterRequest;
import com.wanfeng.shop.util.JsonData;

/**
* @author 85975
* @description 针对表【user】的数据库操作Service
* @createDate 2023-11-02 16:02:39
*/
public interface UserService extends IService<UserDO> {

    JsonData userRegister(UserRegisterRequest registerRequest);
}

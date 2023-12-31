package com.wanfeng.shop.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.wanfeng.shop.model.request.UserLoginRequest;
import com.wanfeng.shop.model.request.UserRegisterRequest;
import com.wanfeng.shop.model.vo.UserVO;
import com.wanfeng.shop.model.entity.UserDO;
import com.wanfeng.shop.util.JsonData;

/**
* @author 85975
* @description 针对表【user】的数据库操作Service
* @createDate 2023-11-02 16:02:39
*/
public interface UserService extends IService<UserDO> {

    JsonData userRegister(UserRegisterRequest registerRequest);

    JsonData userLogin(UserLoginRequest loginRequest);

    UserVO userDetail();
}

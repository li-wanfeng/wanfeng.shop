package com.wanfeng.shop.user.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanfeng.shop.user.mapper.UserMapper;
import com.wanfeng.shop.user.model.entity.UserDO;
import com.wanfeng.shop.user.service.UserService;
import org.springframework.stereotype.Service;

/**
* @author 85975
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-11-02 16:02:39
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO>
    implements UserService {

}





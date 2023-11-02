package com.wanfeng.shop.user.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanfeng.shop.user.mapper.AddressMapper;
import com.wanfeng.shop.user.model.entity.Address;
import com.wanfeng.shop.user.service.AddressService;
import org.springframework.stereotype.Service;

/**
* @author 85975
* @description 针对表【address(电商-公司收发货地址表)】的数据库操作Service实现
* @createDate 2023-11-02 15:50:29
*/
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address>
    implements AddressService {

}





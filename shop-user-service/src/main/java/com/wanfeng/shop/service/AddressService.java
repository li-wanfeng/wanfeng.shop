package com.wanfeng.shop.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.wanfeng.shop.model.entity.AddressDO;
import com.wanfeng.shop.model.request.AddressRequest;
import com.wanfeng.shop.util.JsonData;

/**
* @author 85975
* @description 针对表【address(电商-公司收发货地址表)】的数据库操作Service
* @createDate 2023-11-02 15:50:29
*/
public interface AddressService extends IService<AddressDO> {

    void add(AddressRequest addressRequest);

    JsonData findAddressById(Long id);

    JsonData deleteAddressById(Long id);

    JsonData findAddressList();

}

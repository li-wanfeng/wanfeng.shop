package com.wanfeng.shop.product.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.wanfeng.shop.product.model.entity.BannerDO;
import com.wanfeng.shop.util.JsonData;

/**
* @author 85975
* @description 针对表【banner】的数据库操作Service
* @createDate 2023-11-04 21:01:47
*/
public interface BannerService extends IService<BannerDO> {

    JsonData findBannerList();

}

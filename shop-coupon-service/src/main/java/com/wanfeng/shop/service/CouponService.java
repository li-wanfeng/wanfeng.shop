package com.wanfeng.shop.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.wanfeng.shop.model.entity.CouponDO;
import com.wanfeng.shop.model.request.NewUserRequest;
import com.wanfeng.shop.enums.CouponCategoryEnum;
import com.wanfeng.shop.util.JsonData;

/**
* @author 85975
* @description 针对表【coupon】的数据库操作Service
* @createDate 2023-11-04 01:12:37
*/
public interface CouponService extends IService<CouponDO> {

    JsonData getCouponList();

    JsonData getCouponPage(Integer page, Integer size);

    JsonData ReceiveCoupon(Long id, CouponCategoryEnum categoryEnum);

    JsonData initUserCoupon(NewUserRequest newUserRequest);
}

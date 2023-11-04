package com.wanfeng.shop.coupon.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.wanfeng.shop.coupon.model.entity.CouponRecordDO;
import com.wanfeng.shop.util.JsonData;

/**
* @author 85975
* @description 针对表【coupon_record】的数据库操作Service
* @createDate 2023-11-04 01:13:15
*/
public interface CouponRecordService extends IService<CouponRecordDO> {

    JsonData PageCouponRecord(Integer page, Integer size);

    JsonData getCouponRecordById(Long id);
}

package com.wanfeng.shop.coupon.controller;

import com.wanfeng.shop.coupon.service.CouponService;
import com.wanfeng.shop.enums.BizCodeEnum;
import com.wanfeng.shop.enums.CouponCategoryEnum;
import com.wanfeng.shop.util.JsonData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/coupon/v1")
public class CouponController {
    @Resource
    private CouponService couponService;

    @GetMapping("page")
    public JsonData PageCouponlist(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                   @RequestParam(value = "size", defaultValue = "10") Integer size) {

        return couponService.getCouponPage(page,size);
    }
    @GetMapping("receive")
    public JsonData ReceiveCoupon(@RequestParam("id") Long id) {
        if (null == id || id <= 0) {
            return JsonData.buildResult(BizCodeEnum.COUPON_NO_EXITS);
        }
        return couponService.ReceiveCoupon(id, CouponCategoryEnum.PROMOTION);
    }
}

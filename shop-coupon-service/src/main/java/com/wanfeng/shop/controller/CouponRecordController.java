package com.wanfeng.shop.controller;

import com.wanfeng.shop.service.CouponRecordService;
import com.wanfeng.shop.enums.BizCodeEnum;
import com.wanfeng.shop.util.JsonData;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/coupon_record/v1")
public class CouponRecordController {

    @Resource
    private CouponRecordService couponRecordService;

    @GetMapping("/page")
    public JsonData PageCouponRecord(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                     @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return couponRecordService.PageCouponRecord(page, size);
    }

    @GetMapping("/find/{id}")
    public JsonData getCouponRecordById(@PathVariable("id") Long id) {
        if (null == id || id <= 0) {
            return JsonData.buildResult(BizCodeEnum.COUPON_NO_EXITS);
        }
        return couponRecordService.getCouponRecordById(id);
    }
}

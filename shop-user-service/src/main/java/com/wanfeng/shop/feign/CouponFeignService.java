package com.wanfeng.shop.feign;

import com.wanfeng.shop.model.request.NewUserRequest;
import com.wanfeng.shop.util.JsonData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
@FeignClient("shop-coupon-service")
public interface CouponFeignService {
    @PostMapping("api/coupon/v1/new_iser_coupon")
    JsonData addNewUserCoupn(@RequestBody NewUserRequest newUserRequest);
}

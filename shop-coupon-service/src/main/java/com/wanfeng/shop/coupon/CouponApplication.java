package com.wanfeng.shop.coupon;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
//开启事务
@EnableTransactionManagement
@SpringBootApplication(scanBasePackages = {"com.wanfeng.shop"})
@MapperScan("com.wanfeng.shop.coupon.mapper")

public class CouponApplication {
    public static void main(String[] args) {
        SpringApplication.run(CouponApplication.class, args);
    }
}
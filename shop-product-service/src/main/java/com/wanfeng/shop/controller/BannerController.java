package com.wanfeng.shop.controller;

import com.wanfeng.shop.service.BannerService;
import com.wanfeng.shop.util.JsonData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/banner/v1")
public class BannerController {

    @Resource
    private BannerService bannerService;

    @GetMapping("/list")
    public JsonData listBanner() {
        return bannerService.findBannerList();
    }
}

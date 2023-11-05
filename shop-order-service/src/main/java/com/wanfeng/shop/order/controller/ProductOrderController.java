package com.wanfeng.shop.order.controller;

import com.wanfeng.shop.order.service.ProductOrderService;
import com.wanfeng.shop.util.JsonData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/product_order")
public class ProductOrderController {
    @Resource
    private ProductOrderService productOrderService;

    @GetMapping("/get")
    public JsonData PageProductOrder() {
        return JsonData.buildSuccess(productOrderService.count());
    }
}

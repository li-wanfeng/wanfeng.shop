package com.wanfeng.shop.controller;

import com.wanfeng.shop.model.request.ConfirmOrderRequest;
import com.wanfeng.shop.service.ProductOrderService;
import com.wanfeng.shop.util.JsonData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/product_order")
public class ProductOrderController {
    @Resource
    private ProductOrderService productOrderService;

    @PostMapping("/confirm")
    public void confirmOrder(ConfirmOrderRequest confirmOrderRequest, HttpServletResponse response) {
        JsonData res = productOrderService.confirmOrder(confirmOrderRequest);
        if (res.getCode() == 0) {
            //成功
        } else {
            //失败
        }
    }
}

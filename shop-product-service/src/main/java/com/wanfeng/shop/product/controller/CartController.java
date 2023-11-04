package com.wanfeng.shop.product.controller;

import com.wanfeng.shop.product.model.request.CartRequest;
import com.wanfeng.shop.product.service.CartService;
import com.wanfeng.shop.util.JsonData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/cart/v1")
public class CartController {

    @Resource
    private CartService cartService;
    @PostMapping("addToCart")
    public JsonData addProduct(@RequestBody CartRequest cartRequest) {
        return cartService.addProductToCart(cartRequest);
    }
}

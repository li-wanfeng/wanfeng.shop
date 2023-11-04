package com.wanfeng.shop.product.controller;

import com.wanfeng.shop.product.model.request.CartRequest;
import com.wanfeng.shop.product.service.CartService;
import com.wanfeng.shop.util.JsonData;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/cart/v1")
public class CartController {

    @Resource
    private CartService cartService;
    @PostMapping("add")
    public JsonData addProduct(@RequestBody CartRequest cartRequest) {
        return cartService.addProductToCart(cartRequest);
    }

    @DeleteMapping("clear")
    public JsonData clearMyCart() {
        return cartService.clearMyCart();
    }

    @GetMapping("cart_detail")
    public JsonData cartDetail() {
        return cartService.cartDetail();
    }

}

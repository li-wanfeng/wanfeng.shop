package com.wanfeng.shop.product.service;

import com.wanfeng.shop.product.model.request.CartRequest;
import com.wanfeng.shop.util.JsonData;

public interface CartService {
    JsonData addProductToCart(CartRequest cartRequest);

    JsonData clearMyCart();

    JsonData cartDetail();
}

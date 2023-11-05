package com.wanfeng.shop.service;

import com.wanfeng.shop.model.request.CartRequest;
import com.wanfeng.shop.util.JsonData;

public interface CartService {
    JsonData addProductToCart(CartRequest cartRequest);

    JsonData clearMyCart();

    JsonData cartDetail();

    JsonData deleteItemById(Long productId);
}

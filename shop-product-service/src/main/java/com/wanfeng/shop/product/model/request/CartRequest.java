package com.wanfeng.shop.product.model.request;

import lombok.Data;

@Data
public class CartRequest {
    private Long productId;
    private Long payNum;

}

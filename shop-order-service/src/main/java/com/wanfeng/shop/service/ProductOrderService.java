package com.wanfeng.shop.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.wanfeng.shop.model.entity.ProductOrder;
import com.wanfeng.shop.model.request.ConfirmOrderRequest;
import com.wanfeng.shop.util.JsonData;

/**
* @author 85975
* @description 针对表【product_order】的数据库操作Service
* @createDate 2023-11-05 19:24:31
*/
public interface ProductOrderService extends IService<ProductOrder> {

    JsonData confirmOrder(ConfirmOrderRequest confirmOrderRequest);
}

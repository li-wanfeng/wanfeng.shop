package com.wanfeng.shop.model.request;

import java.math.BigDecimal;
import java.util.List;

public class ConfirmOrderRequest {


    /**
     * 购物车使用的优惠券id
     */
    private Long couponRecorId;

    /**
     * 购买的商品Id列表
     * 详细信息从购物车获取
     */
    private List<Long> productIdList;


    /**
     * 支付方式
     */
    private String payType;

    /**
     * 支付端类型
     */
    private String clientTye;

    /**
     * 收货地址Id
     */
    private Long addressId;

    /**
     * 前端传来的，后端二次验证价格
     */
    private BigDecimal totalPrice;

    /**
     * 实际支付价格，如果使用了优惠券，应该是totalPrice-product的price
     * 如果没有应该和前端传来的totalPrice一致
     */
    private BigDecimal realPayPrice;

    /**
     * 放重令牌  一般是参数+时间戳+ackey->加密
     * 防止重复提交
     */
    private String orderToken;
}

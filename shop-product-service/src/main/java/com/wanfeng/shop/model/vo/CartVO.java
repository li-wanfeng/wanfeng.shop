package com.wanfeng.shop.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 
 * @TableName product
 */
@Data
public class CartVO implements Serializable {
    private List<CartItemVO> cartItemVOS;

    /**
     * 购买总件数
     */
    private Long totalNum;

    /**
     * 购物车总价格
     */
    private BigDecimal totalPrice;

    /**
     * 购物车实际支付价格
     */
    private BigDecimal realPayPrice;

}
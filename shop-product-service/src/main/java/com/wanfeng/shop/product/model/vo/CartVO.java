package com.wanfeng.shop.product.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
    private Integer totalNum;

    /**
     * 购物车总价格
     */
    private BigDecimal totalPrice;

    /**
     * 购物车实际支付价格
     */
    private BigDecimal realPayPrice;

}
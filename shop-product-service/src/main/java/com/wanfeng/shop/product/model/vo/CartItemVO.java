package com.wanfeng.shop.product.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class CartItemVO implements Serializable {
    /**
     * 商品ID
     */
    private Long productId;
    /**
     * 购买数量
     */
    private Long payNum;

    /**
     * 标题
     */
    private String title;

    /**
     * 新价格
     */
    private BigDecimal price;

    /**
     * 总价格，单价+数量
     */
    private BigDecimal totalPrice;





    /**
     * 创建时间
     */
    private Date createTime;
}

package com.wanfeng.shop.product.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @TableName product
 */
@Data
public class CartVO implements Serializable {
    /**
     * 商品ID
     */
    private Long productId;
    /**
     * 标题
     */
    private String title;

    /**
     * 老价格
     */
    private BigDecimal oldPrice;

    /**
     * 新价格
     */
    private BigDecimal price;

    /**
     * 购买数量
     */
    private Long payNum;

    /**
     * 创建时间
     */
    private Date createTime;

}
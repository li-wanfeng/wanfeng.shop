package com.wanfeng.shop.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanfeng.shop.model.entity.ProductOrder;
import com.wanfeng.shop.mapper.ProductOrderMapper;
import com.wanfeng.shop.model.request.ConfirmOrderRequest;
import com.wanfeng.shop.service.ProductOrderService;
import com.wanfeng.shop.util.JsonData;
import org.springframework.stereotype.Service;

/**
* @author 85975
* @description 针对表【product_order】的数据库操作Service实现
* @createDate 2023-11-05 19:24:31
*/
@Service
public class ProductOrderServiceImpl extends ServiceImpl<ProductOrderMapper, ProductOrder>
    implements ProductOrderService {

    /**
     * 提交订单
     * @param confirmOrderRequest
     * @return
     */
    @Override
    public JsonData confirmOrder(ConfirmOrderRequest confirmOrderRequest) {
        /**
         * 防重提交
         * 用户微服务-确认收货地址
         * 商品微服务-获取最新购物项和价格
         * 订单验价
         * 优惠券微服务-获取优惠券
         * 验证价格
         * 锁定优惠券
         * 锁定商品库存
         * 创建订单对象
         * 创建子订单对象
         * 发送延迟消息-用于自动关单
         * 创建支付信息-对接三方支付
         */
        return null;
    }
}





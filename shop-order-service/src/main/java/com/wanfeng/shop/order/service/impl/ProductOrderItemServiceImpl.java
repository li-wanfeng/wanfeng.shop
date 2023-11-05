package com.wanfeng.shop.order.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanfeng.shop.order.mapper.ProductOrderItemMapper;
import com.wanfeng.shop.order.model.entity.ProductOrderItem;
import com.wanfeng.shop.order.service.ProductOrderItemService;
import org.springframework.stereotype.Service;

/**
* @author 85975
* @description 针对表【product_order_item】的数据库操作Service实现
* @createDate 2023-11-05 19:24:34
*/
@Service
public class ProductOrderItemServiceImpl extends ServiceImpl<ProductOrderItemMapper, ProductOrderItem>
    implements ProductOrderItemService {

}





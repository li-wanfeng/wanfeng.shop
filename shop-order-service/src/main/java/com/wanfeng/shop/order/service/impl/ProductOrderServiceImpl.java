package com.wanfeng.shop.order.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanfeng.shop.order.mapper.ProductOrderMapper;
import com.wanfeng.shop.order.model.entity.ProductOrder;
import com.wanfeng.shop.order.service.ProductOrderService;
import org.springframework.stereotype.Service;

/**
* @author 85975
* @description 针对表【product_order】的数据库操作Service实现
* @createDate 2023-11-05 19:24:31
*/
@Service
public class ProductOrderServiceImpl extends ServiceImpl<ProductOrderMapper, ProductOrder>
    implements ProductOrderService {

}





package com.wanfeng.shop.product.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.wanfeng.shop.product.model.entity.ProductDO;
import com.wanfeng.shop.product.model.vo.ProductVO;
import com.wanfeng.shop.util.JsonData;

/**
* @author 85975
* @description 针对表【product】的数据库操作Service
* @createDate 2023-11-04 21:01:49
*/
public interface ProductService extends IService<ProductDO> {

    JsonData PageProduct(Integer page, Integer size);

    ProductVO detailProductById(Long productId);
}

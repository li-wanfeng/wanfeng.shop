package com.wanfeng.shop.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.wanfeng.shop.model.entity.ProductDO;
import com.wanfeng.shop.model.vo.ProductVO;
import com.wanfeng.shop.util.JsonData;

import java.util.ArrayList;
import java.util.List;

/**
* @author 85975
* @description 针对表【product】的数据库操作Service
* @createDate 2023-11-04 21:01:49
*/
public interface ProductService extends IService<ProductDO> {

    JsonData PageProduct(Integer page, Integer size);

    ProductVO detailProductById(Long productId);

    List<ProductVO> findProductsByIdBatch(ArrayList<Long> prudectIds);
}

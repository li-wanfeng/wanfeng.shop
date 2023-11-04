package com.wanfeng.shop.product.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanfeng.shop.enums.BizCodeEnum;
import com.wanfeng.shop.exception.BizException;
import com.wanfeng.shop.product.mapper.ProductMapper;
import com.wanfeng.shop.product.model.entity.ProductDO;
import com.wanfeng.shop.product.model.vo.ProductVO;
import com.wanfeng.shop.product.service.ProductService;
import com.wanfeng.shop.util.JsonData;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 85975
 * @description 针对表【product】的数据库操作Service实现
 * @createDate 2023-11-04 21:01:49
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, ProductDO> implements ProductService {

    @Override
    public JsonData PageProduct(Integer page, Integer size) {
        Page<ProductDO> productPage = new Page<>(page, size);
        QueryWrapper<ProductDO> productDOQueryWrapper = new QueryWrapper<ProductDO>();
        Page<ProductDO> productDOPage = this.page(productPage, productDOQueryWrapper);
        List<ProductVO> collect = productDOPage.getRecords().stream().map(this::getproductVO).collect(Collectors.toList());
        Page<ProductVO> couponVOPage = new Page<>(page, size, productDOPage.getTotal());
        couponVOPage.setRecords(collect);
        return JsonData.buildSuccess(couponVOPage);
    }

    @Override
    public ProductVO detailProductById(Long productId) {
        ProductDO productDO = this.baseMapper.selectOne(new QueryWrapper<ProductDO>().eq("id", productId));
        if (ObjectUtils.isEmpty(productDO)) {
            throw new BizException(BizCodeEnum.PRODUCT_NO_EXITS);
        }
        return getproductVO(productDO);

    }

    @Override
    public List<ProductVO> findProductsByIdBatch(ArrayList<Long> prudectIds) {
        List<ProductDO> productDOS = this.baseMapper.selectList(new QueryWrapper<ProductDO>().in("id", prudectIds));
        List<ProductVO> productVOS = null;
        if (null != productDOS && !productDOS.isEmpty()) {
            productVOS = productDOS.stream().map(this::getproductVO).collect(Collectors.toList());
        }
        return productVOS;
    }

    private ProductVO getproductVO(ProductDO productDO) {
        if (null == productDO) {
            return null;
        }
        ProductVO productVO = new ProductVO();
        BeanUtils.copyProperties(productDO, productVO);
        //库存  : 总库存-锁定数量
        productVO.setStock(productDO.getStock() - productDO.getLockStock());
        return productVO;

    }
}





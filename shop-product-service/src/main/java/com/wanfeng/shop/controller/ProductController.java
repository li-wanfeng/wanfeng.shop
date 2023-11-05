package com.wanfeng.shop.controller;

import com.wanfeng.shop.enums.BizCodeEnum;
import com.wanfeng.shop.model.vo.ProductVO;
import com.wanfeng.shop.service.ProductService;
import com.wanfeng.shop.util.JsonData;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/product/v1")
public class ProductController {
    @Resource
    private ProductService productService;

    @GetMapping("/page")
    public JsonData PageProduct(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return productService.PageProduct(page,size);
    }

    @GetMapping("/detail/{product_id}")
    public JsonData detailProductById(@PathVariable("product_id") Long productId) {
        if (null == productId || productId <= 0) {
            return JsonData.buildResult(BizCodeEnum.PRODUCT_NO_EXITS);
        }
        ProductVO productVO = productService.detailProductById(productId);
        return JsonData.buildSuccess(productVO);
    }
}

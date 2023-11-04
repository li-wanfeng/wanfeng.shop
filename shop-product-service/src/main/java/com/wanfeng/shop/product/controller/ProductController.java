package com.wanfeng.shop.product.controller;

import com.wanfeng.shop.product.service.ProductService;
import com.wanfeng.shop.util.JsonData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}

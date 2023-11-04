package com.wanfeng.shop.product.service.impl;

import com.alibaba.fastjson2.JSON;
import com.wanfeng.shop.constant.CacheKey;
import com.wanfeng.shop.enums.BizCodeEnum;
import com.wanfeng.shop.interceptor.LoginInterceptor;
import com.wanfeng.shop.model.LoginUser;
import com.wanfeng.shop.product.model.request.CartRequest;
import com.wanfeng.shop.product.model.vo.CartItemVO;
import com.wanfeng.shop.product.model.vo.CartVO;
import com.wanfeng.shop.product.model.vo.ProductVO;
import com.wanfeng.shop.product.service.CartService;
import com.wanfeng.shop.product.service.ProductService;
import com.wanfeng.shop.util.JsonData;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ProductService productService;
    /**
     * 添加商品到购物车
     * @param cartRequest
     * @return
     */
    @Override
    public JsonData addProductToCart(CartRequest cartRequest) {

        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        if (ObjectUtils.isEmpty(loginUser)) {
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_NOLOGIN);
        }
        //1. 判空
        Long productId = cartRequest.getProductId();
        Long payNum = cartRequest.getPayNum();
        if (null == productId || null == payNum || productId <= 0 || payNum <= 0) {
            return JsonData.buildResult(BizCodeEnum.CART_ADD_FAIL);
        }
        //2. 获取购物车
        BoundHashOperations<String,Object,Object> myCartOps = getMyCartOps();
        //3. 当前商品是否存在购物车中
        Object cacheObj = myCartOps.get(productId);
        String result = "";
        if (null != cacheObj) {
            result = (String) cacheObj;
        }
        if (StringUtils.isBlank(result)) {
            ProductVO productVO = productService.detailProductById(productId);
            if (ObjectUtils.isEmpty(productVO)) {
                return JsonData.buildResult(BizCodeEnum.CART_ADD_FAIL);
            }
            //不存在 新建商品
            CartItemVO cartItemVO = new CartItemVO();
            cartItemVO.setProductId(cartRequest.getProductId());
            cartItemVO.setPayNum(cartRequest.getPayNum());
            cartItemVO.setTitle(productVO.getTitle());
            cartItemVO.setPrice(productVO.getPrice());
            cartItemVO.setTotalPrice(cartItemVO.getPrice().multiply(BigDecimal.valueOf(cartRequest.getPayNum())));
            cartItemVO.setCreateTime(new Date());
            myCartOps.put(productId, JSON.toJSONString(cartItemVO));
        }else {
            //存在 修改商品数量
            CartItemVO cartItemVO = JSON.parseObject(result, CartItemVO.class);
            cartItemVO.setPayNum(cartItemVO.getPayNum() + payNum);
            cartItemVO.setTotalPrice(cartItemVO.getPrice().multiply(BigDecimal.valueOf(cartRequest.getPayNum())));
            myCartOps.put(productId, JSON.toJSONString(cartItemVO));
        }
        return JsonData.buildSuccess();

    }

    @Override
    public JsonData clearMyCart() {
        String carKey = getCarKey();
        redisTemplate.delete(carKey);
        return JsonData.buildSuccess();
    }


    /**
     * 获取当前登录用户的购物车信息
     * @return
     */
    private BoundHashOperations<String,Object,Object> getMyCartOps() {
        String carKey = getCarKey();
        return redisTemplate.boundHashOps(carKey);
    }
    /**
     * 获取当前登录用户的购物车key
     * @return
     */
    private String getCarKey(){
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        return String.format(CacheKey.CHCHE_CART_KEY, loginUser.getId());
    }
}

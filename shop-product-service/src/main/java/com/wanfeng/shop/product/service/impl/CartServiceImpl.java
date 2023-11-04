package com.wanfeng.shop.product.service.impl;

import com.alibaba.fastjson2.JSON;
import com.wanfeng.shop.constant.CacheKey;
import com.wanfeng.shop.enums.BizCodeEnum;
import com.wanfeng.shop.interceptor.LoginInterceptor;
import com.wanfeng.shop.model.LoginUser;
import com.wanfeng.shop.product.model.request.CartRequest;
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
            CartVO cartVO = new CartVO();
            cartVO.setProductId(cartRequest.getProductId());
            cartVO.setTitle(productVO.getTitle());
            cartVO.setOldPrice(productVO.getOldPrice());
            cartVO.setPrice(productVO.getPrice());
            cartVO.setPayNum(cartRequest.getPayNum());
            cartVO.setCreateTime(productVO.getCreateTime());
            myCartOps.put(productId, JSON.toJSONString(cartVO));
        }else {
            //存在 修改商品数量
            CartVO cartVO = JSON.parseObject(result, CartVO.class);
            cartVO.setPayNum(cartVO.getPayNum() + payNum);
            myCartOps.put(productId, JSON.toJSONString(cartVO));
        }
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

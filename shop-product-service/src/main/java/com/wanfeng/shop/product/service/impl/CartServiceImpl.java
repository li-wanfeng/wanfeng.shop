package com.wanfeng.shop.product.service.impl;
import java.util.Date;

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
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

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
            cartItemVO.setPayNum(cartRequest.getPayNum());
            cartItemVO.setTitle(productVO.getTitle());
            cartItemVO.setPrice(productVO.getPrice());
            cartItemVO.setCreateTime(new Date());
            cartItemVO.setProductId(cartRequest.getProductId());
            cartItemVO.setPayNum(cartRequest.getPayNum());
            cartItemVO.setTotalPrice(cartItemVO.getPrice().multiply(BigDecimal.valueOf(cartRequest.getPayNum())));
            myCartOps.put(productId, JSON.toJSONString(cartItemVO));
        }else {
            //存在 修改商品数量
            CartItemVO cartItemVO = JSON.parseObject(result, CartItemVO.class);
            cartItemVO.setPayNum(cartItemVO.getPayNum()+cartRequest.getPayNum());
            cartItemVO.setTotalPrice(cartItemVO.getPrice().multiply(BigDecimal.valueOf(cartItemVO.getPayNum())));
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
     * 返回购物车全部数据
     * @return
     */
    @Override
    public JsonData cartDetail() {

        List<CartItemVO> cartItemVOS= buildCartItem(true);
        CartVO cartVO = new CartVO();
        if (null!= cartItemVOS && !cartItemVOS.isEmpty()) {
            cartVO.setCartItemVOS(cartItemVOS);
            AtomicReference<Long> totalNum = new AtomicReference<>(0l);
            AtomicReference<BigDecimal> totalPrice = new AtomicReference<>(new BigDecimal(0));
            cartItemVOS.stream().forEach(cartItemVO -> {
                totalPrice.updateAndGet(v -> v.add(cartItemVO.getTotalPrice()));
                totalNum.updateAndGet(v -> v + cartItemVO.getPayNum());
            });
            cartVO.setTotalPrice(totalPrice.get());
            cartVO.setTotalNum(totalNum.get());
        }
        return JsonData.buildSuccess(cartVO);
    }

    @Override
    public JsonData deleteItemById(Long productId) {
        BoundHashOperations<String, Object, Object> myCartOps = getMyCartOps();
        Set<Object> keys = myCartOps.keys();
        for (Object objId : keys) {
            if (productId.longValue() == ((Long) objId).longValue()) {
                myCartOps.delete(productId);
                return JsonData.buildSuccess();
            }
        }
        return JsonData.buildResult(BizCodeEnum.PRODUCT_NO_EXITS);
    }

    /**
     * 获取购物车最新项
     * @param b 是否获取最新价格
     * @return
     */
    private List<CartItemVO> buildCartItem(boolean b) {
        BoundHashOperations<String, Object, Object> myCart = getMyCartOps();
        //全部的商品id
        List<Object> cartItemString = myCart.values();
        List<CartItemVO> cartItemVOS = null;
        if (!cartItemString.isEmpty()) {
            ArrayList<Long> prudectIds = new ArrayList<>();
            cartItemVOS = cartItemString.stream().map(cartItemVOObj -> {
                CartItemVO cartItemVO = JSON.parseObject((String) cartItemVOObj, CartItemVO.class);
                prudectIds.add(cartItemVO.getProductId());
                return cartItemVO;
            }).collect(Collectors.toList());
            //是否查询购物车中最新的商品信息
            if (b) {
                setProductLatestPrice(cartItemVOS,prudectIds);
            }
        }

        return cartItemVOS;
    }

    /**
     * 设置商品最新价格
     * @param cartItemVOS
     * @param prudectIds
     */
    private void setProductLatestPrice(List<CartItemVO> cartItemVOS, ArrayList<Long> prudectIds) {
        if (!prudectIds.isEmpty()) {
            List<ProductVO> productVOList = productService.findProductsByIdBatch(prudectIds);
            if (!(null == productVOList || productVOList.isEmpty())) {
                //根据productId进行分组,这里就是创建了一个map，k是id，v是本身
                Map<Long, ProductVO> map = productVOList.stream().collect(Collectors.toMap(ProductVO::getId, Function.identity()));
                cartItemVOS.stream().forEach(cartItemVO -> {
                    ProductVO productVO = map.get(cartItemVO.getProductId());
                    cartItemVO.setTitle(productVO.getTitle());
                    cartItemVO.setPrice(productVO.getPrice());
                    cartItemVO.setTotalPrice(cartItemVO.getPrice().multiply(BigDecimal.valueOf(cartItemVO.getPayNum())));
                    cartItemVO.setCreateTime(productVO.getCreateTime());
                });
            }
        }

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

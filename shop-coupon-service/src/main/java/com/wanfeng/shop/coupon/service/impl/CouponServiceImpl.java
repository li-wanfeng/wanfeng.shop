package com.wanfeng.shop.coupon.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanfeng.shop.coupon.mapper.CouponMapper;
import com.wanfeng.shop.coupon.model.entity.CouponDO;
import com.wanfeng.shop.coupon.model.vo.CouponVO;
import com.wanfeng.shop.coupon.service.CouponService;
import com.wanfeng.shop.enums.CouponCategoryEnum;
import com.wanfeng.shop.enums.CouponPublishEnum;
import com.wanfeng.shop.util.JsonData;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author 85975
* @description 针对表【coupon】的数据库操作Service实现
* @createDate 2023-11-04 01:12:37
*/
@Service
public class CouponServiceImpl extends ServiceImpl<CouponMapper, CouponDO>
    implements CouponService {

    @Override
    public JsonData getCouponList() {
        List<CouponDO> coupons = this.baseMapper.selectList(new QueryWrapper<>());
        return JsonData.buildSuccess(coupons);
    }

    @Override
    public JsonData getCouponPage(Integer page, Integer size) {
        Page<CouponDO> couponPage = new Page<>(page,size);
        QueryWrapper<CouponDO> couponDOQueryWrapper = new QueryWrapper<CouponDO>()
                .eq("category", CouponCategoryEnum.PROMOTION).eq("publish", CouponPublishEnum.PUBLISH);
        Page<CouponDO> couponDOPage = this.page(couponPage, couponDOQueryWrapper);
        List<CouponVO> collect = couponDOPage.getRecords().stream().map(this::getcouponVO).collect(Collectors.toList());
        Page<CouponVO> couponVOPage = new Page<>(page, size, couponDOPage.getTotal());
        couponVOPage.setRecords(collect);
        return JsonData.buildSuccess(couponVOPage);
    }

    private CouponVO getcouponVO(CouponDO couponDO) {
        if (null == couponDO) {
            return null;
        }
        CouponVO couponVO = new CouponVO();
        BeanUtils.copyProperties(couponDO, couponVO);
        return couponVO;
    }
}





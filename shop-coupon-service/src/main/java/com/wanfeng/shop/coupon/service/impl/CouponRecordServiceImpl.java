package com.wanfeng.shop.coupon.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanfeng.shop.coupon.mapper.CouponRecordMapper;
import com.wanfeng.shop.coupon.model.entity.CouponDO;
import com.wanfeng.shop.coupon.model.entity.CouponRecordDO;
import com.wanfeng.shop.coupon.model.vo.CouponRecordVO;
import com.wanfeng.shop.coupon.model.vo.CouponVO;
import com.wanfeng.shop.coupon.service.CouponRecordService;
import com.wanfeng.shop.enums.BizCodeEnum;
import com.wanfeng.shop.enums.CouponCategoryEnum;
import com.wanfeng.shop.enums.CouponPublishEnum;
import com.wanfeng.shop.enums.CouponStateEnum;
import com.wanfeng.shop.interceptor.LoginInterceptor;
import com.wanfeng.shop.model.LoginUser;
import com.wanfeng.shop.util.JsonData;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author 85975
* @description 针对表【coupon_record】的数据库操作Service实现
* @createDate 2023-11-04 01:13:15
*/
@Service
public class CouponRecordServiceImpl extends ServiceImpl<CouponRecordMapper, CouponRecordDO>
    implements CouponRecordService {

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public JsonData PageCouponRecord(Integer page, Integer size) {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        if (ObjectUtils.isEmpty(loginUser)) {
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_NOLOGIN);
        }
        QueryWrapper<CouponRecordDO> CouponRecordDOQueryWrapper = new QueryWrapper<CouponRecordDO>()
                .eq("user_id", loginUser.getId()).eq("use_state", CouponStateEnum.NEW);
        Page<CouponRecordDO> CouponRecordDOPage = this.page(new Page<>(page,size), CouponRecordDOQueryWrapper);
        List<CouponRecordVO> collect = CouponRecordDOPage.getRecords().stream().map(this::getCouponRecordVO).collect(Collectors.toList());
        Page<CouponRecordVO> couponVOPage = new Page<>(page, size, CouponRecordDOPage.getTotal());
        couponVOPage.setRecords(collect);
        return JsonData.buildSuccess(couponVOPage);
    }

    private CouponRecordVO getCouponRecordVO(CouponRecordDO couponRecordDO) {
        if (null == couponRecordDO) {
            return null;
        }
        CouponRecordVO couponRecordVO = new CouponRecordVO();
        BeanUtils.copyProperties(couponRecordDO, couponRecordVO);
        return couponRecordVO;
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Override
    public JsonData getCouponRecordById(Long id) {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        if (ObjectUtils.isEmpty(loginUser)) {
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_NOLOGIN);
        }
        QueryWrapper<CouponRecordDO> CouponRecordDOQueryWrapper = new QueryWrapper<CouponRecordDO>()
                .eq("id", id)
                .eq("user_id", loginUser.getId()).eq("use_state", CouponStateEnum.NEW);
        CouponRecordDO couponRecordDO = this.baseMapper.selectOne(CouponRecordDOQueryWrapper);
        if (ObjectUtils.isEmpty(couponRecordDO)) {
            return JsonData.buildResult(BizCodeEnum.COUPON_NO_EXITS);
        }
        return JsonData.buildSuccess(couponRecordDO);
    }
}





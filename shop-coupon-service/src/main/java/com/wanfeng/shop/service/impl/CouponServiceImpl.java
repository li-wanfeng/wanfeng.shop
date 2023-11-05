package com.wanfeng.shop.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanfeng.shop.mapper.CouponMapper;
import com.wanfeng.shop.model.entity.CouponDO;
import com.wanfeng.shop.model.entity.CouponRecordDO;
import com.wanfeng.shop.model.request.NewUserRequest;
import com.wanfeng.shop.model.vo.CouponVO;
import com.wanfeng.shop.service.CouponRecordService;
import com.wanfeng.shop.service.CouponService;
import com.wanfeng.shop.enums.BizCodeEnum;
import com.wanfeng.shop.enums.CouponCategoryEnum;
import com.wanfeng.shop.enums.CouponPublishEnum;
import com.wanfeng.shop.enums.CouponStateEnum;
import com.wanfeng.shop.interceptor.LoginInterceptor;
import com.wanfeng.shop.model.LoginUser;
import com.wanfeng.shop.util.CommonUtil;
import com.wanfeng.shop.util.JsonData;
import org.apache.commons.lang3.ObjectUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
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
    @Resource
    private CouponRecordService couponRecordService;

    @Autowired
    private RedissonClient redissonClient;

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

    /**
     * 用户领卷
     * @param id
     * @param categoryEnum
     * @return
     */
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    @Override
    public JsonData ReceiveCoupon(Long id, CouponCategoryEnum categoryEnum) {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        if (ObjectUtils.isEmpty(loginUser)) {
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_NOLOGIN);
        }
        //加锁
        String lockKey = "lock:coupon:" + id;
        RLock lock = redissonClient.getLock(lockKey.intern());
        lock.lock();
        try {
            //查询券是否存在
            QueryWrapper<CouponDO> couponDOQueryWrapper = new QueryWrapper<>();
            couponDOQueryWrapper.eq("id", id).eq("category", categoryEnum.name()).eq("publish", CouponPublishEnum.PUBLISH)
                    .gt("stock", 0);
            CouponDO couponDO = this.baseMapper.selectOne(couponDOQueryWrapper);
            if (ObjectUtils.isEmpty(couponDO)) {
                return JsonData.buildResult(BizCodeEnum.COUPON_NO_EXITS);
            }
            //优惠券是否可以领取

            JsonData jsonData = this.checkCoupon(couponDO, loginUser.getId(), categoryEnum.name(), CouponPublishEnum.PUBLISH.name());
            if (null != jsonData) {
                return jsonData;
            }
            //开始领取优惠券
            CouponRecordDO couponRecordDO = new CouponRecordDO();
            couponRecordDO.setCouponId(couponDO.getId());
            couponRecordDO.setCreateTime(new Date());
            couponRecordDO.setUseState(CouponStateEnum.NEW.name());
            couponRecordDO.setUserId(loginUser.getId());
            couponRecordDO.setUserName(loginUser.getName());
            couponRecordDO.setCouponTitle(couponDO.getCouponTitle());
            couponRecordDO.setStartTime(couponDO.getStartTime());
            couponRecordDO.setEndTime(couponDO.getEndTime());
            couponRecordDO.setPrice(couponDO.getPrice());
            couponRecordDO.setConditionPrice(couponDO.getConditionPrice());
            couponRecordService.save(couponRecordDO);
            UpdateWrapper<CouponDO> updateWrapper = new UpdateWrapper<CouponDO>()
                    .set("stock",couponDO.getStock()-1 ).gt("stock", 0).eq("id",couponDO.getId());
            this.baseMapper.update(couponDO,updateWrapper);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }

        return JsonData.buildSuccess();

    }

    /**
     * 新人注册之后，没有token的，不需要拦截
     * @param newUserRequest
     * @return
     */
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    @Override
    public JsonData initUserCoupon(NewUserRequest newUserRequest) {
        //1. 需要先构造一个用户
        LoginUser loginUser = new LoginUser();
        loginUser.setId(newUserRequest.getUserId());
        loginUser.setName(newUserRequest.getName());
        //2. 查询所有的新人优惠券，未过期的
        QueryWrapper<CouponDO> couponDOQueryWrapper = new QueryWrapper<>();
        couponDOQueryWrapper.eq("category",CouponCategoryEnum.NEW_USER.name())
                .eq("publish",CouponPublishEnum.PUBLISH.name())
                .gt("stock",0);
        List<CouponDO> couponDOS = this.baseMapper.selectList(couponDOQueryWrapper);
        if (null != couponDOS && !couponDOS.isEmpty()) {
            LoginInterceptor.threadLocal.set(loginUser);
            couponDOS.stream().forEach(couponDO -> {
                Integer userLimit = couponDO.getUserLimit();
                for (Integer i = 0; i < userLimit; i++) {
                    //2.1 幂等操作，调用需要加锁
                    this.ReceiveCoupon(couponDO.getId(),CouponCategoryEnum.NEW_USER);
                }
            });
        }
        return JsonData.buildSuccess();
    }

    /**
     * 用户是否可以领取改优惠券
     *
     * @param couponDO
     * @param userId
     * @param category
     * @param publish
     */
    private JsonData checkCoupon(CouponDO couponDO, Long userId, String category, String publish) {
        long currentTime = CommonUtil.getCurrentTimeStamp();
        long startTime = couponDO.getStartTime().getTime();
        long endTime = couponDO.getEndTime().getTime();
        if (currentTime < startTime || currentTime > endTime) {
            return JsonData.buildResult(BizCodeEnum.COUPON_OUT_OF_TIME);
        }
        QueryWrapper<CouponRecordDO> couponRecordDOQueryWrapper = new QueryWrapper<>();
        couponRecordDOQueryWrapper.eq("coupon_id",couponDO.getId()).eq("user_id",userId);
        long count = couponRecordService.count(couponRecordDOQueryWrapper);
        if (count >= couponDO.getUserLimit()) {
            return JsonData.buildResult(BizCodeEnum.COUPON_OUT_OF_LIMIT);
        }
        return null;
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





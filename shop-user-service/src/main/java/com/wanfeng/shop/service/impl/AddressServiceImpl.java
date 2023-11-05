package com.wanfeng.shop.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanfeng.shop.enums.AddressEnum;
import com.wanfeng.shop.enums.BizCodeEnum;
import com.wanfeng.shop.exception.BizException;
import com.wanfeng.shop.interceptor.LoginInterceptor;
import com.wanfeng.shop.model.LoginUser;
import com.wanfeng.shop.service.AddressService;
import com.wanfeng.shop.mapper.AddressMapper;
import com.wanfeng.shop.model.entity.AddressDO;
import com.wanfeng.shop.model.request.AddressRequest;
import com.wanfeng.shop.model.vo.AddressVO;
import com.wanfeng.shop.util.JsonData;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 85975
 * @description 针对表【address(电商-公司收发货地址表)】的数据库操作Service实现
 * @createDate 2023-11-02 15:50:29
 */
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, AddressDO> implements AddressService {

    @Transactional
    @Override
    public void add(AddressRequest addressRequest) {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        //判空
        String receiveName = addressRequest.getReceiveName();
        String phone = addressRequest.getPhone();
        String province = addressRequest.getProvince();
        String city = addressRequest.getCity();
        String region = addressRequest.getRegion();
        String detailAddress = addressRequest.getDetailAddress();
        if (StringUtils.isAnyBlank(receiveName, phone, province, city, region, detailAddress)) {
            throw new BizException(BizCodeEnum.ADDRESS_PARAMS_INCOMPLETE);
        }
        Integer defaultStatus = addressRequest.getDefaultStatus();
        //是否是默认地址,并且每个人只能有最多10个地址
        if (defaultStatus == AddressEnum.default_status.getStatus()) {
            //查找数据库是否设置过默认地址
            AddressDO addressDO = this.baseMapper.selectOne(new QueryWrapper<AddressDO>().eq("user_id", loginUser.getId()).eq("default_status", AddressEnum.default_status.getStatus()));
            if (ObjectUtils.isNotEmpty(addressDO)) {
                addressDO.setDefaultStatus(AddressEnum.COMMON_STATUS.getStatus());
                this.baseMapper.update(addressDO, new QueryWrapper<AddressDO>().eq("id", addressDO.getId()));
            }
        }
        AddressDO addressDO = new AddressDO();
        BeanUtils.copyProperties(addressRequest, addressDO);
        addressDO.setUserId(loginUser.getId());
        addressDO.setCreateTime(new Date());
        this.baseMapper.insert(addressDO);
    }

    @Override
    public JsonData findAddressById(Long id) {
        if (!(null == id || id < 0)) {
            LoginUser loginUser = LoginInterceptor.threadLocal.get();
            AddressDO addressDO = this.baseMapper.selectOne(new QueryWrapper<AddressDO>().eq("id", id).eq("user_id", loginUser.getId()));
            if (ObjectUtils.isNotEmpty(addressDO)) {
                return JsonData.buildSuccess(addressDO);
            }
        }
        return JsonData.buildResult(BizCodeEnum.ADDRESS_PARAMS_ERROR);
    }

    @Override
    public JsonData deleteAddressById(Long id) {
        if (!(null == id || id < 0)) {
            LoginUser loginUser = LoginInterceptor.threadLocal.get();
            int delete = this.baseMapper.delete(new QueryWrapper<AddressDO>().eq("id", id).eq("user_id", loginUser.getId()));
            if (delete>0) {
                return JsonData.buildSuccess();
            }
        }
        return JsonData.buildResult(BizCodeEnum.ADDRESS_PARAMS_ERROR);
    }

    @Override
    public JsonData findAddressList() {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        List<AddressDO> addressDOS = this.baseMapper.selectList(new QueryWrapper<AddressDO>().eq("user_id", loginUser.getId()));
        List<AddressVO> addressVOList = null;
        if (null != addressDOS || addressDOS.isEmpty()) {
            addressVOList = addressDOS.stream().map(addressDO -> {
                AddressVO addressVO = new AddressVO();
                BeanUtils.copyProperties(addressDO, addressVO);
                return addressVO;
            }).collect(Collectors.toList());
        }
        return JsonData.buildSuccess(addressVOList);
    }

}





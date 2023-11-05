package com.wanfeng.shop.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanfeng.shop.mapper.BannerMapper;
import com.wanfeng.shop.model.entity.BannerDO;
import com.wanfeng.shop.service.BannerService;
import com.wanfeng.shop.util.JsonData;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 85975
* @description 针对表【banner】的数据库操作Service实现
* @createDate 2023-11-04 21:01:47
*/
@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, BannerDO>
    implements BannerService {

    @Override
    public JsonData findBannerList() {
        List<BannerDO> bannerDOS = this.baseMapper.selectList(new QueryWrapper<>());
        return JsonData.buildSuccess(bannerDOS);
    }
}





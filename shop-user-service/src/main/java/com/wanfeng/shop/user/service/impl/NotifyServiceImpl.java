package com.wanfeng.shop.user.service.impl;

import com.wanfeng.shop.constant.CacheKey;
import com.wanfeng.shop.enums.BizCodeEnum;
import com.wanfeng.shop.enums.SendCodeEnum;
import com.wanfeng.shop.user.component.MailService;
import com.wanfeng.shop.user.service.NotifyService;
import com.wanfeng.shop.util.CheckUtil;
import com.wanfeng.shop.util.CommonUtil;
import com.wanfeng.shop.util.JsonData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class NotifyServiceImpl implements NotifyService {
    @Autowired
    private MailService mailService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    private static final String SUBJECT = "wanfeng-shop注册邮件";
    private static final String CONTENT = "您的验证码是: %s  切勿将验证码泄露于他人 本条验证码有效期5分钟";
    public static final int CODE_EXPIRED = 60*1000*10;
    @Override
    public JsonData sendCode(SendCodeEnum sendCodeEnum, String to) {
        String cacheCodeKey = String.format(CacheKey.CHCHE_CODE_KEY, sendCodeEnum.name(), to);
        String cacheValue = stringRedisTemplate.opsForValue().get(cacheCodeKey);
        if (StringUtils.isNotBlank(cacheValue)) {
            //存在检查是否是60秒内发送
            long ttl = Long.parseLong(cacheValue.split("_")[1]);
            //当前时间戳-ttl  <60 不允许重复发送
            if (CommonUtil.getCurrentTimeStamp()-ttl < 1000*60) {
                return JsonData.buildResult(BizCodeEnum.CODE_LIMITED);
            }
        }
        String randomCode = CommonUtil.getRandomCode(6);
        String codeformat = randomCode+"_"+CommonUtil.getCurrentTimeStamp();
        stringRedisTemplate.opsForValue().set(cacheCodeKey, codeformat, CODE_EXPIRED, TimeUnit.MILLISECONDS);
        if (CheckUtil.isEmail(to)) {
            //是邮箱
            mailService.sendMail(to,SUBJECT,String.format(CONTENT,randomCode));
            return JsonData.buildSuccess("验证码成功发送至邮箱 请注意查收");
        }
        return JsonData.buildResult(BizCodeEnum.CODE_TO_ERROR);
    }

    @Override
    public boolean checkCode(SendCodeEnum sendCodeEnum, String code, String to) {
        String cacheCodeKey = String.format(CacheKey.CHCHE_CODE_KEY, sendCodeEnum.name(), to);
        String cacheCodeValue = stringRedisTemplate.opsForValue().get(cacheCodeKey);
        if (StringUtils.isNotBlank(cacheCodeValue)) {
            cacheCodeValue = cacheCodeValue.split("_")[0];
            if (cacheCodeValue.equals(code)) {
                //删除的是邮箱验证码
                stringRedisTemplate.delete(cacheCodeKey);
                return true;
            }
        }
        return false;
    }
}

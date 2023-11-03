package com.wanfeng.shop.user.controller;

import com.google.code.kaptcha.Producer;
import com.wanfeng.shop.enums.BizCodeEnum;
import com.wanfeng.shop.enums.SendCodeEnum;
import com.wanfeng.shop.exception.BizException;
import com.wanfeng.shop.user.service.NotifyService;
import com.wanfeng.shop.util.CommonUtil;
import com.wanfeng.shop.util.JsonData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/user/v1")
@Slf4j
public class NotifyController {

    @Autowired
    private Producer captchaProducer;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private NotifyService notifyService;

    @GetMapping("/captcha")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) {
        String text = captchaProducer.createText();
        log.info("图形验证码：{}", text);
        ValueOperations<String, String> forValue = stringRedisTemplate.opsForValue();
        String captchaKey = getCaptchaKey(request);
        String s = forValue.get(captchaKey);
        if (StringUtils.isBlank(s)) {
            //不存在redis，表示第一次注册
            forValue.set(captchaKey, text, 60 * 5, TimeUnit.SECONDS);
        } else {
            throw new BizException(BizCodeEnum.CODE_LIMITED);
        }
        BufferedImage image = captchaProducer.createImage(text);
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            ImageIO.write(image, "jpg", outputStream);
            outputStream.flush();
        } catch (IOException e) {
            log.error("获取图形验证码异常，{}", e);
            throw new RuntimeException(e);
        }

    }

    @GetMapping("send_code")
    public JsonData sendRegisterCode(@RequestParam(value = "to", required = true) String to, @RequestParam(value = "captahc", required = true) String captcha, HttpServletRequest request) {
        String captchaKey = getCaptchaKey(request);
        String chcheCaptcha = stringRedisTemplate.opsForValue().get(captchaKey);
        if (null != captcha && captcha.equalsIgnoreCase(chcheCaptcha)) {
            //成功，发送邮件
            //删除的是 图形验证码
            // TODO 高并发下可能会产生删除失败的问题
            stringRedisTemplate.delete(captchaKey);
            JsonData jsonData = notifyService.sendCode(SendCodeEnum.USER_REGISTER, to);
            return jsonData;
        } else {
            return JsonData.buildResult(BizCodeEnum.CODE_CAPTCHA_ERROR);
        }
    }

    private String getCaptchaKey(HttpServletRequest request) {
        String ipAddr = CommonUtil.getIpAddr(request);
        String agent = request.getHeader("User-Agent");
        String key = "user-service:captcha:" + CommonUtil.MD5(ipAddr + agent);
        return key;
    }
}

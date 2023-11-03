package com.wanfeng.shop.interceptor;

import com.wanfeng.shop.enums.BizCodeEnum;
import com.wanfeng.shop.model.LoginUser;
import com.wanfeng.shop.util.CommonUtil;
import com.wanfeng.shop.util.JWTUtil;
import com.wanfeng.shop.util.JsonData;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    public static ThreadLocal<LoginUser> threadLocal = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        if (StringUtils.isNotBlank(token)) {
            Claims claims = JWTUtil.checkJwt(token);
            if (ObjectUtils.isNotEmpty(claims)) {
                //解密token成功
                LoginUser loginUser = new LoginUser();
                loginUser.setId(Long.valueOf(claims.get("id").toString()));
                if (null != claims.get("name")) {
                    loginUser.setName(claims.get("name").toString());
                }
                if (null != claims.get("head_img")) {
                    loginUser.setHeadImg(claims.get("head_img").toString());
                }
                if (null != claims.get("slogan")) {
                    loginUser.setSlogan(claims.get("slogan").toString());
                }
                loginUser.setMail(claims.get("mail").toString());
                //传递用户数据有两种传递方式
                // 1. requestsetattr进行传递
                // 2. threadlocal创建线程内变量使用(高并发下保证数据的唯一性)
                threadLocal.set(loginUser);
                return true;
            }
            //解密token失败  自定义错误提示给前端
        }
        CommonUtil.sendJsonResponse(response, JsonData.buildResult(BizCodeEnum.ACCOUNT_NOLOGIN));
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}

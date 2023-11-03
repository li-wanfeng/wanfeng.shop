package com.wanfeng.shop.user.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanfeng.shop.enums.BizCodeEnum;
import com.wanfeng.shop.enums.SendCodeEnum;
import com.wanfeng.shop.interceptor.LoginInterceptor;
import com.wanfeng.shop.model.LoginUser;
import com.wanfeng.shop.user.mapper.UserMapper;
import com.wanfeng.shop.user.model.entity.UserDO;
import com.wanfeng.shop.user.model.request.UserLoginRequest;
import com.wanfeng.shop.user.model.request.UserRegisterRequest;
import com.wanfeng.shop.user.model.vo.UserVO;
import com.wanfeng.shop.user.service.NotifyService;
import com.wanfeng.shop.user.service.UserService;
import com.wanfeng.shop.util.CheckUtil;
import com.wanfeng.shop.util.CommonUtil;
import com.wanfeng.shop.util.JWTUtil;
import com.wanfeng.shop.util.JsonData;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

/**
* @author 85975
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-11-02 16:02:39
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO>
    implements UserService {

    @Autowired
    private NotifyService notifyService;
    @Override
    public JsonData userRegister(UserRegisterRequest registerRequest) {

        if (StringUtils.isBlank(registerRequest.getMail())) {
            return JsonData.buildError("邮箱不能为空");
        }
        if (!CheckUtil.isEmail(registerRequest.getMail())) {
            return JsonData.buildError("邮箱格式不正确");
        }
        //1. 判断验证码
        String code = registerRequest.getCode();
        if (StringUtils.isBlank(code)) {
            return JsonData.buildError("验证码不能为空");
        }
        if (!notifyService.checkCode(SendCodeEnum.USER_REGISTER, code, registerRequest.getMail())) {
            return JsonData.buildError("验证码错误");
        }
        //2. 判空
        if (StringUtils.isBlank(registerRequest.getName())) {
            return JsonData.buildError("昵称不能为空");
        }
        if (StringUtils.isBlank(registerRequest.getPwd())) {
            return JsonData.buildError("密码不能为空");
        }
        //3. 账号唯一性检查(邮箱)
        if (checkEmailUnique(registerRequest.getMail())) {
            UserDO userDO = new UserDO();
            BeanUtils.copyProperties(registerRequest,userDO);
            userDO.setCreateTime(new Date());
            //4. 密码加密
            //4.1 生成盐
            userDO.setSecret("$1$" + CommonUtil.getStringNumRandom(8));
            //4.3 密码+盐处理  存储
            String secretPwd = Md5Crypt.md5Crypt(registerRequest.getPwd().getBytes(StandardCharsets.UTF_8), userDO.getSecret());
            userDO.setPwd(secretPwd);

            //5. 插入数据库
            int insert = this.baseMapper.insert(userDO);
            return JsonData.buildSuccess();
        }
        return JsonData.buildError("邮箱已经注册");
    }

    @Override
    public JsonData userLogin(UserLoginRequest loginRequest) {
        //1. 判空

        if (StringUtils.isBlank(loginRequest.getMail())) {
            return JsonData.buildError("邮箱不能为空");
        }
        if (StringUtils.isBlank(loginRequest.getPwd())) {
            return JsonData.buildError("密码不能为空");
        }
        //2. 数据库查找mail
        List<UserDO> mail = this.baseMapper.selectList(new QueryWrapper<UserDO>().eq("mail", loginRequest.getMail()));
        if (null != mail || mail.size() != 1) {
            //3. 找到密码加密和数据库判断
            UserDO userDO = mail.get(0);
            String cryptPwd = Md5Crypt.md5Crypt(loginRequest.getPwd().getBytes(StandardCharsets.UTF_8), userDO.getSecret());
            if (cryptPwd.equals(userDO.getPwd())) {
                //登陆成功
                LoginUser loginUser = new LoginUser();
                BeanUtils.copyProperties(userDO, loginUser);
                String token = JWTUtil.geneJsonWebToken(loginUser);
                return JsonData.buildSuccess(token);
            }
        }
        //未注册 或 密码不正确
        return JsonData.buildResult(BizCodeEnum.ACCOUNT_UNREGISTER);
    }

    @Override
    public UserVO userDetail() {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        UserDO userDO = this.baseMapper.selectById(loginUser.getId());
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userDO, userVO);
        return userVO;
    }

    private boolean checkEmailUnique(String mail) {
        //高并发下保证注册唯一？
        Long l = this.baseMapper.selectCount(new QueryWrapper<UserDO>().eq("mail", mail));
        return l <= 0;
    }

}





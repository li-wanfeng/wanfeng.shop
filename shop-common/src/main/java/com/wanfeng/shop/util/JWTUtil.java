package com.wanfeng.shop.util;

import com.wanfeng.shop.enums.BizCodeEnum;
import com.wanfeng.shop.exception.BizException;
import com.wanfeng.shop.model.LoginUser;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Date;

@Slf4j
public class JWTUtil {

    private static final long EXPIRE = 1000 * 60 * 60 * 24 * 7;
    /**
     * token前缀
     */
    private static final String TOKEN_PREFX = "WANFENGSHOP";
    /**
     * 加密的密钥
     */
    private static final String SECRET = "wanfengshopwdavsdzv2  r24 rfdas";
    /**
     * subject
     */
    private static final String SUBJECT = "wanfeng";

    public static String geneJsonWebToken(LoginUser loginUser) {
        if (ObjectUtils.isEmpty(loginUser)) {
            throw new BizException(BizCodeEnum.ACCOUNT_UNREGISTER);
        }
        Long id = loginUser.getId();
        String name = loginUser.getName();
        String headImg = loginUser.getHeadImg();
        String slogan = loginUser.getSlogan();
        String mail = loginUser.getMail();
        String token = Jwts.builder().setSubject(SUBJECT)
                .claim("head_img", headImg)
                .claim("name", name)
                .claim("id", id)
                .claim("slogan", slogan)
                .claim("mail", mail)//设置的payload
                .setIssuedAt(new Date())//发布时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))//过期时间
                .signWith(SignatureAlgorithm.HS256, SECRET).compact();

        token = TOKEN_PREFX+token;
        return token;
    }
    public static Claims checkJwt(String token) {
        try {
            Claims body = Jwts.parser().setSigningKey(SECRET)//密钥
                    .parseClaimsJws(token.replace(TOKEN_PREFX, "")).getBody();
            return body;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException |
                 IllegalArgumentException e) {
            log.debug("token解密失败");
            return null;
        }
    }

}

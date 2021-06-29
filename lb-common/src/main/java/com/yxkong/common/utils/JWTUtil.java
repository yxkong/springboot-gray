package com.yxkong.common.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.yxkong.common.dto.LoginInfo;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * jwt 工具类
 * <p>
 *   建议不要用jwt存储用户的敏感信息，存储的信息都加密
 *   或者jwt 使用 md5加密作为key，将key和原始信息存入redis中
 * @Author: yxkong
 * @Date: 2021/6/27 5:16 下午
 * @version: 1.0
 */
public class JWTUtil {

    public static  int EXPIRE_FIELD = Calendar.HOUR;
    //过期时间
    public static final int EXPIRE_TIME = 1;
    public static final String LOGIN_INFO_KEY = "loginInfo";
    public static  final String SIGN_USER = "yxkong";
    //私钥
    private static final String JWT_SECRET = "gray_jwt_secret";

    public static String createAESToken(LoginInfo loginInfo){
        final String jwt = createJWT(loginInfo);
        return AESUtil.encrypt(jwt);
    }

    /**
     * 签发jwt token
     * @param loginInfo
     * @return
     */
    public static String createJWT(LoginInfo loginInfo) {
        Date now = new Date();
        // expire time
        Calendar expireTime = Calendar.getInstance();
        expireTime.add(EXPIRE_FIELD,EXPIRE_TIME);
        // header Map
        Map<String, Object> header = new HashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");

        return JWT.create()
                .withHeader(header)
                .withAudience(String.valueOf(loginInfo.getUserId()))
                .withClaim(LOGIN_INFO_KEY, JsonUtils.toJson(loginInfo))
                //签发人
                .withIssuer(SIGN_USER)
                //签发时间
                .withIssuedAt(now)
                .withExpiresAt(expireTime.getTime())
                .sign(Algorithm.HMAC256(JWT_SECRET))
                ;
    }
    public static boolean verifyAES(String token){
        return verify(AESUtil.decrypt(token));
    }
    /**
     * token校验
     * @param token
     * @return
     */
    public static boolean verify(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
            final JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public static LoginInfo getAESLoginInfo(String token){
        return getLoginInfo(AESUtil.decrypt(token));
    }
    /**
     * 从jwt中解析出loginInfo
     * @param token
     * @return
     */
    public static LoginInfo getLoginInfo(String token){
        Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
        final JWTVerifier verifier = JWT.require(algorithm).build();
        try {
            DecodedJWT jwt = verifier.verify(token);
            final Claim claim = jwt.getClaim(LOGIN_INFO_KEY);
            if (Objects.nonNull(claim) && !StringUtils.isEmpty(claim.asString())){
                return JsonUtils.fromJson(claim.asString(),LoginInfo.class);
            }
        } catch (JWTVerificationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
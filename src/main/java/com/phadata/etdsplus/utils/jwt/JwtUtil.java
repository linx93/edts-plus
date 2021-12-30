package com.phadata.etdsplus.utils.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.*;
import java.util.Map.Entry;

/**
 * JWT的token生成和解密的工具类（pom.xml中需要添加依赖java-JWT依赖）
 * 生成的token默认过期时间为7天
 *
 * @author xionglin
 * @since 2018.11.7 01:05:20
 */
@Slf4j
public class JwtUtil {
    /**
     * 默认的自定义的密钥
     */
    private static final String SECRET = "tianji";

    private static final int EXPIRE = 7;

    /**
     * 默认的头map(线程安全)可根据需求自己设置defaultHeaderMap中alg和typ的的值；一般使用默认值
     */
    private static final Map<String, Object> DEFAULT_HEADER_MAP = Collections.synchronizedMap(new HashMap<>(3));

    //默认头设置值
    static {
        DEFAULT_HEADER_MAP.put("alg", "HS256");
        DEFAULT_HEADER_MAP.put("typ", "JWT");
    }

    /**
     * 私有化构成器避免工具类被实例化
     */
    private JwtUtil() {
    }


    /**
     * 创建JwtToken
     *
     * @param payLoadMap 数据
     * @return token
     */
    public static String createJwtToken(Map<String, Object> payLoadMap) {
        String jwtToken = null;
        try {
            Builder builder = getBuilder(DEFAULT_HEADER_MAP, payLoadMap);
            //加密
            assert builder != null;
            jwtToken = builder.sign(Algorithm.HMAC256(SECRET));
        } catch (IllegalArgumentException | JWTCreationException e) {
            log.error("createJwtToken  error >> ex = {}", ExceptionUtils.getStackTrace(e));
        }
        return jwtToken;
    }


    /**
     * 创建JwtToken
     *
     * @param payLoadMap 数据
     * @param secret     密钥
     * @return token
     */
    public static String createJwtToken(Map<String, Object> payLoadMap, String secret) {
        String jwtToken = null;
        try {
            Builder builder = getBuilder(DEFAULT_HEADER_MAP, payLoadMap);
            assert builder != null;
            if (secret == null) {
                //默认秘钥加密
                jwtToken = builder.sign(Algorithm.HMAC256(SECRET));
            } else {
                //默认秘钥加密
                jwtToken = builder.sign(Algorithm.HMAC256(secret));
            }
        } catch (IllegalArgumentException | JWTCreationException e) {
            log.error("createJwtToken  error >> ex = {}", ExceptionUtils.getStackTrace(e));
        }
        return jwtToken;
    }

    /**
     * 创建JwtToken
     *
     * @param headerMap  头
     * @param payLoadMap 数据
     * @return token
     */
    public static String createJwtToken(Map<String, Object> headerMap, Map<String, Object> payLoadMap) {
        String jwtToken = null;
        try {
            Builder builder = getBuilder(headerMap, payLoadMap);
            assert builder != null;
            jwtToken = builder.sign(Algorithm.HMAC256(SECRET));
        } catch (IllegalArgumentException e) {
            log.error("createJwtToken  error >> ex = {}", ExceptionUtils.getMessage(e));
        } catch (JWTCreationException e) {
            log.error("createJwtToken  error >> ex = {}", ExceptionUtils.getStackTrace(e));
        }
        return jwtToken;
    }

    /**
     * 创建JwtToken
     *
     * @param headerMap  头
     * @param payLoadMap 数据
     * @param secret     密钥
     * @return token
     */
    public static String createJwtToken(Map<String, Object> headerMap, Map<String, Object> payLoadMap, String secret) {
        String jwtToken = null;
        try {
            Builder builder = getBuilder(headerMap, payLoadMap);
            assert builder != null;
            if (secret == null) {
                //用默认加密
                jwtToken = builder.sign(Algorithm.HMAC256(SECRET));
            } else {
                //加密
                jwtToken = builder.sign(Algorithm.HMAC256(secret));
            }
        } catch (IllegalArgumentException e) {
            log.error("createJwtToken  error >> ex = {}", ExceptionUtils.getMessage(e));
        } catch (JWTCreationException e) {
            log.error("createJwtToken  error >> ex = {}", ExceptionUtils.getStackTrace(e));
        }
        return jwtToken;
    }

    /**
     * 验证token
     *
     * @param token jwt token
     * @return payload
     */
    public static Map<String, Claim> verifyToken(String token) {
        JWTVerifier jwtVerifier = null;
        try {
            jwtVerifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        } catch (IllegalArgumentException e) {
            log.error("verifyToken  error >> ex = {}", ExceptionUtils.getMessage(e));
        }
        DecodedJWT verify;
        try {
            assert jwtVerifier != null;
            verify = jwtVerifier.verify(token);
        } catch (AlgorithmMismatchException e) {
            log.error("verifyToken  error >> ex = {}", ExceptionUtils.getStackTrace(e));
            throw new JwtVerifyException("token加密的算法和解密用的算法不一致!");
        } catch (SignatureVerificationException e) {
            log.error("verifyToken  error >> ex = {}", ExceptionUtils.getStackTrace(e));
            throw new JwtVerifyException("token的签名无效!");
        } catch (TokenExpiredException e) {
            log.error("verifyToken  error >> ex = {}", ExceptionUtils.getStackTrace(e));
            throw new JwtVerifyException("token令牌已过期!");
        } catch (InvalidClaimException e) {
            log.error("verifyToken  error >> ex = {}", ExceptionUtils.getStackTrace(e));
            throw new JwtVerifyException("token中claim被修改，所以token认证失败");
        } catch (Exception e) {
            log.error("verifyToken  error >> ex = {}", ExceptionUtils.getStackTrace(e));
            throw new JwtVerifyException("登录凭证无效");
        }
        return verify.getClaims();

    }

    /**
     * 解码token
     *
     * @param token jwt token
     * @return payload
     */
    public static Map<String, Claim> verifyToken(String token, String secret) {
        JWTVerifier jwtVerifier = null;
        try {
            if (secret == null || "".equals(secret.trim())) {
                jwtVerifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            } else {
                jwtVerifier = JWT.require(Algorithm.HMAC256(secret)).build();
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        DecodedJWT verify;
        try {
            assert jwtVerifier != null;
            verify = jwtVerifier.verify(token);
        } catch (AlgorithmMismatchException e) {
            log.error("verifyToken  error >> ex = {}", ExceptionUtils.getStackTrace(e));
            throw new JwtVerifyException("token加密的算法和解密用的算法不一致!");
        } catch (SignatureVerificationException e) {
            log.error("verifyToken  error >> ex = {}", ExceptionUtils.getStackTrace(e));
            throw new JwtVerifyException("token签名无效!");
        } catch (TokenExpiredException e) {
            log.error("verifyToken  error >> ex = {}", ExceptionUtils.getStackTrace(e));
            throw new JwtVerifyException("token令牌已过期!");
        } catch (InvalidClaimException e) {
            log.error("verifyToken  error >> ex = {}", ExceptionUtils.getStackTrace(e));
            throw new JwtVerifyException("token中claim被修改，所以token认证失败");
        } catch (Exception e) {
            log.error("verifyToken  error >> ex = {}", ExceptionUtils.getStackTrace(e));
            throw new JwtVerifyException("登录凭证无效（过期），请重新登录");
        }
        return verify.getClaims();
    }


    /**  Payload 默认给出的几个claim
     "iss" => "http://example.org",   #非必须。issuer 请求实体，可以是发起请求的用户的信息，也可是jwt的签发者。
     "iat" => 1356999524,                #非必须。issued at。 token创建时间，unix时间戳格式
     "exp" => "1548333419",            #非必须。expire 指定token的生命周期。unix时间戳格式
     "aud" => "http://example.com",   #非必须。接收该JWT的一方。
     "sub" => "jrocket@example.com",  #非必须。该JWT所面向的用户
     "nbf" => 1357000000,   # 非必须。not before。如果当前时间在nbf里的时间之前，则Token不被接受；一般都会留一些余地，比如几分钟。
     "jti" => '222we',     # 非必须。JWT ID。针对当前token的唯一标识
     */
    /**
     * 为加密构建Builder对象
     *
     * @param headerMap  头 map
     * @param payLoadMap 数据 map
     * @return builder对象
     */
    private static Builder getBuilder(Map<String, Object> headerMap, Map<String, Object> payLoadMap) {

        if (headerMap.size() <= 0 || payLoadMap.size() <= 0) {
            return null;
        }
        Builder builder = JWT.create();
        builder.withHeader(headerMap);
        //签发时间
        builder.withIssuedAt(new Date());
        //默认过期的时间为7天
        builder.withExpiresAt(getDate(EXPIRE));
        Set<Entry<String, Object>> entrySet = payLoadMap.entrySet();
        for (Entry<String, Object> entry : entrySet) {
            if ("exp".equals(entry.getKey())) {
                //过期时间(几天过期)会覆盖默认的过期时间
                builder.withExpiresAt(getDate(Integer.parseInt(String.valueOf(entry.getValue()))));
            } else if ("iss".equals(entry.getKey())) {
                //签发人
                builder.withIssuer(entry.getValue().toString());
            } else if ("subject".equals(entry.getKey())) {
                //主题
                builder.withSubject(entry.getValue().toString());
            } else if ("JWTId".equals(entry.getKey())) {
                //JWTToken的id，针对当前token的唯一标识
                builder.withJWTId(entry.getValue().toString());
            } else {
                //自定义部分（键和值都是String）
                builder.withClaim(entry.getKey(), entry.getValue().toString());
            }
        }
        return builder;
    }


    /**
     * 获取(days)天后时间
     *
     * @param days 天
     * @return 日期
     */
    private static Date getDate(int days) {
        return new Date(System.currentTimeMillis() + (days * 24 * 60 * 60 * 1000));
    }


}

package com.starimmortal.security.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.starimmortal.core.constant.CacheConstant;
import com.starimmortal.core.constant.Constant;
import com.starimmortal.core.constant.PatternConstant;
import com.starimmortal.core.constant.StringConstant;
import com.starimmortal.core.enumeration.Code;
import com.starimmortal.core.exception.TokenInvalidException;
import com.starimmortal.core.util.DateUtil;
import com.starimmortal.core.util.GenericJacksonUtil;
import com.starimmortal.core.vo.ResponseVO;
import com.starimmortal.security.constant.TokenConstant;
import com.starimmortal.security.vo.TokenVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * JWT令牌工具类
 *
 * @author william@StarImmortal
 * @date 2022/10/19
 */
@Component
public class JwtUtil {
    /**
     * 密钥
     */
    private static String secret;

    /**
     * 访问令牌过期时间
     */
    private static long accessExpires;

    /**
     * 刷新令牌过期时间
     */
    private static long refreshExpires;

    /**
     * 白名单
     */
    private static String[] whiteList;

    /**
     * 算法
     */
    private static Algorithm algorithm;

    private static RedisUtil redisUtil;

    private static HttpServletResponse response;

    @Value("${security.secret}")
    public void setSecret(String secret) {
        JwtUtil.secret = secret;
        JwtUtil.algorithm = Algorithm.HMAC256(secret);
    }

    @Value("${security.access-expires}")
    public void setAccessExpires(long accessExpires) {
        JwtUtil.accessExpires = accessExpires;
    }

    @Value("${security.refresh-expires}")
    public void setRefreshExpires(long refreshExpires) {
        JwtUtil.refreshExpires = refreshExpires;
    }

    @Value("${security.white-list}")
    public void setWhiteList(String[] whiteList) {
        JwtUtil.whiteList = whiteList;
    }

    @Autowired
    public void setRedisUtil(RedisUtil redisUtil) {
        JwtUtil.redisUtil = redisUtil;
    }

    @Autowired
    public void setResponse(HttpServletResponse response) {
        JwtUtil.response = response;
    }

    public static long getAccessExpires() {
        return JwtUtil.accessExpires;
    }

    public static long getRefreshExpires() {
        return JwtUtil.refreshExpires;
    }

    public static String[] getWhiteList() {
        return JwtUtil.whiteList;
    }

    /**
     * 生成令牌
     *
     * @param tokenType 令牌类型
     * @param username  用户名
     * @param expires   过期时间
     * @return 令牌
     */
    public static String generateToken(String tokenType, String username, long expires) {
        return JWT.create()
                .withClaim("type", tokenType)
                .withClaim("username", username)
                .withExpiresAt(DateUtil.addSeconds(expires))
                .withJWTId(UUID.randomUUID().toString())
                .sign(algorithm);
    }

    /**
     * 生成访问令牌
     *
     * @param username 用户名
     * @return 访问令牌
     */
    public static String generateAccessToken(String username) {
        return generateToken(TokenConstant.ACCESS_TYPE, username, JwtUtil.accessExpires);
    }

    /**
     * 生成刷新令牌
     *
     * @param username 用户名
     * @return 刷新令牌
     */
    public static String generateRefreshToken(String username) {
        return generateToken(TokenConstant.ACCESS_TYPE, username, JwtUtil.accessExpires);
    }

    /**
     * 生成双令牌
     *
     * @param username 用户名
     * @return 双令牌
     */
    public static TokenVO generateTokens(String username) {
        String access = generateToken(TokenConstant.ACCESS_TYPE, username, JwtUtil.accessExpires);
        String refresh = generateToken(TokenConstant.REFRESH_TYPE, username, JwtUtil.refreshExpires);
        return new TokenVO(access, refresh);
    }

    /**
     * 破译访问令牌
     *
     * @param token 访问令牌
     * @return 密钥内容
     */
    public static Map<String, Claim> decodeAccessToken(String token) {
        JWTVerifier accessVerifier = JWT.require(JwtUtil.algorithm)
                .acceptExpiresAt(JwtUtil.accessExpires)
                .build();
        DecodedJWT jwt = accessVerifier.verify(token);
        checkTokenExpired(jwt.getExpiresAt());
        checkTokenType(jwt.getClaim("type").asString(), TokenConstant.ACCESS_TYPE);
        return jwt.getClaims();
    }

    /**
     * 破译刷新令牌
     *
     * @param token 刷新令牌
     * @return 密钥内容
     */
    public static Map<String, Claim> decodeRefreshToken(String token) {
        JWTVerifier refreshVerifier = JWT.require(JwtUtil.algorithm)
                .acceptExpiresAt(refreshExpires)
                .build();
        DecodedJWT jwt = refreshVerifier.verify(token);
        checkTokenExpired(jwt.getExpiresAt());
        checkTokenType(jwt.getClaim("type").asString(), TokenConstant.REFRESH_TYPE);
        return jwt.getClaims();
    }

    /**
     * 检查令牌类型
     *
     * @param type       源
     * @param targetType 目标
     */
    public static void checkTokenType(String type, String targetType) {
        if (type == null || !type.equals(targetType)) {
            throw new InvalidClaimException("token type is invalid");
        }
    }

    /**
     * 检查令牌是否过期
     *
     * @param expiresAt 过期时间
     */
    public static void checkTokenExpired(Date expiresAt) {
        if (expiresAt.toInstant().isBefore(Instant.now())) {
            throw new TokenExpiredException("token is expired", expiresAt.toInstant());
        }
    }

    /**
     * 检查令牌是否合法
     *
     * @param targetUsername 目标用户名
     */
    public static void checkTokenValid(String token, String targetUsername) {
        String username = getUsername(token);
        assert username != null;
        if (!username.equals(targetUsername)) {
            throw new TokenInvalidException("token is valid");
        }
    }

    /**
     * 从令牌中解析用户名信息
     *
     * @param token 令牌
     * @return 用户名
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT decode = JWT.decode(token);
            return decode.getClaims().get("username").asString();
        } catch (JWTVerificationException e) {
            responseError(response, Code.TOKEN_INVALID.getCode(), Code.TOKEN_INVALID.getZhDescription());
            return null;
        }
    }

    /**
     * 从令牌中解析令牌唯一编号
     *
     * @param token 令牌
     * @return JWT_ID
     */
    public static String getJti(String token) {
        try {
            DecodedJWT decode = JWT.decode(token);
            return decode.getClaims().get(TokenConstant.PAYLOAD_JWT_ID).asString();
        } catch (JWTVerificationException e) {
            responseError(response, Code.TOKEN_INVALID.getCode(), Code.TOKEN_INVALID.getZhDescription());
            return null;
        }
    }

    /**
     * 从令牌中解析过期时间
     *
     * @param token 令牌
     * @return 过期时间
     */
    public static Long getExpires(String token) {
        try {
            DecodedJWT decode = JWT.decode(token);
            return decode.getClaims().get(TokenConstant.PAYLOAD_EXPIRES_AT).asLong();
        } catch (JWTVerificationException e) {
            responseError(response, Code.TOKEN_INVALID.getCode(), Code.TOKEN_INVALID.getZhDescription());
            return null;
        }
    }

    /**
     * 校验请求头并从请求中解析令牌
     *
     * @param request 请求
     * @return 令牌
     */
    public static String decodeTokenFromRequest(HttpServletRequest request, HttpServletResponse response) {
        String header = request.getHeader(Constant.AUTHORIZATION_HEADER);
        if (!StringUtils.hasText(header)) {
            responseError(response, Code.UN_AUTHENTICATION.getCode(), "请传入认证头字段");
        }
        String[] splits = header.split(StringConstant.SPACE);
        if (splits.length != 2) {
            responseError(response, Code.UN_AUTHENTICATION.getCode(), "认证头字段解析失败");
        }
        if (!Pattern.matches(PatternConstant.BEARER_PATTERN, splits[0])) {
            responseError(response, Code.UN_AUTHENTICATION.getCode(), "认证头字段解析失败");
        }
        return splits[1];
    }

    /**
     * 将访问令牌添加至缓存中
     *
     * @param username    用户名
     * @param accessToken 访问令牌
     */
    public static void saveToken(String username, String accessToken) {
        if (StringUtils.hasText(username) && StringUtils.hasText(accessToken)) {
            redisUtil.set(String.format(CacheConstant.LOGIN_TOKEN_KEY, username), accessToken, JwtUtil.accessExpires);
        }
    }

    /**
     * 从缓存中删除访问令牌
     *
     * @param username 用户名
     */
    public static void removeToken(String username) {
        if (StringUtils.hasText(username)) {
            redisUtil.delete(String.format(CacheConstant.LOGIN_TOKEN_KEY, username));
        }
    }

    /**
     * 判断Redis中是否存在令牌
     *
     * @param username 用户名
     * @return 是否存在
     */
    public static boolean hasToken(String username) {
        String redisKey = String.format(CacheConstant.LOGIN_TOKEN_KEY, username);
        if (redisUtil.hasKey(redisKey)) {
            return StringUtils.hasText(redisUtil.get(redisKey));
        }
        return false;
    }

    /**
     * 判断令牌是否存在Redis黑名单中
     *
     * @param token 令牌
     * @return 是否存在
     */
    public static boolean isInBlacklist(String token) {
        String redisKey = String.format(CacheConstant.TOKEN_BLACKLIST_KEY, JwtUtil.getJti(token));
        return redisUtil.hasKey(redisKey);
    }

    /**
     * 加入黑名单
     *
     * @param token 令牌
     */
    public static void blacklist(String token) {
        String jti = JwtUtil.getJti(token);
        Long expires = JwtUtil.getExpires(token);
        assert expires != null;
        if (DateUtil.minusSeconds(expires) > 0) {
            redisUtil.set(String.format(CacheConstant.TOKEN_BLACKLIST_KEY, jti), StringConstant.EMPTY, DateUtil.minusSeconds(expires));
        }
    }

    /**
     * 吊销用户令牌
     *
     * @param username 用户名
     */
    public static void revokeUserToken(String username) {
        if (StringUtils.hasText(username) && JwtUtil.hasToken(username)) {
            String accessToken = redisUtil.get(String.format(CacheConstant.LOGIN_TOKEN_KEY, username));
            JwtUtil.removeToken(username);
            JwtUtil.blacklist(accessToken);
        }
    }

    /**
     * 封装并输出响应结果
     *
     * @param servletResponse 响应结果
     * @param code            消息码
     * @param message         消息
     */
    public static void responseError(ServletResponse servletResponse, Integer code, String message) {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ResponseVO vo = new ResponseVO(code, message, HttpStatus.UNAUTHORIZED);
        OutputStream outputStream;
        try {
            outputStream = response.getOutputStream();
            outputStream.write(GenericJacksonUtil.objectToJson(vo).getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

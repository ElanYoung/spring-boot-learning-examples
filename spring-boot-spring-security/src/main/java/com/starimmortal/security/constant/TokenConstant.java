package com.starimmortal.security.constant;

/**
 * 令牌相关常量
 *
 * @author pedro@TaleLin
 */
public class TokenConstant {
    /**
     * 访问令牌
     */
    public final static String ACCESS_TYPE = "access";

    /**
     * 刷新令牌
     */
    public final static String REFRESH_TYPE = "refresh";

    /**
     * JWT唯一身份标识
     */
    public final static String PAYLOAD_JWT_ID = "jti";

    /**
     * JWT过期时间
     */
    public final static String PAYLOAD_EXPIRES_AT = "exp";
}

package com.starimmortal.core.constant;

/**
 * 通用常量
 *
 * @author william@StarImmortal
 * @date 2021/11/05
 */
public class Constant {
    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";

    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";

    /**
     * 通用成功标识
     */
    public static final String SUCCESS = "0";

    /**
     * 通用失败标识
     */
    public static final String FAIL = "1";

    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "Success";

    /**
     * 注销
     */
    public static final String LOGOUT = "Logout";

    /**
     * 注册
     */
    public static final String REGISTER = "Register";

    /**
     * 登录失败
     */
    public static final String LOGIN_FAIL = "Error";

    /**
     * 验证码有效期（分钟）
     */
    public static final Integer CAPTCHA_EXPIRATION_MINUTES = 2;

    /**
     * 验证码有效期（秒）
     */
    public static final Integer CAPTCHA_EXPIRATION_SECONDS = 300;

    /**
     * 请求头
     */
    public static final String HEADER = "header";

    /**
     * 令牌
     */
    public static final String TOKEN = "token";

    /**
     * 认证头
     */
    public static final String AUTHORIZATION_HEADER = "Authorization";

    /**
     * 令牌前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 用户ID
     */
    public static final String JWT_USER_ID = "uid";

    /**
     * 用户名
     */
    public static final String JWT_USERNAME = "username";

    /**
     * 用户权限
     */
    public static final String JWT_USER_AUTHORITIES = "authorities";

    /**
     * 令牌颁布时间
     */
    public static final String JWT_ISSUED = "issued";

    /**
     * 令牌过期时间
     */
    public static final String JWT_EXPIRES = "expires";

    /**
     * 用户头像上传路径
     */
    public static final String USER_AVATAR_PATH = "avatar";

    /**
     * 省
     */
    public static final String PROVINCE = "province";

    /**
     * 市
     */
    public static final String CITY = "city";

    /**
     * 区
     */
    public static final String COUNTY = "county";

    /**
     * 城镇
     */
    public static final String TOWN = "town";

    /**
     * 乡村
     */
    public static final String VILLAGE = "village";
}

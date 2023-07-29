package com.starimmortal.core.constant;

/**
 * 缓存常量
 *
 * @author william@StarImmortal
 * @date 2021/11/5
 */
public class CacheConstant {

	/**
	 * 验证码 Redis Key
	 */
	public static final String CAPTCHA_CODE_KEY = "captcha:%s:string";

	/**
	 * 登录/注册短信验证码 Redis Key
	 */
	public static final String SMS_CODE_KEY = "sms:user:%s:string";

	/**
	 * 登录用户 Redis Key
	 */
	public static final String LOGIN_TOKEN_KEY = "login:user:token:%s:string";

	/**
	 * 令牌黑名单 Redis Key
	 */
	public static final String TOKEN_BLACKLIST_KEY = "token:black:%s:string";

	/**
	 * 防重提交 Redis Key
	 */
	public static final String REPEAT_SUBMIT_KEY = "repeat:submit:";

	/**
	 * 限流 Redis Key
	 */
	public static final String RATE_LIMIT_KEY = "rate:limit:";

	/**
	 * 登录账户密码错误次数 Redis Key
	 */
	public static final String PASSWORD_ERROR_COUNT_KEY = "password:error.count:";

	/**
	 * 手机号登录验证码错误次数 Redis Key
	 */
	public static final String PHONE_SMS_ERROR_COUNT_KEY = "sms:error.count:%s:string";

	/**
	 * Shiro权限缓存 Redis Key
	 */
	public static final String SHIRO_CACHE_KEY = "shiro:cache:com.starimmortal.shiro.shiro.ShiroRealm.authorizationCache:";

	/**
	 * 参数管理 Redis Key
	 */
	public static final String SYS_CONFIG_KEY = "system:config:";

	/**
	 * 字典管理 Redis Key
	 */
	public static final String SYS_DICT_KEY = "system:dict:";

	/**
	 * 用户获赞总数
	 */
	public static final String USER_TOTAL_LIKES = "user:likes:%s:long";

	/**
	 * 用户关注总数
	 */
	public static final String USER_TOTAL_FOLLOWERS = "user:followers:%s:long";

	/**
	 * 用户粉丝总数
	 */
	public static final String USER_TOTAL_FANS = "user:fans:%s:long";

	/**
	 * 用户关系：用于判断他们之间是否互粉
	 */
	public static final String USER_RELATIONSHIP = "user:relationship:%s:%s:long";

	/**
	 * 笔记获赞总数
	 */
	public static final String NOTE_TOTAL_LIKES = "note:likes:%s:long";

	/**
	 * 笔记评论总数
	 */
	public static final String NOTE_TOTAL_COMMENTS = "note:comment:count:%s:long";

	/**
	 * 笔记评论点赞总数
	 */
	public static final String NOTE_TOTAL_COMMENT_LIKES = "note:comment:likes:%s:long";

	/**
	 * 用户点赞笔记
	 */
	public static final String USER_LIKE_NOTE = "user:like:note:%s:%s:long";

	/**
	 * 用户点赞笔记评论
	 */
	public static final String USER_LIKE_NOTE_COMMENT = "user:like:note:comment:%s:long";

}

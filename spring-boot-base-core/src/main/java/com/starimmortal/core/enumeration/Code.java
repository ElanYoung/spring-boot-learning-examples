package com.starimmortal.jwt.enumeration;

/**
 * @author william@StarImmortal
 * @date 2021/02/08
 */
public enum Code {
    /**
     * 成功
     */
    SUCCESS(0, "OK", "成功"),

    /**
     * 创建成功
     */
    CREATED(1, "Created", "创建成功"),

    /**
     * 更新成功
     */
    UPDATED(2, "Updated", "更新成功"),

    /**
     * 删除成功
     */
    DELETED(3, "Deleted", "删除成功"),

    /**
     * 失败
     */
    FAIL(10200, "Failed", "失败"),

    /**
     * 认证失败
     */
    UN_AUTHORIZATION(10000, "Authorization Failed", "认证失败"),

    /**
     * 授权失败
     */
    UN_AUTHENTICATION(10010, "Authentication Failed", "授权失败"),

    /**
     * 资源不存在
     */
    NOT_FOUND(10020, "Not Found", "资源不存在"),

    /**
     * 参数错误
     */
    PARAMETER_ERROR(10030, "Parameters Error", "参数错误"),

    /**
     * 令牌失效
     */
    TOKEN_INVALID(10040, "Token Invalid", "令牌失效"),

    /**
     * 令牌过期
     */
    TOKEN_EXPIRED(10050, "Token Expired", "令牌过期"),

    /**
     * 字段重复
     */
    DUPLICATED(10060, "Duplicated", "字段重复"),

    /**
     * 服务器未知错误
     */
    INTERNAL_SERVER_ERROR(9999, "Internal Server Error", "服务器未知错误"),

    /**
     * 禁止操作
     */
    FORBIDDEN(10070, "Forbidden", "禁止操作"),

    /**
     * 请求方法不允许
     */
    METHOD_NOT_ALLOWED(10080, "Method Not Allowed", "请求方法不允许"),

    /**
     * 刷新令牌获取失败
     */
    REFRESH_FAILED(10100, "Get Refresh Token Failed", "刷新令牌获取失败"),

    /**
     * 文件体积过大
     */
    FILE_TOO_LARGE(10110, "File Too Large", "文件体积过大"),

    /**
     * 文件数量过多
     */
    FILE_TOO_MANY(10120, "File Too Many", "文件数量过多"),

    /**
     * 文件扩展名不符合规范
     */
    FILE_EXTENSION(10130, "File Extension Not Allowed", "文件扩展名不符合规范"),

    /**
     * 请求过于频繁，请稍后重试
     */
    REQUEST_LIMIT(10140, "Too Many Requests", "请求过于频繁，请稍后重试");

    private final Integer code;

    private final String description;

    private final String zhDescription;

    private Code(Integer code, String description, String zhDescription) {
        this.code = code;
        this.description = description;
        this.zhDescription = zhDescription;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getDescription() {
        return this.description;
    }

    public String getZhDescription() {
        return this.zhDescription;
    }
}

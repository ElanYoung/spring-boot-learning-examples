package com.starimmortal.core.enumeration;

/**
 * 消息码枚举类
 *
 * @author william@StarImmortal
 * @date 2021/02/08
 */
public enum Code {
    SUCCESS(0, "OK", "成功"),
    CREATED(1, "Created", "创建成功"),
    UPDATED(2, "Updated", "更新成功"),
    DELETED(3, "Deleted", "删除成功"),
    INTERNAL_SERVER_ERROR(9999, "Internal Server Error", "服务器未知错误"),
    UN_AUTHENTICATION(10000, "Authentication Failed", "认证失败"),
    UN_AUTHORIZATION(10010, "Authorization Failed", "授权失败"),
    NOT_FOUND(10020, "Not Found", "资源不存在"),
    NOT_FOUND_TOKEN(10021, "Not Found TOKEN", "未携带令牌"),
    PARAMETER_ERROR(10030, "Parameters Error", "参数错误"),
    TOKEN_INVALID(10040, "Token Invalid", "令牌失效"),
    TOKEN_EXPIRED(10050, "Token Expired", "令牌过期"),
    ACCESS_TOKEN_EXPIRED(10051, "Access Token Expired", "访问令牌过期"),
    REFRESH_TOKEN_EXPIRED(10052, "Refresh Token Expired", "刷新令牌过期"),
    DUPLICATED(10060, "Duplicated", "字段重复"),
    DUPLICATED_DATABASE(10061, "Duplicated Database", "数据库中已存在该记录"),
    FORBIDDEN(10070, "Forbidden", "禁止操作"),
    METHOD_NOT_ALLOWED(10080, "Method Not Allowed", "请求方法不允许"),
    REFRESH_FAILED(10100, "Get Refresh Token Failed", "刷新令牌获取失败"),
    FILE_TOO_LARGE(10110, "File Too Large", "文件体积过大"),
    FILE_TOO_MANY(10120, "File Too Many", "文件数量过多"),
    FILE_EXTENSION(10130, "File Extension Not Allowed", "文件扩展名不符合规范"),
    REQUEST_LIMIT(10140, "Too Many Requests", "请求过于频繁，请稍后重试"),
    FAIL(10200, "Failed", "失败");

    /**
     * 消息码
     */
    private final Integer code;

    /**
     * 描述
     */
    private final String description;

    /**
     * 中文描述
     */
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

package com.starimmortal.core.vo;

import com.starimmortal.jwt.enumeration.Code;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一API响应结果封装
 *
 * @author william@StarImmortal
 * @date 2022/09/19
 */
@Data
@NoArgsConstructor
public class ResponseVO<T> {
    /**
     * 状态码
     */
    private Integer code;

    /**
     * 消息
     */
    private String message;

    /**
     * 数据对象
     */
    private T data;

    public ResponseVO(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseVO(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ResponseVO<T> success() {
        return new ResponseVO<T>(Code.SUCCESS.getCode(), Code.SUCCESS.getDescription(), null);
    }

    public static <T> ResponseVO<T> success(String message) {
        return new ResponseVO<T>(Code.SUCCESS.getCode(), message, null);
    }

    public static <T> ResponseVO<T> success(T data) {
        return new ResponseVO<T>(Code.SUCCESS.getCode(), Code.SUCCESS.getDescription(), data);
    }

    public static <T> ResponseVO<T> success(String message, T data) {
        return new ResponseVO<T>(Code.SUCCESS.getCode(), message, data);
    }

    public static <T> ResponseVO<T> error() {
        return new ResponseVO<T>(Code.FAIL.getCode(), Code.FAIL.getDescription(), null);
    }

    public static <T> ResponseVO<T> error(String message) {
        return new ResponseVO<T>(Code.FAIL.getCode(), message, null);
    }

    public static <T> ResponseVO<T> error(T data) {
        return new ResponseVO<T>(Code.FAIL.getCode(), Code.FAIL.getDescription(), data);
    }

    public static <T> ResponseVO<T> error(String message, T data) {
        return new ResponseVO<T>(Code.FAIL.getCode(), message, data);
    }
}

package com.starimmortal.core.vo;

import com.starimmortal.core.enumeration.Code;
import com.starimmortal.core.util.RequestUtil;
import com.starimmortal.core.util.ResponseUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 统一API响应结果封装视图对象
 *
 * @author william@StarImmortal
 * @date 2022/09/19
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

	/**
	 * 请求地址
	 */
	private String request;

	public ResponseVO(T data) {
		this.code = Code.SUCCESS.getCode();
		this.message = Code.SUCCESS.getDescription();
		this.data = data;
		this.request = RequestUtil.getSimpleRequest();
	}

	public ResponseVO(int code, String message) {
		this.code = code;
		this.message = message;
		this.request = RequestUtil.getSimpleRequest();
	}

	public ResponseVO(int code, String message, HttpStatus httpStatus) {
		this.code = code;
		this.message = message;
		this.request = RequestUtil.getSimpleRequest();
		ResponseUtil.setCurrentResponseHttpStatus(httpStatus.value());
	}

	public ResponseVO(int code, String message, T data) {
		this.code = code;
		this.message = message;
		this.data = data;
		this.request = RequestUtil.getSimpleRequest();
	}

	public static <T> ResponseVO<T> success() {
		return new ResponseVO<T>(Code.SUCCESS.getCode(), Code.SUCCESS.getDescription());
	}

	public static <T> ResponseVO<T> success(String message) {
		return new ResponseVO<T>(Code.SUCCESS.getCode(), message);
	}

	public static <T> ResponseVO<T> success(T data) {
		return new ResponseVO<T>(Code.SUCCESS.getCode(), Code.SUCCESS.getDescription(), data);
	}

	public static <T> ResponseVO<T> success(String message, T data) {
		return new ResponseVO<T>(Code.SUCCESS.getCode(), message, data);
	}

	public static <T> ResponseVO<T> error() {
		return new ResponseVO<T>(Code.FAIL.getCode(), Code.FAIL.getDescription());
	}

	public static <T> ResponseVO<T> error(String message) {
		return new ResponseVO<T>(Code.FAIL.getCode(), message);
	}

	public static <T> ResponseVO<T> error(T data) {
		return new ResponseVO<T>(Code.FAIL.getCode(), Code.FAIL.getDescription(), data);
	}

	public static <T> ResponseVO<T> error(int code, String message) {
		return new ResponseVO<T>(code, message);
	}

	public static <T> ResponseVO<T> error(String message, T data) {
		return new ResponseVO<T>(Code.FAIL.getCode(), message, data);
	}

	public static <T> ResponseVO<T> error(int code, String message, T data) {
		return new ResponseVO<T>(code, message, data);
	}

}

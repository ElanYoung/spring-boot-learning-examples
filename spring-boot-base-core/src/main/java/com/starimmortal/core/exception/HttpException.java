package com.starimmortal.core.exception;

import com.starimmortal.core.enumeration.Code;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Http异常
 *
 * @author william@StarImmortal
 * @date 2021/09/21
 */
@Getter
public class HttpException extends RuntimeException {

	/**
	 * 错误码
	 */
	protected int code = Code.INTERNAL_SERVER_ERROR.getCode();

	/**
	 * HTTP状态码
	 */
	protected int httpStatusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();

	/**
	 * 是否默认消息
	 */
	protected boolean defaultMessage = true;

	public HttpException() {
		super(Code.INTERNAL_SERVER_ERROR.getDescription());
	}

	public HttpException(String message) {
		super(message);
		this.defaultMessage = false;
	}

	public HttpException(int code) {
		super(Code.INTERNAL_SERVER_ERROR.getDescription());
		this.code = code;
	}

	public HttpException(int code, int httpStatusCode) {
		super(Code.INTERNAL_SERVER_ERROR.getDescription());
		this.code = code;
		this.httpStatusCode = httpStatusCode;
	}

	public HttpException(int code, String message) {
		super(message);
		this.code = code;
		this.defaultMessage = false;
	}

	public HttpException(int code, String message, int httpStatusCode) {
		super(message);
		this.code = code;
		this.httpStatusCode = httpStatusCode;
		this.defaultMessage = false;
	}

	public HttpException(Throwable cause, int code) {
		super(cause);
		this.code = code;
	}

	public HttpException(Throwable cause, int code, int httpStatusCode) {
		super(cause);
		this.code = code;
		this.httpStatusCode = httpStatusCode;
	}

	public HttpException(String message, Throwable cause) {
		super(message, cause);
		this.defaultMessage = false;
	}

	public Throwable doFillInStackTrace() {
		return super.fillInStackTrace();
	}

}

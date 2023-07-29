package com.starimmortal.core.exception;

import com.starimmortal.core.enumeration.Code;
import org.springframework.http.HttpStatus;

/**
 * 请求方法不允许异常
 *
 * @author william@StarImmortal
 * @date 2022/07/24
 */
public class MethodNotAllowedException extends HttpException {

	/**
	 * 错误码
	 */
	protected int code = Code.METHOD_NOT_ALLOWED.getCode();

	/**
	 * HTTP状态码
	 */
	protected int httpStatusCode = HttpStatus.METHOD_NOT_ALLOWED.value();

	public MethodNotAllowedException() {
		super(Code.METHOD_NOT_ALLOWED.getCode(), Code.METHOD_NOT_ALLOWED.getDescription());
		super.defaultMessage = true;
	}

	public MethodNotAllowedException(String message) {
		super(message);
	}

	public MethodNotAllowedException(int code) {
		super(code, Code.METHOD_NOT_ALLOWED.getDescription());
		this.code = code;
		super.defaultMessage = true;
	}

	public MethodNotAllowedException(int code, String message) {
		super(code, message);
		this.code = code;
	}

}

package com.starimmortal.core.exception;

import com.starimmortal.core.enumeration.Code;
import org.springframework.http.HttpStatus;

/**
 * 403异常：权限不足
 *
 * @author william@StarImmortal
 * @date 2021/02/01
 */
public class ForbiddenException extends HttpException {

	/**
	 * 错误码
	 */
	protected int code = Code.FORBIDDEN.getCode();

	/**
	 * HTTP状态码
	 */
	protected int httpStatusCode = HttpStatus.FORBIDDEN.value();

	public ForbiddenException() {
		super(Code.FORBIDDEN.getCode(), Code.FORBIDDEN.getDescription());
		super.defaultMessage = true;
	}

	public ForbiddenException(int code) {
		super(code, Code.FORBIDDEN.getDescription());
		this.code = code;
		super.defaultMessage = true;
	}

	public ForbiddenException(int code, String message) {
		super(code, message);
		this.code = code;
	}

	public ForbiddenException(String message) {
		super(message);
	}

}

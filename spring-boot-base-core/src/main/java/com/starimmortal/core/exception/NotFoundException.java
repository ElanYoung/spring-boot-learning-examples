package com.starimmortal.core.exception;

import com.starimmortal.core.enumeration.Code;
import org.springframework.http.HttpStatus;

/**
 * 404异常
 *
 * @author william@StarImmortal
 * @date 2021/09/21
 */
public class NotFoundException extends HttpException {

	/**
	 * 错误码
	 */
	protected int code = Code.NOT_FOUND.getCode();

	/**
	 * HTTP状态码
	 */
	protected int httpStatusCode = HttpStatus.NOT_FOUND.value();

	public NotFoundException() {
		super(Code.NOT_FOUND.getCode(), Code.NOT_FOUND.getDescription());
		super.defaultMessage = true;
	}

	public NotFoundException(String message) {
		super(message);
	}

	public NotFoundException(int code) {
		super(code, Code.NOT_FOUND.getDescription());
		this.code = code;
		super.defaultMessage = true;
	}

	public NotFoundException(int code, String message) {
		super(code, message);
		this.code = code;
	}

}

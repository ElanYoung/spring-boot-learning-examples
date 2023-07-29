package com.starimmortal.core.exception;

import com.starimmortal.core.enumeration.Code;
import org.springframework.http.HttpStatus;

/**
 * 授权异常
 *
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 * @author colorful@TaleLin
 */
public class AuthorizationException extends HttpException {

	private static final long serialVersionUID = -432605618235404747L;

	/**
	 * 错误码
	 */
	protected int code = Code.UN_AUTHORIZATION.getCode();

	/**
	 * 状态码
	 */
	protected int httpStatusCode = HttpStatus.UNAUTHORIZED.value();

	public AuthorizationException() {
		super(Code.UN_AUTHORIZATION.getCode(), Code.UN_AUTHORIZATION.getDescription());
		super.defaultMessage = true;
	}

	public AuthorizationException(String message) {
		super(message);
	}

	public AuthorizationException(int code) {
		super(code, Code.UN_AUTHORIZATION.getDescription());
		this.code = code;
		super.defaultMessage = true;
	}

	public AuthorizationException(int code, String message) {
		super(code, message);
		this.code = code;
	}

	@Override
	public int getCode() {
		return code;
	}

	@Override
	public int getHttpStatusCode() {
		return httpStatusCode;
	}

}

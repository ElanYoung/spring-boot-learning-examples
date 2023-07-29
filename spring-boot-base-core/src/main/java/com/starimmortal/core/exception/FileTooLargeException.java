package com.starimmortal.core.exception;

import com.starimmortal.core.enumeration.Code;
import org.springframework.http.HttpStatus;

/**
 * 文件大小过大异常
 *
 * @author william@StarImmortal
 * @date 2022/05/28
 */
public class FileTooLargeException extends HttpException {

	/**
	 * 错误码
	 */
	protected int code = Code.FILE_TOO_LARGE.getCode();

	/**
	 * HTTP状态码
	 */
	protected int httpStatusCode = HttpStatus.PAYLOAD_TOO_LARGE.value();

	public FileTooLargeException() {
		super(Code.FILE_TOO_LARGE.getCode(), Code.FILE_TOO_LARGE.getDescription());
		super.defaultMessage = true;
	}

	public FileTooLargeException(String message) {
		super(message);
	}

	public FileTooLargeException(int code) {
		super(code, Code.FILE_TOO_LARGE.getDescription());
		this.code = code;
		super.defaultMessage = true;
	}

	public FileTooLargeException(int code, String message) {
		super(code, message);
		this.code = code;
	}

}

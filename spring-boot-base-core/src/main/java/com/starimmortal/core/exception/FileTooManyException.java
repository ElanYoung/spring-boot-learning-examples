package com.starimmortal.core.exception;

import com.starimmortal.core.enumeration.Code;
import org.springframework.http.HttpStatus;

/**
 * 文件数量过多异常
 *
 * @author william@StarImmortal
 * @date 2022/05/28
 */
public class FileTooManyException extends HttpException {
    /**
     * 错误码
     */
    protected int code = Code.FILE_TOO_MANY.getCode();

    /**
     * HTTP状态码
     */
    protected int httpStatusCode = HttpStatus.PAYLOAD_TOO_LARGE.value();


    public FileTooManyException() {
        super(Code.FILE_TOO_MANY.getCode(), Code.FILE_TOO_MANY.getDescription());
        super.defaultMessage = true;
    }

    public FileTooManyException(String message) {
        super(message);
    }

    public FileTooManyException(int code) {
        super(code, Code.FILE_TOO_MANY.getDescription());
        this.code = code;
        super.defaultMessage = true;
    }

    public FileTooManyException(int code, String message) {
        super(code, message);
        this.code = code;
    }
}

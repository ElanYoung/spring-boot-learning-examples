package com.starimmortal.core.exception;

import com.starimmortal.core.enumeration.Code;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 失败异常
 *
 * @author william@StarImmortal
 * @date 2022/07/24
 */
@Getter
public class FailedException extends HttpException {
    /**
     * 错误码
     */
    protected int code = Code.FAIL.getCode();

    /**
     * HTTP状态码
     */
    protected int httpStatusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();

    public FailedException() {
        super(Code.FAIL.getCode(), Code.FAIL.getDescription());
        super.defaultMessage = true;
    }

    public FailedException(int code) {
        super(code, Code.FAIL.getDescription());
        this.code = code;
        super.defaultMessage = true;
    }

    public FailedException(String message) {
        super(message);
    }

    public FailedException(int code, String message) {
        super(code, message);
        this.code = code;
    }
}

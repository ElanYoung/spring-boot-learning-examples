package com.starimmortal.core.exception;

import com.starimmortal.core.enumeration.Code;
import org.springframework.http.HttpStatus;

/**
 * 参数异常
 *
 * @author william@StarImmortal
 * @date 2021/09/21
 */
public class ParameterException extends HttpException {
    /**
     * 错误码
     */
    protected int code = Code.PARAMETER_ERROR.getCode();

    /**
     * HTTP状态码
     */
    protected int httpStatusCode = HttpStatus.BAD_REQUEST.value();

    public ParameterException() {
        super(Code.PARAMETER_ERROR.getCode(), Code.PARAMETER_ERROR.getDescription());
        super.defaultMessage = true;
    }

    public ParameterException(int code) {
        super(code, Code.PARAMETER_ERROR.getDescription());
        this.code = code;
        super.defaultMessage = true;
    }

    public ParameterException(String message) {
        super(message);
    }

    public ParameterException(int code, String message) {
        super(code, message);
        this.code = code;
    }
}

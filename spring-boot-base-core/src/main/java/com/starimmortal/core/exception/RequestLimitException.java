package com.starimmortal.core.exception;

import com.starimmortal.core.enumeration.Code;
import org.springframework.http.HttpStatus;

/**
 * 请求过多异常
 *
 * @author william@StarImmortal
 * @date 2022/11/19
 */
public class RequestLimitException extends HttpException {
    /**
     * 错误码
     */
    protected int code = Code.REQUEST_LIMIT.getCode();

    /**
     * HTTP状态码
     */
    protected int httpStatusCode = HttpStatus.TOO_MANY_REQUESTS.value();

    public RequestLimitException() {
        super(Code.REQUEST_LIMIT.getCode(), Code.REQUEST_LIMIT.getDescription());
        super.defaultMessage = true;
    }

    public RequestLimitException(String message) {
        super(message);
    }

    public RequestLimitException(int code) {
        super(code, Code.REQUEST_LIMIT.getDescription());
        this.code = code;
        super.defaultMessage = true;
    }

    public RequestLimitException(int code, String message) {
        super(code, message);
        this.code = code;
    }
}

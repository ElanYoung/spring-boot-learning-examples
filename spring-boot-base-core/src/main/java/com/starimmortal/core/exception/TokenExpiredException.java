package com.starimmortal.core.exception;

import com.starimmortal.core.enumeration.Code;
import org.springframework.http.HttpStatus;

/**
 * 令牌过期异常
 *
 * @author william@StarImmortal
 * @date 2021/09/21
 */
public class TokenExpiredException extends HttpException {
    /**
     * 错误码
     */
    protected int code = Code.TOKEN_EXPIRED.getCode();

    /**
     * HTTP状态码
     */
    protected int httpStatusCode = HttpStatus.UNAUTHORIZED.value();

    public TokenExpiredException() {
        super(Code.TOKEN_EXPIRED.getCode(), Code.TOKEN_EXPIRED.getDescription());
        super.defaultMessage = true;
    }

    public TokenExpiredException(String message) {
        super(message);
    }

    public TokenExpiredException(int code) {
        super(code, Code.TOKEN_EXPIRED.getDescription());
        this.code = code;
        super.defaultMessage = true;
    }

    public TokenExpiredException(int code, String message) {
        super(code, message);
        this.code = code;
    }
}

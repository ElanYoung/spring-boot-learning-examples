package com.starimmortal.core.exception;

import com.starimmortal.core.enumeration.Code;
import org.springframework.http.HttpStatus;

/**
 * 令牌无效异常
 *
 * @author william@StarImmortal
 * @date 2021/09/21
 */
public class TokenInvalidException extends HttpException {
    /**
     * 错误码
     */
    protected int code = Code.TOKEN_INVALID.getCode();

    /**
     * HTTP状态码
     */
    protected int httpStatusCode = HttpStatus.UNAUTHORIZED.value();

    public TokenInvalidException() {
        super(Code.TOKEN_INVALID.getCode(), Code.TOKEN_INVALID.getDescription());
        super.defaultMessage = true;
    }

    public TokenInvalidException(String message) {
        super(message);
    }

    public TokenInvalidException(int code) {
        super(code, Code.TOKEN_INVALID.getDescription());
        this.code = code;
        super.defaultMessage = true;
    }

    public TokenInvalidException(int code, String message) {
        super(code, message);
        this.code = code;
    }
}

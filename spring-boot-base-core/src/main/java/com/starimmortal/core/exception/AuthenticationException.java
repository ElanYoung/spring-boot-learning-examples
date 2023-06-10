package com.starimmortal.core.exception;

import com.starimmortal.core.enumeration.Code;
import org.springframework.http.HttpStatus;

/**
 * 认证异常
 *
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 * @author colorful@TaleLin
 */
public class AuthenticationException extends HttpException {

    private static final long serialVersionUID = -222891683232481602L;

    /**
     * 错误码
     */
    protected int code = Code.UN_AUTHENTICATION.getCode();

    /**
     * 状态码
     */
    protected int httpStatusCode = HttpStatus.UNAUTHORIZED.value();

    public AuthenticationException() {
        super(Code.UN_AUTHENTICATION.getCode(), Code.UN_AUTHENTICATION.getDescription());
        super.defaultMessage = true;
    }

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(int code) {
        super(code, Code.UN_AUTHENTICATION.getDescription());
        this.code = code;
        super.defaultMessage = true;
    }

    public AuthenticationException(int code, String message) {
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

package com.starimmortal.core.exception;

import org.springframework.http.HttpStatus;

/**
 * 服务器异常
 *
 * @author william@StarImmortal
 * @date 2021/09/21
 */
public class ServerErrorException extends HttpException {
    public ServerErrorException(Integer code) {
        this.code = code;
        this.httpStatusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
    }
}

package com.starimmortal.core.exception;

import com.starimmortal.core.enumeration.Code;
import org.springframework.http.HttpStatus;

/**
 * 文件扩展异常
 *
 * @author william@StarImmortal
 * @date 2021/09/21
 */
public class FileExtensionException extends HttpException {
    /**
     * 错误码
     */
    protected int code = Code.FILE_EXTENSION.getCode();

    /**
     * HTTP状态码
     */
    protected int httpStatusCode = HttpStatus.NOT_ACCEPTABLE.value();

    public FileExtensionException() {
        super(Code.FILE_EXTENSION.getCode(), Code.FILE_EXTENSION.getDescription());
        super.defaultMessage = true;
    }

    public FileExtensionException(String message) {
        super(message);
    }

    public FileExtensionException(int code) {
        super(code, Code.FILE_EXTENSION.getDescription());
        this.code = code;
        super.defaultMessage = true;
    }

    public FileExtensionException(int code, String message) {
        super(code, message);
        this.code = code;
    }
}

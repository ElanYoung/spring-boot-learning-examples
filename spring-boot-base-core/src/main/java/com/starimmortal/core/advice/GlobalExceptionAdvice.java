package com.starimmortal.core.advice;

import com.starimmortal.core.configuration.CodeMessageConfiguration;
import com.starimmortal.core.enumeration.Code;
import com.starimmortal.core.exception.HttpException;
import com.starimmortal.core.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

import javax.servlet.ServletException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * 全局异常处理
 *
 * @author william@StarImmortal
 * @date 2021/02/08
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionAdvice {

    @Autowired
    CodeMessageConfiguration codeMessageConfiguration;

    /**
     * Exception
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseVO<String> handleException(Exception exception) {
        log.error(exception.getMessage());
        return new ResponseVO<>(Code.INTERNAL_SERVER_ERROR.getCode(), codeMessageConfiguration.getMessage(Code.INTERNAL_SERVER_ERROR.getCode()));
    }

    /**
     * HttpException
     */
    @ExceptionHandler(value = HttpException.class)
    public ResponseEntity<ResponseVO> handleHttpException(HttpException exception) {
        String errorMessage = exception.getIfDefaultMessage() ? codeMessageConfiguration.getMessage(exception.getCode()) : exception.getMessage();
        ResponseVO response = new ResponseVO<>(exception.getCode(), errorMessage);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpStatus httpStatus = HttpStatus.resolve(exception.getHttpStatusCode());
        assert httpStatus != null;
        return new ResponseEntity<>(response, httpHeaders, httpStatus);
    }

    /**
     * BindException
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseVO<String> handleBeanValidationException(BindException exception) {
        List<ObjectError> errorList = exception.getBindingResult().getAllErrors();
        String message = formatAllErrorMessages(errorList);
        return new ResponseVO<>(Code.PARAMETER_ERROR.getCode(), message);
    }

    /**
     * ConstraintViolationException
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseVO<String> handleConstraintException(ConstraintViolationException exception) {
        StringBuilder errorMessage = new StringBuilder();
        for (ConstraintViolation<?> error : exception.getConstraintViolations()) {
            String message = error.getMessage();
            String path = error.getPropertyPath().toString();
            String name = path.split("[.]")[1];
            errorMessage.append(name).append(" ").append(message);
        }
        return new ResponseVO<>(Code.PARAMETER_ERROR.getCode(), errorMessage.toString());
    }

    /**
     * NoHandlerFoundException
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ResponseVO<String> handleNoHandlerFoundException(NoHandlerFoundException exception) {
        String errorMessage = StringUtils.hasText(codeMessageConfiguration.getMessage(10025)) ? codeMessageConfiguration.getMessage(10025) : exception.getMessage();
        return new ResponseVO<>(Code.NOT_FOUND.getCode(), errorMessage);
    }

    /**
     * MaxUploadSizeExceededException
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(code = HttpStatus.PAYLOAD_TOO_LARGE)
    public ResponseVO<String> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException exception) {
        String errorMessage = StringUtils.hasText(codeMessageConfiguration.getMessage(10180)) ? codeMessageConfiguration.getMessage(10180) : exception.getMessage();
        return new ResponseVO<>(Code.FILE_TOO_LARGE.getCode(), errorMessage);
    }

    /**
     * 请求方式不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseVO<String> handleException(HttpRequestMethodNotSupportedException exception) {
        String errorMessage = exception.getMethod() + codeMessageConfiguration.getMessage(10080);
        return new ResponseVO<>(Code.METHOD_NOT_ALLOWED.getCode(), errorMessage);
    }

    /**
     * MissingServletRequestParameterException
     */
    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseVO<String> missingServletRequestParameterException(MissingServletRequestParameterException exception) {
        String errorMessage = StringUtils.hasText(codeMessageConfiguration.getMessage(10150)) ? codeMessageConfiguration.getMessage(10150) + exception.getParameterName() : exception.getMessage();
        return new ResponseVO<>(Code.PARAMETER_ERROR.getCode(), errorMessage);
    }

    /**
     * MethodArgumentTypeMismatchException
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseVO<String> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        String errorMessage = StringUtils.hasText(codeMessageConfiguration.getMessage(10160)) ? exception.getValue() + codeMessageConfiguration.getMessage(10160) : exception.getMessage();
        return new ResponseVO<>(Code.PARAMETER_ERROR.getCode(), errorMessage);
    }

    /**
     * ServletException
     */
    @ExceptionHandler({ServletException.class})
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseVO<String> handleServletException(ServletException exception) {
        return new ResponseVO<>(Code.FAIL.getCode(), exception.getMessage());
    }

    /**
     * HttpMessageNotReadableException
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseVO<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        String errorMessage = StringUtils.hasText(codeMessageConfiguration.getMessage(10170)) ? codeMessageConfiguration.getMessage(10170) : exception.getMessage();
        return new ResponseVO<>(Code.PARAMETER_ERROR.getCode(), errorMessage);
    }

    /**
     * DuplicateKeyException
     */
    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseVO<?> handleDuplicateKeyException(DuplicateKeyException exception) {
        return new ResponseVO<>(Code.DUPLICATED_DATABASE.getCode(), Code.DUPLICATED_DATABASE.getZhDescription());
    }

    private String formatAllErrorMessages(List<ObjectError> errorList) {
        StringBuilder errorMessages = new StringBuilder();
        errorList.forEach(error -> errorMessages.append(error.getDefaultMessage()).append(";"));
        return errorMessages.toString();
    }
}

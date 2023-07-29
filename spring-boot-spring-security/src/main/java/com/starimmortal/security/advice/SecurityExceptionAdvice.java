package com.starimmortal.security.advice;

import com.starimmortal.core.advice.GlobalExceptionAdvice;
import com.starimmortal.core.enumeration.Code;
import com.starimmortal.core.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 *
 * @author william@StarImmortal
 * @date 2021/02/08
 */
@RestControllerAdvice
@Slf4j
public class SecurityExceptionAdvice extends GlobalExceptionAdvice {

	/**
	 * Spring Security 用户名或密码错误异常
	 */
	@ExceptionHandler({ BadCredentialsException.class })
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ResponseVO<?> handleBadCredentialsException(BadCredentialsException exception) {
		return new ResponseVO<>(Code.UN_AUTHENTICATION.getCode(), exception.getMessage());
	}

	/**
	 * Spring Security 账户异常（禁用、锁定、过期）
	 */
	@ExceptionHandler({ AccountStatusException.class })
	@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
	public ResponseVO<?> handleAccountStatusException(AccountStatusException exception) {
		return new ResponseVO<>(Code.UN_AUTHENTICATION.getCode(), exception.getMessage());
	}

}

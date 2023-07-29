package com.starimmortal.core.util;

import com.starimmortal.core.enumeration.Code;
import com.starimmortal.core.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 响应结果生成工具
 *
 * @author william@StarImmortal
 * @date 2021/02/08
 */
@SuppressWarnings("unchecked")
@Slf4j
public class ResponseUtil {

	/**
	 * 获得当前响应
	 * @return 响应
	 */
	public static HttpServletResponse getResponse() {
		return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
			.getResponse();
	}

	public static void setCurrentResponseHttpStatus(Integer httpStatus) {
		getResponse().setStatus(httpStatus);
	}

	public static <T> ResponseVO<T> generateCreatedResponse(Integer code) {
		return (ResponseVO<T>) ResponseVO.builder()
			.message(Code.CREATED.getDescription())
			.code(code)
			.request(RequestUtil.getSimpleRequest())
			.build();
	}

	public static <T> ResponseVO<T> generateCreatedResponse(Integer code, String message) {
		return (ResponseVO<T>) ResponseVO.builder()
			.message(message)
			.code(code)
			.request(RequestUtil.getSimpleRequest())
			.build();
	}

	public static <T> ResponseVO<T> generateDeletedResponse(Integer code) {
		return (ResponseVO<T>) ResponseVO.builder()
			.message(Code.SUCCESS.getDescription())
			.code(code)
			.request(RequestUtil.getSimpleRequest())
			.build();
	}

	public static <T> ResponseVO<T> generateDeletedResponse(Integer code, String message) {
		return (ResponseVO<T>) ResponseVO.builder()
			.message(message)
			.code(code)
			.request(RequestUtil.getSimpleRequest())
			.build();
	}

	public static <T> ResponseVO<T> generateUpdatedResponse(Integer code) {
		return (ResponseVO<T>) ResponseVO.builder()
			.message(Code.SUCCESS.getDescription())
			.code(code)
			.request(RequestUtil.getSimpleRequest())
			.build();
	}

	public static <T> ResponseVO<T> generateUpdatedResponse(Integer code, String message) {
		return (ResponseVO<T>) ResponseVO.builder()
			.message(message)
			.code(code)
			.request(RequestUtil.getSimpleRequest())
			.build();
	}

	public static <T> ResponseVO<T> generateUnifyResponse(Integer code) {
		return (ResponseVO<T>) ResponseVO.builder().code(code).request(RequestUtil.getSimpleRequest()).build();
	}

}

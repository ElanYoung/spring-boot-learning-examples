package com.starimmortal.core.advice;

import com.starimmortal.core.annotation.IgnoreResponseAdvice;
import com.starimmortal.core.vo.ResponseVO;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

/**
 * 全局统一响应处理
 *
 * @author william@StarImmortal
 * @date 2023/03/02
 */
@RestControllerAdvice(value = "com.starimmortal")
public class GlobalResponseAdvice implements ResponseBodyAdvice<Object> {

	/**
	 * 判断是否对响应进行处理
	 * @param returnType 方法参数类型
	 * @param converterType 选中的 HttpMessageConverter 的类型，用于将 Controller 返回值序列化为对应的媒体类型
	 * @return true || false
	 */
	@Override
	public boolean supports(@NonNull MethodParameter returnType,
			@NonNull Class<? extends HttpMessageConverter<?>> converterType) {
		if (returnType.getDeclaringClass().isAnnotationPresent(IgnoreResponseAdvice.class)) {
			return false;
		}
		return !Objects.requireNonNull(returnType.getMethod()).isAnnotationPresent(IgnoreResponseAdvice.class);
	}

	/**
	 * 处理返回值
	 * @param body 返回对象
	 * @param returnType 方法参数类型
	 * @param selectedContentType 请求媒体类型
	 * @param selectedConverterType 选中的 HttpMessageConverter 的类型，用于将返回对象序列化为对应的媒体类型
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return 返回值
	 */
	@Override
	public Object beforeBodyWrite(Object body, @NonNull MethodParameter returnType,
			@NonNull MediaType selectedContentType,
			@NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType, @NonNull ServerHttpRequest request,
			@NonNull ServerHttpResponse response) {
		if (body instanceof ResponseVO) {
			return body;
		}

		if (body == null) {
			return new ResponseVO<>();
		}

		return new ResponseVO<>(body);
	}

}

package com.starimmortal.excel.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 拦截带有 @ExecutionTime 注解方法，并记录方法执行时间
 *
 * @author william@StarImmortal
 * @date 2023/03/17
 */
@Aspect
@Component
@Slf4j
public class ExecutionTimeAspect {

	@Around(value = "@annotation(com.starimmortal.excel.annotation.ExecutionTime)")
	public Object recordExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
		// 获取当前请求对象
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		assert attributes != null;
		HttpServletRequest request = attributes.getRequest();
		// 记录接口耗时
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Object proceed = joinPoint.proceed();
		stopWatch.stop();
		log.info("{} => executed in {}s", request.getRequestURI(), stopWatch.getTotalTimeSeconds());
		return proceed;
	}

}

package com.starimmortal.core.aop;

import com.starimmortal.core.configuration.CodeMessageConfiguration;
import com.starimmortal.core.vo.ResponseVO;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 处理返回结果为 UnifyResponseVO 的控制器层方法 message 默认为 null，在此处通过 code 设置为对应消息
 *
 * @author william@StarImmortal
 * @date 2021/02/08
 */
@Aspect
@Component
public class ResultAspect {

	@Autowired
	private CodeMessageConfiguration codeMessageConfiguration;

	@AfterReturning(returning = "result", pointcut = "execution(public * com.starimmortal..controller..*.*(..))")
	public void doAfterReturning(ResponseVO<String> result) {
		int code = result.getCode();
		String oldMessage = result.getMessage();
		// code-message.properties 中配置的 message
		String newMessage = codeMessageConfiguration.getMessage(code);
		// 如果 code-message.properties 中指定了相应的 message 并且 UnifyResponseVO 的 message 为null
		// 则使用 newMessage 替换 oldMessage
		if (StringUtils.hasText(newMessage) && !StringUtils.hasText(oldMessage)) {
			result.setMessage(newMessage);
		}
	}

}

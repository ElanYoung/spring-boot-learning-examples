package com.starimmortal.core.configuration;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息码配置类
 *
 * @author william@StarImmortal
 * @date 2021/02/08
 */
@SuppressWarnings("ConfigurationProperties")
@ConfigurationProperties
@PropertySource(value = "classpath:code-message.properties", encoding = "UTF-8")
@Component
@Getter
public class CodeMessageConfiguration {

	private final Map<Integer, String> codeMessage = new HashMap<>();

	public String getMessage(Integer code) {
		return codeMessage.get(code);
	}

}

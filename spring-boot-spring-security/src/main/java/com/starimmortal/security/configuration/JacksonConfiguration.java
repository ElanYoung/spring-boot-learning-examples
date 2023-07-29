package com.starimmortal.security.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

/**
 * Jackson配置类
 *
 * @author william@StarImmortal
 * @date 2023/05/10
 */
@Configuration
@ConditionalOnClass(ObjectMapper.class)
@AutoConfigureBefore(JacksonAutoConfiguration.class)
public class JacksonConfiguration {

	/**
	 * 自定义Jackson配置
	 * @return Jackson2ObjectMapperBuilderCustomizer
	 */
	@Bean
	@ConditionalOnMissingBean
	public Jackson2ObjectMapperBuilderCustomizer customizer() {
		return jacksonObjectMapperBuilder -> {
			// 默认区域设置为中国
			jacksonObjectMapperBuilder.locale(Locale.CHINA);
			// 遇到未知属性时不报错
			jacksonObjectMapperBuilder.failOnUnknownProperties(false);
			// 配置属性命名策略为 SNAKE_CASE（即使用下划线代替驼峰式命名）
			jacksonObjectMapperBuilder.propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
			// 解决前后端交互Long类型精度丢失问题
			jacksonObjectMapperBuilder.serializerByType(Long.class, ToStringSerializer.instance);
		};
	}

}

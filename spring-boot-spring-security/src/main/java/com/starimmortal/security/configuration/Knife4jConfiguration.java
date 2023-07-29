package com.starimmortal.security.configuration;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.plugins.WebFluxRequestHandlerProvider;
import springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Knife4j 配置项
 *
 * @author william@StarImmortal
 * @date 2021/02/08
 */
@Configuration
@EnableSwagger2
public class Knife4jConfiguration implements WebMvcConfigurer {

	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
			.select()
			.apis(RequestHandlerSelectors.basePackage("com.starimmortal.security.controller"))
			.paths(PathSelectors.any())
			.build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("星野团队接口文档")
			.description("基于SpringBoot的CMS/DMS/管理系统开发框架")
			.contact(new Contact("william", "https://doc.starimmortal.com", "william@starimmortal.com"))
			.version("1.0.0")
			.build();
	}

	@Bean
	public static BeanPostProcessor springfoxHandlerProviderBeanPostProcessor() {
		return new BeanPostProcessor() {

			@Override
			public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName)
					throws BeansException {
				if (bean instanceof WebMvcRequestHandlerProvider || bean instanceof WebFluxRequestHandlerProvider) {
					customizeSpringfoxHandlerMappings(getHandlerMappings(bean));
				}
				return bean;
			}

			private <T extends RequestMappingInfoHandlerMapping> void customizeSpringfoxHandlerMappings(
					List<T> mappings) {
				List<T> copy = mappings.stream()
					.filter(mapping -> mapping.getPatternParser() == null)
					.collect(Collectors.toList());
				mappings.clear();
				mappings.addAll(copy);
			}

			@SuppressWarnings("unchecked")
			private List<RequestMappingInfoHandlerMapping> getHandlerMappings(Object bean) {
				try {
					Field field = ReflectionUtils.findField(bean.getClass(), "handlerMappings");
					assert field != null;
					field.setAccessible(true);
					return (List<RequestMappingInfoHandlerMapping>) field.get(bean);
				}
				catch (IllegalArgumentException | IllegalAccessException e) {
					throw new IllegalStateException(e);
				}
			}
		};
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("favicon.ico").addResourceLocations("classpath:/static/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

}

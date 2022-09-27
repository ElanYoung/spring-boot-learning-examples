package com.starimmortal.jwt.configuration;

import com.starimmortal.jwt.interceptor.AuthorizeInterceptor;
import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置项
 *
 * @author william@StarImmortal
 * @date 2022/09/24
 */
@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {

    @Bean
    public AuthorizeInterceptor authorizeInterceptor() {
        return new AuthorizeInterceptor();
    }

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(authorizeInterceptor());
    }
}

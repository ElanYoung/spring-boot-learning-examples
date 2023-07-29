package com.starimmortal.security.configuration;

import com.starimmortal.security.filter.JwtAuthenticationTokenFilter;
import com.starimmortal.security.handler.LogoutSuccessHandler;
import com.starimmortal.security.handler.RestAccessDeniedHandler;
import com.starimmortal.security.handler.RestAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Spring Security 通用配置项（解决循环依赖）
 *
 * @author william@StarImmortal
 * @date 2022/12/16
 */
@Configuration
public class CommonSecurityConfiguration {

	/**
	 * 强散列哈希加密实现
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * 认证管理器
	 * @param authenticationConfiguration AuthenticationConfiguration
	 * @return AuthenticationManager
	 */
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	/**
	 * JWT登录授权过滤器
	 * @return JwtAuthenticationTokenFilter
	 */
	@Bean
	public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() {
		return new JwtAuthenticationTokenFilter();
	}

	/**
	 * 认证失败处理类
	 * @return RestAuthenticationEntryPoint
	 */
	@Bean
	public RestAuthenticationEntryPoint restAuthenticationEntryPoint() {
		return new RestAuthenticationEntryPoint();
	}

	/**
	 * 自定义无权访问处理类
	 * @return RestAccessDeniedHandler
	 */
	@Bean
	public RestAccessDeniedHandler restAccessDeniedHandler() {
		return new RestAccessDeniedHandler();
	}

	/**
	 * 退出登录成功处理器
	 * @return LogoutSuccessHandler
	 */
	@Bean
	public LogoutSuccessHandler logoutSuccessHandler() {
		return new LogoutSuccessHandler();
	}

}

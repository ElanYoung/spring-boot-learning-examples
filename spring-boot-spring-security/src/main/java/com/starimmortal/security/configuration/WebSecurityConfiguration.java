package com.starimmortal.security.configuration;

import com.starimmortal.security.filter.JwtAuthenticationTokenFilter;
import com.starimmortal.security.handler.LogoutHandler;
import com.starimmortal.security.handler.LogoutSuccessHandler;
import com.starimmortal.security.handler.RestAccessDeniedHandler;
import com.starimmortal.security.handler.RestAuthenticationEntryPoint;
import com.starimmortal.security.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 配置项
 *
 * @author william@StarImmortal
 * @date 2022/12/16
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfiguration {

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    private RestAccessDeniedHandler restAccessDeniedHandler;

    @Autowired
    private LogoutHandler logoutHandler;

    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;

    /**
     * 静态资源白名单
     */
    public static final String[] STATIC_RESOURCE_WHITE_LIST = {
            // 静态资源文件
            "/",
            "/**/*.js",
            "/**/*.css",
            "/**/*.html",
            "/**/*.svg",
            "/**/*.pdf",
            "/**/*.jpg",
            "/**/*.png",
            "/**/*.gif",
            "/**/*.ico",
            // 排除字体格式后缀
            "/**/*.ttf",
            "/**/*.woff",
            "/**/*.woff2",
            // 排除Druid、Swagger、H2
            "/druid/**",
            "/v2/api-docs",
            "/webjars/**",
            "/swagger-resources/**",
            "/h2-console/**/**"
    };

    /**
     * anyRequest          |   匹配所有请求路径
     * access              |   SpringEl表达式结果为true时可以访问
     * anonymous           |   匿名可以访问
     * denyAll             |   用户不能访问
     * fullyAuthenticated  |   用户完全认证可以访问（非remember-me下自动登录）
     * hasAnyAuthority     |   如果有参数，参数表示权限，则其中任何一个权限可以访问
     * hasAnyRole          |   如果有参数，参数表示角色，则其中任何一个角色可以访问
     * hasAuthority        |   如果有参数，参数表示权限，则其权限可以访问
     * hasIpAddress        |   如果有参数，参数表示IP地址，如果用户IP和参数匹配，则可以访问
     * hasRole             |   如果有参数，参数表示角色，则其角色可以访问
     * permitAll           |   用户可以任意访问
     * rememberMe          |   允许通过remember-me登录的用户访问
     * authenticated       |   用户登录后可访问
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // TODO 静态资源与方法放行采用配置文件读取形式
        httpSecurity
                // 过滤请求
                .authorizeRequests()
                // 静态资源放行
                .antMatchers(STATIC_RESOURCE_WHITE_LIST).permitAll()
                // 接口放行
                .antMatchers(JwtUtil.getWhiteList()).permitAll()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest()
                .authenticated()
                .and()
                // CSRF禁用
                .csrf().disable()
                // 禁用HTTP响应标头
                .headers().cacheControl().disable()
                .and()
                // 基于JWT令牌，无需Session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 认证与授权失败处理类
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .accessDeniedHandler(restAccessDeniedHandler)
                .and()
                // 拦截器
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                // 退出登录
                .logout()
                .logoutUrl("/user/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler(logoutSuccessHandler)
                .and()
                // 跨域
                .cors()
                .and()
                .headers().frameOptions().disable();
        return httpSecurity.build();
    }
}

package com.starimmortal.security.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.starimmortal.core.enumeration.Code;
import com.starimmortal.security.configuration.WebSecurityConfiguration;
import com.starimmortal.security.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

/**
 * JWT登录授权过滤器
 *
 * @author william@StarImmortal
 * @date 2022/12/16
 */
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        // 静态资源放行
        if (Arrays.stream(WebSecurityConfiguration.STATIC_RESOURCE_WHITE_LIST).anyMatch(uri -> uri.equals(request.getServletPath()))) {
            filterChain.doFilter(request, response);
            return;
        }
        // 白名单放行
        if (Arrays.stream(JwtUtil.getWhiteList()).anyMatch(uri -> uri.equals(request.getServletPath()))) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = JwtUtil.decodeTokenFromRequest(request, response);
        // 判断令牌是否存在黑名单中
        if (JwtUtil.isInBlacklist(token)) {
            JwtUtil.responseError(response, Code.TOKEN_INVALID.getCode(), Code.TOKEN_INVALID.getZhDescription());
        }
        String username = JwtUtil.getUsername(token);
        if (StringUtils.hasText(username) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (!JwtUtil.hasToken(username)) {
                JwtUtil.responseError(response, Code.ACCESS_TOKEN_EXPIRED.getCode(), Code.ACCESS_TOKEN_EXPIRED.getZhDescription());
            }
            // 校验令牌是否有效
            try {
                JwtUtil.decodeAccessToken(token);
                JwtUtil.checkTokenValid(token, userDetails.getUsername());
            } catch (TokenExpiredException e) {
                JwtUtil.responseError(response, Code.ACCESS_TOKEN_EXPIRED.getCode(), Code.ACCESS_TOKEN_EXPIRED.getZhDescription());
            } catch (JWTVerificationException e) {
                JwtUtil.responseError(response, Code.TOKEN_INVALID.getCode(), Code.TOKEN_INVALID.getZhDescription());
            }
            // 权限信息
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}

package com.starimmortal.security.handler;

import com.starimmortal.core.enumeration.Code;
import com.starimmortal.core.util.GenericJacksonUtil;
import com.starimmortal.core.vo.ResponseVO;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证失败处理类
 *
 * @author william@StarImmortal
 * @date 2022/12/16
 */
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().println(GenericJacksonUtil.objectToJson(ResponseVO.error(Code.UN_AUTHENTICATION.getCode(), authException.getMessage())));
        response.getWriter().flush();
    }
}

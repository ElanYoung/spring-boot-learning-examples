package com.starimmortal.security.handler;

import com.starimmortal.core.enumeration.Code;
import com.starimmortal.core.util.GenericJacksonUtil;
import com.starimmortal.core.vo.ResponseVO;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义无权访问处理类
 *
 * @author william@StarImmortal
 * @date 2022/12/16
 */
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.getWriter().println(GenericJacksonUtil.objectToJson(ResponseVO.error(Code.UN_AUTHENTICATION.getCode(), Code.UN_AUTHENTICATION.getZhDescription(), accessDeniedException.getMessage())));
        response.getWriter().flush();
    }
}

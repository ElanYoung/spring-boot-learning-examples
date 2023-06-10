package com.starimmortal.core.util;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求结果生成工具
 *
 * @author william@StarImmortal
 * @date 2021/02/08
 */
public class RequestUtil {
    public RequestUtil() {
    }

    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        } else if (requestAttributes instanceof ServletRequestAttributes) {
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
            return servletRequestAttributes.getRequest();
        } else {
            return null;
        }
    }

    public static String getRequestUrl() {
        HttpServletRequest request = getRequest();
        return request == null ? null : request.getServletPath();
    }

    public static String getSimpleRequest(HttpServletRequest request) {
        return request.getMethod() + " " + request.getServletPath();
    }

    public static String getSimpleRequest() {
        HttpServletRequest request = getRequest();
        return request == null ? null : request.getMethod() + " " + request.getServletPath();
    }
}

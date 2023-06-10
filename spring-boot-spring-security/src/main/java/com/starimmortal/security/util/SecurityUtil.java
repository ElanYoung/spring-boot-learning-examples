package com.starimmortal.security.util;

import com.starimmortal.security.bean.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Spring Security 工具类
 *
 * @author william@StarImmortal
 * @date 2022/12/16
 */
@Slf4j
public class SecurityUtil {
    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取当前登录用户
     *
     * @return 当前登录用户
     */
    public static LoginUser getLoginUser() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            log.error("no authentication in security context found");
            throw new RuntimeException("no authentication in security context found");
        }
        return getLoginUser(authentication);
    }

    /**
     * 获取当前登录用户
     *
     * @return 当前登录用户
     */
    public static LoginUser getLoginUser(Authentication authentication) {
        if (authentication.getPrincipal() instanceof LoginUser) {
            return (LoginUser) authentication.getPrincipal();
        }
        return null;
    }
}

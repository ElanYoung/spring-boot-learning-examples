package com.starimmortal.security.handler;

import com.starimmortal.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 退出登录处理器
 *
 * @author william@StarImmortal
 * @date 2023/05/22
 */
@Service
@RequiredArgsConstructor
public class LogoutHandler implements org.springframework.security.web.authentication.logout.LogoutHandler {

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		String token = JwtUtil.decodeTokenFromRequest(request, response);
		JwtUtil.blacklist(token);
		JwtUtil.removeToken(JwtUtil.getUsername(token));
		SecurityContextHolder.clearContext();
	}

}

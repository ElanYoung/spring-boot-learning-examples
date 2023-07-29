package com.starimmortal.security.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.starimmortal.core.exception.TokenInvalidException;
import com.starimmortal.core.vo.ResponseVO;
import com.starimmortal.security.dto.LoginDTO;
import com.starimmortal.security.pojo.UserDO;
import com.starimmortal.security.service.UserService;
import com.starimmortal.security.util.JwtUtil;
import com.starimmortal.security.vo.TokenVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户控制器
 *
 * @author william@StarImmortal
 */
@RestController
@RequestMapping("/user")
@Validated
public class UserController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserService userService;

	@ApiOperation(value = "登录")
	@PostMapping("/login")
	public ResponseVO<TokenVO> login(@RequestBody @Validated LoginDTO dto) {
		authenticationManager
			.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
		UserDO user = userService.getUserByUsername(dto.getUsername());
		TokenVO token = JwtUtil.generateTokens(user.getUsername());
		JwtUtil.revokeUserToken(user.getUsername());
		JwtUtil.saveToken(user.getUsername(), token.getAccessToken());
		return ResponseVO.success("登录成功", token);
	}

	@ApiOperation(value = "刷新令牌")
	@PostMapping("/refresh")
	public ResponseVO<TokenVO> refreshToken(HttpServletRequest request, HttpServletResponse response) {
		String refreshToken = JwtUtil.decodeTokenFromRequest(request, response);
		String username = JwtUtil.getUsername(refreshToken);
		UserDO user = userService.getUserByUsername(username);
		try {
			JwtUtil.decodeRefreshToken(refreshToken);
			JwtUtil.checkTokenValid(refreshToken, user.getUsername());
			JwtUtil.revokeUserToken(user.getUsername());
			String accessToken = JwtUtil.generateAccessToken(user.getUsername());
			JwtUtil.saveToken(user.getUsername(), accessToken);
			TokenVO token = new TokenVO(accessToken, refreshToken);
			return ResponseVO.success(token);
		}
		catch (TokenExpiredException e) {
			throw new com.starimmortal.core.exception.TokenExpiredException();
		}
		catch (JWTVerificationException e) {
			throw new TokenInvalidException();
		}
	}

}

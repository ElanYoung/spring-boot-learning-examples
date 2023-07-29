package com.starimmortal.security.controller;

import com.starimmortal.core.vo.ResponseVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 权限测试控制器
 *
 * @author william@StarImmortal
 */
@RestController
public class HelloController {

	@PreAuthorize("hasAuthority('sys:user:add')")
	@GetMapping("/hello")
	public ResponseVO<String> hello() {
		return ResponseVO.success("Hello, Spring Security!");
	}

}

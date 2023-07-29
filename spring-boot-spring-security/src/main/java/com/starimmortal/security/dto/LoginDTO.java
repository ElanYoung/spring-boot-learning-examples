package com.starimmortal.security.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 登录数据传输对象
 *
 * @author william@StarImmortal
 * @date 2022/12/16
 */
@Data
public class LoginDTO {

	/**
	 * 用户名
	 */
	@NotBlank(message = "用户名不能为空")
	private String username;

	/**
	 * 密码
	 */
	@NotBlank(message = "密码不能为空")
	private String password;

}

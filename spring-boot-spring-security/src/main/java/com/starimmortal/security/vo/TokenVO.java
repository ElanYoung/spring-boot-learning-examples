package com.starimmortal.security.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 令牌视图对象
 *
 * @author william@StarImmortal
 * @date 2022/12/16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenVO {

	/**
	 * 访问令牌
	 */
	private String accessToken;

	/**
	 * 刷新令牌
	 */
	private String refreshToken;

}

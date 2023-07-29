package com.starimmortal.jwt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author william@StarImmortal
 * @date 2022/09/24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenVO {

	/**
	 * 令牌
	 */
	private String token;

}

package com.starimmortal.security.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 用户数据对象
 *
 * @author william@StarImmortal
 * @date 2022/12/16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@TableName(value = "sys_user")
public class UserDO extends BaseEntity {

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 昵称
	 */
	private String nickname;

	/**
	 * 密码
	 */
	@JsonIgnore
	private String password;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 手机号
	 */
	private String phone;

	/**
	 * 用户性别（0-男；1-女；2-未知）
	 */
	private Integer gender;

	/**
	 * 头像
	 */
	private String avatar;

	/**
	 * 账号状态（0-正常；1-停用）
	 */
	private Integer status;

}

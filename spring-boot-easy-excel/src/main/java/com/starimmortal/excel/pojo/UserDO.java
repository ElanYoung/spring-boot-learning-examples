package com.starimmortal.excel.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.Date;

/**
 * 用户数据对象
 *
 * @author william@StarImmortal
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "user")
public class UserDO extends BaseEntity {

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 昵称
	 */
	private String nickname;

	/**
	 * 生日
	 */
	private Date birthday;

	/**
	 * 手机号
	 */
	private String phone;

	/**
	 * 身高（米）
	 */
	private Double height;

	/**
	 * 性别
	 */
	private Integer gender;

}

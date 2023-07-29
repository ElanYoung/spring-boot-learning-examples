package com.starimmortal.security.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户角色数据对象
 *
 * @author william@StarImmortal
 * @date 2022/12/16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "sys_user_role")
public class UserRoleDO {

	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 用户编号
	 */
	private Long userId;

	/**
	 * 角色编号
	 */
	private Long roleId;

}

package com.starimmortal.security.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 菜单数据对象
 *
 * @author william@StarImmortal
 * @date 2022/12/16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@TableName(value = "sys_menu")
public class MenuDO extends BaseEntity {

	/**
	 * 父级菜单ID
	 */
	private Long parentId;

	/**
	 * 菜单名称
	 */
	private String name;

	/**
	 * 路由地址
	 */
	private String path;

	/**
	 * 权限标识
	 */
	private String permission;

	/**
	 * 菜单图标
	 */
	private String icon;

	/**
	 * 组件路径
	 */
	private String component;

	/**
	 * 菜单类型（0-菜单；1-按钮）
	 */
	private Integer type;

	/**
	 * 菜单状态（0-显示；1-隐藏）
	 */
	private Integer visible;

	/**
	 * 菜单状态（0-正常；1-停用）
	 */
	private Integer status;

	/**
	 * 缓存页面（0-开启；1- 关闭）
	 */
	private Integer keepAlive;

	/**
	 * 排序值
	 */
	private Integer sortOrder;

}

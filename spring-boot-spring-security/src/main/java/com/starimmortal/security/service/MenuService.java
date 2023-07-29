package com.starimmortal.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.starimmortal.security.pojo.MenuDO;

import java.util.List;

/**
 * 菜单服务接口
 *
 * @author william@StarImmortal
 * @date 2022/12/16
 */
public interface MenuService extends IService<MenuDO> {

	/**
	 * 查询角色对应菜单权限集合
	 * @param roleIds 角色编号列表
	 * @return 菜单权限集合
	 */
	List<MenuDO> listMenusByRoleIds(List<Long> roleIds);

}

package com.starimmortal.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.starimmortal.security.pojo.MenuDO;
import com.starimmortal.security.pojo.UserDO;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

/**
 * 用户服务接口
 *
 * @author william@StarImmortal
 * @date 2022/12/16
 */
public interface UserService extends IService<UserDO> {

	/**
	 * 基于数据库加载用户
	 * @param username 用户名
	 * @return 用户信息
	 */
	UserDetails loadUserByUsername(String username);

	/**
	 * 根据用户名查询用户
	 * @param username 用户名
	 * @return 用户信息
	 */
	UserDO getUserByUsername(String username);

	/**
	 * 查询用户所有权限
	 * @param userId 用户编号
	 * @return 权限列表
	 */
	List<MenuDO> listUserPermissions(Long userId);

}

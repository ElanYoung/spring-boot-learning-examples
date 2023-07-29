package com.starimmortal.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.starimmortal.core.util.GenericJacksonUtil;
import com.starimmortal.security.bean.LoginUser;
import com.starimmortal.security.mapper.UserMapper;
import com.starimmortal.security.pojo.MenuDO;
import com.starimmortal.security.pojo.UserDO;
import com.starimmortal.security.service.MenuService;
import com.starimmortal.security.service.RoleService;
import com.starimmortal.security.service.UserService;
import com.starimmortal.security.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * 用户服务接口实现类
 *
 * @author william@StarImmortal
 * @date 2022/12/16
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService, UserDetailsService {

	@Autowired
	private RoleService roleService;

	@Autowired
	private MenuService menuService;

	@Autowired
	private RedisUtil redisUtil;

	@Override
	public UserDetails loadUserByUsername(String username) {
		// 查询用户信息
		UserDO user = getUserByUsername(username);
		// 查询用户菜单列表
		List<MenuDO> menuList = listUserPermissions(user.getId());
		// 查询用户权限信息
		return new LoginUser(user, menuList);
	}

	@Override
	public UserDO getUserByUsername(String username) {
		LambdaQueryWrapper<UserDO> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(UserDO::getUsername, username);
		Optional<UserDO> optional = Optional.ofNullable(baseMapper.selectOne(queryWrapper));
		return optional.orElseThrow(() -> new UsernameNotFoundException("用户不存在"));
	}

	@Override
	public List<MenuDO> listUserPermissions(Long userId) {
		// TODO 用户权限新增问题
		String redisKey = "user:menu:" + userId + ":set";
		String redisValue = redisUtil.get(redisKey);
		if (StringUtils.hasText(redisValue)) {
			return GenericJacksonUtil.jsonToObject(redisValue, new TypeReference<List<MenuDO>>() {
			});
		}
		List<Long> roleIds = roleService.listRoleIdsByUserId(userId);
		List<MenuDO> menuList = menuService.listMenusByRoleIds(roleIds);
		if (!menuList.isEmpty()) {
			redisUtil.set(redisKey, GenericJacksonUtil.objectToJson(menuList));
		}
		return menuList;
	}

}

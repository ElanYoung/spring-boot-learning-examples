package com.starimmortal.security.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.starimmortal.security.mapper.RoleMapper;
import com.starimmortal.security.pojo.RoleDO;
import com.starimmortal.security.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色服务接口实现类
 *
 * @author william@StarImmortal
 * @date 2022/12/16
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RoleDO> implements RoleService {

	@Override
	public List<Long> listRoleIdsByUserId(Long userId) {
		return baseMapper.listUserRoleIds(userId);
	}

}

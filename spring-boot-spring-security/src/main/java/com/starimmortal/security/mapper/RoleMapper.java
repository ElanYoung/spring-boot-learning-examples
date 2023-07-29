package com.starimmortal.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.starimmortal.security.pojo.RoleDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 角色 Mapper 接口
 *
 * @author william@StarImmortal
 * @date 2022/12/16
 */
@Mapper
public interface RoleMapper extends BaseMapper<RoleDO> {

	/**
	 * 查询用户角色编号列表
	 * @param userId 用户编号
	 * @return 角色编号列表
	 */
	List<Long> listUserRoleIds(Long userId);

}

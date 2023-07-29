package com.starimmortal.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.starimmortal.security.pojo.MenuDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单 Mapper 接口
 *
 * @author william@StarImmortal
 * @date 2022/12/16
 */
@Mapper
public interface MenuMapper extends BaseMapper<MenuDO> {

	/**
	 * 根据角色编号列表查询对应菜单权限集合
	 * @param roleIds 角色编号列表
	 * @return 菜单权限集合
	 */
	List<MenuDO> listMenusByRoleIds(@Param("roleIds") List<Long> roleIds);

}

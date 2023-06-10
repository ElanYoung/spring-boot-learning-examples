package com.starimmortal.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.starimmortal.security.pojo.UserRoleDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户角色 Mapper 接口
 *
 * @author william@StarImmortal
 * @date 2022/12/16
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRoleDO> {

}

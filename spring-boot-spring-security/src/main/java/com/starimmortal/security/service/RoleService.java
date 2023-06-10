package com.starimmortal.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.starimmortal.security.pojo.RoleDO;

import java.util.List;

/**
 * 角色服务接口
 *
 * @author william@StarImmortal
 * @date 2022/12/16
 */
public interface RoleService extends IService<RoleDO> {
    /**
     * 查询某用户所有角色编号
     *
     * @param userId 用户编号
     * @return 角色编号列表
     */
    List<Long> listRoleIdsByUserId(Long userId);
}

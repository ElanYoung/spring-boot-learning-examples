package com.starimmortal.security.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.starimmortal.security.mapper.MenuMapper;
import com.starimmortal.security.pojo.MenuDO;
import com.starimmortal.security.service.MenuService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 菜单服务接口实现类
 *
 * @author william@StarImmortal
 * @date 2022/12/16
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, MenuDO> implements MenuService {

    @Override
    public List<MenuDO> listMenusByRoleIds(List<Long> roleIds) {
        return baseMapper.listMenusByRoleIds(roleIds);
    }
}

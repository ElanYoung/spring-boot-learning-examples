package com.starimmortal.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.starimmortal.security.pojo.UserDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户 Mapper 接口
 *
 * @author william@StarImmortal
 * @date 2022/12/16
 */
@Mapper
public interface UserMapper extends BaseMapper<UserDO> {

}

package com.starimmortal.excel.mapper;

import com.starimmortal.excel.base.CustomBaseMapper;
import com.starimmortal.excel.pojo.UserDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户 Mapper 接口
 *
 * @author william@StarImmortal
 * @date 2023/03/10
 */
@Mapper
public interface UserMapper extends CustomBaseMapper<UserDO> {

}

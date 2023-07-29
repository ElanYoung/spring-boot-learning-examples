package com.starimmortal.excel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.starimmortal.excel.pojo.UserDO;

import java.util.List;

/**
 * 用户服务接口
 *
 * @author william@StarImmortal
 * @date 2023/03/10
 */
public interface UserService extends IService<UserDO> {

	/**
	 * 导入用户
	 * @param userExcelList 用户Excel数据
	 */
	void importUser(List<Object> userExcelList);

}

package com.starimmortal.excel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.starimmortal.excel.converter.UserConverter;
import com.starimmortal.excel.dto.UserExcelDTO;
import com.starimmortal.excel.mapper.UserMapper;
import com.starimmortal.excel.pojo.UserDO;
import com.starimmortal.excel.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务接口实现类
 *
 * @author william@StarImmortal
 * @date 2023/03/10
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void importUser(List<Object> userExcelList) {
		if (!userExcelList.isEmpty()) {
			List<UserDO> userList = convertUserList(userExcelList);
			int rows = baseMapper.insertBatchSomeColumn(userList);
			if (rows != userList.size()) {
				throw new RuntimeException("Some entities were not saved");
			}
		}
	}

	private List<UserDO> convertUserList(List<Object> batchList) {
		// 使用 MapStruct 优化
		return batchList.stream()
			.map(bean -> UserConverter.INSTANCE.convert((UserExcelDTO) bean))
			.collect(Collectors.toList());
	}

}

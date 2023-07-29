package com.starimmortal.security.handler;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.starimmortal.security.bean.LoginUser;
import com.starimmortal.security.util.SecurityUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;

import java.nio.charset.Charset;

/**
 * Mybatis Plus 自动填充配置
 *
 * @author william@StarImmortal
 * @date 2023/02/08
 */
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {

	@Override
	public void insertFill(MetaObject metaObject) {
		fillValIfNullByName("createBy", getUsername(), metaObject, false);
		fillValIfNullByName("updateBy", getUsername(), metaObject, false);
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		fillValIfNullByName("updateBy", getUsername(), metaObject, true);
	}

	/**
	 * 填充值，先判断是否有手动设置，优先手动设置的值
	 * @param fieldName 属性名
	 * @param fieldVal 属性值
	 * @param metaObject MetaObject
	 * @param isCover 是否覆盖原有值，避免更新操作手动入参
	 */
	private static void fillValIfNullByName(String fieldName, Object fieldVal, MetaObject metaObject, boolean isCover) {
		// 1. 没有 set 方法
		if (!metaObject.hasSetter(fieldName)) {
			return;
		}
		// 2. 如果用户有手动设置的值
		Object userSetValue = metaObject.getValue(fieldName);
		String setValueStr = StrUtil.str(userSetValue, Charset.defaultCharset());
		if (StrUtil.isNotBlank(setValueStr) && !isCover) {
			return;
		}
		// 3. field 类型相同时设置
		Class<?> getterType = metaObject.getGetterType(fieldName);
		if (ClassUtils.isAssignableValue(getterType, fieldVal)) {
			metaObject.setValue(fieldName, fieldVal);
		}
	}

	/**
	 * 获取 Spring Security 当前用户名
	 * @return 当前用户名
	 */
	private String getUsername() {
		LoginUser loginUser = SecurityUtil.getLoginUser();
		if (!ObjectUtils.isEmpty(loginUser)) {
			return loginUser.getUsername();
		}
		return null;
	}

}

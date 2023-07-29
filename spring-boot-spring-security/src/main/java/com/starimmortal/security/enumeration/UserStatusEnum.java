package com.starimmortal.security.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * 用户状态枚举
 *
 * @author william@StarImmortal
 * @date 2021/08/17
 */
@Getter
public enum UserStatusEnum implements IEnum<Integer> {

	/**
	 * 启用
	 */
	ENABLE(0, "启用"),

	/**
	 * 停用
	 */
	DISABLE(1, "停用");

	@EnumValue
	private final Integer value;

	@JsonValue
	private final String description;

	UserStatusEnum(Integer value, String description) {
		this.value = value;
		this.description = description;
	}

	public static UserStatusEnum convert(Integer value) {
		return Stream.of(UserStatusEnum.values()).filter(bean -> bean.value.equals(value)).findAny().orElse(null);
	}

}

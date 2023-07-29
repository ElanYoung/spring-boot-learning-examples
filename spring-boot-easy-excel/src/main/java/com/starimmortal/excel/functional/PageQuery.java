package com.starimmortal.excel.functional;

import java.util.List;

/**
 * 分页查询接口
 *
 * @author william@StarImmortal
 * @date 2023/03/14
 */
@FunctionalInterface
public interface PageQuery<T> {

	/**
	 * 分页查询
	 * @param current 当前页
	 * @param size 每页大小
	 * @return 分页数据
	 */
	List<T> apply(long current, long size);

}

package com.starimmortal.excel.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 自定义拓展 BaseMapper 接口
 *
 * @param <T>
 * @author william@StarImmortal
 * @date 2023/03/13
 */
public interface CustomBaseMapper<T> extends BaseMapper<T> {
    /**
     * 真实批量插入
     *
     * @param entityList 插入数据
     * @return 影响行数
     */
    int insertBatchSomeColumn(List<T> entityList);
}

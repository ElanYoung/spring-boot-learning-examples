package com.starimmortal.excel.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;

import java.util.List;

/**
 * 自定义SQL注入器
 * <p>
 * Mapper 层选装件：<a href="https://baomidou.com/pages/49cc81/#insertbatchsomecolumn">...</a>
 *
 * @author william@StarImmortal
 * @date 2023/03/13
 */
public class CustomSqlInjector extends DefaultSqlInjector {
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
        methodList.add(new InsertBatchSomeColumn(i -> i.getFieldFill() != FieldFill.UPDATE && !i.isLogicDelete()));
        return methodList;
    }
}

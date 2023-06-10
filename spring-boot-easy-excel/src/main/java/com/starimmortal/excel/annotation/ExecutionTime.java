package com.starimmortal.excel.annotation;

import java.lang.annotation.*;

/**
 * 记录接口耗时注解
 *
 * @author william@StarImmortal
 * @date 2023/03/17
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
@Documented
public @interface ExecutionTime {

}

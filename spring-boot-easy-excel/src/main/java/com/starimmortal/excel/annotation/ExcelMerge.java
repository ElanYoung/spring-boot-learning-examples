package com.starimmortal.excel.annotation;

import java.lang.annotation.*;

/**
 * 用于判断是否需要合并以及合并的主键
 *
 * @author william@StarImmortal
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelMerge {

	/**
	 * 是否合并单元格
	 * @return true || false
	 */
	boolean merge() default true;

	/**
	 * 是否为主键（即该字段相同的行合并）
	 * @return true || false
	 */
	boolean isPrimaryKey() default false;

}

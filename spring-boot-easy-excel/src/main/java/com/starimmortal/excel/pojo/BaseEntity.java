package com.starimmortal.excel.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 基础数据模型类
 *
 * @author william@StarImmortal
 * @date 2021/02/08
 */
@Data
public class BaseEntity {

	/**
	 * 主键
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@TableField(update = "now()", fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime updateTime;

	/**
	 * 删除标志（0-未删除；时间戳-已删除）
	 */
	@TableLogic
	@TableField(value = "is_deleted")
	private Integer deleted;

}

package com.starimmortal.security.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 基础数据模型类
 *
 * @author william@StarImmortal
 * @date 2021/02/08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class BaseEntity {

	/**
	 * 主键
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	/**
	 * 创建人
	 */
	@TableField(fill = FieldFill.INSERT)
	private String createBy;

	/**
	 * 创建时间
	 */
	@JsonIgnore
	private LocalDateTime createTime;

	/**
	 * 更新人
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private String updateBy;

	/**
	 * 更新时间
	 */
	@TableField(update = "now()")
	@JsonIgnore
	private LocalDateTime updateTime;

	/**
	 * 删除标志（0-未删除；时间戳-已删除）
	 */
	@TableLogic
	@TableField(value = "is_deleted")
	private Integer deleted;

}

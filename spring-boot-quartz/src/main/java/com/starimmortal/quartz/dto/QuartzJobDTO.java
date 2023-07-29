package com.starimmortal.quartz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * 定时任务数据传输对象
 *
 * @author william@StarImmortal
 * @date 2022/09/26
 */
@Data
@ApiModel(value = "定时任务")
public class QuartzJobDTO {

	/**
	 * 任务类名
	 */
	@ApiModelProperty(value = "任务类名")
	private String jobClassName;

	/**
	 * cron表达式
	 */
	@ApiModelProperty(value = "cron表达式")
	private String cron;

	/**
	 * 定时任务名称
	 */
	@ApiModelProperty(value = "定时任务名称")
	private String jobName;

	/**
	 * 定时任务分组名称
	 */
	@ApiModelProperty(value = "定时任务分组名称")
	private String jobGroupName;

	/**
	 * 触发器名称
	 */
	@ApiModelProperty(value = "触发器名称")
	private String triggerName;

	/**
	 * 触发器分组名称
	 */
	@ApiModelProperty(value = "触发器分组名称")
	private String triggerGroupName;

	/**
	 * 任务描述
	 */
	@ApiModelProperty(value = "任务描述")
	private String description;

	/**
	 * 任务参数
	 */
	@ApiModelProperty(value = "任务参数")
	private Map<String, Object> params;

}

package com.starimmortal.quartz.vo;

import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * 定时任务视图对象
 *
 * @author william@StarImmortal
 * @date 2022/09/26
 */
@Data
public class QuartzJobVO {

	/**
	 * 任务类名
	 */
	private String jobClassName;

	/**
	 * cron表达式
	 */
	private String cron;

	/**
	 * 定时任务名称
	 */
	private String jobName;

	/**
	 * 定时任务分组名称
	 */
	private String jobGroupName;

	/**
	 * 触发器名称
	 */
	private String triggerName;

	/**
	 * 触发器分组名称
	 */
	private String triggerGroupName;

	/**
	 * 任务开始时间
	 */
	private Date startTime;

	/**
	 * 最后一次发生时间
	 */
	private Date previousFireTime;

	/**
	 * 下次发生时间
	 */
	private Date nextFireTime;

	/**
	 * 时区
	 */
	private String timeZone;

	/**
	 * 任务状态
	 */
	private String jobStatus;

	/**
	 * 任务描述
	 */
	private String description;

	/**
	 * 任务参数
	 */
	private Map<String, Object> params;

}

package com.starimmortal.quartz.service.impl;

import com.starimmortal.quartz.dto.QuartzJobDTO;
import com.starimmortal.quartz.service.QuartzJobService;
import com.starimmortal.quartz.vo.QuartzJobVO;
import lombok.SneakyThrows;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author william@StarImmortal
 * @date 2022/09/26
 */
@Service
public class QuartzJobServiceImpl implements QuartzJobService {

	@Autowired
	private Scheduler scheduler;

	@SneakyThrows
	@Override
	public void scheduleJob(QuartzJobDTO dto) {
		JobDetail jobDetail = JobBuilder.newJob(getClass(dto.getJobClassName()).getClass())
			.withIdentity(dto.getJobName(), dto.getJobGroupName())
			.withDescription(dto.getDescription())
			.storeDurably()
			.build();

		// 设置任务参数
		if (Objects.nonNull(dto.getParams()) && !dto.getParams().isEmpty()) {
			jobDetail.getJobDataMap().putAll(dto.getParams());
		}

		CronTrigger cronTrigger = TriggerBuilder.newTrigger()
			.withIdentity(dto.getTriggerName(), dto.getTriggerGroupName())
			.startNow()
			.withSchedule(CronScheduleBuilder.cronSchedule(dto.getCron()))
			.build();

		scheduler.scheduleJob(jobDetail, cronTrigger);

		// 启动调度器
		if (!scheduler.isShutdown()) {
			scheduler.start();
		}
	}

	@SneakyThrows
	@Override
	public void rescheduleJob(QuartzJobDTO dto) {
		TriggerKey triggerKey = TriggerKey.triggerKey(dto.getTriggerName(), dto.getTriggerGroupName());
		CronTrigger cronTrigger = TriggerBuilder.newTrigger()
			.withIdentity(triggerKey)
			.withSchedule(CronScheduleBuilder.cronSchedule(dto.getCron()))
			.startNow()
			.build();
		scheduler.rescheduleJob(triggerKey, cronTrigger);
	}

	@SneakyThrows
	@Override
	public void pauseJob(String jobName, String jobGroupName) {
		scheduler.pauseJob(JobKey.jobKey(jobName, jobGroupName));
	}

	@SneakyThrows
	@Override
	public void resumeJob(String jobName, String jobGroupName) {
		scheduler.resumeJob(JobKey.jobKey(jobName, jobGroupName));
	}

	@SneakyThrows
	@Override
	public void deleteJob(String jobName, String jobGroupName) {
		scheduler.pauseTrigger(TriggerKey.triggerKey(jobName, jobGroupName));
		scheduler.unscheduleJob(TriggerKey.triggerKey(jobName, jobGroupName));
		scheduler.deleteJob(JobKey.jobKey(jobName, jobGroupName));
	}

	@SneakyThrows
	@Override
	public List<QuartzJobVO> listJobs() {
		List<QuartzJobVO> jobList = new ArrayList<>();
		GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
		Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
		for (JobKey jobKey : jobKeys) {
			List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
			for (Trigger trigger : triggers) {
				QuartzJobVO quartzJob = new QuartzJobVO();
				quartzJob.setTriggerGroupName(trigger.getKey().getName());
				quartzJob.setTriggerName(trigger.getKey().getGroup());
				quartzJob.setJobGroupName(jobKey.getGroup());
				quartzJob.setJobName(jobKey.getName());
				quartzJob.setStartTime(trigger.getStartTime());
				quartzJob.setJobClassName(scheduler.getJobDetail(jobKey).getJobClass().getName());
				quartzJob.setNextFireTime(trigger.getNextFireTime());
				quartzJob.setPreviousFireTime(trigger.getPreviousFireTime());
				quartzJob.setJobStatus(scheduler.getTriggerState(trigger.getKey()).name());
				quartzJob.setDescription(scheduler.getJobDetail(jobKey).getDescription());
				quartzJob.setParams(scheduler.getJobDetail(jobKey).getJobDataMap());
				if (trigger instanceof CronTrigger) {
					CronTrigger cronTrigger = (CronTrigger) trigger;
					quartzJob.setCron(cronTrigger.getCronExpression());
					quartzJob.setTimeZone(cronTrigger.getTimeZone().getDisplayName());
				}
				jobList.add(quartzJob);
			}
		}
		return jobList;
	}

	/**
	 * 通过反射获取类
	 * @param className 类名
	 * @return {@link Job}
	 */
	@SneakyThrows
	private static Job getClass(String className) {
		Class<?> clazz = Class.forName(className);
		return (Job) clazz.newInstance();
	}

}

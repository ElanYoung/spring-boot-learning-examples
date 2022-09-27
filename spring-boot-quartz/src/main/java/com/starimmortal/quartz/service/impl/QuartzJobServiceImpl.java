package com.starimmortal.quartz.service.impl;

import com.starimmortal.quartz.dto.QuartzJobDTO;
import com.starimmortal.quartz.service.QuartzJobService;
import lombok.SneakyThrows;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public void scheduleJob(QuartzJobDTO quartzJobDTO) {
        Class<?> jobClazz = Class.forName("com.starimmortal.quartz.job" + quartzJobDTO.getJobClazz());
        JobDetail jobDetail = JobBuilder.newJob((Class<? extends Job>) jobClazz.newInstance())
                .withIdentity(quartzJobDTO.getJobName(), quartzJobDTO.getJobGroupName())
                .storeDurably()
                .build();

        CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                .withIdentity(quartzJobDTO.getTriggerName(), quartzJobDTO.getTriggerGroupName())
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule(quartzJobDTO.getCron()))
                .build();

        scheduler.scheduleJob(jobDetail, cronTrigger);
        if (!scheduler.isShutdown()) {
            scheduler.start();
        }
    }

    @SneakyThrows
    @Override
    public void rescheduleJob(QuartzJobDTO quartzJobDTO) {
        TriggerKey triggerKey = TriggerKey.triggerKey(quartzJobDTO.getJobName(), quartzJobDTO.getJobGroupName());
        CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerKey)
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule(quartzJobDTO.getCron()))
                .build();
        scheduler.rescheduleJob(triggerKey, cronTrigger);
    }

    @SneakyThrows
    @Override
    public void pauseJob(QuartzJobDTO quartzJobDTO) {
        scheduler.pauseJob(JobKey.jobKey(quartzJobDTO.getJobName(), quartzJobDTO.getJobGroupName()));
    }

    @SneakyThrows
    @Override
    public void resumeJob(QuartzJobDTO quartzJobDTO) {
        scheduler.resumeJob(JobKey.jobKey(quartzJobDTO.getJobName(), quartzJobDTO.getJobGroupName()));
    }

    @SneakyThrows
    @Override
    public void deleteJob(QuartzJobDTO quartzJobDTO) {
        scheduler.pauseTrigger(TriggerKey.triggerKey(quartzJobDTO.getJobName(), quartzJobDTO.getJobGroupName()));
        scheduler.unscheduleJob(TriggerKey.triggerKey(quartzJobDTO.getJobName(), quartzJobDTO.getJobGroupName()));
        scheduler.deleteJob(JobKey.jobKey(quartzJobDTO.getJobName(), quartzJobDTO.getJobGroupName()));
    }

    @SneakyThrows
    @Override
    public List<String> listJobs() {
        Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.anyGroup());
        return jobKeys.stream().map(JobKey::getName).collect(Collectors.toList());
    }
}

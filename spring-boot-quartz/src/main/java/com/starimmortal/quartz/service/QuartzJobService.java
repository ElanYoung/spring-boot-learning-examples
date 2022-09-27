package com.starimmortal.quartz.service;

import com.starimmortal.quartz.dto.QuartzJobDTO;

import java.util.List;

/**
 * @author william@StarImmortal
 * @date 2022/09/26
 */
public interface QuartzJobService {
    /**
     * 调度定时任务
     *
     * @param quartzJobDTO 定时任务校验器
     */
    void scheduleJob(QuartzJobDTO quartzJobDTO);


    /**
     * 重新定制定时任务
     *
     * @param quartzJobDTO 定时任务校验器
     */
    void rescheduleJob(QuartzJobDTO quartzJobDTO);

    /**
     * 暂停定时任务
     *
     * @param quartzJobDTO 定时任务校验器
     */
    void pauseJob(QuartzJobDTO quartzJobDTO);

    /**
     * 恢复定时任务
     *
     * @param quartzJobDTO 定时任务校验器
     */
    void resumeJob(QuartzJobDTO quartzJobDTO);

    /**
     * 删除定时任务
     *
     * @param quartzJobDTO 定时任务校验器
     */
    void deleteJob(QuartzJobDTO quartzJobDTO);

    /**
     * 查询所有任务
     *
     * @return 所有任务
     */
    List<String> listJobs();
}

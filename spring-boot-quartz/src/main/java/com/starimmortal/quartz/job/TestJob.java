package com.starimmortal.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author william@StarImmortal
 * @date 2023/7/4
 */
@Slf4j
public class TestJob implements Job {

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		log.info("A test job start...");
		JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
		log.info("参数: {}", jobDataMap.get("name"));
	}

}

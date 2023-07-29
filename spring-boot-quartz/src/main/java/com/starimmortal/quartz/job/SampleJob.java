package com.starimmortal.quartz.job;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author william@StarImmortal
 * @date 2022/09/25
 */
@Slf4j
public class SampleJob extends QuartzJobBean {

	@Override
	protected void executeInternal(@NonNull JobExecutionContext context) {
		log.info("A sample job start...");
	}

}

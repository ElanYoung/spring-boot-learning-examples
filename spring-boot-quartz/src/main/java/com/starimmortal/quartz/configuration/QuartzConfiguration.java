package com.starimmortal.quartz.configuration;

import com.starimmortal.quartz.job.SampleJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Quartz配置项：定义任务描述及任务触发规则
 *
 * @author william@StarImmortal
 * @date 2022/09/25
 */
@Configuration
public class QuartzConfiguration {

    @Bean
    public JobDetail jobDetail() {
        return JobBuilder.newJob(SampleJob.class)
                .withIdentity("job", "group")
                .withDescription("任务描述：内存方式运行")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger trigger() {
        return TriggerBuilder.newTrigger()
                .withIdentity("job", "group")
                .forJob(jobDetail())
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule("0,5 * * * * ?"))
                .build();
    }
}

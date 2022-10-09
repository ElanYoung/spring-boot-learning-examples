# Spring Boot 集成 Quartz（任务调度框架）

[官方网站](http://quartz-scheduler.org/)

[在线 Cron 表达式生成器](https://cron.qqe2.com/)

## 介绍

Quartz 是完全由 `Java` 开发的作业调度框架，当定时任务愈加复杂时，使用 `Spring` 注解 `@Schedule` 已经不能满足业务需要。

## 核心概念

### Scheduler

> Quartz 中的任务调度器，通过 `Trigger` 和 `JobDetail` 可以用来调度、暂停和删除任务。调度器就相当于一个容器，装载着任务和触发器，该类是一个接口，代表一个 `Quartz` 的独立运行容器，`Trigger` 和 `JobDetail` 可以注册到 `Scheduler` 中，两者在 `Scheduler` 中拥有各自的组及名称，组及名称是 `Scheduler` 查找定位容器中某一对象的依据，`Trigger` 的组及名称必须唯一，`JobDetail` 的组和名称也必须唯一（但可以和 `Trigger` 的组和名称相同，因为它们是不同类型的）

### Trigger

> Quartz 中的触发器，是一个类，描述触发 `Job` 执行的时间触发规则，主要有 `SimpleTrigger` 和 `CronTrigger` 这两个子类。

### JobDetail

> Quartz 中需要执行的任务详情，包括了任务的唯一标识和具体要执行的任务，可以通过 `JobDataMap` 往任务中传递数据。

### Job

> Quartz 中具体的任务，包含了执行任务的具体方法。

## 作业存储类型

### RAMJobStore

> 默认情况下 Quartz 会将任务调度存储在内存中，这种方式性能是最好的，因为内存的速度是最快的。不好的地方就是数据缺乏持久性，但程序崩溃或者重新发布的时候，所有运行信息都会丢失。

### JDBC

> 存储数据库后，可以做单点也可以做集群，当任务多了之后，可以统一进行管理，随时停止、暂停、修改任务。 关闭或者重启服务器，运行的信息都不会丢失。缺点就是运行速度快慢取决于连接数据库的快慢。

## 内存方式

### 引入依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-quartz</artifactId>
</dependency>
```

### 定义任务类

```java
@Slf4j
public class SampleJob extends QuartzJobBean {

    @Override
    protected void executeInternal(@NonNull JobExecutionContext context) {
      log.info("A sample job start...");
    }
}
```

### 定义任务描述及任务触发规则

```java
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
                .withIdentity("trigger", "group")
                .forJob(jobDetail())
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule("0,5 * * * * ?"))
                .build();
    }
}
```

> 注意：需指定storeDurably()，否则提示 Jobs added with no trigger must be durable. 异常

## 数据库方式

> 提示：项目示例采用MySQL方式

### 初始化 Quartz 数据表

下载 Quartz 发布包：[http://www.quartz-scheduler.org/downloads/](http://www.quartz-scheduler.org/downloads/)

![quartz-2.3.0-distribution.tar.gz](https://c2.im5i.com/2022/09/25/HInbm.md.png)

解压缩进入SQL脚本所在位置：`quartz-2.3.0-SNAPSHOT/src/org/quartz/impl/jdbcjobstore/tables_mysql_innodb.sql`

创建数据库 `quartz` 并导入 `tables_mysql_innodb.sql` 脚本文件：

![SQL](https://c2.im5i.com/2022/09/25/HIYNq.md.png)

### 引入依赖

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-quartz</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jdbc</artifactId>
    </dependency>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>druid-spring-boot-starter</artifactId>
        <version>1.2.12</version>
    </dependency>
</dependencies>
```

### 配置文件

```yaml
spring:
  # 数据源配置
  datasource:
    name: quartz
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/quartz?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=UTF8&rewriteBatchedStatements=true
    username: "root"
    password: "88888888"
    druid:
      # 开启监控页面
      stat-view-servlet:
        # 监控页面访问账号
        login-username: admin
        # 监控页面访问密码
        login-password: 123456
        enabled: true
      # 用于采集 web-jdbc 关联监控的数据
      web-stat-filter:
        enabled: true
        exclusions: "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*"
      # 监控统计拦截
      filters: stat,wall
  quartz:
    # 任务存储类型
    job-store-type: "jdbc"
    # 关闭时等待任务完成
    wait-for-jobs-to-complete-on-shutdown: false
    # 是否覆盖已有的任务
    overwrite-existing-jobs: true
    # 是否自动启动计划程序
    auto-startup: true
    # 延迟启动
    startup-delay: 0s
    jdbc:
      # 数据库架构初始化模式（never：从不进行初始化；always：每次都清空数据库进行初始化；embedded：只初始化内存数据库（默认值））
      initialize-schema: "never"
    # 相关属性配置
    properties:
      org:
        quartz:
          scheduler:
            # 调度器实例名称
            instanceName: QuartzScheduler
            # 分布式节点ID自动生成
            instanceId: AUTO
          jobStore:
            class: org.springframework.scheduling.quartz.LocalDataSourceJobStore
            driverDelegateClass: org.quartz.impl.jdbcjobstore.StdJDBCDelegate
            # 表前缀
            tablePrefix: QRTZ_
            # 是否开启集群
            isClustered: true
            # 数据源别名（自定义）
            dataSource: quartz
            # 分布式节点有效性检查时间间隔（毫秒）
            clusterCheckinInterval: 10000
            useProperties: false
          # 线程池配置
          threadPool:
            class: org.quartz.simpl.SimpleThreadPool
            threadCount: 10
            threadPriority: 5
            threadsInheritContextClassLoaderOfInitializingThread: true
```

> 注意：如果 Spring Boot 版本在 2.5.7 之前，需要将 jobStore.class 属性修改为：org.quartz.impl.jdbcjobstore.JobStoreTX

### 调度定时任务

```java
@Service
public class QuartzJobServiceImpl implements QuartzJobService {

    @Autowired
    private Scheduler scheduler;

    @SneakyThrows
    @Override
    public void scheduleJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName, String jobClazz, String cron) {
        Class<?> jobClazz = Class.forName("com.starimmortal.quartz.job" + jobClazz);
        JobDetail jobDetail = JobBuilder.newJob((Class<? extends Job>) jobClazz.newInstance())
                .withIdentity(jobName, jobGroupName)
                .storeDurably()
                .build();

        CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerName, triggerGroupName)
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                .build();

        scheduler.scheduleJob(jobDetail, cronTrigger);
        if (!scheduler.isShutdown()) {
            scheduler.start();
        }
    }
}
```

### 重新定制定时任务

```java
@Service
public class QuartzJobServiceImpl implements QuartzJobService {

    @Autowired
    private Scheduler scheduler;

    @SneakyThrows
    @Override
    public void rescheduleJob(String jobName, String jobGroupName, String cron) {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroupName);
        CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerKey)
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                .build();
        scheduler.rescheduleJob(triggerKey, cronTrigger);
    }
```

### 暂停定时任务

```java
@Service
public class QuartzJobServiceImpl implements QuartzJobService {

    @Autowired
    private Scheduler scheduler;

    @SneakyThrows
    @Override
    public void pauseJob(String jobName, String jobGroupName) {
        scheduler.pauseJob(JobKey.jobKey(jobName, jobGroupName));
    }
}
```

### 恢复定时任务

```java
@Service
public class QuartzJobServiceImpl implements QuartzJobService {

    @Autowired
    private Scheduler scheduler;
    
    @SneakyThrows
    @Override
    public void resumeJob(String jobName, String jobGroupName) {
        scheduler.resumeJob(JobKey.jobKey(jobName, jobGroupName));
    }
}
```

### 删除定时任务

```java
@Service
public class QuartzJobServiceImpl implements QuartzJobService {

    @Autowired
    private Scheduler scheduler;

    @SneakyThrows
    @Override
    public void deleteJob(String jobName, String jobGroupName) {
        scheduler.pauseTrigger(TriggerKey.triggerKey(jobName, jobGroupName));
        scheduler.unscheduleJob(TriggerKey.triggerKey(jobName, jobGroupName));
        scheduler.deleteJob(JobKey.jobKey(jobName, jobGroupName));
    }
}
```

### 查询所有定时任务

```java
@Service
public class QuartzJobServiceImpl implements QuartzJobService {

    @Autowired
    private Scheduler scheduler;

    @SneakyThrows
    @Override
    public List<String> listJobs() {
        Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.anyGroup());
        return jobKeys.stream().map(JobKey::getName).collect(Collectors.toList());
    }
}
```
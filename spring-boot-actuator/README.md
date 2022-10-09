# Spring Boot 集成 Actuator 监控工具

## 介绍

Spring Boot Actuator 提供了对 SpringBoot 应用程序（可以是生产环境）监视和管理的能力， 可以选择通过使用 **HTTP Endpoint** 或使用 **JMX** 来管理和监控 SpringBoot 应用程序。

Spring Boot Actuator 允许通过 Endpoints 对 Spring Boot 进行监控和交互。

Spring Boot 内置的 Endpoint 包括（两种Endpoint： WEB和JMX，WEB方式考虑到安全性默认只开启了/health）：

| ID               | JMX | Web     | Endpoint功能描述                                                                          |
|------------------|-----|---------|---------------------------------------------------------------------------------------|
| auditevents      | Yes | No      | 暴露当前应用的audit events （依赖AuditEventRepository）                                          |
| beans            | Yes | No      | Spring中所有Beans                                                                        |
| caches           | Yes | No      | 暴露可用的缓存                                                                               |
| conditions       | Yes | No      | 展示configuration 和auto-configuration类中解析的condition，并展示是否匹配的信息.                         |
| configprops      | Yes | No      | 展示所有的@ConfigurationProperties                                                         |
| env              | Yes | No      | 展示环境变量，来源于ConfigurableEnvironment                                                     |
| flyway           | Yes | No      | flyway数据迁移信息（依赖Flyway）                                                                |
| health           | Yes | **Yes** | 展示应用的健康信息                                                                             |
| heapdump         | N/A | No      | （**web应用时**）hprof 堆的dump文件（依赖HotSpot JVM）                                             |
| httptrace        | Yes | No      | 展示HTTP trace信息, 默认展示前100个（依赖HttpTraceRepository）                                      |
| info             | Yes | No      | 应用信息                                                                                  |
| integrationgraph | Yes | No      | 展示spring集成信息（依赖spring-integration-core）                                               |
| jolokia          | N/A | No      | （**web应用时**）通过HTTP暴露JMX beans（依赖jolokia-core）                                         |
| logfile          | N/A | No      | （**web应用时**）如果配置了logging.file.name 或者 logging.file.path，展示logfile内容                   |
| loggers          | Yes | No      | 展示或者配置loggers，比如修改日志的等级                                                               |
| liquibase        | Yes | No      | Liquibase 数据迁移信息（依赖Liquibase）                                                         |
| metrics          | Yes | No      | 指标信息                                                                                  |
| mappings         | Yes | No      | @RequestMapping映射路径                                                                   |
| prometheus       | N/A | No      | （**web应用时**）向prometheus暴露监控信息（依赖micrometer-registry-prometheus）                       |
| quartz           | Yes | No      | 展示 quartz任务信息                                                                         |
| scheduledtasks   | Yes | No      | 展示Spring Scheduled 任务信息                                                               |
| sessions         | Yes | No      | session信息                                                                             |
| shutdown         | Yes | No      | 关闭应用                                                                                  |
| startup          | Yes | No      | 展示ApplicationStartup的startup步骤的数据（依赖通在SpringApplication配置BufferingApplicationStartup） |
| threaddump       | Yes | No      | 线程dump                                                                                |

## 快速开始

### 引入依赖

```yml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

### 配置文件

```yml
management:
  endpoints:
    web:
      # 自定义管理端点路径
      base-path: /manage
      # 自定义端点
      exposure:
        include: "info,health,env,beans"
        # 启动所有端点
        # include: "*"
```

> 上述配置只暴露info、health、env、beans四个endpoints，web可通过`/manage/${endpoint}`进行访问

![beans](https://s1.ax1x.com/2022/08/09/v1Z8SK.png)

### 跨域访问

```yml
management:
  endpoints:
    web:
      cors:
        allowed-origins: "https://example.com"
        allowed-methods: "GET,POST"
```

### 获取Info信息

在`spring-boot-maven-plugin`中加入`build-info`， 编译成jar后运行，即可获取info：

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <executions>
                <execution>
                    <goals>
                        <goal>build-info</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

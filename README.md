<h1 align="center"><a href="https://github.com/ElanYoung" target="_blank">🤖 Spring Boot 2.x 实践案例</a></h1>
<p align="center">
  <a href="https://travis-ci.com/ElanYoung/spring-boot-learning-examples"><img alt="Travis-CI" src="https://travis-ci.com/xkcoding/spring-boot-demo.svg?branch=master"/></a>
  <a href="https://www.codacy.com/app/ElanYoung/spring-boot-learning-examples?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=xkcoding/spring-boot-demo&amp;utm_campaign=Badge_Grade"><img alt="Codacy" src="https://api.codacy.com/project/badge/Grade/1f2e3d437b174bfc943dae1600332ec1"/></a>
  <a href="https://doc.starimmortal.com"><img alt="author" src="https://img.shields.io/badge/author-ElanYoung-blue.svg"/></a>
  <a href="https://www.oracle.com/technetwork/java/javase/downloads/index.html"><img alt="JDK" src="https://img.shields.io/badge/JDK-1.8.0_312-orange.svg"/></a>
  <a href="https://docs.spring.io/spring-boot/docs/2.7.11/reference/html/"><img alt="Spring Boot" src="https://img.shields.io/badge/Spring Boot-2.7.11-brightgreen.svg"/></a>
  <a href="https://github.com/ElanYoung/spring-boot-learning-examples/blob/master/LICENSE"><img alt="LICENSE" src="https://img.shields.io/github/license/ElanYoung/spring-boot-learning-examples.svg"/></a>
</p>

<p align="center">
  <a href="https://github.com/ElanYoung/spring-boot-learning-examples/stargazers"><img alt="star" src="https://img.shields.io/github/stars/ElanYoung/spring-boot-learning-examples.svg?label=Stars&style=social"/></a>
  <a href="https://github.com/ElanYoung/spring-boot-learning-examples/network/members"><img alt="star" src="https://img.shields.io/github/forks/ElanYoung/spring-boot-learning-examples.svg?label=Fork&style=social"/></a>
  <a href="https://github.com/ElanYoung/spring-boot-learning-examples/watchers"><img alt="star" src="https://img.shields.io/github/watchers/ElanYoung/spring-boot-learning-examples.svg?label=Watch&style=social"/></a>
</p>

<p align="center">
  <span>English | <a href="./README.zh-CN.md">简体中文</a></span>
</p>

## Introduction

`spring-boot-learning-examples` is developed based on the `Spring Boot 2.7.x` version. It integrates the technology
stack and middleware commonly used in development. It is a project for deep learning and actual combat
with `Spring Boot`.

> If you have an example to contribute or needs to meet, it is very welcome to submit
> a [issue](https://github.com/ElanYoung/spring-boot-learning-examples/issues/new).

## Environment

- **JDK 1.8 +**
- **Maven 3.5 +**
- **Mysql 5.7 +**
- **IntelliJ IDEA 2018.2 +** (*Note: Please use IDEA and make sure plugin `lombok` installed.*)

## Getting Started

### Get Project

```bash
git clone https://github.com/ElanYoung/spring-boot-learning-examples.git
```

### Import Project

> Open `spring-boot-learning-examples` project in `IntelliJ IDEA`.

### Run Project

> Find the `Application` class in each module, right-click `Run 'Application'` to run each practice case.

## Learning Examples

| Module                       | Description                                | Code                                                                                                                                | Article                                                                                                  |
|------------------------------|--------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------|
| spring-boot-banner           | Spring  Boot 自定义 Banner                    | [spring-boot-banner](https://github.com/ElanYoung/spring-boot-learning-examples/tree/master/spring-boot-banner)                     | [《Spring Boot 自定义 Banner》](https://blog.csdn.net/qq991658923/article/details/121302050)                  |
| spring-boot-actuator         | Spring Boot 集成 Actuator 监控工具               | [spring-boot-actuator](https://github.com/ElanYoung/spring-boot-learning-examples/tree/master/spring-boot-actuator)                 | [《Spring Boot 集成 Actuator 监控工具》](https://blog.csdn.net/qq991658923/article/details/127112107)            |
| spring-boot-druid            | Spring Boot 集成 Druid 连接池                   | [spring-boot-druid](https://github.com/ElanYoung/spring-boot-learning-examples/tree/master/spring-boot-druid)                       | [《Spring Boot 集成 Druid 连接池》](https://blog.csdn.net/qq991658923/article/details/127112527)                |
| spring-boot-jasypt           | Spring Boot 集成 jasypt 实现敏感信息加密             | [spring-boot-jasypt](https://github.com/ElanYoung/spring-boot-learning-examples/tree/master/spring-boot-jasypt)                     | [《Spring Boot 集成 jasypt 实现敏感信息加密》](https://blog.csdn.net/qq991658923/article/details/127112431)          |
| spring-boot-websocket-native | Spring Boot 集成 WebSocket（原生注解）             | [spring-boot-websocket-native](https://github.com/ElanYoung/spring-boot-learning-examples/tree/master/spring-boot-websocket-native) | [《Spring Boot 集成 WebSocket（原生注解与Spring封装）》](https://blog.csdn.net/qq991658923/article/details/127022522) |
| spring-boot-websocket-spring | Spring Boot 集成 WebSocket（Spring封装）         | [spring-boot-websocket-spring](https://github.com/ElanYoung/spring-boot-learning-examples/tree/master/spring-boot-websocket-spring) | [《Spring Boot 集成 WebSocket（原生注解与Spring封装）》](https://blog.csdn.net/qq991658923/article/details/127022522) |
| spring-boot-jwt              | Spring Boot 集成 JWT                         | [spring-boot-jwt](https://github.com/ElanYoung/spring-boot-learning-examples/tree/master/spring-boot-jwt)                           | [《Spring Boot 集成 JWT》](https://blog.csdn.net/qq991658923/article/details/127027528)                      |
| spring-boot-minio            | Spring Boot 集成 MinIO（分布式文件存储系统）            | [spring-boot-minio](https://github.com/ElanYoung/spring-boot-learning-examples/tree/master/spring-boot-minio)                       | [《Spring Boot 集成 MinIO》](https://blog.csdn.net/qq991658923/article/details/124623495)                    |
| spring-boot-quartz           | Spring Boot 集成 Quartz（定时任务）                | [spring-boot-quartz](https://github.com/ElanYoung/spring-boot-learning-examples/tree/master/spring-boot-quartz)                     | [《Spring Boot 集成 Quartz》](https://blog.csdn.net/qq991658923/article/details/127078993)                   |
| spring-boot-easy-excel       | Spring Boot 集成 EasyExcel                   | [spring-boot-easy-excel](https://github.com/ElanYoung/spring-boot-learning-examples/tree/master/spring-boot-easy-excel)             | [《Spring Boot 集成 EasyExcel》](https://blog.csdn.net/qq991658923/article/details/128153012)                |
| spring-boot-h2               | Spring Boot 集成 H2（轻量级数据库）                  | [spring-boot-h2](https://github.com/ElanYoung/spring-boot-learning-examples/tree/master/spring-boot-h2)                             |                                                                                                          |
| spring-boot-spring-security  | Spring Boot 集成 Spring Security 5.7.x（安全框架） | [spring-boot-spring-security](https://github.com/ElanYoung/spring-boot-learning-examples/tree/master/spring-boot-spring-security)   | [《Spring Boot 优雅集成 Spring Security 5.7.x（安全框架）》](https://juejin.cn/post/7244089396567982136)             |

## Stargazers over time

[![Stargazers over time](https://starchart.cc/ElanYoung/spring-boot-learning-examples.svg)](https://starchart.cc/ElanYoung/spring-boot-learning-examples)

## License

[MIT](http://opensource.org/licenses/MIT)

Copyright (c) 2022 ElanYoung

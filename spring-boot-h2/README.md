# Spring Boot 集成 H2 数据库

> H2文档地址：[https://www.h2database.com](https://www.h2database.com)

## 介绍

Welcome to H2, the Java SQL database. The main features of H2 are:

+ Very fast, open source, JDBC API
+ Embedded and server modes; in-memory databases
+ Browser based Console application
+ Small footprint: around 2.5 MB jar file size

## 特点

+ Very fast, open source, JDBC API
+ Embedded and server modes; disk-based or in-memory databases
+ Transaction support, multi-version concurrency
+ Browser based Console application
+ Encrypted databases
+ Fulltext search
+ Pure Java with small footprint: around 2.5 MB jar file size
+ ODBC driver

## 快速开始

### 引入依赖

```xml
<dependencies>
    <!-- JPA -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <!-- h2 数据库 -->
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
</dependencies>
```

### 配置文件

```yml
spring:
  datasource:
    driver-class-name: org.h2.Driver
    # 内存模式
    url: jdbc:h2:mem:test;MODE=MYSQL
  h2:
    # 控制台
    console:
      enabled: true
      path: /h2
  sql:
    init:
      platform: h2
      schema-locations: sql/schema.sql
      data-locations: sql/data.sql
```

> 控制台Web地址：[http://localhost:8080/h2](http://localhost:8080/h2)

![h2 console](https://s2.loli.net/2023/05/18/LRw5UfO2zJAuGal.png)

### 连接模式

#### Embedded 嵌入式

`jdbc:h2:~/test`: 'test' in the user home directory

`jdbc:h2:/data/test`: 'test' in the directory /data

`jdbc:h2:./test`: in the current(!) working directory

#### Remote Mode 远程连接

`jdbc:h2:tcp://localhost/~/test`: user home dir

`jdbc:h2:tcp://localhost//data/test` or `jdbc:h2:tcp://localhost/D:/data/test`: absolute dir

#### In-Memory 内存模式

`jdbc:h2:mem:test`: multiple connections in one process, database is removed when all connections are closed

`jdbc:h2:mem:test;DB_CLOSE_DELAY=-1`: multiple connections in one process, database in not removed when all connections are closed (may create a memory leak)

`jdbc:h2:mem`: unnamed private; one connection

### 常见问题

#### Database "mem:test" not found, either pre-create it or allow remote database creation (not recommended in secure environments)

错误原因：`1.4.198` (2019-02-22) 版本开始，H2不再自动创建数据库

解决方案：降低版本H2版本或引入ORM框架（JPA或MyBatis）
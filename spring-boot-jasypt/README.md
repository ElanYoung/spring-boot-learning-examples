# Spring Boot 集成 jasypt 实现敏感信息加密

## 介绍

> jasypt 是一个开源的 Java 加密库，可以用来加密敏感信息，比如数据库密码、配置文件中的密码等。它提供了强大的加密算法，比如 AES、DES、PBE 等，可以满足大部分的加密需求。

[官方仓库](https://github.com/ulisesbocchio/jasypt-spring-boot)

## 引入依赖

```xml
<dependency>
    <groupId>com.github.ulisesbocchio</groupId>
    <artifactId>jasypt-spring-boot-starter</artifactId>
    <version>3.0.5</version>
</dependency>
```

## 配置加密密钥

在`application.yml`配置文件里新增如下配置：

```yml
jasypt:
  encryptor:
  	# 配置加密密钥
    password: StarImmortal
```

> 加密密钥建议使用环境变量或者启动参数：--jasypt.encryptor.password=StarImmortal

## 配置密文前后缀

`jasypt`默认提供`ENC`来标记加密字段，可以自定义密文前后缀标记：

```yml
jasypt:
  encryptor:
  	# 配置加密密钥
    password: StarImmortal
    # 配置密文前后缀
    property:
    	prefix: StarImmortal(
    	suffix: )
```

## 加密测试

```java
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class JasyptApplication implements CommandLineRunner {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private StringEncryptor stringEncryptor;

    public static void main(String[] args) {
        SpringApplication.run(JasyptApplication.class, args);
    }

    @Override
    public void run(String... args) {
        Environment environment = applicationContext.getEnvironment();

        // 首先获取配置文件里的原始明文信息
        String mysqlOriginPassword = environment.getProperty("spring.datasource.password");

        // 加密
        String mysqlEncryptedPassword = stringEncryptor.encrypt(mysqlOriginPassword);

        // 打印加密前后的结果对比
        System.out.println("MySQL原始明文密码为：" + mysqlOriginPassword);
        System.out.println("====================================");
        System.out.println("MySQL原始明文密码加密后的结果为：" + mysqlEncryptedPassword);
    }
}
```

```
MySQL原始明文密码为：123456
====================================
MySQL原始明文密码加密后的结果为：IV7SyeQOfG4GhiXeGLboVgOLPDO+dJMDoOdmEOQp3KyVjruI+dKKeehsTriWPKbo
```

## 替换待加密配置项

```yml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://127.0.0.1:3306/jasypt?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=UTF8
    username: "root"
    password: StarImmortal(IV7SyeQOfG4GhiXeGLboVgOLPDO+dJMDoOdmEOQp3KyVjruI+dKKeehsTriWPKbo)
```

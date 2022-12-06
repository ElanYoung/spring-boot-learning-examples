# Spring Boot 集成 Druid 连接池

> Druid文档地址：[https://github.com/alibaba/druid/wiki/](https://github.com/alibaba/druid/wiki/)

## 介绍

Druid 是阿里巴巴开源平台上一个数据库连接池实现，结合了 C3P0、DBCP 等 DB 池的优点，同时加入了日志监控。

Druid 可以很好的监控 DB 池连接和 SQL 的执行情况，天生就是针对监控而生的 DB 连接池。

Druid 已经在阿里巴巴部署了超过 600 个应用，经过一年多生产环境大规模部署的严苛考验。

## 快速开始

### 引入依赖

```xml
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid-spring-boot-starter</artifactId>
    <version>1.2.12</version>
</dependency>
```

### 配置文件

```yml
spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://127.0.0.1:3306/druid?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=UTF8
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
```

> 监控页面Web地址：[http://localhost:8080/druid/index.html](http://localhost:8080/druid/index.html)

### 移除广告

```java
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.alibaba.druid.spring.boot.autoconfigure.properties.DruidStatProperties;
import com.alibaba.druid.util.Utils;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import java.io.IOException;

/**
 * Druid 连接池配置类
 *
 * @author william@StarImmortal
 * @date 2022/08/09
 */
@Configuration
@AutoConfigureAfter(DruidDataSourceAutoConfigure.class)
public class DruidConfiguration {

    /**
     * 带有广告的common.js全路径，druid-1.2.11
     */
    private static final String FILE_PATH = "support/http/resources/js/common.js";

    /**
     * 原始脚本，触发构建广告的语句
     */
    private static final String ORIGIN_JS = "this.buildFooter();";

    /**
     * 替换后的脚本
     */
    private static final String NEW_JS = "//this.buildFooter();";

    /**
     * 去除Druid监控页面的广告
     *
     * @param properties DruidStatProperties属性集合
     * @return {@link FilterRegistrationBean}
     */
    @Bean
    @ConditionalOnWebApplication
    @ConditionalOnProperty(name = "spring.datasource.druid.stat-view-servlet.enabled", havingValue = "true")
    public FilterRegistrationBean<RemoveAdFilter> removeDruidAdFilter(DruidStatProperties properties) throws IOException {
        // 获取web监控页面的参数
        DruidStatProperties.StatViewServlet config = properties.getStatViewServlet();
        // 提取common.js的配置路径
        String pattern = config.getUrlPattern() != null ? config.getUrlPattern() : "/druid/*";
        String commonJsPattern = pattern.replaceAll("\\*", "js/common.js");
        // 获取common.js
        String text = Utils.readFromResource(FILE_PATH);
        // 屏蔽 this.buildFooter(); 不构建广告
        final String newJs = text.replace(ORIGIN_JS, NEW_JS);
        FilterRegistrationBean<RemoveAdFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RemoveAdFilter(newJs));
        registration.addUrlPatterns(commonJsPattern);
        return registration;
    }

    /**
     * 删除druid的广告过滤器
     *
     * @author BBF
     */
    private static class RemoveAdFilter implements Filter {

        private final String newJs;

        public RemoveAdFilter(String newJs) {
            this.newJs = newJs;
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            chain.doFilter(request, response);
            // 重置缓冲区，响应头不会被重置
            response.resetBuffer();
            response.getWriter().write(newJs);
        }
    }
}
```

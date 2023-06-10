package com.starimmortal.security;

import com.starimmortal.core.annotation.IgnoreResponseAdvice;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 启动类
 *
 * @author william@StarImmortal
 * @date 2022/12/16
 */
@RestController
@SpringBootApplication
@MapperScan(basePackages = "com.starimmortal.security.mapper")
@ComponentScan(basePackages = "com.starimmortal")
public class SecurityApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityApplication.class);

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(SecurityApplication.class);
        Environment environment = application.run(args).getEnvironment();
        LOGGER.info("启动成功...");
        LOGGER.info("地址：http://127.0.0.1:{}", environment.getProperty("server.port"));
    }

    @IgnoreResponseAdvice
    @GetMapping("/")
    public String index() {
        return "<style type=\"text/css\">*{ padding: 0; margin: 0; } div{ padding: 4px 48px;} a{color:#2E5CD5;cursor:" +
                "pointer;text-decoration: none} a:hover{text-decoration:underline; } body{ background: #fff; font-family:" +
                "\"Century Gothic\",\"Microsoft yahei\"; color: #333;font-size:18px;} h1{ font-size: 100px; font-weight: normal;" +
                "margin-bottom: 12px; } p{ line-height: 1.6em; font-size: 42px }</style><div style=\"padding: 24px 48px;\"><p>" +
                "星野团队 <br/><span style=\"font-size:30px\">凡心所向，素履以往，生如逆旅，一苇以航</span></p></div> ";
    }
}

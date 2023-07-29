package com.starimmortal.jasypt;

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
		String redisOriginPassword = environment.getProperty("spring.redis.password");

		// 加密
		String mysqlEncryptedPassword = stringEncryptor.encrypt(mysqlOriginPassword);
		String redisEncryptedPassword = stringEncryptor.encrypt(redisOriginPassword);

		// 打印加密前后的结果对比
		System.out.println("MySQL原始明文密码为：" + mysqlOriginPassword);
		System.out.println("Redis原始明文密码为：" + redisOriginPassword);
		System.out.println("====================================");
		System.out.println("MySQL原始明文密码加密后的结果为：" + mysqlEncryptedPassword);
		System.out.println("Redis原始明文密码加密后的结果为：" + redisEncryptedPassword);
	}

}

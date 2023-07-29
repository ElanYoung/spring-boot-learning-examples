package com.starimmortal.security;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
@Slf4j
class SecurityApplicationTests {

	@Test
	void passwordEncoder() {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String password = passwordEncoder.encode("123456");
		log.info("加密密文：{}", password);
		boolean matches = passwordEncoder.matches("123456", password);
		log.info("是否匹配：{}", matches);
	}

}

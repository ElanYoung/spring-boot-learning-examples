package com.starimmortal.minio.configuration;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MinIO配置类
 *
 * @author william@StarImmortal
 * @date 2022/09/02
 */
@Configuration
public class MinioConfiguration {

	private MinioProperties minioProperties;

	@Autowired
	public void setMinioProperties(MinioProperties minioProperties) {
		this.minioProperties = minioProperties;
	}

	/**
	 * 初始化客户端
	 * @return 客户端
	 */
	@Bean
	public MinioClient minioClient() {
		return MinioClient.builder()
			.endpoint(minioProperties.getEndpoint())
			.credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
			.build();
	}

}

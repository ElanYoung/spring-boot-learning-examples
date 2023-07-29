package com.starimmortal.minio.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author william@StarImmortal
 * @date 2022/9/4
 */
@Configuration
@ConfigurationProperties("minio")
public class MinioProperties {

	/**
	 * 服务地址
	 */
	private String endpoint;

	/**
	 * 文件预览地址
	 */
	private String preview;

	/**
	 * 存储桶名称
	 */
	private String bucket;

	/**
	 * 用户名
	 */
	private String accessKey;

	/**
	 * 密码
	 */
	private String secretKey;

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getPreview() {
		return preview;
	}

	public void setPreview(String preview) {
		this.preview = preview;
	}

	public String getBucket() {
		return bucket;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

}

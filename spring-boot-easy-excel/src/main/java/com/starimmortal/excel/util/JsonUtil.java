package com.starimmortal.excel.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Json工具类
 *
 * @author william@StarImmortal
 */
@Component
public class JsonUtil {

	public static ObjectMapper objectMapper;

	@Autowired
	public void setObjectMapper(ObjectMapper objectMapper) {
		JsonUtil.objectMapper = objectMapper;
	}

	public static <T> T readJsonFile(String filePath, TypeReference<T> typeReference) {
		if (!StringUtils.hasText(filePath)) {
			throw new RuntimeException();
		}
		try {
			ClassPathResource classPathResource = new ClassPathResource(filePath);
			InputStream inputStream = classPathResource.getInputStream();
			return JsonUtil.objectMapper.readValue(inputStream, typeReference);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> List<T> readJsonArrayFile(String filePath, Class<T> clazz) {
		if (!StringUtils.hasText(filePath)) {
			throw new RuntimeException();
		}
		try {
			ClassPathResource classPathResource = new ClassPathResource(filePath);
			InputStream inputStream = classPathResource.getInputStream();
			JavaType javaType = getCollectionType(List.class, clazz);
			return JsonUtil.objectMapper.readValue(inputStream, javaType);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
		return objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
	}

}

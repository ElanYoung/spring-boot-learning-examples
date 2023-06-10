package com.starimmortal.core.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.starimmortal.core.exception.ServerErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Jackson工具类
 *
 * @author william@StarImmortal
 * @date 2022/02/19
 */
@Component
public class GenericJacksonUtil {

    public static ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        GenericJacksonUtil.objectMapper = objectMapper;
    }

    public static <T> String objectToJson(T object) {
        try {
            assert GenericJacksonUtil.objectMapper != null;
            return GenericJacksonUtil.objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ServerErrorException(9999);
        }
    }

    public static <T> T jsonToObject(String s, TypeReference<T> typeReference) {
        if (s == null) {
            return null;
        }
        try {
            assert GenericJacksonUtil.objectMapper != null;
            return GenericJacksonUtil.objectMapper.readValue(s, typeReference);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ServerErrorException(9999);
        }
    }

    public static <T> List<T> jsonToList(String s) {
        if (s == null) {
            return null;
        }
        try {
            assert GenericJacksonUtil.objectMapper != null;
            return GenericJacksonUtil.objectMapper.readValue(s, new TypeReference<List<T>>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ServerErrorException(9999);
        }
    }

}

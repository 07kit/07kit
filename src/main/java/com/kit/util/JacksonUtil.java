package com.kit.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 */
public class JacksonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T deserialize(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> String serialize(T bean) {
        try {
            return objectMapper.writeValueAsString(bean);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

}

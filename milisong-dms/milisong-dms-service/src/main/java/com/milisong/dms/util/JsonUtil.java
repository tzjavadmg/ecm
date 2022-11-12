package com.milisong.dms.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import java.io.IOException;

/**
 * @author zhaozhonghui
 * @date 2018-11-04
 */
public class JsonUtil {
    /**
     * 17      * 将对象的大写转换为下划线加小写，例如：userName-->user_name
     * 18      *
     * 19      * @param object
     * 20      * @return
     * 21      * @throws JsonProcessingException
     * 22
     */
    public static String toUnderlineJSONString(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String reqJson = mapper.writeValueAsString(object);
        return reqJson;
    }

    /**
     * 32      * 将下划线转换为驼峰的形式，例如：user_name-->userName
     * 33      *
     * 34      * @param json
     * 35      * @param clazz
     * 36      * @return
     * 37      * @throws IOException
     * 38
     */
    public static <T> T toSnakeObject(String json, Class<T> clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        // 如果json中有新增的字段并且是实体类类中不存在的，不报错
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        T reqJson = mapper.readValue(json, clazz);
        return reqJson;
    }
}
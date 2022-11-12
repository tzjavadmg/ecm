package com.milisong.oms.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.google.common.base.Strings;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 名    称：下划线驼峰相互转换
 * 功    能：
 * 创 建 人：sailor
 * 创建时间：2017/9/27下午6:45
 * 修 改 人：
 * 修改时间：
 * 说    明：
 * 版 本 号：
 */
public class Underline2Camel {

    private final static Pattern pattern = Pattern.compile("([A-Za-z\\d]+)(_)?");

    /**
     * 将对象的大写转换为下划线加小写，例如：userName-->user_name
     *
     * @param object object
     * @return 转换后的字符串
     */
    public static String underline2Camel(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String reqJson = mapper.writeValueAsString(object);
        return reqJson;
    }

    /**
     * 下划线转驼峰法   例如：user_name-->userName
     *
     * @param line       源字符串
     * @param smallCamel 大小驼峰,是否为小驼峰
     * @return 转换后的字符串
     */
    public static String underline2Camel(String line, boolean smallCamel) {
        if (Strings.isNullOrEmpty(line)) {
            return line;
        }
        StringBuffer sb = new StringBuffer();
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String word = matcher.group();
            sb.append(smallCamel && matcher.start() == 0 ? Character.toLowerCase(word.charAt(0)) : Character.toUpperCase(word.charAt(0)));
            int index = word.lastIndexOf('_');
            if (index > 0) {
                sb.append(word.substring(1, index).toLowerCase());
            } else {
                sb.append(word.substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }

    /**
     * 将下划线转换为驼峰的形式，例如：user_name-->userName
     *
     * @param json
     * @param clazz
     * @return
     * @throws IOException
     */
    public static <T> T toSnakeObject(String json, Class<T> clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        T reqJson = mapper.readValue(json, clazz);
        return reqJson;
    }

    public static void main(String[] args) {

    }

}

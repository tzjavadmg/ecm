package com.milisong.dms.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhaozhonghui
 * @date 2018-11-13
 */
@Slf4j
@Component
public class UrlUtils {
    @Autowired
    private RestTemplate restTemplate;
    @Value("${url.compress-url}")
    private String shortUrl;

    public String compressToShortUrl(String url) {
        Map<String, Object> map = new HashMap<>();
        map.put("url", url);
        JSONObject result = restTemplate.getForObject(shortUrl, JSONObject.class, map);
        JSONArray urls = result.getJSONArray("urls");
        JSONObject jsonObject = urls.getJSONObject(0);
        log.info("【生成短网址】：result={}", JSONObject.toJSONString(result));
        return jsonObject.getString("url_short");

    }
}

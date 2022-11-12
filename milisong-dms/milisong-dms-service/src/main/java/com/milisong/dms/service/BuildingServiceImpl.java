package com.milisong.dms.service;

import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.milisong.dms.api.BuildingService;
import com.milisong.dms.dto.httpdto.BuildingDto;
import com.milisong.dms.dto.httpdto.ScmUrlDto;
import com.milisong.dms.util.ShunfengRedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhaozhonghui
 * @Description 门店http请求接口
 * @date 2018-12-11
 */
@Slf4j
@Service
public class BuildingServiceImpl implements BuildingService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ScmUrlDto scmUrlDto;

    @Override
    public BuildingDto getBuildingById(Long buildingId) {
        log.info("http请求楼宇接口,请求路径:{}", scmUrlDto.getQueryBuildingUrl());
        Map<String, Object> map = new HashMap<>();
        map.put("id", buildingId);
        ResponseResult result = restTemplate.getForObject(scmUrlDto.getQueryBuildingUrl(), ResponseResult.class, map);
        log.info("http请求scm楼宇信息返回:{}", JSONObject.toJSONString(result));
        if (result.isSuccess()) {
            return JSONObject.parseObject(JSONObject.toJSONString(result.getData()), BuildingDto.class);
        } else {
            log.error(result.getCode() + ":" + result.getMessage());
            return null;
        }
    }

    @Override
    public void syncBuildingInfo(BuildingDto dto) {
        String buildingKey = ShunfengRedisKeyUtil.getBuildingKey();
        RedisCache.hPut(buildingKey, String.valueOf(dto.getId()), JSONObject.toJSONString(dto));
    }
}

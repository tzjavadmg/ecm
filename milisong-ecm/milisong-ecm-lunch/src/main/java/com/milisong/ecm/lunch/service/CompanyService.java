package com.milisong.ecm.lunch.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.farmland.core.util.BeanMapper;
import com.google.common.collect.Lists;
import com.milisong.ecm.common.enums.StatusConstant;
import com.milisong.ecm.common.util.RedisKeyUtils;
import com.milisong.ecm.common.util.RestHttpUtils;
import com.milisong.ecm.lunch.dto.CompanyDto;
import com.milisong.ecm.lunch.dto.PointSearchParamDto;
import com.milisong.ecm.lunch.dto.PointSearchResultDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <pre>
 *    author  : tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2019/2/18   15:07
 *    desc    : 用户定位附近公司业务
 *    version : v1.0
 * </pre>
 */
@Slf4j
@RestController
public class CompanyService {

    @Value("${lbs.distance:3}")
    private Double lbsDistance;
    @Value("${lbs.search-company-service-url}")
    private String LBS_SEARCH_AROUND_COMPANY;
    @Autowired
    private RestTemplate restTemplate;

    public List<CompanyDto> listNearbyCompany(double lon, double lat, int limit) {
        log.info("查询LBS楼宇信息，经度：{}，纬度：{}，条数：{}", lon, lat, limit);
        // 获取redis里全局的配置
        String redisLbsDistance = (String) RedisCache.hGet("config:global", "lbsDistance");
        if(StringUtils.isNotBlank(redisLbsDistance)) {
            lbsDistance = Double.valueOf(redisLbsDistance);
        }
        PointSearchParamDto postDto = new PointSearchParamDto();
        postDto.setLat(lat);
        postDto.setLon(lon);
        postDto.setDistance(lbsDistance);
        postDto.setLimit(limit);
        postDto.setType("baidu");
        postDto.setMetrics("km");
        ResponseResult<Object> responseResult = RestHttpUtils.post(restTemplate,LBS_SEARCH_AROUND_COMPANY, postDto);
        List<PointSearchResultDto> result = BeanMapper.mapList((List)responseResult.getData(),PointSearchResultDto.class);
        log.info("查询lbs后的公司id数据：{}", JSONObject.toJSONString(result));
        if(!CollectionUtils.isEmpty(result)) {
            Map<Long,PointSearchResultDto> buildings = result.stream().collect(Collectors.toMap(o-> Long.parseLong(o.getBusinessId()), o-> o));
            List<CompanyDto> buildingList = getCompanyList(buildings.keySet());
            if (!CollectionUtils.isEmpty(buildingList)) {
                List<CompanyDto> resp = new ArrayList<>(buildingList.size());
                List<CompanyDto> closeResp = new ArrayList<>();
                buildingList.stream().forEach(item -> {
                    item.setDistance(buildings.get(item.getId()).getDistance());
                    if (StatusConstant.CompanyStatusEnum.OPENED.getValue() == item.getOpenStatus()) {
                        resp.add(item);
                    }else if(StatusConstant.CompanyStatusEnum.CLOSED.getValue() == item.getOpenStatus()){
                        closeResp.add(item);
                    }
                });
                //排序要求，优先显示开通的，再次根据距离排序
                resp.sort((o1,o2)-> (o1.getDistance()>o2.getDistance()?1:-1));
                closeResp.sort((o1,o2)-> (o1.getDistance()>o2.getDistance()?1:-1));
                resp.addAll(closeResp);
                log.info("查询LBS楼宇信息的结果：{}", JSONObject.toJSONString(resp));
                return resp;
            }
        }
        log.warn("未查询到LBS信息");
        return null;
    }

    private List<CompanyDto> getCompanyList(Set<Long> companyIds) {
        log.info("获取公司详细信息{}", JSON.toJSONString(companyIds));
        List<CompanyDto> resultList =  Lists.newArrayList();
        if (!CollectionUtils.isEmpty(companyIds)) {
            companyIds.stream().forEach(o->{
                String buildResult = RedisCache.get(RedisKeyUtils.getCompanyKey(o));
                if(!StringUtils.isBlank(buildResult)){
                    CompanyDto companyDto = JSONObject.parseObject(buildResult, CompanyDto.class);
                    resultList.add(companyDto);
                }
            });
        }
        return resultList;
    }

}

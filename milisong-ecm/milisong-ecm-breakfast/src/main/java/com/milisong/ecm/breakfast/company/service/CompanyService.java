package com.milisong.ecm.breakfast.company.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.farmland.core.util.BeanMapper;
import com.google.common.collect.Lists;
import com.milisong.ecm.breakfast.dto.CompanyDto;
import com.milisong.ecm.breakfast.dto.PointSearchParamDto;
import com.milisong.ecm.breakfast.dto.PointSearchResultDto;
import com.milisong.ecm.common.enums.StatusConstant;
import com.milisong.ecm.common.util.RedisKeyUtils;
import com.milisong.ecm.common.util.RestHttpUtils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class CompanyService {

	@Value("${lbs.distance:3}")
	private Double lbsDistance;
	@Value("${lbs.search-company-service-url}")
	private String LBS_SEARCH_AROUND_COMPANY;
	@Autowired
	private RestTemplate restTemplate;

	public List<CompanyDto> getCompanyList(List<Long> companyIds) {
		log.info("获取公司详细信息{}",JSON.toJSONString(companyIds));
		List<CompanyDto> resultList =  Lists.newArrayList();
		if (!CollectionUtils.isEmpty(companyIds)) {
			for (Long companyId : companyIds) {
				String buildingKey = RedisKeyUtils.getCompanyKey(companyId);
				String buildResult = RedisCache.get(buildingKey);
				CompanyDto companyDto = JSONObject.parseObject(buildResult, CompanyDto.class);
                if (companyDto != null) {
                    resultList.add(companyDto);
                }
			}
			
		}
		return resultList;
	}


    public ResponseResult<List<CompanyDto>> getCompanyList() {
		String companyListKey = RedisKeyUtils.getCompanyListKey();
		List<CompanyDto> companyList = Lists.newArrayList();
		//从redis获取公司集合数据
		Map<Object, Object> companyMap = RedisCache.hGetAll(companyListKey);
		for (Map.Entry<Object, Object> m : companyMap.entrySet()) {
			Object value = m.getValue();
			if (value !=null) {
				CompanyDto buildingDto = JSONObject.parseObject(value.toString(),CompanyDto.class);
				companyList.add(buildingDto);
			}
		}
        log.info("获取公司信息：{}", JSON.toJSONString(companyList));
        return ResponseResult.buildSuccessResponse(companyList);
    }

	public List<CompanyDto> listNearbyCompany(double lon, double lat, int limit) {
		log.info("查询LBS楼宇信息，经度：{}，纬度：{}，条数：{}", lon, lat, limit);
		// 获取redis里全局的配置
		String redisLbsDistance = (String)RedisCache.hGet("config:global", "lbsDistance");
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
		if(null != result && result.size() > 0) {
			Map<Long, Double> data = new HashMap<>();
			List<Long> buildings = new ArrayList<>();
			result.forEach(item -> {
				data.put(Long.valueOf(item.getBusinessId()), item.getDistance());
				buildings.add(Long.valueOf(item.getBusinessId()));
			});
			if(!CollectionUtils.isEmpty(buildings)) {
				List<CompanyDto> buildingList = getCompanyList(buildings);
				if(!CollectionUtils.isEmpty(buildingList)) {
					List<CompanyDto> resp = new ArrayList<>();
					buildingList.stream().forEach(item -> {
						if(StatusConstant.CompanyStatusEnum.OPENED.getValue() == item.getOpenStatus()) {
							item.setDistance(data.get(item.getId()));
							resp.add(item);
						}
					});
					buildingList.stream().forEach(item -> {
						if(StatusConstant.CompanyStatusEnum.CLOSED.getValue() == item.getOpenStatus()) {
							item.setDistance(data.get(item.getId()));
							resp.add(item);
						}
					});
					//排序要求，有限显示开通的，再次根据距离排序
					log.info("查询LBS楼宇信息的结果：{}", JSONObject.toJSONString(resp));
					return resp;
				}
			}
		}
		log.warn("未查询LBS楼宇信息");
		return null;
	}
}

/*
package com.milisong.ecm.lunch.goods.service;

import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.farmland.core.util.BeanMapper;
import com.milisong.ecm.common.enums.StatusConstant;
import com.milisong.ecm.common.util.RedisKeyUtils;
import com.milisong.ecm.common.util.RestHttpUtils;
import com.milisong.ecm.lunch.dto.PointSearchParamDto;
import com.milisong.ecm.lunch.dto.PointSearchResultDto;
import com.milisong.ecm.lunch.goods.api.LbsService;
import com.milisong.ecm.lunch.goods.dto.BuildingDto;
import com.milisong.ecm.lunch.goods.dto.BuildingLbsDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RestController
@Slf4j
public class LbsServiceImpl implements LbsService {
	@Autowired
	private BuildingService buildingService;
	@Value("${lbs.distance:3}")
	private Double lbsDistance;
	@Value("${lbs.search-building-service-url}")
	private String LBS_SEARCH_AROUND_COMPANY;
	@Autowired
	private RestTemplate restTemplate;

	@Override
	public void saveBuildingLonAndLat(@RequestBody BuildingDto buildDto) {
		log.info("开始处理楼宇的经纬度数据,楼宇：{}", JSONObject.toJSONString(buildDto));
		GeoOperations<String, String> ops = RedisCache.getRedisTemplate().opsForGeo();
		String member = String.valueOf(buildDto.getId());
		// 查询楼宇的geo数据是否存在
		List<String> list = ops.hash(RedisKeyUtils.getBuildingLbsKey(), member);
		if (!CollectionUtils.isEmpty(list)) {
			log.info("该楼宇数据存在，先进行删除操作");
			// 存在，先删除，后新增
			ops.remove(RedisKeyUtils.getBuildingLbsKey(), member);
		}
		ops.add(RedisKeyUtils.getBuildingLbsKey(),
				new RedisGeoCommands.GeoLocation<String>(member, new Point(buildDto.getLon(), buildDto.getLat())));
		log.info("楼宇经纬度信息处理完成");
	}

	@Override
	public List<BuildingLbsDto> listNearbyBuilding(double lon, double lat, int limit) {
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
				List<BuildingDto> buildingList = buildingService.getBuildList(buildings);
				if(!CollectionUtils.isEmpty(buildingList)) {
					List<BuildingLbsDto> resp = new ArrayList<>();
					buildingList.stream().forEach(item -> {
						BuildingLbsDto dto = BeanMapper.map(item, BuildingLbsDto.class);
						if(StatusConstant.BuildingStatusEnum.OPENED.getValue() == dto.getStatus()) {
							dto.setDistance(data.get(item.getId()));
							resp.add(dto);
						}
					});
					log.info("查询LBS楼宇信息的结果：{}", JSONObject.toJSONString(resp));
					return resp;
				}
			}
		}
		log.warn("未查询LBS楼宇信息");
		return null;
	}

}
*/

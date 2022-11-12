/*
package com.milisong.ecm.lunch.goods.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.google.common.collect.Lists;
import com.milisong.ecm.common.util.RedisKeyUtils;
import com.milisong.ecm.lunch.goods.api.BuildingService;
import com.milisong.ecm.lunch.goods.dto.BuildingApplyDto;
import com.milisong.ecm.lunch.goods.dto.BuildingDto;
import com.milisong.ecm.lunch.goods.param.BuildingParam;
import com.milisong.ecm.lunch.mq.BuildingProducer;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RestController
@Slf4j
public class BuildingServiceImpl implements BuildingService {
	
	@Autowired
	private BuildingProducer buildingProducer;
	
	@Override
	public List<BuildingDto> getBuildList(@RequestBody List<Long> buildingId) {
		log.info("获取楼宇详细信息{}",JSON.toJSONString(buildingId));
		List<BuildingDto> resultList =  Lists.newArrayList();
		if (!CollectionUtils.isEmpty(buildingId)) {
			for (Long buildId : buildingId) {
				String buildingKey = RedisKeyUtils.getBuildingKey(buildId);
				String buildResult = RedisCache.get(buildingKey);
                BuildingDto buildDto = JSONObject.parseObject(buildResult, BuildingDto.class);
                if (buildDto != null) {
                    resultList.add(buildDto);
                }
			}
			
		}
		return resultList;
	}
	
    @Override
    public ResponseResult<List<BuildingDto>> getBuildingList() {
        Set<String> keys = RedisCache.keys(RedisKeyUtils.getBuildingSearchKey());
        List<BuildingDto> buildingList = new ArrayList<>();
        for (String key : keys) {
            String json = RedisCache.get(key);
            BuildingDto buildingDto = JSON.parseObject(json, BuildingDto.class);
            if (buildingDto.getStatus() == 1) {
            	buildingList.add(buildingDto);
            }
            
        }
        log.info("获取楼宇信息：{}", JSON.toJSONString(buildingList));
        return ResponseResult.buildSuccessResponse(buildingList);
    }

    @Override
    public ResponseResult<String> toOpenBuilding(@RequestBody BuildingParam buildingParam) {
    	BuildingApplyDto applyDto = new BuildingApplyDto();
    	applyDto.setUserId(buildingParam.getUserId());
    	applyDto.setNikeName(buildingParam.getNikeName());
    	applyDto.setMobile(buildingParam.getMobile());
    	applyDto.setBuildingId(buildingParam.getDeliveryBuildingId());
    	applyDto.setApplyTime(new Date());
    	buildingProducer.sendCompanyInfo(applyDto);
    	return ResponseResult.buildSuccessResponse();
    }
    
    @Override
    public ResponseResult<List<BuildingDto>> searchBuilding(String name) {
    	ResponseResult<List<BuildingDto>> responseResult = ResponseResult.buildSuccessResponse(new ArrayList<>());
    	String buildingListKey = RedisKeyUtils.getBuildingListKey();
    	List<BuildingDto> buildingList = Lists.newArrayList();
    	//从redis获取楼宇集合数据
    	Map<Object, Object> buildingMap = RedisCache.hGetAll(buildingListKey);
    	for (Map.Entry<Object, Object> m : buildingMap.entrySet()) {
    		Object value = m.getValue();
    		if (value !=null) {
    			BuildingDto buildingDto = JSONObject.parseObject(value.toString(),BuildingDto.class);
    			buildingList.add(buildingDto);
     		}
     	}
    	if(name.indexOf("(")>=0||name.indexOf(")")>=0) {
    		name = name.replace("(", "").replace(")", "");
    	}
    	if (StringUtils.isNotBlank(name)) {
        	//从集合模糊搜索
        	List<BuildingDto> resultList = Lists.newArrayList();
        	Pattern pattern = Pattern.compile(name,Pattern.CASE_INSENSITIVE);
        	for (BuildingDto bulidingDto :buildingList) {
        		Matcher matcher = pattern.matcher(bulidingDto.getName()+bulidingDto.getDetailAddress());
        		if (matcher.find()) {
        			resultList.add(bulidingDto);
        		}
        	}
        	resultList.sort(Comparator.comparingInt(BuildingDto::getStatus).reversed());
        	if (resultList.size()>20) {
        		resultList = resultList.subList(0, 20);
        	}
        	responseResult.setData(resultList);
    	}else {
    		buildingList.sort(Comparator.comparingInt(BuildingDto::getStatus).reversed());
    		if (buildingList.size()>20) {
    			buildingList = buildingList.subList(0, 20);
    		}
    		responseResult.setData(buildingList);
    	}
    	return responseResult;
    }
}
*/

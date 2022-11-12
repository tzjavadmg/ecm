package com.milisong.pos.production.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.util.BeanMapper;
import com.milisong.pos.production.api.PreProductionService;
import com.milisong.pos.production.dto.PreProductionDto;
import com.milisong.pos.production.properties.ServiceUrl;

import lombok.extern.slf4j.Slf4j;

/**
*@author    created by benny
*@date  2018年10月24日---下午6:48:45
*
*/
@Slf4j
@RestController
@RequestMapping("/preproduction")
@Component
@EnableScheduling
public class PreProductionTask {

	@Autowired
	private PreProductionService preProductionService;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	private ServiceUrl serviceurl;
	
	@GetMapping("/task")
	public void initPreProduction() {
		log.info("定时初始化预生产数量,请求scm地址:{}",serviceurl.getPreProductionUrl());
		ResponseResult<List<PreProductionDto>>  result = preProductionService.getListByAll();
		log.info("校验数据是否有当天预生产数据:{}",JSON.toJSONString(result));
		if(null != result) {
			if(null != result.getData()) {
				if(result.getData().size() > 0) {
					log.info("存在当天的数据、不在进行处理。");
					return;
				}
			}
		}
		ResponseEntity<String> responseEntity = restTemplate.getForEntity(serviceurl.getPreProductionUrl(), String.class);
		JSONObject jsonMap = JSON.parseObject(responseEntity.getBody());
		List<PreProductionDto> listPreProductionParam = JSONObject.parseArray(jsonMap.getString("data"),PreProductionDto.class);
		preProductionService.createPreProduction(listPreProductionParam);
	}
}

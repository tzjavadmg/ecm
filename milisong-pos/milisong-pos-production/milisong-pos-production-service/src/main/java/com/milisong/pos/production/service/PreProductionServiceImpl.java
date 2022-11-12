package com.milisong.pos.production.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.farmland.core.db.IdGenerator;
import com.milisong.pos.production.api.PreProductionService;
import com.milisong.pos.production.constant.RedisKeyConstant;
import com.milisong.pos.production.domain.PreProduction;
import com.milisong.pos.production.domain.PreProductionExample;
import com.milisong.pos.production.dto.PreProductionDto;
import com.milisong.pos.production.dto.SaleVolumes;
import com.milisong.pos.production.mapper.BasePreProductionMapper;
import com.milisong.pos.production.mapper.PreProductionMapper;
import com.milisong.pos.production.properties.ServiceUrl;
import com.milisong.pos.production.util.DateUtil;

import lombok.extern.slf4j.Slf4j;

/**
*@author    created by benny
*@date  2018年10月24日---下午4:23:40
*
*/
@Slf4j
@Transactional
@Service
public class PreProductionServiceImpl implements PreProductionService {

	@Autowired
	private PreProductionMapper preProductionMapper;
	
	@Autowired
	private BasePreProductionMapper basePreProductionMapper;
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	private ServiceUrl serviceurl;
	
	@Override
	public void createPreProduction(List<PreProductionDto> listPreProductionParam) {
		log.info("初始化每天预生产数据:{}",JSON.toJSONString(listPreProductionParam));
		if(CollectionUtils.isEmpty(listPreProductionParam)) {
			log.info("没有预生产数据。");
			return;
		}
		listPreProductionParam.forEach(preProduction ->{
			preProduction.setId(IdGenerator.nextId());
		});
		preProductionMapper.savePreProductionByList(listPreProductionParam);
	}

	@Override
	public ResponseResult<List<PreProductionDto>> getListByShopId(Long shopId) {
		log.info("根据门店ID获取预售商品量门店Id{}",shopId);
		List<PreProductionDto> list = preProductionMapper.getListByShopId(shopId);
		ResponseEntity<String> responseEntity = restTemplate.getForEntity(serviceurl.getDayPreproductionUrl()+shopId, String.class);
		log.info("请求获取门店近五天的销售数据{}",serviceurl.getDayPreproductionUrl()+shopId);
		JSONObject json = JSON.parseObject(responseEntity.getBody());
		if(!json.getBooleanValue("success")) {
			log.info("获取门店近五天的销售数据失败");
			return ResponseResult.buildSuccessResponse(list);
		}
		Map<String,Object> map = JSONObject.parseObject(json.getString("data"), Map.class);
		log.info("门店近五天的销售数据门店ID{}，数据size{}",shopId,map.keySet().size());
		
		for (PreProductionDto preProductionSaleDto : list) {
			List<SaleVolumes> listSale = (List<SaleVolumes>) map.get(preProductionSaleDto.getGoodsCode());
			preProductionSaleDto.setList(listSale);
			String key = RedisKeyConstant.getShopGoodsSaleCount(shopId,DateUtil.getTodayYyyyMmDdStr(),preProductionSaleDto.getGoodsCode());
			String value = RedisCache.get(key);
			if(StringUtils.isBlank(value)) {
				value = "0";
			}
			preProductionSaleDto.setActualSaleCount(Integer.parseInt(value));
		}
		return ResponseResult.buildSuccessResponse(list);
		  
	}

	@Override
	public ResponseResult<String> updateActualProductionCountById(Long id,Integer productionCount,String updateBy) {
		log.info("修改实际生产数量");
		PreProductionExample proProductionExample = new PreProductionExample();
		proProductionExample.createCriteria().andIdEqualTo(id);
		PreProduction preProduction = new PreProduction();
		preProduction.setActualProductionCount(productionCount);
		preProduction.setUpdateBy(updateBy);
		int row = basePreProductionMapper.updateByExampleSelective(preProduction, proProductionExample);
		log.info("修改实际生产数量查询结果:{}",row);
		return ResponseResult.buildSuccessResponse();
	}

	@Override
	public ResponseResult<List<PreProductionDto>> getListByAll() {
		List<PreProductionDto> list = preProductionMapper.getListByShopId(null);
		return ResponseResult.buildSuccessResponse(list);
	}

}

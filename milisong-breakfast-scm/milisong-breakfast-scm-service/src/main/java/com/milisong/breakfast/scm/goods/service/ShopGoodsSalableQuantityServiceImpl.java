package com.milisong.breakfast.scm.goods.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.db.IdGenerator;
import com.farmland.core.util.BeanMapper;
import com.milisong.breakfast.scm.common.properties.SystemProperties;
import com.milisong.breakfast.scm.goods.api.GoodsService;
import com.milisong.breakfast.scm.goods.api.ShopGoodsSalableQuantityService;
import com.milisong.breakfast.scm.goods.domain.ShopGoodsSalableQuantity;
import com.milisong.breakfast.scm.goods.domain.ShopGoodsSalableQuantityExample;
import com.milisong.breakfast.scm.goods.dto.GoodsCombinationRefDto;
import com.milisong.breakfast.scm.goods.dto.GoodsDto;
import com.milisong.breakfast.scm.goods.dto.ShopGoodsSalableQuantityDto;
import com.milisong.breakfast.scm.goods.dto.ShopOnsaleStockDto;
import com.milisong.breakfast.scm.goods.mapper.ShopGoodsSalableQuantityMapper;
import com.milisong.breakfast.scm.goods.param.GoodsStockDto;
import com.milisong.breakfast.scm.goods.param.GoosSalableParam;
import com.milisong.breakfast.scm.goods.param.ShopGoodsSalableQuantityParam;
import com.milisong.breakfast.scm.order.api.OrderService;
import com.milisong.breakfast.scm.order.constant.OrderTypeEnum;
import com.milisong.breakfast.scm.order.dto.param.OrderGoodsSumParam;
import com.milisong.scm.base.api.ShopService;
import com.milisong.scm.base.dto.ShopDto;

import lombok.extern.slf4j.Slf4j;

/**
*@author    created by benny
*@date  2018???12???5???---??????10:31:07
*
*/
@Slf4j
@Service
@RefreshScope
public class ShopGoodsSalableQuantityServiceImpl implements ShopGoodsSalableQuantityService{

	@Autowired
	private ShopGoodsSalableQuantityMapper shopGoodsSalableQuantityMapper;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private GoodsService goodsService;
	
	@Autowired
	private ShopService shopService;
	
	@Resource
    private SystemProperties systemProperties;
	
    @Autowired
	private RestTemplate restTemplate;
    
	@Override
	@Transactional
	public void initShopGoodsSalableQuantity(Map<Date,List<String>> goodsMap, ShopDto shopDto) {
		log.info("??????????????????????????????????????????????????????{} ???????????????{}",JSON.toJSONString(shopDto),JSON.toJSONString(goodsMap));
		List<ShopGoodsSalableQuantity> listParam = new ArrayList<ShopGoodsSalableQuantity>();
		Map<Date,ShopOnsaleStockDto> map = new HashMap<Date,ShopOnsaleStockDto>();
		for (Date salaDate : goodsMap.keySet()) {
			List<String> list =goodsMap.get(salaDate);
			List<GoodsDto> listGoodsInfo = goodsService.getGoodsInfoByGoodsCodes(list);
			List<GoodsDto> listGoodsQuantity = new ArrayList<GoodsDto>();
			Set<String> comboGoodsCode = new HashSet<String>();
			if(CollectionUtils.isEmpty(listGoodsInfo)) {
				continue;
			}
			for (GoodsDto goodsDto : listGoodsInfo) {
				if(BooleanUtils.isNotFalse(goodsDto.getIsCombo())) {
					comboGoodsCode.add(goodsDto.getCode());
				}else {
					listGoodsQuantity.add(goodsDto);
				}
			}
			List<GoodsCombinationRefDto> listCombination = goodsService.getGoodsCombinationDtoByGoodsCode(comboGoodsCode);
			if(CollectionUtils.isNotEmpty(listCombination)) {
				List<String> listSingleCode = listCombination.stream().map(GoodsCombinationRefDto::getSingleCode).collect(Collectors.toList());
					List<GoodsDto> listComboGoodsInfo = goodsService.getGoodsInfoByGoodsCodes(listSingleCode);
					if(CollectionUtils.isNotEmpty(listComboGoodsInfo)) {
						for (GoodsDto goodsDto : listComboGoodsInfo) {
							if(!list.contains(goodsDto.getCode())) {
								listGoodsQuantity.add(goodsDto);
							}
						}
					}
			}
			// ????????????+??????ID????????????????????????????????????
			List<String> oldGoodsCodeList = getSalableQuantityGoodsCode(salaDate,shopDto.getCode());
			// ????????????????????????????????????code
			if(CollectionUtils.isNotEmpty(oldGoodsCodeList)) {
				log.info("????????????{} ????????????{}",salaDate,JSON.toJSONString(oldGoodsCodeList));
				List<String> newGoodsCodeList = listGoodsQuantity.stream().map(GoodsDto::getCode).collect(Collectors.toList());
				log.info("????????????:{}???????????????{}",salaDate,JSON.toJSONString(newGoodsCodeList));
				List<String> resultList = removeRepeat(newGoodsCodeList, oldGoodsCodeList);
				log.info("?????????????????????Code:{}",JSON.toJSONString(resultList));
				if(CollectionUtils.isNotEmpty(resultList)) {
					listGoodsQuantity = goodsService.getGoodsInfoByGoodsCodes(resultList);
				}else {
					listGoodsQuantity = null;
				}
			}
			log.info("???????????????{}????????????{}",salaDate,JSON.toJSONString(listGoodsQuantity));
			if(CollectionUtils.isEmpty(listGoodsQuantity)) {
				log.info("???????????????{} ????????????????????????",salaDate);
				continue;
			}
			for (GoodsDto goodsDto : listGoodsQuantity) {
				ShopGoodsSalableQuantity shopGoodsSalableQuantity
				= new ShopGoodsSalableQuantity();
				shopGoodsSalableQuantity.setId(IdGenerator.nextId());
				shopGoodsSalableQuantity.setIsDeleted(false);
				int DefaultCount =systemProperties.getEcmUrl().getGoodsDefaultCount();
				if(systemProperties.getEcmUrl().getSpecialGoods().contains(goodsDto.getCode())) {
					DefaultCount = 650;
				} 
				shopGoodsSalableQuantity.setAvailableVolume(DefaultCount);
				shopGoodsSalableQuantity.setShopCode(shopDto.getCode()); 
				shopGoodsSalableQuantity.setShopName(shopDto.getName());
				shopGoodsSalableQuantity.setShopId(shopDto.getId());
				shopGoodsSalableQuantity.setGoodsCode(goodsDto.getCode());
				shopGoodsSalableQuantity.setGoodsName(goodsDto.getName());
				shopGoodsSalableQuantity.setSaleDate(salaDate);
				listParam.add(shopGoodsSalableQuantity);
				//????????????ecm??????????????????
				ShopOnsaleStockDto  shopOnsaleStockDto = map.get(salaDate);
				List<GoodsStockDto> listGoodsStocks = new ArrayList<GoodsStockDto>();
				GoodsStockDto goodsStocks = new GoodsStockDto();
				goodsStocks.setGoodsCode(shopGoodsSalableQuantity.getGoodsCode());
				goodsStocks.setGoodsCount(shopGoodsSalableQuantity.getAvailableVolume());
				listGoodsStocks.add(goodsStocks);
				if(null == shopOnsaleStockDto) {
					shopOnsaleStockDto = new ShopOnsaleStockDto();
					shopOnsaleStockDto.setSaleDate(salaDate);
					shopOnsaleStockDto.setShopCode(shopDto.getCode());
				}  
				List<GoodsStockDto> ListGoodsStockDto =	shopOnsaleStockDto.getGoodsStocks();
				if(CollectionUtils.isEmpty(ListGoodsStockDto)) {
					ListGoodsStockDto = new ArrayList<GoodsStockDto>();
				}
				ListGoodsStockDto.addAll(listGoodsStocks);
				shopOnsaleStockDto.setGoodsStocks(ListGoodsStockDto);
				map.put(salaDate,shopOnsaleStockDto);
			}
		}
		
		if(CollectionUtils.isNotEmpty(listParam)) {
			int row = shopGoodsSalableQuantityMapper.insertBatchSelective(listParam);
			log.info("??????????????????????????????{}",row);
			if(CollectionUtils.isNotEmpty(map.values())) {
				List<ShopOnsaleStockDto> listStock = new ArrayList<ShopOnsaleStockDto>();
				listStock.addAll(map.values());
				log.info("??????C???????????????????????????:{}",JSON.toJSONString(listStock));
				if(!setShopGoodsStockList(listStock)) {
					 throw new RuntimeException("??????C?????????????????????");
				}
			}
		}
		
	}

	private List<String> removeRepeat(List<String> newList,List<String> oldList){
		List<String> resultList = new ArrayList<String>();
		if(CollectionUtils.isEmpty(newList)) {
			return null;
		}
		newList.forEach(item->{
			if(!oldList.contains(item)) {
				resultList.add(item);
			}
		});
		return resultList;
	}
	/**
	 * ????????????+??????code????????????????????????????????????code
	 * @param salaDate
	 * @param code
	 * @return
	 */
	private List<String> getSalableQuantityGoodsCode(Date salaDate, String code) {
		ShopGoodsSalableQuantityExample example = new ShopGoodsSalableQuantityExample();
		example.createCriteria().andSaleDateEqualTo(salaDate).andShopCodeEqualTo(code);
		List<ShopGoodsSalableQuantity> goodsCode = shopGoodsSalableQuantityMapper.selectByExample(example);
		if (CollectionUtils.isNotEmpty(goodsCode)) {
			return goodsCode.stream().map(ShopGoodsSalableQuantity::getGoodsCode).collect(Collectors.toList());
		}
		return null;
	}

	@Override
	public ResponseResult<String> update(ShopGoodsSalableQuantityParam shopGoodsSalableQuantityParam,String orderGoodsCountUrl) {
		ShopGoodsSalableQuantity shopGoodsSalableQuantityOld = shopGoodsSalableQuantityMapper.selectByPrimaryKey(shopGoodsSalableQuantityParam.getId());
		log.info("???????????????????????????{}",JSON.toJSONString(shopGoodsSalableQuantityOld));
		OrderGoodsSumParam param = new OrderGoodsSumParam();
		param.setGoodsCode(shopGoodsSalableQuantityParam.getGoodsCode());
		param.setShopId(shopGoodsSalableQuantityParam.getShopId());
		param.setDeliveryDate(shopGoodsSalableQuantityOld.getSaleDate()); 
		int alreadySoldQuantity = getOrderGoodsSumByParam(param,orderGoodsCountUrl);
		log.info("???????????????????????????:count:{}",alreadySoldQuantity);
		if(shopGoodsSalableQuantityParam.getAvailableVolume() < alreadySoldQuantity) {
			log.info("??????????????????????????????????????????????????????");
			return ResponseResult.buildFailResponse("8323","??????????????????????????????????????????????????????");
		}
		ResponseResult<ShopDto> shopsResponse = shopService.queryById(shopGoodsSalableQuantityParam.getShopId());
		if (null == shopsResponse || !shopsResponse.isSuccess() || null == shopsResponse.getData()) {
			log.info("??????ID???????????????ID???{}",param.getShopId());
            throw new RuntimeException("???????????????????????????");
		}
		ShopDto shopDto = shopsResponse.getData();
		// ??????ECM????????????????????????
		GoodsStockDto goods = new GoodsStockDto();
		goods.setGoodsCode(shopGoodsSalableQuantityParam.getGoodsCode());
		goods.setGoodsCount(shopGoodsSalableQuantityParam.getAvailableVolume()-shopGoodsSalableQuantityOld.getAvailableVolume());
		List<GoodsStockDto> listGoods = new ArrayList<GoodsStockDto>();
		listGoods.add(goods);
		ShopOnsaleStockDto shopOnsaleStockDto = new ShopOnsaleStockDto();
		shopOnsaleStockDto.setSaleDate(shopGoodsSalableQuantityOld.getSaleDate());
		shopOnsaleStockDto.setShopCode(shopDto.getCode());
		shopOnsaleStockDto.setGoodsStocks(listGoods);
		Boolean result = setShopGoodsStock(shopOnsaleStockDto);
		if(!result) {
			log.info("??????ecm????????????????????????");
			return ResponseResult.buildFailResponse();
		}
		if(null != shopGoodsSalableQuantityOld) {
			shopGoodsSalableQuantityOld.setUpdateBy(shopGoodsSalableQuantityParam.getOperator());
			shopGoodsSalableQuantityOld.setAvailableVolume(shopGoodsSalableQuantityParam.getAvailableVolume());
			ShopGoodsSalableQuantityExample example = new ShopGoodsSalableQuantityExample();
			example.createCriteria().andIdEqualTo(shopGoodsSalableQuantityParam.getId()).andIsDeletedEqualTo(false);
			int row = shopGoodsSalableQuantityMapper.updateByExampleSelective(shopGoodsSalableQuantityOld, example);
			log.info("???????????????????????? ?????????{}",row);
		}
		return ResponseResult.buildSuccessResponse();
	}

	private Boolean setShopGoodsStock(ShopOnsaleStockDto shopOnsaleStockDto) {
		log.info("??????ecm?????????????????????????????????????????? ?????????{}",JSONObject.toJSONString(shopOnsaleStockDto));
		log.info("ecm?????????????????????????????? URL:{}",systemProperties.getEcmUrl().getGoodsStockUrl());
		ResponseResult obj = ResponseResult.buildFailResponse();
		try {
			RestTemplate restTemplate = new RestTemplate();
			obj = restTemplate.postForObject(systemProperties.getEcmUrl().getGoodsStockUrl(),shopOnsaleStockDto, ResponseResult.class);
		} catch (RestClientException e) {
			log.error("??????ecm????????????????????????????????????,{}{}",e.getMessage(),e);
		}
		log.info("??????ecm???????????????????????????????????? ??????{}",JSONObject.toJSONString(obj));
		return obj.isSuccess();
	}
	
	/**
	 * ??????ecm????????????????????????????????????
	 * @return
	 */
	private boolean setShopGoodsStockList(List<ShopOnsaleStockDto> shopOnsaleStockDtoList) {
		log.info("??????ecm?????????????????????????????????????????? ?????????{}",JSONObject.toJSONString(shopOnsaleStockDtoList));
		log.info("ecm???????????????????????????????????? URL:{}",systemProperties.getEcmUrl().getGoodsStockListUrl());
		ResponseResult obj = ResponseResult.buildFailResponse();
		try {
			RestTemplate restTemplate = new RestTemplate();
			obj = restTemplate.postForObject(systemProperties.getEcmUrl().getGoodsStockListUrl(),shopOnsaleStockDtoList, ResponseResult.class);
		} catch (RestClientException e) {
			log.error("??????ecm????????????????????????????????????,{}{}",e.getMessage(),e);
		}
		log.info("??????ecm???????????????????????????????????? ??????{}",JSONObject.toJSONString(obj));
		return obj.isSuccess();
	}

	@Override
	public ResponseResult<Pagination<ShopGoodsSalableQuantityDto>> getByShopIdAndDate(GoosSalableParam goodsSalableParam,String orderGoodsCountUrl) {
		Pagination<ShopGoodsSalableQuantityDto> pagination = new Pagination<ShopGoodsSalableQuantityDto>();
		ShopGoodsSalableQuantityExample shopGoodsSalableQuantityExample =new ShopGoodsSalableQuantityExample();
		shopGoodsSalableQuantityExample.setPageSize(goodsSalableParam.getPageSize());
		shopGoodsSalableQuantityExample.setStartRow(goodsSalableParam.getStartRow());
		shopGoodsSalableQuantityExample.createCriteria().andShopIdEqualTo(goodsSalableParam.getShopId())
												 .andSaleDateEqualTo(goodsSalableParam.getSaleDate()).andIsDeletedEqualTo(false);
		long count = shopGoodsSalableQuantityMapper.countByExample(shopGoodsSalableQuantityExample);
		if(count == 0) {
			return ResponseResult.buildSuccessResponse(pagination);
		}
		pagination.setTotalCount(count);
		List<ShopGoodsSalableQuantity> list = shopGoodsSalableQuantityMapper.selectByExample(shopGoodsSalableQuantityExample);
		if(CollectionUtils.isNotEmpty(list)) {
			List<String> listGoodsCode = list.stream().map(ShopGoodsSalableQuantity::getGoodsCode).collect(Collectors.toList());
			Map<String,Integer> goodsMap = getGoodsCountByListCode(listGoodsCode);
			List<ShopGoodsSalableQuantityDto> listResult = BeanMapper.mapList(list, ShopGoodsSalableQuantityDto.class);
			for (ShopGoodsSalableQuantityDto shopGoodsSalableQuantityDto : listResult) {
				OrderGoodsSumParam param = new OrderGoodsSumParam();
				param.setGoodsCode(shopGoodsSalableQuantityDto.getGoodsCode());
				param.setShopId(shopGoodsSalableQuantityDto.getShopId());
				param.setDeliveryDate(shopGoodsSalableQuantityDto.getSaleDate());
				int salesVolume = getOrderGoodsSumByParam(param,orderGoodsCountUrl);
				shopGoodsSalableQuantityDto.setSalesVolume(salesVolume);
				Integer goodsCount = goodsMap.get(shopGoodsSalableQuantityDto.getGoodsCode());
				if(goodsCount == null) {
					log.info("????????????????????????{}",shopGoodsSalableQuantityDto.getGoodsCode());	
					goodsCount = 1;
				}
				shopGoodsSalableQuantityDto.setStockUpVolume((goodsCount*salesVolume));
			}
			pagination.setDataList(listResult);
		}
		return ResponseResult.buildSuccessResponse(pagination);
	}

	private Map<String,Integer> getGoodsCountByListCode(List<String> goodsCode){
		List<GoodsDto> listGodsCode = goodsService.getGoodsInfoByGoodsCodes(goodsCode);
		Map<String,Integer> map = new HashMap<String,Integer>();
		if(null != listGodsCode) {
			map = listGodsCode.stream().collect(Collectors.toMap(GoodsDto::getCode, a -> a.getGoodsCount(),(k1,k2)->k1));
		}
		return map;
	}
	
	private Integer getOrderGoodsSumByParam(OrderGoodsSumParam param,String orderGoodsCountUrl) {
		param.setOrderType(OrderTypeEnum.BREAKFAST.getCode());
		log.info("???????????????????????????:{}",JSON.toJSONString(param));
		String resultStr = restTemplate.postForObject(orderGoodsCountUrl, param, String.class);
		ResponseResult result = JSONObject.parseObject(resultStr, ResponseResult.class);
		if (!result.isSuccess() || null == result.getData()) {
			log.error("???????????????????????????????????????:{}",JSON.toJSONString(param));
			return 0;
		}
		return Integer.parseInt(result.getData().toString());
	}
}

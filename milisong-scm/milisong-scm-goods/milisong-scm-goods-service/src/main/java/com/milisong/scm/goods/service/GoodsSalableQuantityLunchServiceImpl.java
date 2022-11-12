package com.milisong.scm.goods.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
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
import com.milisong.scm.base.api.ShopService;
import com.milisong.scm.base.dto.ShopDto;
import com.milisong.scm.goods.api.GoodsLunchService;
import com.milisong.scm.goods.api.GoodsSalableQuantityLunchService;
import com.milisong.scm.goods.domain.GoodsSalableQuantityLunch;
import com.milisong.scm.goods.domain.GoodsSalableQuantityLunchExample;
import com.milisong.scm.goods.dto.GoodsLunchDto;
import com.milisong.scm.goods.dto.GoodsSalableQuantityLunchDto;
import com.milisong.scm.goods.dto.GoodsStockDto;
import com.milisong.scm.goods.dto.ShopOnsaleStockDto;
import com.milisong.scm.goods.mapper.GoodsSalableQuantityLunchMapper;
import com.milisong.scm.goods.param.GoodsSalableQuantityLunchParam;
import com.milisong.scm.goods.param.GoosSalableLunchParam;
import com.milisong.scm.orderset.api.OrderService;
import com.milisong.scm.orderset.dto.param.OrderGoodsSumParam;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
@RefreshScope
public class GoodsSalableQuantityLunchServiceImpl implements GoodsSalableQuantityLunchService {

	@Autowired
	private GoodsSalableQuantityLunchMapper shopGoodsSalableQuantityMapper;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private GoodsLunchService goodsService;
	
	
	@Value("${ecm.shoponsale.goods-default-count}")
	private int goodsDefaultCount;
	
	
	@Autowired
	private ShopService shopService;
	
	@Value("${ecm.shoponsale.goodsStock-url}")
	private String ecmShopOnsaleGoodsStockUrl;
	
	@Value("${ecm.shoponsale.goodsStockList-url}")
	private String ecmShopOnsaleGoodsStockListUrl;
	
	@Override
	public void initShopGoodsSalableQuantity(Map<Date, List<String>> goodsMap, ShopDto shopDto) {
		List<GoodsSalableQuantityLunch> listParam = new ArrayList<GoodsSalableQuantityLunch>();
		Map<Date,ShopOnsaleStockDto> map = new HashMap<Date,ShopOnsaleStockDto>();
		for (Date salaDate : goodsMap.keySet()) {
			List<String> list =goodsMap.get(salaDate);
			List<GoodsLunchDto> listGoodsInfo = goodsService.getGoodsLunchInfoByGoodsCodes(list);
			List<GoodsLunchDto> listGoodsQuantity = new ArrayList<GoodsLunchDto>();
			Set<String> comboGoodsCode = new HashSet<String>();
			for (GoodsLunchDto goodsDto : listGoodsInfo) {
				GoodsSalableQuantityLunch shopGoodsSalableQuantity
				= new GoodsSalableQuantityLunch();
				shopGoodsSalableQuantity.setId(IdGenerator.nextId());
				shopGoodsSalableQuantity.setIsDeleted(false);
				shopGoodsSalableQuantity.setAvailableVolume(goodsDefaultCount);
				shopGoodsSalableQuantity.setShopCode(shopDto.getCode());
				shopGoodsSalableQuantity.setShopName(shopDto.getName());
				shopGoodsSalableQuantity.setShopId(shopDto.getId());
				shopGoodsSalableQuantity.setGoodsCode(goodsDto.getCode());
				shopGoodsSalableQuantity.setGoodsName(goodsDto.getName());
				shopGoodsSalableQuantity.setSaleDate(salaDate);
				listParam.add(shopGoodsSalableQuantity);
				//组装给到ecm可售量的数据
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
		
		 
		int row = shopGoodsSalableQuantityMapper.insertBatchSelective(listParam);
		log.info("初始化默认库存操作：{}",row);
		List<ShopOnsaleStockDto> listStock = new ArrayList<ShopOnsaleStockDto>();
		listStock.addAll(map.values());
		setShopGoodsStockList(listStock);
		
	}

	@Override
	public ResponseResult<String> update(GoodsSalableQuantityLunchParam shopGoodsSalableQuantityParam) {
		GoodsSalableQuantityLunch shopGoodsSalableQuantityOld = shopGoodsSalableQuantityMapper.selectByPrimaryKey(shopGoodsSalableQuantityParam.getId());
		log.info("修改前的可售商品量{}",JSON.toJSONString(shopGoodsSalableQuantityOld));
		OrderGoodsSumParam param = new OrderGoodsSumParam();
		param.setGoodsCode(shopGoodsSalableQuantityParam.getGoodsCode());
		param.setShopId(shopGoodsSalableQuantityParam.getShopId());
		param.setDeliveryDate(shopGoodsSalableQuantityOld.getSaleDate()); 
		int alreadySoldQuantity = orderService.getOrderGoodsCount(param);
		log.info("查询已售商品总数量:count:{}",alreadySoldQuantity);
		if(shopGoodsSalableQuantityParam.getAvailableVolume() < alreadySoldQuantity) {
			log.info("订单数量大于可售商品数量、不允许修改");
			return ResponseResult.buildFailResponse("8323","订单数量大于可售商品数量、不允许修改");
		}
		ResponseResult<ShopDto> shopsResponse = shopService.queryById(shopGoodsSalableQuantityParam.getShopId());
		if (null == shopsResponse || !shopsResponse.isSuccess() || null == shopsResponse.getData()) {
			log.info("门店ID不存在门店ID：{}",shopGoodsSalableQuantityParam.getShopId());
            throw new RuntimeException("未能查询到门店信息");
		}
		// 调用ECM接口修改可售数量
		GoodsStockDto goods = new GoodsStockDto();
		goods.setGoodsCode(shopGoodsSalableQuantityParam.getGoodsCode());
		goods.setGoodsCount(shopGoodsSalableQuantityParam.getAvailableVolume()-shopGoodsSalableQuantityOld.getAvailableVolume());
		List<GoodsStockDto> listGoods = new ArrayList<GoodsStockDto>();
		listGoods.add(goods);
		ShopOnsaleStockDto shopOnsaleStockDto = new ShopOnsaleStockDto();
		shopOnsaleStockDto.setSaleDate(shopGoodsSalableQuantityOld.getSaleDate());
		shopOnsaleStockDto.setShopCode(shopsResponse.getData().getCode());
		shopOnsaleStockDto.setGoodsStocks(listGoods);
		Boolean result = setShopGoodsStock(shopOnsaleStockDto);
		if(!result) {
			log.info("修改ecm商品可售库存失败");
			return ResponseResult.buildFailResponse();
		}
		if(null != shopGoodsSalableQuantityOld) {
			shopGoodsSalableQuantityOld.setUpdateBy(shopGoodsSalableQuantityParam.getOperator());
			shopGoodsSalableQuantityOld.setAvailableVolume(shopGoodsSalableQuantityParam.getAvailableVolume());
			GoodsSalableQuantityLunchExample example = new GoodsSalableQuantityLunchExample();
			example.createCriteria().andIdEqualTo(shopGoodsSalableQuantityParam.getId()).andIsDeletedEqualTo(false);
			int row = shopGoodsSalableQuantityMapper.updateByExampleSelective(shopGoodsSalableQuantityOld, example);
			log.info("修改商品可售库存 结果：{}",row);
		}
		return ResponseResult.buildSuccessResponse();
	}

	@Override
	public ResponseResult<Pagination<GoodsSalableQuantityLunchDto>> getByShopIdAndDate(
			GoosSalableLunchParam goodsSalableParam) {
		Pagination<GoodsSalableQuantityLunchDto> pagination = new Pagination<GoodsSalableQuantityLunchDto>();
		GoodsSalableQuantityLunchExample shopGoodsSalableQuantityExample =new GoodsSalableQuantityLunchExample();
		shopGoodsSalableQuantityExample.setPageSize(goodsSalableParam.getPageSize());
		shopGoodsSalableQuantityExample.setStartRow(goodsSalableParam.getStartRow());
		shopGoodsSalableQuantityExample.createCriteria().andShopIdEqualTo(goodsSalableParam.getShopId())
												 .andSaleDateEqualTo(goodsSalableParam.getSaleDate()).andIsDeletedEqualTo(false);
		long count = shopGoodsSalableQuantityMapper.countByExample(shopGoodsSalableQuantityExample);
		if(count == 0) {
			return ResponseResult.buildSuccessResponse(pagination);
		}
		pagination.setTotalCount(count);
		List<GoodsSalableQuantityLunch> list = shopGoodsSalableQuantityMapper.selectByExample(shopGoodsSalableQuantityExample);
		if(CollectionUtils.isNotEmpty(list)) {
			List<GoodsSalableQuantityLunchDto> listResult = BeanMapper.mapList(list, GoodsSalableQuantityLunchDto.class);
			for (GoodsSalableQuantityLunchDto shopGoodsSalableQuantityDto : listResult) {
				OrderGoodsSumParam param = new OrderGoodsSumParam();
				param.setGoodsCode(shopGoodsSalableQuantityDto.getGoodsCode());
				param.setShopId(shopGoodsSalableQuantityDto.getShopId());
				param.setDeliveryDate(shopGoodsSalableQuantityDto.getSaleDate());
				int salesVolume = orderService.getOrderGoodsCount(param);
				shopGoodsSalableQuantityDto.setSalesVolume(salesVolume);
			}
			pagination.setDataList(listResult);
		}
		return ResponseResult.buildSuccessResponse(pagination);
	}

	private Boolean setShopGoodsStock(ShopOnsaleStockDto shopOnsaleStockDto) {
		log.info("调用ecm接口设置门店单个商品可用库存 入参：{}",JSONObject.toJSONString(shopOnsaleStockDto));
		log.info("ecm设置门店商品可售库存 URL:{}",ecmShopOnsaleGoodsStockUrl);
		ResponseResult obj = ResponseResult.buildFailResponse();
		try {
			RestTemplate restTemplate = new RestTemplate();
			obj = restTemplate.postForObject(ecmShopOnsaleGoodsStockUrl,shopOnsaleStockDto, ResponseResult.class);
		} catch (RestClientException e) {
			log.error("调用ecm设置单个商品可售库存异常,{}{}",e.getMessage(),e);
		}
		log.info("调用ecm接口设置单个商品可售库存 结果{}",JSONObject.toJSONString(obj));
		return obj.isSuccess();
	}
	
	/**
	 * 调用ecm接口设置门店商品可用库存
	 * @return
	 */
	private boolean setShopGoodsStockList(List<ShopOnsaleStockDto> shopOnsaleStockDtoList) {
		log.info("调用ecm接口批量设置门店商品可用库存 入参：{}",JSONObject.toJSONString(shopOnsaleStockDtoList));
		log.info("ecm批量设置门店商品可售库存 URL:{}",ecmShopOnsaleGoodsStockListUrl);
		ResponseResult obj = ResponseResult.buildFailResponse();
		try {
			RestTemplate restTemplate = new RestTemplate();
			obj = restTemplate.postForObject(ecmShopOnsaleGoodsStockListUrl,shopOnsaleStockDtoList, ResponseResult.class);
		} catch (RestClientException e) {
			log.error("调用ecm设置批量商品可售库存异常,{}{}",e.getMessage(),e);
		}
		log.info("调用ecm接口批量设置商品可售库存 结果{}",JSONObject.toJSONString(obj));
		return obj.isSuccess();
	}
}

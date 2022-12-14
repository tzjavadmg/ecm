package com.milisong.scm.stock.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.milisong.scm.orderset.constant.OrderTypeEnum;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.db.IdGenerator;
import com.milisong.scm.base.api.DimDateService;
import com.milisong.scm.base.api.ShopService;
import com.milisong.scm.base.dto.ShopDto;
import com.milisong.scm.orderset.api.OrderService;
import com.milisong.scm.orderset.dto.param.OrderGoodsSumParam;
import com.milisong.scm.shop.api.ConfigService;
import com.milisong.scm.shop.api.ShopOnsaleGoodsService;
import com.milisong.scm.shop.dto.config.ShopInterceptConfigDto;
import com.milisong.scm.shop.dto.shop.ShopOnsaleGoodsDto;
import com.milisong.scm.stock.api.ShopOnsaleGoodsStockService;
import com.milisong.scm.stock.domain.ShopOnsaleGoodsStock;
import com.milisong.scm.stock.domain.ShopOnsaleGoodsStockExample;
import com.milisong.scm.stock.dto.GoodsStockDto;
import com.milisong.scm.stock.dto.OnsaleGoodsStockDto;
import com.milisong.scm.stock.dto.PreProductionDto;
import com.milisong.scm.stock.dto.SaleVolumes;
import com.milisong.scm.stock.dto.ShopOnsaleGoodsStockDto;
import com.milisong.scm.stock.dto.ShopOnsaleStockDto;
import com.milisong.scm.stock.mapper.ShopOnsaleGoodsStockExtMapper;
import com.milisong.scm.stock.param.ShopOnsaleGoodsStockParam;
import com.milisong.scm.stock.util.DateUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author benny
 *
 * ?????????????????????
 */
@Slf4j
@Transactional
@Service
public class ShopOnsaleGoodsStockServiceImpl implements ShopOnsaleGoodsStockService{

	@Autowired
	private ShopOnsaleGoodsService shopOnsaleGoodsService;
	
	@Autowired
	private ShopOnsaleGoodsStockExtMapper shopOnsaleGoodsStockExtMapper;
	
	@Autowired
	private ShopService shopService;
	
	@Autowired
	private OrderService orderService;
	
/*	@Autowired
	private RestTemplate restTemplate;*/
	
	@Value("${ecm.shoponsale.goodsStock-url}")
	private String ecmShopOnsaleGoodsStockUrl;
	
	@Value("${ecm.shoponsale.goodsStockList-url}")
	private String ecmShopOnsaleGoodsStockListUrl;
	
	@Autowired
	private DimDateService dimDateService;
	
	@Autowired
	ConfigService configService;

	@Autowired
	private RestTemplate restTemplate;

	
	@Override
	public Pagination<ShopOnsaleGoodsStockDto> pageSearchByCondition(ShopOnsaleGoodsStockParam shopOnsaleGOodsStockParam,String goodsCountUrl) {
		log.info("????????????????????????????????????{}",JSONObject.toJSON(shopOnsaleGOodsStockParam));
		// ????????????
		Map<String,Object> mapParam = new HashMap<String,Object>();
		mapParam.put("startTime",shopOnsaleGOodsStockParam.getSaleDate());
		mapParam.put("endTime",shopOnsaleGOodsStockParam.getSaleDate());
		mapParam.put("shopId",shopOnsaleGOodsStockParam.getShopId());
		mapParam.put("startRow",shopOnsaleGOodsStockParam.getStartRow());
		mapParam.put("pageSize",shopOnsaleGOodsStockParam.getPageSize());
		//???????????????
		int count = shopOnsaleGoodsStockExtMapper.getCountByCondition(mapParam);
  		log.info("???????????????????????????count={}",count);
		Pagination<ShopOnsaleGoodsStockDto> page = new Pagination<ShopOnsaleGoodsStockDto>();
		page.setPageNo(shopOnsaleGOodsStockParam.getPageNo());
		page.setPageSize(shopOnsaleGOodsStockParam.getPageSize());
		page.setTotalCount(count);
		//????????????????????????
		List<ShopOnsaleGoodsStockDto>  listResult = shopOnsaleGoodsStockExtMapper.getListByCondition(mapParam);
		for (ShopOnsaleGoodsStockDto shopOnsaleGoodsStockDto : listResult) {
			OrderGoodsSumParam param = new OrderGoodsSumParam();
			param.setGoodsCode(shopOnsaleGoodsStockDto.getGoodsCode());
			param.setShopId(shopOnsaleGOodsStockParam.getShopId());
			param.setDeliveryDate(DateUtil.getDateParse(shopOnsaleGOodsStockParam.getSaleDate(), DateUtil.YYYY_MM_DD));
			int salesVolume = getOrderGoodsSumByParam(param,goodsCountUrl);
			shopOnsaleGoodsStockDto.setSalesVolume(salesVolume);
		}
		page.setDataList(listResult);
		return page;
	}

	private Date getShopSaleFinishByShopId(Long shopId) {
		log.info("???????????????????????????shopId={}", shopId);
		if (shopId == null) {
			log.info("??????id??????");
			return null;
		}
 
		List<ShopInterceptConfigDto> shopInterceptConfig = configService.queryInterceptConfigByShopId(shopId);
		if(CollectionUtils.isEmpty(shopInterceptConfig)) {
			log.error("??????????????????????????????????????????{}",JSON.toJSONString(shopInterceptConfig));
			return null;
		}
		List<Date> list = shopInterceptConfig.stream().map(ShopInterceptConfigDto::getOrderInterceptTime).collect(Collectors.toList());
		Collections.sort(list,new Comparator<Date>() {

			@Override
			public int compare(Date o1, Date o2) {
				if(o1.getTime()>o2.getTime()) {
					return -1;
				}
				if(o1.getTime()< o2.getTime()) {
					return 1;
				}
				return 0;
			}
			
		});
		log.info("??????????????????{}",JSON.toJSONString(list));
		log.info("??????????????????????????????{}",list.get(0));
		return list.get(0);
	}

	
	@Override
	public  ResponseResult<Object> updateByCondition(@RequestBody ShopOnsaleGoodsStockParam shopOnsaleGOodsStockParam,String goodsCountUrl) {
		log.info("????????????????????????????????????{}",JSON.toJSONString(shopOnsaleGOodsStockParam));
		if(null == shopOnsaleGOodsStockParam.getAvailableVolume()) {
			return ResponseResult.buildSuccessResponse();
		}
		ShopOnsaleGoodsStock shopOnsaleGoodsStock = shopOnsaleGoodsStockExtMapper.selectByPrimaryKey(shopOnsaleGOodsStockParam.getId());
		log.info("???????????????????????????{}",JSON.toJSONString(shopOnsaleGoodsStock));
		// ????????????????????????
		Date interceptingTime = getShopSaleFinishByShopId(shopOnsaleGOodsStockParam.getShopId());
		ResponseResult<ShopDto> shop = shopService.queryById(shopOnsaleGOodsStockParam.getShopId());
		if (null == shop || !shop.isSuccess() || null == shop.getData()) {
			log.info("??????ID???????????????ID???{}",shopOnsaleGOodsStockParam.getShopId());
            throw new RuntimeException("???????????????????????????");
		}
		log.info("????????????????????????:{}",interceptingTime);
		Date interceptingDateTimeFormat = null;
		try {
			String interceptingTimeFormat = DateFormatUtils.format(interceptingTime, "HH:mm:ss");
			Date saleDate = shopOnsaleGoodsStock.getSaleDate();
			String interceptingDateFormat = DateFormatUtils.format(saleDate, "yyyy-MM-dd");
			String interceptingDateTime = interceptingDateFormat+" "+interceptingTimeFormat;
			interceptingDateTimeFormat = DateUtils.parseDate(interceptingDateTime, "yyyy-MM-dd HH:mm:ss");
		} catch (ParseException e) {
			log.error("????????????????????????{}",e.getMessage(),e);
		}
		// ??????????????????
		Date nowDate = new Date();
		log.info("??????????????????{},{}",interceptingDateTimeFormat.getTime(),nowDate.getTime());
		if(nowDate.getTime() > interceptingDateTimeFormat.getTime()) {
			log.info("????????????????????????????????????????????????");
			return ResponseResult.buildFailResponse("888","????????????????????????????????????????????????");
		}
		OrderGoodsSumParam param = new OrderGoodsSumParam();
		param.setGoodsCode(shopOnsaleGOodsStockParam.getGoodsCode());
		param.setShopId(shopOnsaleGOodsStockParam.getShopId());
		param.setDeliveryDate(shopOnsaleGoodsStock.getSaleDate());
		// ????????????????????????
		int alreadySoldQuantity = getOrderGoodsSumByParam(param,goodsCountUrl);
		log.info("???????????????????????????:count:{}",alreadySoldQuantity);
		// ??????????????????
		if(shopOnsaleGOodsStockParam.getAvailableVolume() < alreadySoldQuantity) {
			log.info("??????????????????????????????????????????????????????");
			return ResponseResult.buildFailResponse("8323","??????????????????????????????????????????????????????");
		}
		
		// ??????ECM????????????????????????
		GoodsStockDto goods = new GoodsStockDto();
		goods.setGoodsCode(shopOnsaleGOodsStockParam.getGoodsCode());
		goods.setGoodsCount(shopOnsaleGOodsStockParam.getAvailableVolume()-shopOnsaleGoodsStock.getAvailableVolume());
		List<GoodsStockDto> listGoods = new ArrayList<GoodsStockDto>();
		listGoods.add(goods);
		ShopOnsaleStockDto shopOnsaleStockDto = new ShopOnsaleStockDto();
		shopOnsaleStockDto.setSaleDate(shopOnsaleGoodsStock.getSaleDate());
		shopOnsaleStockDto.setShopCode(shop.getData().getCode());
		shopOnsaleStockDto.setGoodsStocks(listGoods);
		// ?????????????????????
		log.info("??????ecm?????????????????????????????? ??????{}",JSONObject.toJSONString(shopOnsaleStockDto));
		Boolean result = setShopGoodsStock(shopOnsaleStockDto);
		if(!result) {
			log.info("??????ecm????????????????????????");
			return ResponseResult.buildFailResponse();
		}
		// ????????????????????????
		if(null != shopOnsaleGoodsStock) {
			shopOnsaleGoodsStock.setUpdateBy(shopOnsaleGOodsStockParam.getOperator());
			shopOnsaleGoodsStock.setAvailableVolume(shopOnsaleGOodsStockParam.getAvailableVolume());
			ShopOnsaleGoodsStockExample example = new ShopOnsaleGoodsStockExample();
			example.createCriteria().andIdEqualTo(shopOnsaleGOodsStockParam.getId()).andIsDeletedEqualTo(false);
			int row = shopOnsaleGoodsStockExtMapper.updateByExample(shopOnsaleGoodsStock, example);
			log.info("???????????????????????? ?????????{}",row);
		}
		return ResponseResult.buildSuccessResponse();
	}
	public static void main(String[] args) {
		
//		Date saleDate = DateUtil.getDayBeginTime(new Date(), 1);
//		System.out.println(saleDate);
	}
	
	@Override
	public void initOnsaleGoodsStockV2() {
		ResponseResult<List<String>> shopsResponse = dimDateService.getWorkDateByNow(5);
		if (null == shopsResponse || !shopsResponse.isSuccess() || null == shopsResponse.getData()) {
			log.error("????????????");
            throw new RuntimeException("????????????");
		}
		List<ShopOnsaleGoodsDto> listResult = shopOnsaleGoodsService.getByCondition();
		List<ShopOnsaleGoodsStock> list = new ArrayList<ShopOnsaleGoodsStock>();
		Map<String,Map<String,List<GoodsStockDto>>> ecmDataMap = new HashMap<String,Map<String,List<GoodsStockDto>>>();
		for (String saleDate : shopsResponse.getData()) {
			Date date = DateUtil.getDateParse(saleDate, DateUtil.YYYY_MM_DD);
			Map<String,List<GoodsStockDto>> shopGoodsStockMap = new HashMap<String,List<GoodsStockDto>>();
			List<Long> listRelationId = shopOnsaleGoodsStockExtMapper.getListRelationIdByNow(date);
			for (ShopOnsaleGoodsDto shopOnsaleGoodsDto : listResult) {
				Date endDate = shopOnsaleGoodsDto.getEndDate();
				Date beginDate = shopOnsaleGoodsDto.getBeginDate();
				Long id = shopOnsaleGoodsDto.getId();
				if(!listRelationId.contains(id)) {
					if(endDate != null && beginDate != null) {
						boolean result = DateUtil.compare_date(date, endDate,beginDate);
						if(result) {
							//??????????????????????????????
							getInsertGoodsStockByOnsaleGoods(date, list, shopOnsaleGoodsDto);
							combinationDataToEcm(shopGoodsStockMap, shopOnsaleGoodsDto);
						}
					}
				}
			}
			Map<String, List<GoodsStockDto>> map = ecmDataMap.get(saleDate);
			if(map == null) {
				map = new HashMap<String, List<GoodsStockDto>>();
			}
			map.putAll(shopGoodsStockMap);
			ecmDataMap.put(saleDate, map);
		}
		combinationDateV2(ecmDataMap);
		int row = 0 ;
		if(!CollectionUtils.isEmpty(list)) {
			row = shopOnsaleGoodsStockExtMapper.insertList(list);
			
		}
		log.info("?????????????????????????????????{}",row);
	}
	    
	@Override
	public void initOnsaleGoodsStock() {
		Date saleDate = DateUtil.getDayBeginTime(new Date(), 1);
		// ???????????????????????????
		List<ShopOnsaleGoodsDto> listResult = shopOnsaleGoodsService.getByCondition();
		// ????????????
		List<ShopOnsaleGoodsStock> list = new ArrayList<ShopOnsaleGoodsStock>();
		Map<String,List<GoodsStockDto>> shopGoodsStockMap = new HashMap<String,List<GoodsStockDto>>();
		for (ShopOnsaleGoodsDto shopOnsaleGoodsDto : listResult) {
			getInsertGoodsStockByOnsaleGoods(saleDate, list, shopOnsaleGoodsDto);
			// ???????????????ecm
			combinationDataToEcm(shopGoodsStockMap, shopOnsaleGoodsDto);
		}
		combinationDate(saleDate, shopGoodsStockMap);
		// ?????????????????????
		int row = shopOnsaleGoodsStockExtMapper.insertList(list);
		log.info("?????????????????????????????????????????????{}",row);
	}

	private void combinationDataToEcm(Map<String, List<GoodsStockDto>> shopGoodsStockMap,
			ShopOnsaleGoodsDto shopOnsaleGoodsDto) {
		List<GoodsStockDto> listGoodsStock = shopGoodsStockMap.get(shopOnsaleGoodsDto.getShopCode());
		if(CollectionUtils.isEmpty(listGoodsStock)) {
			listGoodsStock = new ArrayList<GoodsStockDto>();
		}
		GoodsStockDto g = new GoodsStockDto();
		g.setGoodsCode(shopOnsaleGoodsDto.getGoodsCode());
		g.setGoodsCount(shopOnsaleGoodsDto.getDefaultAvailableVolume());
		listGoodsStock.add(g);
		shopGoodsStockMap.put(shopOnsaleGoodsDto.getShopCode(), listGoodsStock);
	}

	private void getInsertGoodsStockByOnsaleGoods(Date saleDate, List<ShopOnsaleGoodsStock> list,
			ShopOnsaleGoodsDto shopOnsaleGoodsDto) {
		ShopOnsaleGoodsStock goodsStock = new ShopOnsaleGoodsStock();
		goodsStock.setRelationId(shopOnsaleGoodsDto.getId());
		goodsStock.setSaleDate(saleDate);
		goodsStock.setAvailableVolume(shopOnsaleGoodsDto.getDefaultAvailableVolume());
		goodsStock.setId(IdGenerator.nextId());
		list.add(goodsStock);
	}

	/**
	 * ?????????ECM??????????????????????????????
	 * @param saleDate
	 * @param shopGoodsStockMap
	 */
	private void combinationDateV2(Map<String,Map<String,List<GoodsStockDto>>> shopGoodsStockMap) {
		//log.info("?????????ECM?????????????????? ,???????????????{}????????????:{},",saleDate,JSONObject.toJSONString(shopGoodsStockMap));
		
		if( null != shopGoodsStockMap || !shopGoodsStockMap.isEmpty()) {
			List<ShopOnsaleStockDto> shopOnsaleStockDto = new ArrayList<ShopOnsaleStockDto>();
			for (String saleDate : shopGoodsStockMap.keySet()) {
				Map<String,List<GoodsStockDto>> map =	shopGoodsStockMap.get(saleDate);
				for (String shopCode : map.keySet()) {
					ShopOnsaleStockDto shop = new ShopOnsaleStockDto();
					shop.setShopCode(shopCode);
					shop.setSaleDate(DateUtil.getDateParse(saleDate, DateUtil.YYYY_MM_DD));
					shop.setGoodsStocks(map.get(shopCode));
					shopOnsaleStockDto.add(shop);
				}
			}
			
			//??????????????????????????????
			boolean result = setShopGoodsStockList(shopOnsaleStockDto);
			log.info("??????????????????????????????:{}",result);
		}
	}
	
	/**
	 * ?????????ECM??????????????????????????????
	 * @param saleDate
	 * @param shopGoodsStockMap
	 */
	private void combinationDate(Date saleDate, Map<String, List<GoodsStockDto>> shopGoodsStockMap) {
		log.info("?????????ECM?????????????????? ,???????????????{}????????????:{},",saleDate,JSONObject.toJSONString(shopGoodsStockMap));
		if( null != shopGoodsStockMap || !shopGoodsStockMap.isEmpty()) {
			List<ShopOnsaleStockDto> shopOnsaleStockDto = new ArrayList<ShopOnsaleStockDto>();
			for (String shopCode : shopGoodsStockMap.keySet()) {
				ShopOnsaleStockDto shop = new ShopOnsaleStockDto();
				shop.setShopCode(shopCode);
				shop.setSaleDate(saleDate);
				shop.setGoodsStocks(shopGoodsStockMap.get(shopCode));
				shopOnsaleStockDto.add(shop);
			}
			//??????????????????????????????
			boolean result = setShopGoodsStockList(shopOnsaleStockDto);
			log.info("??????????????????????????????:{}",result);
		}
	}
	
	/**?????????????????????
	 * @return
	 * ??????ecm???????????????
	 */
	private boolean setShopGoodsStock(ShopOnsaleStockDto shopOnsaleStockDto) {
		log.info("??????ecm?????????????????????????????????????????? ?????????{}",JSONObject.toJSONString(shopOnsaleStockDto));
		log.info("ecm?????????????????????????????? URL:{}",ecmShopOnsaleGoodsStockUrl);
		ResponseResult obj = ResponseResult.buildFailResponse();
		try {
			RestTemplate restTemplate = new RestTemplate();
			obj = restTemplate.postForObject(ecmShopOnsaleGoodsStockUrl,shopOnsaleStockDto, ResponseResult.class);
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
		log.info("ecm???????????????????????????????????? URL:{}",ecmShopOnsaleGoodsStockListUrl);
		ResponseResult obj = ResponseResult.buildFailResponse();
		try {
			RestTemplate restTemplate = new RestTemplate();
			obj = restTemplate.postForObject(ecmShopOnsaleGoodsStockListUrl,shopOnsaleStockDtoList, ResponseResult.class);
		} catch (RestClientException e) {
			log.error("??????ecm????????????????????????????????????,{}{}",e.getMessage(),e);
		}
		log.info("??????ecm???????????????????????????????????? ??????{}",JSONObject.toJSONString(obj));
		return obj.isSuccess();
	}

	@Override
	public void testInitOnsaleGoodsStock(Date sale_date) {
		// ???????????????????????????
		List<ShopOnsaleGoodsStock> list = new ArrayList<ShopOnsaleGoodsStock>();
		List<ShopOnsaleGoodsDto> listResult = shopOnsaleGoodsService.getByCondition();
		log.info("???????????????????????????:{}",JSONObject.toJSONString(listResult));
		List<Long> listRelationId = shopOnsaleGoodsStockExtMapper.getListRelationIdByNow(sale_date);
		Map<String,List<GoodsStockDto>> shopGoodsStockMap = new HashMap<String,List<GoodsStockDto>>();
		for (ShopOnsaleGoodsDto shopOnsaleGoodsDto : listResult) {
			Long id = shopOnsaleGoodsDto.getId();
			if(!listRelationId.contains(id)) {
				getInsertGoodsStockByOnsaleGoods(sale_date, list, shopOnsaleGoodsDto);
				
				combinationDataToEcm(shopGoodsStockMap, shopOnsaleGoodsDto);
			}
		}
		combinationDate(sale_date, shopGoodsStockMap);
		//??????????????????????????????
		int row = 0 ;
		if(!CollectionUtils.isEmpty(list)) {
			row = shopOnsaleGoodsStockExtMapper.insertList(list);
			
		}
		log.info("?????????????????????????????????{}",row);
	}

	@Override
	public ResponseResult<List<PreProductionDto>> goodsPreProductionByWorkDay(List<Date> listDay) {
		//????????????????????????????????????
		Map<String,Object> mapParam = new HashMap<String,Object>();
		mapParam.put("startTime",DateUtil.formatNowDate("yyyy-MM-dd"));
		mapParam.put("endTime",DateUtil.formatNowDate("yyyy-MM-dd"));
		
		List<OnsaleGoodsStockDto>  listGodsShop = shopOnsaleGoodsStockExtMapper.getNowListByCondition(mapParam);
		if(CollectionUtils.isEmpty(listGodsShop)) {
			return ResponseResult.buildFailResponse();
		}
		List<PreProductionDto> listPreProduction = new ArrayList<PreProductionDto>();
		//??????????????????????????????????????????
		for (OnsaleGoodsStockDto onsaleGoodsStockDto : listGodsShop) {
			PreProductionDto prePrpuction = new PreProductionDto(); 
			prePrpuction.setGoodsCode(onsaleGoodsStockDto.getGoodsCode());
			prePrpuction.setGoodsName(onsaleGoodsStockDto.getGoodsName());
			prePrpuction.setShopId(onsaleGoodsStockDto.getShopId());
			prePrpuction.setPreProductionDate(new Date());
			int salesCount = 0;
			int saleSum = 0;
			// ???????????????????????????????????????
			for (Date dateString : listDay) {
				OrderGoodsSumParam param = new OrderGoodsSumParam();
				param.setGoodsCode(onsaleGoodsStockDto.getGoodsCode());
				param.setShopId(onsaleGoodsStockDto.getShopId());
				param.setDeliveryDate(dateString);
				int salesVolume = orderService.getOrderGoodsCount(param);
				if(salesVolume>0) {
					salesCount = salesCount + salesVolume;
					saleSum++;
				}
			}
			BigDecimal a= new BigDecimal(saleSum);
			BigDecimal b= new BigDecimal(salesCount);
			log.info("?????????{}????????????{}",saleSum,salesCount);
			if(salesCount == 0)continue;
			BigDecimal avgSaleCount = b.divide(a, 2, BigDecimal.ROUND_HALF_DOWN);
			prePrpuction.setSalesAverageCount(avgSaleCount);
			prePrpuction.setPreProductionCount(avgSaleCount.multiply(new BigDecimal(0.7)).setScale(2,BigDecimal.ROUND_DOWN));
			listPreProduction.add(prePrpuction);
		}
		return ResponseResult.buildSuccessResponse(listPreProduction);
	}

	@Override
	public ResponseResult<Map<String, Object>> goodsDayPreProductionByWorkDay(List<Date> listDay,String shopId) {
		//????????????????????????????????????
			Map<String,Object> mapParam = new HashMap<String,Object>();
			mapParam.put("startTime",DateUtil.formatNowDate("yyyy-MM-dd"));
			mapParam.put("endTime",DateUtil.formatNowDate("yyyy-MM-dd"));
			mapParam.put("shopId", shopId);
			List<OnsaleGoodsStockDto>  listGodsShop = shopOnsaleGoodsStockExtMapper.getNowListByCondition(mapParam);
			if(CollectionUtils.isEmpty(listGodsShop)) {
				return ResponseResult.buildFailResponse();
			}
			Map<String, Object> resultMap = new HashMap<String, Object>();
			for (OnsaleGoodsStockDto onsaleGoodsStockDto : listGodsShop) {
				// ???????????????????????????????????????
				List<SaleVolumes> list = new ArrayList<SaleVolumes>();
				for (Date dateString : listDay) {
					SaleVolumes saleVolumes = new SaleVolumes();
					OrderGoodsSumParam param = new OrderGoodsSumParam();
					param.setGoodsCode(onsaleGoodsStockDto.getGoodsCode());
					param.setShopId(onsaleGoodsStockDto.getShopId());
					param.setDeliveryDate(dateString);
					int salesVolume = orderService.getOrderGoodsCount(param);
					saleVolumes.setSaleDate(dateString);
					saleVolumes.setVolumes(salesVolume);
					list.add(saleVolumes);
				}
				resultMap.put(onsaleGoodsStockDto.getGoodsCode(), list); 
			}
		return ResponseResult.buildSuccessResponse(resultMap);
	}

	private Integer getOrderGoodsSumByParam(OrderGoodsSumParam param,String orderGoodsCountUrl) {
		param.setOrderType(OrderTypeEnum.LUNCH.getCode());
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

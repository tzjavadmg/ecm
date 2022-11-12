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
 * 门店商品可售量
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
		log.info("分页查询，门店商品可售量{}",JSONObject.toJSON(shopOnsaleGOodsStockParam));
		// 校验参数
		Map<String,Object> mapParam = new HashMap<String,Object>();
		mapParam.put("startTime",shopOnsaleGOodsStockParam.getSaleDate());
		mapParam.put("endTime",shopOnsaleGOodsStockParam.getSaleDate());
		mapParam.put("shopId",shopOnsaleGOodsStockParam.getShopId());
		mapParam.put("startRow",shopOnsaleGOodsStockParam.getStartRow());
		mapParam.put("pageSize",shopOnsaleGOodsStockParam.getPageSize());
		//查询总条数
		int count = shopOnsaleGoodsStockExtMapper.getCountByCondition(mapParam);
  		log.info("统计商品列表数据，count={}",count);
		Pagination<ShopOnsaleGoodsStockDto> page = new Pagination<ShopOnsaleGoodsStockDto>();
		page.setPageNo(shopOnsaleGOodsStockParam.getPageNo());
		page.setPageSize(shopOnsaleGOodsStockParam.getPageSize());
		page.setTotalCount(count);
		//分页查询当前页数
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
		log.info("查询门店截单时间：shopId={}", shopId);
		if (shopId == null) {
			log.info("门店id为空");
			return null;
		}
 
		List<ShopInterceptConfigDto> shopInterceptConfig = configService.queryInterceptConfigByShopId(shopId);
		if(CollectionUtils.isEmpty(shopInterceptConfig)) {
			log.error("没有找到门店截单时间配置信息{}",JSON.toJSONString(shopInterceptConfig));
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
		log.info("排序后的集合{}",JSON.toJSONString(list));
		log.info("当天最后一次截单时间{}",list.get(0));
		return list.get(0);
	}

	
	@Override
	public  ResponseResult<Object> updateByCondition(@RequestBody ShopOnsaleGoodsStockParam shopOnsaleGOodsStockParam,String goodsCountUrl) {
		log.info("修改门店可售商品量入参：{}",JSON.toJSONString(shopOnsaleGOodsStockParam));
		if(null == shopOnsaleGOodsStockParam.getAvailableVolume()) {
			return ResponseResult.buildSuccessResponse();
		}
		ShopOnsaleGoodsStock shopOnsaleGoodsStock = shopOnsaleGoodsStockExtMapper.selectByPrimaryKey(shopOnsaleGOodsStockParam.getId());
		log.info("修改前的可售商品量{}",JSON.toJSONString(shopOnsaleGoodsStock));
		// 获取商品截单时间
		Date interceptingTime = getShopSaleFinishByShopId(shopOnsaleGOodsStockParam.getShopId());
		ResponseResult<ShopDto> shop = shopService.queryById(shopOnsaleGOodsStockParam.getShopId());
		if (null == shop || !shop.isSuccess() || null == shop.getData()) {
			log.info("门店ID不存在门店ID：{}",shopOnsaleGOodsStockParam.getShopId());
            throw new RuntimeException("未能查询到门店信息");
		}
		log.info("获取门店截单时间:{}",interceptingTime);
		Date interceptingDateTimeFormat = null;
		try {
			String interceptingTimeFormat = DateFormatUtils.format(interceptingTime, "HH:mm:ss");
			Date saleDate = shopOnsaleGoodsStock.getSaleDate();
			String interceptingDateFormat = DateFormatUtils.format(saleDate, "yyyy-MM-dd");
			String interceptingDateTime = interceptingDateFormat+" "+interceptingTimeFormat;
			interceptingDateTimeFormat = DateUtils.parseDate(interceptingDateTime, "yyyy-MM-dd HH:mm:ss");
		} catch (ParseException e) {
			log.error("时间处理出现异常{}",e.getMessage(),e);
		}
		// 校验截单时间
		Date nowDate = new Date();
		log.info("门店截单时间{},{}",interceptingDateTimeFormat.getTime(),nowDate.getTime());
		if(nowDate.getTime() > interceptingDateTimeFormat.getTime()) {
			log.info("当前时间大于截单时间、不允许修改");
			return ResponseResult.buildFailResponse("888","当前时间大于截单时间、不允许修改");
		}
		OrderGoodsSumParam param = new OrderGoodsSumParam();
		param.setGoodsCode(shopOnsaleGOodsStockParam.getGoodsCode());
		param.setShopId(shopOnsaleGOodsStockParam.getShopId());
		param.setDeliveryDate(shopOnsaleGoodsStock.getSaleDate());
		// 获取商品已售数量
		int alreadySoldQuantity = getOrderGoodsSumByParam(param,goodsCountUrl);
		log.info("查询已售商品总数量:count:{}",alreadySoldQuantity);
		// 校验可售数量
		if(shopOnsaleGOodsStockParam.getAvailableVolume() < alreadySoldQuantity) {
			log.info("订单数量大于可售商品数量、不允许修改");
			return ResponseResult.buildFailResponse("8323","订单数量大于可售商品数量、不允许修改");
		}
		
		// 调用ECM接口修改可售数量
		GoodsStockDto goods = new GoodsStockDto();
		goods.setGoodsCode(shopOnsaleGOodsStockParam.getGoodsCode());
		goods.setGoodsCount(shopOnsaleGOodsStockParam.getAvailableVolume()-shopOnsaleGoodsStock.getAvailableVolume());
		List<GoodsStockDto> listGoods = new ArrayList<GoodsStockDto>();
		listGoods.add(goods);
		ShopOnsaleStockDto shopOnsaleStockDto = new ShopOnsaleStockDto();
		shopOnsaleStockDto.setSaleDate(shopOnsaleGoodsStock.getSaleDate());
		shopOnsaleStockDto.setShopCode(shop.getData().getCode());
		shopOnsaleStockDto.setGoodsStocks(listGoods);
		// 校验修改返回值
		log.info("调用ecm接口设置商品可售库存 参数{}",JSONObject.toJSONString(shopOnsaleStockDto));
		Boolean result = setShopGoodsStock(shopOnsaleStockDto);
		if(!result) {
			log.info("修改ecm商品可售库存失败");
			return ResponseResult.buildFailResponse();
		}
		// 修改商品可售库存
		if(null != shopOnsaleGoodsStock) {
			shopOnsaleGoodsStock.setUpdateBy(shopOnsaleGOodsStockParam.getOperator());
			shopOnsaleGoodsStock.setAvailableVolume(shopOnsaleGOodsStockParam.getAvailableVolume());
			ShopOnsaleGoodsStockExample example = new ShopOnsaleGoodsStockExample();
			example.createCriteria().andIdEqualTo(shopOnsaleGOodsStockParam.getId()).andIsDeletedEqualTo(false);
			int row = shopOnsaleGoodsStockExtMapper.updateByExample(shopOnsaleGoodsStock, example);
			log.info("修改商品可售库存 结果：{}",row);
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
			log.error("日期信息");
            throw new RuntimeException("日期信息");
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
							//存入数据中的库存数据
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
		log.info("设置商品可售库存结果：{}",row);
	}
	    
	@Override
	public void initOnsaleGoodsStock() {
		Date saleDate = DateUtil.getDayBeginTime(new Date(), 1);
		// 获取需要设置的商品
		List<ShopOnsaleGoodsDto> listResult = shopOnsaleGoodsService.getByCondition();
		// 组合数据
		List<ShopOnsaleGoodsStock> list = new ArrayList<ShopOnsaleGoodsStock>();
		Map<String,List<GoodsStockDto>> shopGoodsStockMap = new HashMap<String,List<GoodsStockDto>>();
		for (ShopOnsaleGoodsDto shopOnsaleGoodsDto : listResult) {
			getInsertGoodsStockByOnsaleGoods(saleDate, list, shopOnsaleGoodsDto);
			// 组合数据给ecm
			combinationDataToEcm(shopGoodsStockMap, shopOnsaleGoodsDto);
		}
		combinationDate(saleDate, shopGoodsStockMap);
		// 新增商品可售量
		int row = shopOnsaleGoodsStockExtMapper.insertList(list);
		log.info("批量新增商品可售量、影响行数：{}",row);
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
	 * 组合给ECM设置可售商品库存数据
	 * @param saleDate
	 * @param shopGoodsStockMap
	 */
	private void combinationDateV2(Map<String,Map<String,List<GoodsStockDto>>> shopGoodsStockMap) {
		//log.info("组合给ECM设置可售商品 ,销售时间：{}库存数据:{},",saleDate,JSONObject.toJSONString(shopGoodsStockMap));
		
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
			
			//批量设置商品可售库存
			boolean result = setShopGoodsStockList(shopOnsaleStockDto);
			log.info("设置商品可售库存结果:{}",result);
		}
	}
	
	/**
	 * 组合给ECM设置可售商品库存数据
	 * @param saleDate
	 * @param shopGoodsStockMap
	 */
	private void combinationDate(Date saleDate, Map<String, List<GoodsStockDto>> shopGoodsStockMap) {
		log.info("组合给ECM设置可售商品 ,销售时间：{}库存数据:{},",saleDate,JSONObject.toJSONString(shopGoodsStockMap));
		if( null != shopGoodsStockMap || !shopGoodsStockMap.isEmpty()) {
			List<ShopOnsaleStockDto> shopOnsaleStockDto = new ArrayList<ShopOnsaleStockDto>();
			for (String shopCode : shopGoodsStockMap.keySet()) {
				ShopOnsaleStockDto shop = new ShopOnsaleStockDto();
				shop.setShopCode(shopCode);
				shop.setSaleDate(saleDate);
				shop.setGoodsStocks(shopGoodsStockMap.get(shopCode));
				shopOnsaleStockDto.add(shop);
			}
			//批量设置商品可售库存
			boolean result = setShopGoodsStockList(shopOnsaleStockDto);
			log.info("设置商品可售库存结果:{}",result);
		}
	}
	
	/**店商品可用库存
	 * @return
	 * 调用ecm接口设置门
	 */
	private boolean setShopGoodsStock(ShopOnsaleStockDto shopOnsaleStockDto) {
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

	@Override
	public void testInitOnsaleGoodsStock(Date sale_date) {
		// 获取需要设置的商品
		List<ShopOnsaleGoodsStock> list = new ArrayList<ShopOnsaleGoodsStock>();
		List<ShopOnsaleGoodsDto> listResult = shopOnsaleGoodsService.getByCondition();
		log.info("获取需要设置的商品:{}",JSONObject.toJSONString(listResult));
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
		//批量设置商品可售库存
		int row = 0 ;
		if(!CollectionUtils.isEmpty(list)) {
			row = shopOnsaleGoodsStockExtMapper.insertList(list);
			
		}
		log.info("设置商品可售库存结果：{}",row);
	}

	@Override
	public ResponseResult<List<PreProductionDto>> goodsPreProductionByWorkDay(List<Date> listDay) {
		//获取当前时间可售商品列表
		Map<String,Object> mapParam = new HashMap<String,Object>();
		mapParam.put("startTime",DateUtil.formatNowDate("yyyy-MM-dd"));
		mapParam.put("endTime",DateUtil.formatNowDate("yyyy-MM-dd"));
		
		List<OnsaleGoodsStockDto>  listGodsShop = shopOnsaleGoodsStockExtMapper.getNowListByCondition(mapParam);
		if(CollectionUtils.isEmpty(listGodsShop)) {
			return ResponseResult.buildFailResponse();
		}
		List<PreProductionDto> listPreProduction = new ArrayList<PreProductionDto>();
		//获取门店商品指定时间的销售量
		for (OnsaleGoodsStockDto onsaleGoodsStockDto : listGodsShop) {
			PreProductionDto prePrpuction = new PreProductionDto(); 
			prePrpuction.setGoodsCode(onsaleGoodsStockDto.getGoodsCode());
			prePrpuction.setGoodsName(onsaleGoodsStockDto.getGoodsName());
			prePrpuction.setShopId(onsaleGoodsStockDto.getShopId());
			prePrpuction.setPreProductionDate(new Date());
			int salesCount = 0;
			int saleSum = 0;
			// 根据最近工作日查询销售情况
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
			log.info("天数：{}、总数：{}",saleSum,salesCount);
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
		//获取当前时间可售商品列表
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
				// 根据最近工作日查询销售情况
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
		log.info("获取订单商品已售量:{}",JSON.toJSONString(param));
		String resultStr = restTemplate.postForObject(orderGoodsCountUrl, param, String.class);
		ResponseResult result = JSONObject.parseObject(resultStr, ResponseResult.class);
		if (!result.isSuccess() || null == result.getData()) {
			log.error("获取订单商品已售量出现异常:{}",JSON.toJSONString(param));
			return 0;
		}
		return Integer.parseInt(result.getData().toString());
	}
}

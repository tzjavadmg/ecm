package com.milisong.breakfast.scm.goods.service;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.db.IdGenerator;
import com.farmland.core.distrib.LockProvider;
import com.farmland.core.util.BeanMapper;
import com.milisong.breakfast.scm.common.constant.ErrorEnum;
import com.milisong.breakfast.scm.common.properties.SystemProperties;
import com.milisong.breakfast.scm.common.util.DateUtil;
import com.milisong.breakfast.scm.goods.api.GoodsScheduleService;
import com.milisong.breakfast.scm.goods.api.ShopGoodsSalableQuantityService;
import com.milisong.breakfast.scm.goods.domain.GoodsCategory;
import com.milisong.breakfast.scm.goods.domain.GoodsCategoryExample;
import com.milisong.breakfast.scm.goods.domain.GoodsSchedule;
import com.milisong.breakfast.scm.goods.domain.GoodsScheduleDetail;
import com.milisong.breakfast.scm.goods.domain.GoodsScheduleDetailExample;
import com.milisong.breakfast.scm.goods.domain.GoodsScheduleExample;
import com.milisong.breakfast.scm.goods.dto.GoodsCatalogListMqDto;
import com.milisong.breakfast.scm.goods.dto.GoodsDto;
import com.milisong.breakfast.scm.goods.dto.GoodsScheduleDetailDto;
import com.milisong.breakfast.scm.goods.dto.GoodsScheduleDto;
import com.milisong.breakfast.scm.goods.dto.GoodsScheduleInfoDto;
import com.milisong.breakfast.scm.goods.mapper.GoodsCategoryMapper;
import com.milisong.breakfast.scm.goods.mapper.GoodsScheduleDetailMapper;
import com.milisong.breakfast.scm.goods.mapper.GoodsScheduleMapper;
import com.milisong.breakfast.scm.goods.param.GoodsScheduleParam;
import com.milisong.breakfast.scm.goods.utils.MqProducerGoodsUtil;
import com.milisong.scm.base.api.DimDateService;
import com.milisong.scm.base.api.ShopService;
import com.milisong.scm.base.dto.DimDateDto;
import com.milisong.scm.base.dto.ShopDto;

import lombok.extern.slf4j.Slf4j;

/**
*@author    created by benny
*@date  2018???12???3???---??????7:18:12
*
*/
@Slf4j
@Service
@RefreshScope
public class GoodsScheduleServiceImpl implements GoodsScheduleService{

	@Autowired
	private GoodsScheduleMapper goodsScheduleMapper;
	
	@Autowired
	private GoodsScheduleDetailMapper goodsScheduleDetailMapper;
	
	@Autowired
	private DimDateService dimDateService;
	
	@Autowired
	private ShopService shopService;
	
	@Autowired
	private ShopGoodsSalableQuantityService shopGoodsSalableQuantityService;
	
	@Autowired
	private GoodsCategoryMapper goodsCategoryMapper;
 
	@Resource
    private SystemProperties systemProperties;
	/**
	 * ?????????????????? ????????? ??????????????????
	 */
	private final static String  GOODS_SCHEDULE_KEY = "goodsScheduleKey";
	
	@Override
	public ResponseResult<GoodsScheduleInfoDto> getGoodsScheduleByWeekOfYear(Integer year, String weekOfYear,Long shopId,String updateBy) {
		 //????????????
		if(null == year) {
			return ResponseResult.buildFailResponse(ErrorEnum.PARAMETER_CHECK_FAIL.getCode(),ErrorEnum.PARAMETER_CHECK_FAIL.getDesc());
		}
		if(null == weekOfYear) {
			return ResponseResult.buildFailResponse(ErrorEnum.PARAMETER_CHECK_FAIL.getCode(),ErrorEnum.PARAMETER_CHECK_FAIL.getDesc());
		}
		if(null == shopId) {
			return ResponseResult.buildFailResponse(ErrorEnum.PARAMETER_CHECK_FAIL.getCode(),ErrorEnum.PARAMETER_CHECK_FAIL.getDesc());
		}
		//???????????????????????????
		GoodsScheduleExample goodsScheduleExample = new GoodsScheduleExample();
		goodsScheduleExample.createCriteria().andYearEqualTo(year).andShopIdEqualTo(shopId).andWeekOfYearEqualTo(Integer.parseInt(weekOfYear));
		List<GoodsSchedule> listGoodsSchedule = goodsScheduleMapper.selectByExample(goodsScheduleExample);
		if(CollectionUtils.isEmpty(listGoodsSchedule)) {
			// ??????????????????
			ResponseResult<ShopDto> shopsResponse = shopService.queryById(shopId);
			if (null == shopsResponse || !shopsResponse.isSuccess() || null == shopsResponse.getData()) {
				log.info("??????ID???????????????ID???{}",shopId);
                throw new RuntimeException("???????????????????????????");
			}
			ShopDto shopDto = shopsResponse.getData();
			//????????????
			Map<Integer, DimDateDto> mapDate = getWeekDateByWeekOfYear(weekOfYear+"", year+"");
			List<GoodsSchedule> listSchedule = new ArrayList<GoodsSchedule>();
			for (int i = 1; i <= 7; i++) {
				GoodsSchedule goodsSchedule = new GoodsSchedule();
				goodsSchedule.setId(IdGenerator.nextId());
				goodsSchedule.setShopId(shopId);
				goodsSchedule.setShopCode(shopDto.getCode());
				goodsSchedule.setShopName(shopDto.getName());
				goodsSchedule.setYear(year);
				goodsSchedule.setWeekOfYear(Integer.parseInt(weekOfYear));
				goodsSchedule.setScheduleDate(mapDate.get(i).getDate());
				goodsSchedule.setWeek(i);
				goodsSchedule.setStatus(getWorkStatusByWeekStatus(mapDate.get(i).getStatus()));
				goodsSchedule.setIsDeleted(false);
				goodsSchedule.setCreateBy(updateBy);
				listSchedule.add(goodsSchedule);
			}
			// ????????????
			log.info("?????????????????????:{}",JSON.toJSONString(listSchedule));
			goodsScheduleMapper.insertBatchSelective(listSchedule);
			listGoodsSchedule = goodsScheduleMapper.selectByExample(goodsScheduleExample);
		}
		
		List<GoodsScheduleDetailDto> listGoodsScheduleDetail = getGoodsScheduleDetailByWeekOfYear(year, Integer.parseInt(weekOfYear), shopId);
		GoodsScheduleInfoDto goodsScheduleInfoDto = new GoodsScheduleInfoDto();
		
		goodsScheduleInfoDto.setGoodsSchedule(BeanMapper.mapList(listGoodsSchedule, GoodsScheduleDto.class));
		
		goodsScheduleInfoDto.setGoodsScheduleDetail(listGoodsScheduleDetail);
		
		return ResponseResult.buildSuccessResponse(goodsScheduleInfoDto);
	}
	
	@Override
	public ResponseResult<String> saveGoodsSchedule(GoodsScheduleParam goodsScheduleParam) {
		RLock lock = LockProvider.getLock(GOODS_SCHEDULE_KEY);
		boolean lockResult = lock.tryLock();
		try {
			if(!lockResult) {
				return ResponseResult.buildFailResponse("2348","????????????????????????????????????");
			}
			if(null == goodsScheduleParam) {
				return ResponseResult.buildFailResponse(ErrorEnum.PARAMETER_CHECK_FAIL.getCode(),ErrorEnum.PARAMETER_CHECK_FAIL.getDesc());
			}
			String updateBy = goodsScheduleParam.getUpdateBy();
			//??????????????????
			List<GoodsScheduleDto> listGoodsSchedule = goodsScheduleParam.getGoodsSchedule();
			if(CollectionUtils.isEmpty(listGoodsSchedule)) {
				log.info("??????????????????");
				return ResponseResult.buildFailResponse(ErrorEnum.PARAMETER_CHECK_FAIL.getCode(),ErrorEnum.PARAMETER_CHECK_FAIL.getDesc());
			}
			
			GoodsScheduleDto goodsSchedule = listGoodsSchedule.get(0);
			if(null == goodsSchedule.getPublishTime()) {
				log.info("??????????????????????????????");
				return ResponseResult.buildFailResponse(ErrorEnum.PARAMETER_CHECK_FAIL.getCode(),ErrorEnum.PARAMETER_CHECK_FAIL.getDesc());
			}
			Long nowTime = DateUtil.addTime(DateUtil.getNowDateTime2(),systemProperties.getEcmUrl().getSchedulerPushMiute(),Calendar.MINUTE).getTime();
			//??????????????????????????????????????????
			log.info("???????????????{}",nowTime);
			if(goodsSchedule.getPublishTime().getTime()<= nowTime) {
				return ResponseResult.buildFailResponse("9001","??????????????????????????????"+DateUtil.formatDate(DateUtil.addTime(DateUtil.getNowDateTime2(),systemProperties.getEcmUrl().getSchedulerPushMiute(),Calendar.MINUTE),"yyyy-MM-dd HH:mm;ss"));
			}
			for (GoodsScheduleDto goodsScheduleDto : listGoodsSchedule) {
				GoodsScheduleExample goodsScheduleExample = new GoodsScheduleExample();
				goodsScheduleExample.createCriteria().andIdEqualTo(goodsScheduleDto.getId());
				//???????????????????????????????????????????????????
//				List<GoodsSchedule> listGoodsScheduleOld = goodsScheduleMapper.selectByExample(goodsScheduleExample);
//				if(CollectionUtils.isNotEmpty(listGoodsScheduleOld)) {
//					GoodsSchedule goodsScheduleOld = listGoodsScheduleOld.get(0);
//					if(null != goodsScheduleOld.getPublishTime()) {
//						if(goodsScheduleOld.getPublishTime().getTime() <= DateUtil.getNowDateTime2().getTime()) {
//							return ResponseResult.buildFailResponse("9002","?????????????????????????????????????????????");
//						}
//					}
//				}
				GoodsSchedule record = new GoodsSchedule();
				record.setPublishTime(goodsScheduleDto.getPublishTime());
				record.setStatus(goodsScheduleDto.getStatus());
				record.setUpdateBy(updateBy);
				goodsScheduleMapper.updateByExampleSelective(record, goodsScheduleExample);
			}
			//????????????????????????
			List<GoodsScheduleDetailDto> listGoodsScheduleDetail = goodsScheduleParam.getGoodsScheduleDetail();
			if(CollectionUtils.isEmpty(listGoodsScheduleDetail)) {
				log.info("????????????????????????");
				return ResponseResult.buildFailResponse(ErrorEnum.PARAMETER_CHECK_FAIL.getCode(),ErrorEnum.PARAMETER_CHECK_FAIL.getDesc());
			}
			for (GoodsScheduleDetailDto goodsScheduleDetailDto : listGoodsScheduleDetail) {
				if(null == goodsScheduleDetailDto.getId()) {
					// ??????
					goodsScheduleDetailDto.setId(IdGenerator.nextId());
					GoodsScheduleDetail goodsScheduleDetail = BeanMapper.map(goodsScheduleDetailDto, GoodsScheduleDetail.class);
					goodsScheduleDetail.setIsDeleted(false);
					goodsScheduleDetail.setShopCode(goodsSchedule.getShopCode());
					goodsScheduleDetail.setShopName(goodsSchedule.getShopName());
					goodsScheduleDetail.setCreateBy(updateBy);
					goodsScheduleDetailMapper.insertSelective(goodsScheduleDetail);
				}else {
					// ??????
					GoodsScheduleDetailExample goodsScheduleDetailExample = new GoodsScheduleDetailExample();
					goodsScheduleDetailExample.createCriteria().andIdEqualTo(goodsScheduleDetailDto.getId());
					GoodsScheduleDetail goodsScheduleDetail = new GoodsScheduleDetail();
					goodsScheduleDetail.setFlag1(goodsScheduleDetailDto.getFlag1());
					goodsScheduleDetail.setFlag2(goodsScheduleDetailDto.getFlag2());
					goodsScheduleDetail.setFlag3(goodsScheduleDetailDto.getFlag3());
					goodsScheduleDetail.setFlag4(goodsScheduleDetailDto.getFlag4());
					goodsScheduleDetail.setFlag5(goodsScheduleDetailDto.getFlag5());
					goodsScheduleDetail.setFlag6(goodsScheduleDetailDto.getFlag6());
					goodsScheduleDetail.setFlag7(goodsScheduleDetailDto.getFlag7());
					goodsScheduleDetail.setShopCode(goodsSchedule.getShopCode());
					goodsScheduleDetail.setShopName(goodsSchedule.getShopName());
					goodsScheduleDetail.setUpdateBy(updateBy);
					goodsScheduleDetailMapper.updateByExampleSelective(goodsScheduleDetail, goodsScheduleDetailExample);
				}
			}
		} finally {
			if(lockResult) {
				lock.unlock();
			}
		}
		return ResponseResult.buildSuccessResponse();
	}
	
	private Map<Integer,DimDateDto> getWeekDateByWeekOfYear(String nowWeek,String nowYear) {
		ResponseResult<List<DimDateDto>>  listDimDtoResult = dimDateService.getWeekDateInfoByWeek(nowWeek, nowYear);
		if(!listDimDtoResult.isSuccess()||listDimDtoResult == null || CollectionUtils.isEmpty(listDimDtoResult.getData())) {
			 throw new RuntimeException("???????????????????????????");
		}
		List<DimDateDto> listDimDto = listDimDtoResult.getData();
		Map<Integer,DimDateDto> mapResult = new HashMap<Integer, DimDateDto>();
		if(CollectionUtils.isEmpty(listDimDto)) {
			return mapResult;
		}
		for (DimDateDto dimDateDto : listDimDto) {
			mapResult.put(dimDateDto.getDayOfWeek(), dimDateDto);
		}
		return mapResult;
	}
	
	private Boolean getWorkStatusByWeekStatus(Integer status) {
		if(status == 0) {
			return true;
		}
		return false;
	}

	@Override
	public List<GoodsScheduleDetailDto> getGoodsScheduleDetailByWeekOfYear(Integer year, Integer weekOfYear,
			Long shopId) {
		//?????????????????????????????????
		GoodsScheduleDetailExample goodsScheduleDetailExample = new GoodsScheduleDetailExample();
		
		goodsScheduleDetailExample.createCriteria().andYearEqualTo(year).andShopIdEqualTo(shopId).andWeekOfYearEqualTo(weekOfYear).andIsDeletedEqualTo(false);
	
		List<GoodsScheduleDetail> listGoodsScheduleDetail = goodsScheduleDetailMapper.selectByExample(goodsScheduleDetailExample);
		if(CollectionUtils.isNotEmpty(listGoodsScheduleDetail)) {
			return BeanMapper.mapList(listGoodsScheduleDetail, GoodsScheduleDetailDto.class);
		}
		return null;
	}
	
	/**
	 * ???????????????????????????????????????
	 */
	private Map<String, List<GoodsSchedule>> getGoodsScheduleInfoByPushingTime(List<GoodsSchedule> listGoodsSchedule){
		Map<String,List<GoodsSchedule>> maps = new HashMap<String,List<GoodsSchedule>>();
		for (GoodsSchedule goodsSchedule : listGoodsSchedule) {
			String key = goodsSchedule.getShopCode()+"_"+goodsSchedule.getYear()+"_"+goodsSchedule.getWeekOfYear();
			List<GoodsSchedule> listGoodsScheduleDto =	maps.get(key);
			if(listGoodsScheduleDto == null) {
				listGoodsScheduleDto = new ArrayList<GoodsSchedule>();
			}
			listGoodsScheduleDto.add(goodsSchedule);
			maps.put(key, listGoodsScheduleDto);
		}
		log.info("???????????????????????????????????????{}",JSON.toJSONString(maps));
		return maps;
	}
	@Override
	public ResponseResult<String> publishGoodsSchedule() {
		RLock lock = LockProvider.getLock(GOODS_SCHEDULE_KEY);
		lock.lock();
		try {
			//???????????????????????????
			GoodsScheduleExample goodsScheduleExample = new GoodsScheduleExample();
			goodsScheduleExample.createCriteria()
								.andPublishTimeGreaterThanOrEqualTo(DateUtil.getNowDateTimeFormatStart())
								.andPublishTimeLessThan(DateUtil.getNowDateTimeFormatEnd());
							//	.andStatusEqualTo(true);
			List<GoodsSchedule> listGoodsSchedule = goodsScheduleMapper.selectByExample(goodsScheduleExample);
			GoodsSchedule param = new GoodsSchedule();
			//???????????????????????????????????????????????????
			if(CollectionUtils.isNotEmpty(listGoodsSchedule)) {
				log.info("???????????????????????????Date :{}",JSON.toJSONString(listGoodsSchedule));
				// ?????????code+???+??? ??????key ????????????
				Map<String, List<GoodsSchedule>> mapGoodsSchedule = getGoodsScheduleInfoByPushingTime(listGoodsSchedule);
				for (String key : mapGoodsSchedule.keySet()) {
					log.info(key);
					List<GoodsSchedule> list = mapGoodsSchedule.get(key);
					if(CollectionUtils.isNotEmpty(list)) {
						param = list.get(0);
						//?????????????????????
						List<GoodsScheduleDetailDto> listGoodsScheduleDetail = getGoodsScheduleDetailByWeekOfYear(param.getYear(), param.getWeekOfYear(), param.getShopId());
						log.info("?????????????????????:{}",JSON.toJSONString(listGoodsScheduleDetail));
						//??????????????????????????????????????????
						List<String> listSalableQuantityDto = new ArrayList<String>();
						Map<Date,List<String>> goodsScheduleMqMap = new HashMap<Date,List<String>>();
						for (GoodsSchedule goodsSchedule : list) {
							List<String> goodsCode = new ArrayList<String>();
							if(!goodsSchedule.getStatus()) {
								continue;
							}
							for (GoodsScheduleDetailDto goodsScheduleDetailDto : listGoodsScheduleDetail) {
								if(getGoodsStatusBy(goodsSchedule.getWeek(),goodsScheduleDetailDto)) {
									log.info("?????????????????????{},??????",goodsSchedule.getScheduleDate());
									//??????????????????
									goodsCode.add(goodsScheduleDetailDto.getGoodsCode());
									// ?????????????????????
									listSalableQuantityDto.add(goodsScheduleDetailDto.getGoodsCode());
								}
							}
							goodsScheduleMqMap.put(goodsSchedule.getScheduleDate(),goodsCode);
						}
						ResponseResult<ShopDto> shopsResponse = shopService.queryById(param.getShopId());
						if (null == shopsResponse || !shopsResponse.isSuccess() || null == shopsResponse.getData()) {
							log.info("??????ID???????????????ID???{}",param.getShopId());
			                throw new RuntimeException("???????????????????????????");
						}
						ShopDto shopDto = shopsResponse.getData();
						Map<String,Map<Date,List<String>>> resultMap = new HashMap<String, Map<Date,List<String>>>();
						resultMap.put(shopDto.getCode(), goodsScheduleMqMap);
						//??????MQ??????ECM
						MqProducerGoodsUtil.sendGoodsSalableMsg(resultMap);
						MqProducerGoodsUtil.sendGoodsScheduleMsg(list);
						// ???????????????????????????
						shopGoodsSalableQuantityService.initShopGoodsSalableQuantity(goodsScheduleMqMap,shopDto);
					}
				}
			}else {
				
				return ResponseResult.buildSuccessResponse();
			}
		}finally {
			lock.unlock();
		}
		return ResponseResult.buildSuccessResponse();
	}
	
	private Map<String,GoodsCatalogListMqDto> getGoodsCatalog(List<GoodsDto> listGoodsInfo){
		Map<String,GoodsCatalogListMqDto> mapCatalog = new HashMap<String, GoodsCatalogListMqDto>();
		Map<String,GoodsCategory> goodsCategoryMap = new HashMap<String,GoodsCategory>();
		if (CollectionUtils.isNotEmpty(listGoodsInfo)) {
			List<String> listGoodsCategoryCode =listGoodsInfo.stream().map(GoodsDto::getCategoryCode).collect(Collectors.toList());
			goodsCategoryMap = getPictureByCode(listGoodsCategoryCode);
		}
		for (GoodsDto goodsDto : listGoodsInfo) {
			GoodsCatalogListMqDto catalog = mapCatalog.get(goodsDto.getCode());
			if(null == catalog) {
				catalog = new GoodsCatalogListMqDto();
				catalog.setCategoryCode(goodsDto.getCategoryCode());
				catalog.setCategoryName(goodsDto.getCategoryName());
				GoodsCategory g = goodsCategoryMap.get(goodsDto.getCategoryCode());
				if(null != g) {
					catalog.setPicture(systemProperties.getFileOss().getViewUrl().concat("/6/").concat(g.getPicture()));
				}
				mapCatalog.put(goodsDto.getCode(), catalog);
			}
		}
		return mapCatalog;
	}
	
	private Boolean getGoodsStatusBy(Integer week,GoodsScheduleDetailDto goodsScheduleDetailDto) {
		Boolean result = false;
		switch (week) {
		case 1:
			result = goodsScheduleDetailDto.getFlag1();
			break;
		case 2:
			result = goodsScheduleDetailDto.getFlag2();		
			break;
		case 3:
			result = goodsScheduleDetailDto.getFlag3();
			break;
		case 4:
			result = goodsScheduleDetailDto.getFlag4();
			break;
		case 5:
			result = goodsScheduleDetailDto.getFlag5();
			break;
		case 6:
			result = goodsScheduleDetailDto.getFlag6();
			break;
		case 7:
			result = goodsScheduleDetailDto.getFlag7();
			break;
		default:
			break;
		}
		log.info("?????????????????????????????????{},????????????:{}",week,JSON.toJSONString(goodsScheduleDetailDto));
		return result;
	}

	@Override
	public ResponseResult<List<GoodsScheduleDto>> getDateByShopId(Long shopId) {
		//?????????????????????????????????
		GoodsScheduleExample goodsScheduleExample = new GoodsScheduleExample();
		List<GoodsScheduleDto> listResult = new ArrayList<GoodsScheduleDto>();
//		goodsScheduleExample.setPageSize(5);
//		goodsScheduleExample.setStartRow(1);
		goodsScheduleExample.createCriteria().andShopIdEqualTo(shopId)
							.andScheduleDateGreaterThanOrEqualTo(new Date())
							.andStatusEqualTo(true);
		goodsScheduleExample.setOrderByClause(" schedule_date asc ");
		List<GoodsSchedule> ListGoodsSchedule = goodsScheduleMapper.selectByExample(goodsScheduleExample);
		if(CollectionUtils.isNotEmpty(ListGoodsSchedule)) {
			listResult = BeanMapper.mapList(ListGoodsSchedule, GoodsScheduleDto.class);
			return ResponseResult.buildSuccessResponse(listResult);
		}
		return ResponseResult.buildSuccessResponse(listResult);
	}
	
	/**
	 * ??????????????????
	 * @param codes
	 * @return
	 */
	private Map<String,GoodsCategory> getPictureByCode(List<String> codes){
		GoodsCategoryExample goodsCategory = new GoodsCategoryExample();
		goodsCategory.createCriteria().andCodeIn(codes);
		List<GoodsCategory>  listDto = goodsCategoryMapper.selectByExample(goodsCategory);
		Map<String,GoodsCategory> result = new HashMap<String,GoodsCategory>();
		if(CollectionUtils.isNotEmpty(listDto)) {
			for (GoodsCategory goodsCategory2 : listDto) {
				result.put(goodsCategory2.getCode(), goodsCategory2);
			}
		}
		return result;
	}
}

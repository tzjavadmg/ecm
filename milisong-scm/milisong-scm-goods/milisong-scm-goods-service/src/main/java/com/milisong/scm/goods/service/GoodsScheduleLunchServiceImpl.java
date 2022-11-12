package com.milisong.scm.goods.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.db.IdGenerator;
import com.farmland.core.distrib.LockProvider;
import com.farmland.core.util.BeanMapper;
import com.milisong.scm.base.api.DimDateService;
import com.milisong.scm.base.api.ShopService;
import com.milisong.scm.base.dto.DimDateDto;
import com.milisong.scm.base.dto.ShopDto;
import com.milisong.scm.goods.api.GoodsSalableQuantityLunchService;
import com.milisong.scm.goods.api.GoodsScheduleLunchService;
import com.milisong.scm.goods.constant.ErrorEnum;
import com.milisong.scm.goods.domain.GoodsScheduleDetailLunch;
import com.milisong.scm.goods.domain.GoodsScheduleDetailLunchExample;
import com.milisong.scm.goods.domain.GoodsScheduleLunch;
import com.milisong.scm.goods.domain.GoodsScheduleLunchExample;
import com.milisong.scm.goods.dto.GoodsScheduleDetailLunchDto;
import com.milisong.scm.goods.dto.GoodsScheduleLunchDto;
import com.milisong.scm.goods.dto.GoodsScheduleLunchInfoDto;
import com.milisong.scm.goods.mapper.GoodsScheduleDetailLunchMapper;
import com.milisong.scm.goods.mapper.GoodsScheduleLunchMapper;
import com.milisong.scm.goods.mq.MqProducerGoodsUtil;
import com.milisong.scm.goods.param.GoodsScheduleLunchParam;
import com.milisong.scm.goods.util.DateUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
@RefreshScope
public class GoodsScheduleLunchServiceImpl implements GoodsScheduleLunchService {

	@Autowired
	private GoodsScheduleLunchMapper goodsScheduleLunchMapper;
	
	@Autowired
	private GoodsScheduleDetailLunchMapper goodsScheduleDetailLunchMapper;
	
	@Autowired
	private DimDateService dimDateService;
	
	@Autowired
	private ShopService shopService;
	
	/**
	 * 全局锁、防止 修改和 发布档期并发
	 */
	private final static String  GOODS_SCHEDULE_KEY = "goodsScheduleKey";
	
	
	@Autowired
	private GoodsSalableQuantityLunchService shopGoodsSalableQuantityService;
	
	@Value("${ecm.shoponsale.goods-default-count}")
	private int goodsDefaultCount;
	
	@Value("${file.oss.viewUrl}")
	private String picViewUrl;
	
	@Override
	public ResponseResult<GoodsScheduleLunchInfoDto> getGoodsScheduleByWeekOfYear(Integer year, String weekOfYear,
			Long shopId, String updateBy) {
		 //校验参数
		if(null == year) {
			return ResponseResult.buildFailResponse(ErrorEnum.PARAMETER_CHECK_FAIL.getCode(),ErrorEnum.PARAMETER_CHECK_FAIL.getDesc());
		}
		if(null == weekOfYear) {
			return ResponseResult.buildFailResponse(ErrorEnum.PARAMETER_CHECK_FAIL.getCode(),ErrorEnum.PARAMETER_CHECK_FAIL.getDesc());
		}
		if(null == shopId) {
			return ResponseResult.buildFailResponse(ErrorEnum.PARAMETER_CHECK_FAIL.getCode(),ErrorEnum.PARAMETER_CHECK_FAIL.getDesc());
		}
		//获取指定日期的当期
		GoodsScheduleLunchExample goodsScheduleLunchExample = new GoodsScheduleLunchExample();
		goodsScheduleLunchExample.createCriteria().andYearEqualTo(year).andShopIdEqualTo(shopId).andWeekOfYearEqualTo(Integer.parseInt(weekOfYear));
		List<GoodsScheduleLunch> listGoodsScheduleLunch = goodsScheduleLunchMapper.selectByExample(goodsScheduleLunchExample);
		if(CollectionUtils.isEmpty(listGoodsScheduleLunch)) {
			// 新增主表信息
			ResponseResult<ShopDto> shopsResponse = shopService.queryById(shopId);
			if (null == shopsResponse || !shopsResponse.isSuccess() || null == shopsResponse.getData()) {
				log.info("门店ID不存在门店ID：{}",shopId);
                throw new RuntimeException("未能查询到门店信息");
			}
			ShopDto shopDto = shopsResponse.getData();
			//获取日期
			Map<Integer, DimDateDto> mapDate = getWeekDateByWeekOfYear(weekOfYear+"", year+"");
			List<GoodsScheduleLunch> listScheduleLunch = new ArrayList<GoodsScheduleLunch>();
			for (int i = 1; i <= 7; i++) {
				GoodsScheduleLunch goodsScheduleLunch = new GoodsScheduleLunch();
				goodsScheduleLunch.setId(IdGenerator.nextId());
				goodsScheduleLunch.setShopId(shopId);
				goodsScheduleLunch.setShopCode(shopDto.getCode());
				goodsScheduleLunch.setShopName(shopDto.getName());
				goodsScheduleLunch.setYear(year);
				goodsScheduleLunch.setWeekOfYear(Integer.parseInt(weekOfYear));
				goodsScheduleLunch.setScheduleDate(mapDate.get(i).getDate());
				goodsScheduleLunch.setWeek(i);
				goodsScheduleLunch.setStatus(getWorkStatusByWeekStatus(mapDate.get(i).getStatus()));
				goodsScheduleLunch.setIsDeleted(false);
				goodsScheduleLunch.setCreateBy(updateBy);
				listScheduleLunch.add(goodsScheduleLunch);
			}
			// 批量新增
			goodsScheduleLunchMapper.insertBatchSelective(listScheduleLunch);
			listGoodsScheduleLunch = goodsScheduleLunchMapper.selectByExample(goodsScheduleLunchExample);
		}
		
		List<GoodsScheduleDetailLunchDto> listGoodsScheduleDetail = getGoodsScheduleDetailByWeekOfYear(year, Integer.parseInt(weekOfYear), shopId);
		GoodsScheduleLunchInfoDto goodsScheduleInfoDto = new GoodsScheduleLunchInfoDto();
		
		goodsScheduleInfoDto.setGoodsScheduleLunch(BeanMapper.mapList(listGoodsScheduleLunch, GoodsScheduleLunchDto.class));
		
		goodsScheduleInfoDto.setGoodsScheduleDetailLunch(listGoodsScheduleDetail);
		
		return ResponseResult.buildSuccessResponse(goodsScheduleInfoDto);
	}

	@Override
	public List<GoodsScheduleDetailLunchDto> getGoodsScheduleDetailByWeekOfYear(Integer year, Integer weekOfYear,
			Long shopId) {
		//获取指定日期的当期商品
		GoodsScheduleDetailLunchExample goodsScheduleDetailExample = new GoodsScheduleDetailLunchExample();
		
		goodsScheduleDetailExample.createCriteria().andYearEqualTo(year).andShopIdEqualTo(shopId).andWeekOfYearEqualTo(weekOfYear).andIsDeletedEqualTo(false);
	
		List<GoodsScheduleDetailLunch> listGoodsScheduleDetail = goodsScheduleDetailLunchMapper.selectByExample(goodsScheduleDetailExample);
		if(CollectionUtils.isNotEmpty(listGoodsScheduleDetail)) {
			return BeanMapper.mapList(listGoodsScheduleDetail, GoodsScheduleDetailLunchDto.class);
		}
		return null;
	}

	@Override
	public ResponseResult<String> saveGoodsSchedule(GoodsScheduleLunchParam goodsScheduleParam) {
		RLock lock = LockProvider.getLock(GOODS_SCHEDULE_KEY);
		boolean lockResult = lock.tryLock();
		try {
			if(!lockResult) {
				return ResponseResult.buildFailResponse("2348","已档期在发布、请稍后执行型");
			}
			if(null == goodsScheduleParam) {
				return ResponseResult.buildFailResponse(ErrorEnum.PARAMETER_CHECK_FAIL.getCode(),ErrorEnum.PARAMETER_CHECK_FAIL.getDesc());
			}
			String updateBy = goodsScheduleParam.getUpdateBy();
			//处理档期信息
			List<GoodsScheduleLunchDto> listGoodsScheduleLunch = goodsScheduleParam.getGoodsScheduleLunch();
			if(CollectionUtils.isEmpty(listGoodsScheduleLunch)) {
				log.info("档期信息为空");
				return ResponseResult.buildFailResponse(ErrorEnum.PARAMETER_CHECK_FAIL.getCode(),ErrorEnum.PARAMETER_CHECK_FAIL.getDesc());
			}
			
			GoodsScheduleLunchDto goodsSchedule = listGoodsScheduleLunch.get(0);
			if(null == goodsSchedule.getPublishTime()) {
				log.info("档期发布日期不能为空");
				return ResponseResult.buildFailResponse(ErrorEnum.PARAMETER_CHECK_FAIL.getCode(),ErrorEnum.PARAMETER_CHECK_FAIL.getDesc());
			}
			//校验档期日期是否大于当前时间
			if(goodsSchedule.getPublishTime().getTime()<= DateUtil.getNowDateTime2().getTime()) {
				return ResponseResult.buildFailResponse("9001","档期发布日期必须大于当前时间");
			}
			for (GoodsScheduleLunchDto goodsScheduleDto : listGoodsScheduleLunch) {
				GoodsScheduleLunchExample goodsScheduleExample = new GoodsScheduleLunchExample();
				goodsScheduleExample.createCriteria().andIdEqualTo(goodsScheduleDto.getId());
				//判断以前的发布时间是否大于当前时间
				List<GoodsScheduleLunch> listGoodsScheduleOld = goodsScheduleLunchMapper.selectByExample(goodsScheduleExample);
				if(CollectionUtils.isNotEmpty(listGoodsScheduleOld)) {
					GoodsScheduleLunch goodsScheduleOld = listGoodsScheduleOld.get(0);
					if(null != goodsScheduleOld.getPublishTime()) {
						if(goodsScheduleOld.getPublishTime().getTime() <= DateUtil.getNowDateTime2().getTime()) {
							return ResponseResult.buildFailResponse("9002","档期已发布、不能再修改发布时间");
						}
					}
				}
				GoodsScheduleLunch record = new GoodsScheduleLunch();
				record.setPublishTime(goodsScheduleDto.getPublishTime());
				record.setStatus(goodsScheduleDto.getStatus());
				record.setUpdateBy(updateBy);
				goodsScheduleLunchMapper.updateByExampleSelective(record, goodsScheduleExample);
			}
			//处理档期商品信息
			List<GoodsScheduleDetailLunchDto> listGoodsScheduleDetail = goodsScheduleParam.getGoodsScheduleDetailLunch();
			if(CollectionUtils.isEmpty(listGoodsScheduleDetail)) {
				log.info("档期商品信息为空");
				return ResponseResult.buildFailResponse(ErrorEnum.PARAMETER_CHECK_FAIL.getCode(),ErrorEnum.PARAMETER_CHECK_FAIL.getDesc());
			}
			for (GoodsScheduleDetailLunchDto goodsScheduleDetailDto : listGoodsScheduleDetail) {
				if(null == goodsScheduleDetailDto.getId()) {
					// 新增
					goodsScheduleDetailDto.setId(IdGenerator.nextId());
					GoodsScheduleDetailLunch goodsScheduleDetail = BeanMapper.map(goodsScheduleDetailDto, GoodsScheduleDetailLunch.class);
					goodsScheduleDetail.setIsDeleted(false);
					goodsScheduleDetail.setShopCode(goodsSchedule.getShopCode());
					goodsScheduleDetail.setShopName(goodsSchedule.getShopName());
					goodsScheduleDetail.setCreateBy(updateBy);
					goodsScheduleDetailLunchMapper.insertSelective(goodsScheduleDetail);
				}else {
					// 编辑
					GoodsScheduleDetailLunchExample goodsScheduleDetailExample = new GoodsScheduleDetailLunchExample();
					goodsScheduleDetailExample.createCriteria().andIdEqualTo(goodsScheduleDetailDto.getId());
					GoodsScheduleDetailLunch goodsScheduleDetail = new GoodsScheduleDetailLunch();
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
					goodsScheduleDetailLunchMapper.updateByExampleSelective(goodsScheduleDetail, goodsScheduleDetailExample);
				}
			}
		} finally {
			if(lockResult) {
				lock.unlock();
			}
		}
		return ResponseResult.buildSuccessResponse();
	}

	@Override
	public ResponseResult<String> publishGoodsSchedule() {
		RLock lock = LockProvider.getLock(GOODS_SCHEDULE_KEY);
		lock.lock();
		try {
			//获取指定日期的档期
			GoodsScheduleLunchExample goodsScheduleExample = new GoodsScheduleLunchExample();
			goodsScheduleExample.createCriteria()
								.andPublishTimeGreaterThanOrEqualTo(DateUtil.getNowDateTimeFormatStart())
								.andPublishTimeLessThan(DateUtil.getNowDateTimeFormatEnd())
								.andStatusEqualTo(true);
			List<GoodsScheduleLunch> listGoodsSchedule = goodsScheduleLunchMapper.selectByExample(goodsScheduleExample);
			GoodsScheduleLunch param = new GoodsScheduleLunch();
			//如果存在多个周、或者不同门店的档期
			if(CollectionUtils.isNotEmpty(listGoodsSchedule)) {
				log.info("开始发布档期信息，Date :{}",JSON.toJSONString(listGoodsSchedule));
				// 以门店code+年+周 做为key 分组数据
				Map<String, List<GoodsScheduleLunch>> mapGoodsSchedule = getGoodsScheduleInfoByPushingTime(listGoodsSchedule);
				for (String key : mapGoodsSchedule.keySet()) {
					log.info(key);
					List<GoodsScheduleLunch> list = mapGoodsSchedule.get(key);
					if(CollectionUtils.isNotEmpty(list)) {
						param = list.get(0);
						//获取所有的商品
						List<GoodsScheduleDetailLunchDto> listGoodsScheduleDetail = getGoodsScheduleDetailByWeekOfYear(param.getYear(), param.getWeekOfYear(), param.getShopId());
						log.info("获取所有的商品:{}",JSON.toJSONString(listGoodsScheduleDetail));
						//保存需要操作可售量的可售商品
						List<String> listSalableQuantityDto = new ArrayList<String>();
						Map<Date,List<String>> goodsScheduleMqMap = new HashMap<Date,List<String>>();
						for (GoodsScheduleLunch goodsSchedule : list) {
							List<String> goodsCode = new ArrayList<String>();
							for (GoodsScheduleDetailLunchDto goodsScheduleDetailDto : listGoodsScheduleDetail) {
								if(getGoodsStatusBy(goodsSchedule.getWeek(),goodsScheduleDetailDto)) {
									log.info("当前档期日期：{},当前",goodsSchedule.getScheduleDate());
									//记录可售商品
									goodsCode.add(goodsScheduleDetailDto.getGoodsCode());
									// 设置默认可售量
									listSalableQuantityDto.add(goodsScheduleDetailDto.getGoodsCode());
								}
							}
							goodsScheduleMqMap.put(goodsSchedule.getScheduleDate(),goodsCode);
						}
						ResponseResult<ShopDto> shopsResponse = shopService.queryById(param.getShopId());
						if (null == shopsResponse || !shopsResponse.isSuccess() || null == shopsResponse.getData()) {
							log.info("门店ID不存在门店ID：{}",param.getShopId());
			                throw new RuntimeException("未能查询到门店信息");
						}
						Map<String,Map<Date,List<String>>> resultMap = new HashMap<String, Map<Date,List<String>>>();
						resultMap.put(shopsResponse.getData().getCode(), goodsScheduleMqMap);
						//发送MQ给到ECM
						MqProducerGoodsUtil.sendGoodsSalableMsg(resultMap);
						MqProducerGoodsUtil.sendGoodsScheduleMsg(list);
						// 设置默认可售商品量
						shopGoodsSalableQuantityService.initShopGoodsSalableQuantity(goodsScheduleMqMap,shopsResponse.getData());
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

	@Override
	public ResponseResult<List<GoodsScheduleLunchDto>> getDateByShopId(Long shopId) {
		//获取大于当前时间的五天
		GoodsScheduleLunchExample goodsScheduleExample = new GoodsScheduleLunchExample();
		List<GoodsScheduleLunchDto> listResult = new ArrayList<GoodsScheduleLunchDto>();
//				goodsScheduleExample.setPageSize(5);
//				goodsScheduleExample.setStartRow(1);
		goodsScheduleExample.createCriteria().andShopIdEqualTo(shopId)
							.andScheduleDateGreaterThanOrEqualTo(new Date())
							.andStatusEqualTo(true);
		goodsScheduleExample.setOrderByClause(" schedule_date asc ");
		List<GoodsScheduleLunch> ListGoodsSchedule = goodsScheduleLunchMapper.selectByExample(goodsScheduleExample);
		if(CollectionUtils.isNotEmpty(ListGoodsSchedule)) {
			listResult = BeanMapper.mapList(ListGoodsSchedule, GoodsScheduleLunchDto.class);
			return ResponseResult.buildSuccessResponse(listResult);
		}
		return ResponseResult.buildSuccessResponse(listResult);
	}
	
	private Map<Integer,DimDateDto> getWeekDateByWeekOfYear(String nowWeek,String nowYear) {
		ResponseResult<List<com.milisong.scm.base.dto.DimDateDto>>  listDimDto = dimDateService.getWeekDateInfoByWeek(nowWeek, nowYear);
		Map<Integer,DimDateDto> mapResult = new HashMap<Integer, DimDateDto>();
		if(CollectionUtils.isEmpty(listDimDto.getData())) {
			return mapResult;
		}
		for (DimDateDto dimDateDto : listDimDto.getData()) {
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
	
	private Boolean getGoodsStatusBy(Integer week,GoodsScheduleDetailLunchDto goodsScheduleDetailDto) {
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
		log.info("校验日期是否可售，日期{},商品信息:{}",week,JSON.toJSONString(goodsScheduleDetailDto));
		return result;
	}
	
	/**
	 * 获取本次需要同步的档期数据
	 */
	private Map<String, List<GoodsScheduleLunch>> getGoodsScheduleInfoByPushingTime(List<GoodsScheduleLunch> listGoodsSchedule){
		Map<String,List<GoodsScheduleLunch>> maps = new HashMap<String,List<GoodsScheduleLunch>>();
		for (GoodsScheduleLunch goodsSchedule : listGoodsSchedule) {
			String key = goodsSchedule.getShopCode()+"_"+goodsSchedule.getYear()+"_"+goodsSchedule.getWeekOfYear();
			List<GoodsScheduleLunch> listGoodsScheduleDto =	maps.get(key);
			if(listGoodsScheduleDto == null) {
				listGoodsScheduleDto = new ArrayList<GoodsScheduleLunch>();
			}
			listGoodsScheduleDto.add(goodsSchedule);
			maps.put(key, listGoodsScheduleDto);
		}
		log.info("组合本次需要同步的档期数据{}",JSON.toJSONString(maps));
		return maps;
	}


}

package com.milisong.breakfast.pos.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.farmland.core.distrib.LockProvider;
import com.farmland.core.util.BeanMapper;
import com.milisong.breakfast.pos.api.PosProductionService;
import com.milisong.breakfast.pos.constant.OrderSetStatusEnum;
import com.milisong.breakfast.pos.constant.ProductionFlagEnum;
import com.milisong.breakfast.pos.constant.RedisKeyConstant;
import com.milisong.breakfast.pos.domain.OrderSetDetail;
import com.milisong.breakfast.pos.domain.OrderSetDetailExample;
import com.milisong.breakfast.pos.domain.OrderSetDetailGoods;
import com.milisong.breakfast.pos.dto.result.OrderSetGoodsInfoResult;
import com.milisong.breakfast.pos.dto.result.OrderSetInfoResult;
import com.milisong.breakfast.pos.mapper.OrderSetDetailExtMapper;
import com.milisong.breakfast.pos.mapper.OrderSetDetailGoodsMapper;
import com.milisong.breakfast.pos.mapper.OrderSetDetailMapper;
import com.milisong.breakfast.pos.mq.dto.OrderSetDetailDto;
import com.milisong.breakfast.pos.mq.dto.OrderSetDetailGoodsDto;
import com.milisong.breakfast.pos.mq.dto.OrderSetProductionMsg;
import com.milisong.breakfast.pos.param.PosProductionParam;
import com.milisong.breakfast.pos.service.help.DelayQueueService;
import com.milisong.breakfast.pos.util.ConfigRedisUtil;
import com.milisong.breakfast.pos.util.MqSendUtil;

import lombok.extern.slf4j.Slf4j;

/**
*@author    created by benny
*@date  2018???10???25???---??????9:24:32
*
*/
@Slf4j
@Service
public class PosProductionServiceImpl implements PosProductionService {
	@Autowired
	private OrderSetDetailGoodsMapper orderSetDetailGoodsMapper;
	@Autowired
	private OrderSetDetailMapper orderSetDetailMapper;
	@Autowired
	private DelayQueueService delayQueueService;
	
	@Autowired
	private OrderSetDetailExtMapper orderSetDetailExtMapper;

	@Override
	@Transactional
	public Long saveOrderSetInfo(String msg) {
		log.info("?????????????????????????????????{}", msg);
		OrderSetProductionMsg msgDto = JSONObject.parseObject(msg, OrderSetProductionMsg.class);
		
		RLock lock = LockProvider.getLock(RedisKeyConstant.getOrderSetLockKey(msgDto.getOrderSet().getDetailSetNo()));
		
		try {
			lock.lock(10, TimeUnit.SECONDS);
			OrderSetDetail orderSet = orderSetDetailMapper.selectByPrimaryKey(msgDto.getOrderSet().getId());
			if(null != orderSet) {
				return orderSet.getShopId();
			}
			
			orderSet = BeanMapper.map(msgDto.getOrderSet(), OrderSetDetail.class);
			// ??????
			orderSet.setType(Byte.valueOf("0"));
			orderSet.setStatus(OrderSetStatusEnum.WATTING_ORDER_1.getValue());
			orderSet.setUpdateTime(null);
			
			List<OrderSetDetailGoods> goods = new ArrayList<>(msgDto.getGoods().size());
			msgDto.getGoods().stream().forEach(item -> {
				OrderSetDetailGoods good = BeanMapper.map(item, OrderSetDetailGoods.class);
				goods.add(good);
			});
			
			// ?????????
			orderSetDetailMapper.insertSelective(orderSet);
			orderSetDetailGoodsMapper.insertBatchSelective(goods);
			
			// ???redis??????
			Long shopId = orderSet.getShopId();
			//RedisCache.lLeftPush(RedisKeyConstant.getOrderSetAllListKey(shopId), msg);
			RedisCache.zAdd(RedisKeyConstant.getOrderSetAllListKey(shopId), JSON.toJSONString(msgDto), getSetNo(orderSet));
			
			return shopId;
		} catch (Throwable e) {
			log.error("????????????????????????redis??????", e);
			throw new RuntimeException(e);
		} finally {
			lock.unlock();
		}
	}
	
	@Override
	public boolean checkOrderSetSequence(String msg) {
		log.info("????????????????????????-------------");
		OrderSetProductionMsg msgDto = JSONObject.parseObject(msg, OrderSetProductionMsg.class);
		RLock lock = LockProvider.getLock(RedisKeyConstant.getShopOrderSetSequenceLockKey(msgDto.getOrderSet().getShopId()));
		
		try {
			lock.lock(10, TimeUnit.SECONDS);
			if(canProduction(msgDto.getOrderSet())) {
				String key = RedisKeyConstant.getShopProducedOrderSetKey(msgDto.getOrderSet().getShopId());
				long now = getSetNo(msgDto.getOrderSet());
				RedisCache.set(key, String.valueOf(now));
				RedisCache.expire(key, 1, TimeUnit.DAYS);
				return true;
			} else {
				log.warn("???????????????{}???????????????????????????", msgDto.getOrderSet().getDetailSetNo());
				return false;
			}
		} catch (Throwable e) {
			log.error("????????????????????????redis??????", e);
			return true;
		} finally {
			lock.unlock();
		}
	}
	
	@Override
	public void notifyProduction(Long shopId) {
		log.info("?????????????????????id???{}", shopId);
		
		String productionFlag = RedisKeyConstant.getProductionFlagKey(shopId);
		if(StringUtils.isBlank(productionFlag)) {
			productionFlag = ProductionFlagEnum.RUN.getValue();
			RedisCache.setEx(productionFlag, productionFlag, 1, TimeUnit.DAYS);
		}
		if(ProductionFlagEnum.PAUSE.getValue().equals(productionFlag)) {
			log.warn("?????????{}????????????????????????", shopId);
			return;
		}
		
		RLock lock = LockProvider.getLock(RedisKeyConstant.getOrderSetProductionLockKey(shopId));
		
		try {
			lock.lock(10, TimeUnit.SECONDS);
			// ????????????
			int maxOutput = 10;
			try {
				maxOutput = ConfigRedisUtil.getMaxOutput(shopId);
			} catch (Exception e) {
				log.error("????????????????????????", e);
			}
			
			// ??????????????????
			processMaxOutput(maxOutput, shopId);
		} finally {
			lock.unlock();
		}
	}
	
	@Override
	public void countdown(String msg) {
		log.info("???????????????????????????{}", msg);
		OrderSetProductionMsg msgDto = JSONObject.parseObject(msg, OrderSetProductionMsg.class);
		Long shopId = msgDto.getOrderSet().getShopId();
		
		RLock lock = LockProvider.getLock(RedisKeyConstant.getShopTimeoutLockKey(shopId));
		try {
			lock.lock(10, TimeUnit.SECONDS);
			
			// ??????????????????
			setTimeout(shopId, msgDto);
			
		} finally {
			lock.unlock();
		}
	}
	
	@Override
	@Transactional
	public ResponseResult<String> updateOrderSetStatusByNo(Long shopId,String orderSetNo, Byte orderStatus,String updateBy) {
		log.info("????????????{},{}",orderSetNo,orderStatus);
		
		RLock lock = LockProvider.getLock(RedisKeyConstant.getShopTimeoutLockKey(shopId));
		try {
			lock.lock(10, TimeUnit.SECONDS);
			updateOrdersetStatus(orderSetNo, orderStatus,updateBy);
			/*if(0 == updateResult) {
				return ResponseResult.buildSuccessResponse();
			}*/
			//?????????????????????????????????
			List<OrderSetProductionMsg> list = getOrderSetProductionList(shopId);
			log.info("?????????????????????????????????{}",JSON.toJSON(list));
			if(!CollectionUtils.isEmpty(list)) {
				OrderSetProductionMsg  orderSetDetailDtoToDelete = null;
				//???????????????????????? ??????
				for (OrderSetProductionMsg orderSetProductionMsg : list) {
					//log.info("??????????????????????????????{}",JSON.toJSON(orderSetProductionMsg));
					OrderSetDetailDto  orderSetDetailDto = orderSetProductionMsg.getOrderSet();
					if(orderSetDetailDto.getDetailSetNo().equals(orderSetNo)) {
						orderSetDetailDtoToDelete = orderSetProductionMsg;
						break;
					}
				}
				//??????????????????
				log.info("?????????????????????{}",JSON.toJSONString(orderSetDetailDtoToDelete));
				if(orderSetDetailDtoToDelete != null) {
					RedisCache.lRemove(RedisKeyConstant.getOrderSetLineUpListKey(shopId), 0, this.transformJsonToSpecialStr(JSON.toJSONString(orderSetDetailDtoToDelete)));
					RedisCache.incrBy(RedisKeyConstant.getOrderSetLineUpCountKey(shopId), orderSetDetailDtoToDelete.getOrderSet().getGoodsSum()*-1);
					RedisCache.delete(RedisKeyConstant.getOrderSetTimeoutTtlKey(shopId, orderSetNo));
					
					// ???????????????????????????
					long now = getSetNo(orderSetDetailDtoToDelete.getOrderSet()) + 1;
					RedisCache.set(RedisKeyConstant.getShopProducedOrderSetKey(orderSetDetailDtoToDelete.getOrderSet().getShopId()), String.valueOf(now));
					
					if(!"system_??????".equals(updateBy) && isRun(shopId)) {
						delayQueueService.cancel(orderSetDetailDtoToDelete.getOrderSet().getId());
					}
				}
			}
			// ?????????????????????
			OrderSetProductionMsg dto = getFirstOrderSet(shopId);
			if(null != dto) {
				this.setTimeout(shopId, dto);
			}
			// ??????????????????
			this.notifyProduction(shopId);
			
			return ResponseResult.buildSuccessResponse();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public Pagination<OrderSetInfoResult> getOrderSetListByStatus(PosProductionParam posProductionParam) {
		Pagination<OrderSetInfoResult> page = new Pagination<OrderSetInfoResult>();
		//?????????????????????????????????
		if(posProductionParam.getOrderStatus().equals(OrderSetStatusEnum.PRODUCTION_ORDER_2.getValue())) {
			page.setDataList(getOrderSetForProductionQueue(posProductionParam.getShopId()));
			return page;
		}
		log.info("???????????????or?????????????????????{}",JSON.toJSON(posProductionParam));
		Map<String,Object> mapParam = new HashMap<String,Object>();
		mapParam.put("status",posProductionParam.getOrderStatus());
		mapParam.put("shopId",posProductionParam.getShopId());
		mapParam.put("startRow",posProductionParam.getStartRow());
		mapParam.put("pageSize",posProductionParam.getPageSize());
		log.info("????????????{}",JSON.toJSONString(mapParam));
		int count = orderSetDetailExtMapper.getCountByShopIdAndStatus(mapParam);
		page.setPageNo(posProductionParam.getPageNo());
		page.setPageSize(posProductionParam.getPageSize());
		page.setTotalCount(count);
		List<OrderSetInfoResult> list = orderSetDetailExtMapper.getListByShopIdAndStatus(mapParam);
		//log.info("??????????????????????????????{}",JSON.toJSONString(list));
		Map<String,OrderSetInfoResult> mapResult = new LinkedHashMap<String,OrderSetInfoResult>();
		for (OrderSetInfoResult orderSetInfoResult : list) {
			mapResult.put(orderSetInfoResult.getDetailSetNo(), orderSetInfoResult);
		}
		for (OrderSetInfoResult orderSetInfoResult : list) {
			OrderSetInfoResult info = mapResult.get(orderSetInfoResult.getDetailSetNo());
			if(info != null) {
				OrderSetGoodsInfoResult goodsInfo = new OrderSetGoodsInfoResult();
				goodsInfo.setGoodsName(orderSetInfoResult.getGoodsName());
				goodsInfo.setGoodsCode(orderSetInfoResult.getGoodsCode());
				goodsInfo.setGoodsSum(orderSetInfoResult.getGoodsNumber());
				goodsInfo.setCoupletNo(orderSetInfoResult.getCoupletNo());
				List<OrderSetGoodsInfoResult> listGoods = info.getList();
				if(CollectionUtils.isEmpty(listGoods)) {
					listGoods = new ArrayList<OrderSetGoodsInfoResult>();
				}
				listGoods.add(goodsInfo);
				info.setList(listGoods);
			}
		}
		list.clear();
		list.addAll(mapResult.values());
		page.setDataList(list);
		return page;
	}
	
	@Override
	public void pauseOrRestart(Long shopId, String opUser) {
		log.info("?????????{}???{}??????????????????????????????", shopId, opUser);
		
		RLock lock = LockProvider.getLock(RedisKeyConstant.getProductionFlagLockKey(shopId));
		try {
			lock.lock(10, TimeUnit.SECONDS);
			
			String productionFlag = RedisCache.get(RedisKeyConstant.getProductionFlagKey(shopId));
			if(StringUtils.isBlank(productionFlag)) {
				productionFlag = ProductionFlagEnum.RUN.getValue();
				RedisCache.setEx(productionFlag, productionFlag, 1, TimeUnit.DAYS);
			}
			log.info("?????????{}???????????????????????????{}", shopId, ProductionFlagEnum.getNameByValue(productionFlag));
			
			if(ProductionFlagEnum.PAUSE.getValue().equals(productionFlag)) {
				log.info("??????--->??????");
				RedisCache.set(RedisKeyConstant.getProductionFlagKey(shopId), ProductionFlagEnum.RUN.getValue());
								
				OrderSetProductionMsg item = getFirstOrderSet(shopId);
				if(null != item) {
					String ttlKey = RedisKeyConstant.getOrderSetTimeoutTtlKey(shopId, item.getOrderSet().getDetailSetNo());
					// ????????????ttl
					long pauseTtl = Long.valueOf(RedisCache.get(ttlKey));
					log.info("???????????????{}???????????????ttl??????{}", item.getOrderSet().getDetailSetNo(), pauseTtl);
					delayQueueService.setDelay(item.getOrderSet().getId(), item.getOrderSet().getDetailSetNo(), shopId ,pauseTtl);
					RedisCache.setEx(ttlKey, String.valueOf(pauseTtl), pauseTtl, TimeUnit.SECONDS);
				}
				this.notifyProduction(shopId);
			} else {
				log.info("??????--->??????");
				RedisCache.set(RedisKeyConstant.getProductionFlagKey(shopId), ProductionFlagEnum.PAUSE.getValue());
								
				OrderSetProductionMsg item = getFirstOrderSet(shopId);
				if(null != item) {
					String ttlKey = RedisKeyConstant.getOrderSetTimeoutTtlKey(shopId, item.getOrderSet().getDetailSetNo());
					long redisTtl = RedisCache.getExpire(ttlKey, TimeUnit.SECONDS);
					long ttl = delayQueueService.getTtl(item.getOrderSet().getId());
					if(redisTtl!=ttl) {
						log.info("??????redis??????????????????????????????????????????????????????redis?????????????????????{}", redisTtl);
						ttl = redisTtl;
					}
					
					if(ttl > 0) {
						log.info("???????????????{}???????????????ttl??????{}", item.getOrderSet().getDetailSetNo(), ttl);
						RedisCache.setEx(ttlKey, String.valueOf(ttl), 30, TimeUnit.DAYS);
						delayQueueService.pause(item.getOrderSet().getId());
					}
				}
			}
		} finally {
			lock.unlock();
		}
	}
	
	@Override
	public Integer getShopProductionStatus(Long shopId) {
		String status = RedisKeyConstant.getProductionFlagKey(shopId);
		String value = RedisCache.get(status);
		if(null == value ) {
			return 1;
		}
		log.info("????????????????????????shopId:{},status{}",shopId,value);
		return Integer.valueOf(value);
	}
	
	/**
	 * ????????????????????????????????????
	 * @param shopId
	 * @return
	 */
	private List<OrderSetProductionMsg> getOrderSetProductionList(Long shopId) {
		Long lenth = RedisCache.lLen(RedisKeyConstant.getOrderSetLineUpListKey(shopId));
		List<String> productionOrderSet = RedisCache.lRange(RedisKeyConstant.getOrderSetLineUpListKey(shopId), 0L, lenth -1);
		if(!CollectionUtils.isEmpty(productionOrderSet)) {
			List<OrderSetProductionMsg> list = new ArrayList<OrderSetProductionMsg>();
			//?????????????????????????????????????????????
			for (String string : productionOrderSet) {
				OrderSetProductionMsg obj =JSONObject.parseObject(this.transformSpecialStrToJson(string), OrderSetProductionMsg.class);
				list.add(obj);
			}
			return list;
		}
		return null;
	}
	
	/**
	 * ???????????????????????????????????????
	 * @param shopId
	 * @return
	 */
	private OrderSetProductionMsg getFirstOrderSet(Long shopId) {
		Long length = RedisCache.lLen(RedisKeyConstant.getOrderSetLineUpListKey(shopId));
		if(length > 0) {
			String item = RedisCache.lIndex(RedisKeyConstant.getOrderSetLineUpListKey(shopId), -1);
			if(StringUtils.isNotBlank(item)) {
				return JSONObject.parseObject(this.transformSpecialStrToJson(item), OrderSetProductionMsg.class);
			}
		}
		return null;
	}
	
	private List<OrderSetInfoResult> getOrderSetForProductionQueue(Long shopId){
		//log.info("?????????????????????????????????????????????ID???{}",shopId);
		List<OrderSetInfoResult> resultInfo = new ArrayList<OrderSetInfoResult>();
		List<OrderSetProductionMsg> list = getOrderSetProductionList(shopId);
		if(CollectionUtils.isEmpty(list)) {
			return resultInfo;
		}
		int index = 0;
		for (OrderSetProductionMsg orderSetProductionMsg : list) {
			index++;
			OrderSetInfoResult order = new OrderSetInfoResult();
			OrderSetDetailDto ordersetDetailDto = orderSetProductionMsg.getOrderSet();
			order.setDetailSetNo(ordersetDetailDto.getDetailSetNo());
			order.setDetailSetNoDescription(ordersetDetailDto.getDetailSetNoDescription());
			Long outTime = 0L;
			if(index == list.size()) {
				outTime = getOrderSetTtl(shopId, ordersetDetailDto.getDetailSetNo(), ordersetDetailDto.getId());
			}
			order.setOutTime(outTime);
			List<OrderSetGoodsInfoResult> goodsInfo = new ArrayList<OrderSetGoodsInfoResult>();
			List<OrderSetDetailGoodsDto> listGoods = orderSetProductionMsg.getGoods();
			for (OrderSetDetailGoodsDto orderSetDetailGoodsDto : listGoods) {
				OrderSetGoodsInfoResult orderSetGoodsInfo = new OrderSetGoodsInfoResult();
				orderSetGoodsInfo.setCoupletNo(orderSetDetailGoodsDto.getCoupletNo());
				orderSetGoodsInfo.setGoodsCode(orderSetDetailGoodsDto.getGoodsCode());
				orderSetGoodsInfo.setGoodsName(orderSetDetailGoodsDto.getGoodsName());
				orderSetGoodsInfo.setGoodsSum(orderSetDetailGoodsDto.getGoodsNumber());
				goodsInfo.add(orderSetGoodsInfo);
			}
			order.setList(goodsInfo);
			
			resultInfo.add(order);
		}
		Collections.reverse(resultInfo);
		return resultInfo;
	}
	
	/**
	 * ???????????????????????????
	 * @param shopId
	 * @param detailSetNo
	 * @return
	 */
	private long getOrderSetTtl(Long shopId, String detailSetNo, Long detailSetId) {
		String productionFlag = RedisCache.get(RedisKeyConstant.getProductionFlagKey(shopId));
		if(StringUtils.isBlank(productionFlag) || ProductionFlagEnum.RUN.getValue().equals(productionFlag)) {
			return delayQueueService.getTtl(detailSetId);
		} else {
			String val =  RedisCache.get(RedisKeyConstant.getOrderSetTimeoutTtlKey(shopId, detailSetNo));
			if(StringUtils.isNotBlank(val)) {
				return Long.valueOf(val);
			}
		}
		return 0;
	}
		
	/**
	 * ????????????????????????????????????????????????
	 * @param shopId
	 * @param msgDto
	 * @return
	 */
	private int calculationOrderSetTimeoutSecond(Long shopId, OrderSetProductionMsg msgDto) {	
		// ????????????????????????(???)
		int singleMealTime = 24;
		try {
			String singleMealTimeStr = ConfigRedisUtil.getShopSingleConfigByKey(shopId, "singleMealTime");
			if(StringUtils.isNotBlank(singleMealTimeStr)) {
				singleMealTime = Integer.valueOf(singleMealTimeStr);
			}
		} catch (Exception e) {
			log.error("???????????????????????????????????????????????????", e);
		}
		// ???????????????????????????(???)
		int benchmarkTime = 4;
		try {
			String benchmarkTimeStr = ConfigRedisUtil.getShopSingleConfigByKey(shopId, "benchmarkTime");
			if(StringUtils.isNotBlank(benchmarkTimeStr)) {
				benchmarkTime = Integer.valueOf(benchmarkTimeStr);
			}
		} catch (Exception e) {
			log.error("????????????????????????????????????????????????????????????", e);
		}
		
		// ????????????????????????????????????
		int requiredSecond = singleMealTime * msgDto.getOrderSet().getGoodsSum();
		// ??????+????????????????????????
		int timeOutSecond = benchmarkTime + requiredSecond;
		
		return timeOutSecond;
	}

	/**
	 * ???json?????????????????????????????????
	 * @param json
	 * @return
	 */
	private String transformJsonToSpecialStr(String json) {
		if(StringUtils.isNotBlank(json)) {
			return json.replaceAll("\"", "&@");
		}
		return json;
	}
	
	/**
	 * ??????????????????json???????????????json
	 * @param specialStr
	 * @return
	 */
	private String transformSpecialStrToJson(String specialStr) {
		if(StringUtils.isNotBlank(specialStr)) {
			return specialStr.replaceAll("&@", "\"");
		}
		return specialStr;
	}
	
	/**
	 * ????????????????????????????????????
	 * @param maxOutput
	 */
	private void processMaxOutput(int maxOutput, Long shopId) {
		// String msg = RedisCache.lIndex(RedisKeyConstant.getOrderSetAllListKey(shopId), -1);
		Set<String> msgs = RedisCache.zRange(RedisKeyConstant.getOrderSetAllListKey(shopId), 0, 0);
		//if(StringUtils.isBlank(msg)) {
		if(CollectionUtils.isEmpty(msgs)) {
			return;
		}
		String msg = (String)msgs.toArray()[0];
		
		OrderSetProductionMsg msgDto = JSONObject.parseObject(msg, OrderSetProductionMsg.class);
		// ??????????????????
		int currentOrderSet = msgDto.getOrderSet().getGoodsSum();
		
		// ????????????????????????
		String now = RedisCache.get(RedisKeyConstant.getOrderSetLineUpCountKey(shopId));
		int nowValue = 0;
		if(StringUtils.isNotBlank(now)) {
			nowValue = Integer.valueOf(now);
		}
		log.info("??????:{}??????????????????????????????{}", shopId, nowValue);
		log.info("??????:{}??????????????????????????????{}", shopId, currentOrderSet);
		
		// ?????????????????????????????????
		if(currentOrderSet+nowValue <= maxOutput) {
			// ????????????
			updateOrdersetStatus(msgDto.getOrderSet().getDetailSetNo(), OrderSetStatusEnum.PRODUCTION_ORDER_2.getValue(),"system");
			// ???????????????+
			RedisCache.incrBy(RedisKeyConstant.getOrderSetLineUpCountKey(shopId), currentOrderSet);
			// ???????????????????????????
			RedisCache.lLeftPush(RedisKeyConstant.getOrderSetLineUpListKey(shopId), transformJsonToSpecialStr(msg));
			// ????????????????????????
			//RedisCache.lRightPop(RedisKeyConstant.getOrderSetAllListKey(shopId));
			//RedisCache.zRemoveRange(RedisKeyConstant.getOrderSetAllListKey(shopId), 0, 0);
			RedisCache.zRemoveRangeByScore(RedisKeyConstant.getOrderSetAllListKey(shopId), getSetNo(msgDto.getOrderSet()), getSetNo(msgDto.getOrderSet()));
			// ???MQ??????????????????????????????????????????
			MqSendUtil.sendOrderSetPrintMsg(msgDto);
			
			// ????????????
			processMaxOutput(maxOutput, shopId);
		} else {
			log.info("???????????????{}???????????????????????????????????????", shopId);
			return;
		}
	}
	
	/**
	 * ??????????????????
	 * @param shopId
	 * @param msgDto
	 */
	private void setTimeout(Long shopId, OrderSetProductionMsg msgDto) {
		OrderSetProductionMsg first = this.getFirstOrderSet(shopId);
		log.info("?????????{}?????????????????????????????????????????????{}", shopId, msgDto.getOrderSet().getDetailSetNo());
		if(null != first) {
			log.info("???????????????{}???????????????????????????{}", shopId, first.getOrderSet().getDetailSetNo());
		}
		if(null != first && first.getOrderSet().getId().equals(msgDto.getOrderSet().getId())) {
			if(!RedisCache.hasKey(RedisKeyConstant.getOrderSetTimeoutTtlKey(shopId, msgDto.getOrderSet().getDetailSetNo()))) {
				// ?????????????????????????????????
				int timeOut = calculationOrderSetTimeoutSecond(shopId, msgDto);
				log.info("?????????{}?????????????????????(???)???{}", msgDto.getOrderSet().getDetailSetNo(), timeOut);
				if(isRun(shopId)) {
					delayQueueService.setDelay(msgDto.getOrderSet().getId(), msgDto.getOrderSet().getDetailSetNo(), shopId, timeOut);
					RedisCache.setEx(RedisKeyConstant.getOrderSetTimeoutTtlKey(shopId, msgDto.getOrderSet().getDetailSetNo()),String.valueOf(timeOut), timeOut, TimeUnit.SECONDS);
				} else {
					RedisCache.setEx(RedisKeyConstant.getOrderSetTimeoutTtlKey(shopId, msgDto.getOrderSet().getDetailSetNo()),String.valueOf(timeOut), 30, TimeUnit.DAYS);
				}
				
			}
		}
	}
	
	/**
	 * ???????????????????????????
	 * @param orderSetNo
	 * @param orderStatus
	 * @param updateBy
	 * @return
	 */
	private int updateOrdersetStatus(String orderSetNo, Byte orderStatus,String updateBy) {
		OrderSetDetail record = new OrderSetDetail();
		record.setStatus(orderStatus);
		record.setUpdateBy(updateBy);
		OrderSetDetailExample orderSetDetailExample = new OrderSetDetailExample();
		orderSetDetailExample.createCriteria().andDetailSetNoEqualTo(orderSetNo).andStatusNotEqualTo(orderStatus);
		
		int updateResult = orderSetDetailMapper.updateByExampleSelective(record, orderSetDetailExample);
		return updateResult;
	}
	
	/**
	 * ???????????????
	 * @param shopId
	 * @return
	 */
	private boolean isRun(Long shopId) {
		String productionFlag = RedisCache.get(RedisKeyConstant.getProductionFlagKey(shopId));
		if(StringUtils.isBlank(productionFlag) || ProductionFlagEnum.RUN.getValue().equals(productionFlag)) {
			return true;
		}
		return false;
	}
	
	/**
	 * ???????????????????????????
	 * @param orderSet
	 * @return
	 */
	private long getSetNo(OrderSetDetail orderSet) {
		String setNo = orderSet.getDetailSetNoDescription().substring(1, orderSet.getDetailSetNoDescription().length());
		return Long.valueOf(setNo);
	}
	
	/**
	 * ???????????????????????????
	 * @param orderSet
	 * @return
	 */
	private long getSetNo(OrderSetDetailDto orderSet) {
		String setNo = orderSet.getDetailSetNoDescription().substring(1, orderSet.getDetailSetNoDescription().length());
		return Long.valueOf(setNo);
	}
	
	/**
	 * ???????????????????????????????????????????????????
	 * @param orderSet
	 * @return
	 */
	private boolean canProduction(OrderSetDetailDto orderSet) {
		String key = RedisKeyConstant.getShopProducedOrderSetKey(orderSet.getShopId());
		String value = RedisCache.get(key);
		if(StringUtils.isBlank(value)) {
			value = "1";
		}
		long before = Long.valueOf(value);
		long now = getSetNo(orderSet);
		if(now>before) {
			return false;
		}
		return true;
	}
}

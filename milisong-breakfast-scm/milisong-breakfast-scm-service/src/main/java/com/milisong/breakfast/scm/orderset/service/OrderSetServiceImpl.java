package com.milisong.breakfast.scm.orderset.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.milisong.breakfast.scm.configuration.dto.ShopInterceptConfigDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.util.BeanMapper;
import com.milisong.scm.base.api.OperationLogService;
import com.milisong.scm.base.constant.OperationModularEnum;
import com.milisong.scm.base.constant.OperationTypeEnum;
import com.milisong.scm.base.dto.mq.OperationLogDto;
import com.milisong.scm.base.utils.CompareDifferenceUtil;
import com.milisong.breakfast.scm.common.util.DateUtil;
import com.milisong.breakfast.scm.order.api.OrderService;
import com.milisong.breakfast.scm.order.dto.param.OrderUpdateStatusParam;
import com.milisong.breakfast.scm.order.dto.result.OrderDetailResult;
import com.milisong.breakfast.scm.orderset.api.OrderSetService;
import com.milisong.breakfast.scm.orderset.constant.OrderSetActionEnum;
import com.milisong.breakfast.scm.orderset.constant.OrderSetStatusEnum;
import com.milisong.breakfast.scm.orderset.domain.OrderSetDetail;
import com.milisong.breakfast.scm.orderset.domain.OrderSetDetailExample;
import com.milisong.breakfast.scm.orderset.domain.OrderSetDetailExample.Criteria;
import com.milisong.breakfast.scm.orderset.domain.OrderSetDetailGoods;
import com.milisong.breakfast.scm.orderset.domain.OrderSetDetailGoodsExample;
import com.milisong.breakfast.scm.orderset.dto.param.OrderSetSearchParam;
import com.milisong.breakfast.scm.orderset.dto.param.UpdateOrderSetStatusParam;
import com.milisong.breakfast.scm.orderset.dto.result.NotifyOrderSetQueryResult;
import com.milisong.breakfast.scm.orderset.dto.result.OrderSetDetailDto;
import com.milisong.breakfast.scm.orderset.dto.result.OrderSetDetailGoodsDto;
import com.milisong.breakfast.scm.orderset.dto.result.OrderSetDetailResultDto;
import com.milisong.breakfast.scm.orderset.dto.result.OrderSetProductionMsgByPrint;
import com.milisong.breakfast.scm.orderset.dto.result.OrdersetInfoResult;
import com.milisong.breakfast.scm.orderset.mapper.OrderSetDetailExtMapper;
import com.milisong.breakfast.scm.orderset.mapper.OrderSetDetailGoodsExtMapper;
import com.milisong.breakfast.scm.orderset.mapper.OrderSetDetailGoodsMapper;
import com.milisong.breakfast.scm.orderset.mapper.OrderSetDetailMapper;
import com.milisong.breakfast.scm.orderset.mq.MqProducerOrdersetUtil;
import com.milisong.breakfast.scm.orderset.mq.OrderSetOrderStatusChangeMsg;
import com.milisong.breakfast.scm.orderset.mq.OrderSetProductionMsg;
import com.milisong.breakfast.scm.orderset.result.OrderSetDetailStatusDto;
import com.milisong.dms.constant.OrderDeliveryStatus;
import com.milisong.dms.dto.shunfeng.DeliveryOrderMqDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderSetServiceImpl implements OrderSetService {
	@Autowired
	private OrderSetDetailMapper orderSetDetailMapper;
	@Autowired
	private OrderSetDetailGoodsMapper orderSetDetailGoodsMapper;
	@Autowired
	private OrderSetDetailGoodsExtMapper orderSetDetailGoodsExtMapper;
	@Autowired
	private OrderSetDetailExtMapper orderSetDetailExtMapper;
	
	@Autowired
	private OrderService orderService;
	@Autowired
	private OperationLogService operationLogService;
	
	/**
	 * 分页查询集单
	 */
	@Override
	public Pagination<OrderSetDetailResultDto> pageSearch(OrderSetSearchParam param) {
		log.info("分页条件查询集单信息{}",JSON.toJSONString(param));
		Pagination<OrderSetDetailResultDto> result = new Pagination<>();
		
		OrderSetDetailExample example = new OrderSetDetailExample();
		OrderSetDetailExample.Criteria criteria = example.createCriteria();
		if(null != param.getShopId()) {
			criteria.andShopIdEqualTo(param.getShopId());
		}
		if(StringUtils.isNotBlank(param.getCompanyName())) {
			criteria.andCompanyAbbreviationLike("%".concat(param.getCompanyName()).concat("%"));
		}
		if(null != param.getCompanyId()) {
			criteria.andCompanyIdEqualTo(param.getCompanyId());
		}
		if(StringUtils.isNotBlank(param.getBuildingName())) {
			criteria.andBuildingAbbreviationLike("%".concat(param.getBuildingName()).concat("%"));
		}
		if(StringUtils.isNotBlank(param.getDate())) {
			criteria.andDetailDeliveryDateEqualTo(DateUtil.parseDate(param.getDate(), DateUtil.YYYY_MM_DD));
		}
		
		long count = orderSetDetailMapper.countByExample(example);
		result.setTotalCount(count);
		
		if(count >0) {
			example.setOrderByClause(" detail_set_no asc ");
			example.setPageSize(param.getPageSize());
			example.setPageSize(param.getPageSize());
			
			List<OrderSetDetail> list = orderSetDetailMapper.selectByExample(example);
			if(CollectionUtils.isEmpty(list)) {
				result.setDataList(Collections.emptyList());
			} else {
				List<OrderSetDetailResultDto> data = BeanMapper.mapList(list, OrderSetDetailResultDto.class);
				// 处理顾客联数
				processCustomerNumber(data);
				
				result.setDataList(data);
			}
		}

		return result;
	}
	
	/**
	 * 根据集单号查询集单的详情
	 * @param setNo
	 * @return
	 */
	@Override
	public List<OrdersetInfoResult> getDetailByOrderSetNo(String setNo) {
		log.info("根据集单号{}查询集单详情",setNo);
		List<OrdersetInfoResult>  listResult = orderSetDetailExtMapper.getCustomerOrderByOrderSetNo(setNo);
		Map<String,OrdersetInfoResult> mapResult = new TreeMap<String,OrdersetInfoResult>();
		for (OrdersetInfoResult OrdersetInfoResult : listResult) {
			mapResult.put(OrdersetInfoResult.getCoupletNo(), OrdersetInfoResult);
		}
		for (OrdersetInfoResult OrdersetInfoResult : listResult) {
			OrdersetInfoResult detail = mapResult.get(OrdersetInfoResult.getCoupletNo());
			if(detail != null) {
				OrderSetDetailGoodsDto goodsInfo = new OrderSetDetailGoodsDto();
				goodsInfo.setGoodsName(OrdersetInfoResult.getGoodsName());
				goodsInfo.setGoodsNumber(OrdersetInfoResult.getGoodsNumber());
				List<OrderSetDetailGoodsDto>  listGoods = detail.getGoodsInfo();
				if (CollectionUtils.isEmpty(listGoods)) {
					listGoods = new ArrayList<OrderSetDetailGoodsDto>();
				}
				listGoods.add(goodsInfo);
				detail.setGoodsInfo(listGoods);
			}
		}
		listResult.clear();
		listResult.addAll(mapResult.values());
		return listResult;
	}

	/**
	 * 更改集单的状态
	 * 
	 * @param param
	 */
	@Override
	@Transactional
	public ResponseResult<Object> updateStatus(UpdateOrderSetStatusParam param) {
		log.info("修改子集单的状态，入参：{}", JSONObject.toJSONString(param));
		ResponseResult<Object> result = ResponseResult.buildSuccessResponse();
		
		if (null != param.getSfDto()) {
			param.setDetailSetId(Long.valueOf(param.getSfDto().getDetailSetId()));
		}
		// 数据库中的集单
		OrderSetDetail orderSetDetail = orderSetDetailMapper.selectByPrimaryKey(param.getDetailSetId());
		if(null == orderSetDetail) {
			result.setSuccess(false);
			result.setMessage("操作的记录不存在");
			log.warn("集单id：{}未查询到数据", param.getDetailSetId());
			return result;
		}
		
		// 原来的集单
		OrderSetDetail oldOrderSetDetail = new OrderSetDetail();
		BeanUtils.copyProperties(orderSetDetail, oldOrderSetDetail);
		
		orderSetDetail.setUpdateBy(param.getUpdateBy());
		orderSetDetail.setUpdateTime(null);
		
		OrderSetDetailExample example = new OrderSetDetailExample();
		Criteria criteria = example.createCriteria().andIdEqualTo(param.getDetailSetId()).andIsDeletedEqualTo(false);

		if (null != param.getSfDto()) {
			param.setDetailSetId(Long.valueOf(param.getSfDto().getDetailSetId()));
			
			orderSetDetail.setDistributionStatus(param.getSfDto().getStatus());

			if (OrderDeliveryStatus.DISPATCHED_ORDER.getValue().equals(param.getSfDto().getStatus())
					|| OrderDeliveryStatus.RECEIVED_ORDER.getValue().equals(param.getSfDto().getStatus())
					|| OrderDeliveryStatus.ARRIVED_SHOP.getValue().equals(param.getSfDto().getStatus())) {
				// 已打包
				orderSetDetail.setStatus(OrderSetStatusEnum.ALREADY_PACKED.getCode());
			} else if (OrderDeliveryStatus.COMPLETED.getValue().equals(param.getSfDto().getStatus())) {
				// 已经送达
				orderSetDetail.setStatus(OrderSetStatusEnum.NOTIFIED.getCode());
			} else if (OrderDeliveryStatus.RECEIVED_GOODS.getValue().equals(param.getSfDto().getStatus())) {
				// 已取件配送
				orderSetDetail.setStatus(OrderSetStatusEnum.IN_DISTRIBUTION.getCode());
			}
			criteria.andStatusEqualTo(oldOrderSetDetail.getStatus());
			criteria.andDistributionStatusEqualTo(oldOrderSetDetail.getDistributionStatus());
		} else {
			// 兼容老数据接口
			
			Byte oldStatus = null;
			if (param.getAction().equals(OrderSetActionEnum.BALE.getCode())) {
				// 打包操作
				oldStatus = OrderSetStatusEnum.WAITING_FOR_PACKAGING.getCode();
				orderSetDetail.setStatus(OrderSetStatusEnum.ALREADY_PACKED.getCode());
			} else if (param.getAction().equals(OrderSetActionEnum.DELIVERY.getCode())) {
				// 交配送
				oldStatus = OrderSetStatusEnum.ALREADY_PACKED.getCode();
				orderSetDetail.setStatus(OrderSetStatusEnum.IN_DISTRIBUTION.getCode());
			} else if (param.getAction().equals(OrderSetActionEnum.NOTIFY.getCode())) {
				// 通知客户
				oldStatus = OrderSetStatusEnum.IN_DISTRIBUTION.getCode();
				orderSetDetail.setStatus(OrderSetStatusEnum.NOTIFIED.getCode());
			} else {
				result.setSuccess(false);
				result.setMessage("非法的操作类型");
				return result;
			}
			criteria.andStatusEqualTo(oldStatus);
		}

		int ct = orderSetDetailMapper.updateByExampleSelective(orderSetDetail, example);
		if (ct == 0) {
			result.setSuccess(false);
			result.setMessage("非法操作，状态不一致");
			log.warn("集单id：{}状态不一致", param.getDetailSetId());
			return result;
		}

		// 发送操作日志
		sendOpLog(param, oldOrderSetDetail, orderSetDetail);

		OrderSetDetailGoodsExample goodsExample = new OrderSetDetailGoodsExample();
		goodsExample.createCriteria().andDetailSetNoEqualTo(orderSetDetail.getDetailSetNo())
				.andIsDeletedEqualTo(false);
		List<OrderSetDetailGoods> list = orderSetDetailGoodsMapper.selectByExample(goodsExample);
		if (!CollectionUtils.isEmpty(list)) {
			Set<String> orderNoSet = list.stream().map(item -> item.getOrderNo()).collect(Collectors.toSet());
			log.info("集单号{}里共包含了如下订单号:{}", orderSetDetail.getDetailSetNo(), JSONObject.toJSONString(orderNoSet));
			orderNoSet.stream().forEach(orderNo -> {
				// 查询订单号里的所有的商品的状态
				List<OrderSetDetailStatusDto> statusList = orderSetDetailGoodsExtMapper
						.listAllStatusByOrderNo(orderNo);
				log.info("订单号：{}的状态list：{}", orderNo, JSONObject.toJSONString(statusList));
				if (!CollectionUtils.isEmpty(statusList)) {
					OrderSetDetailStatusDto dto = statusList.get(0);

					if(!OrderDeliveryStatus.INIT.getValue().equals(dto.getDistributionStatus())) {
						// 更定订单的配送状态
						OrderUpdateStatusParam orderStatusParam = new OrderUpdateStatusParam();
						orderStatusParam.setOrderNo(orderNo);
						orderStatusParam.setDeliveryStatus(dto.getDistributionStatus());
						orderService.updateStatus(orderStatusParam);

						log.info("订单号：{}里的商品状态发生了变更，准备发送MQ", orderNo);
						OrderSetOrderStatusChangeMsg msg = new OrderSetOrderStatusChangeMsg();
						msg.setDate(new Date());
						msg.setOrderNo(orderNo);
						msg.setStatus(dto.getStatus());
						msg.setSfStatus(dto.getDistributionStatus());
						MqProducerOrdersetUtil.sendOrderSetOrderStatusMsg(msg);
					}
				}
			});
		}

		return result;
	}

	@Override
	public NotifyOrderSetQueryResult getOrderSetInfoByDetailSetNo(String detailSetNo, Long detailSetId) {
		if (StringUtils.isBlank(detailSetNo) && null == detailSetId) {
			return null;
		}
		OrderSetDetailExample detailExample = new OrderSetDetailExample();
		Criteria criteria = detailExample.createCriteria().andIsDeletedEqualTo(false);
		if (StringUtils.isNotBlank(detailSetNo)) {
			criteria.andDetailSetNoEqualTo(detailSetNo);
		}
		if (null != detailSetId) {
			criteria.andIdEqualTo(detailSetId);
		}
		List<OrderSetDetail> detailList = orderSetDetailMapper.selectByExample(detailExample);
		if (CollectionUtils.isEmpty(detailList)) {
			return null;
		}
		detailSetNo = detailList.get(0).getDetailSetNo();

		OrderSetDetailGoodsExample goodsExample = new OrderSetDetailGoodsExample();
		goodsExample.createCriteria().andDetailSetNoEqualTo(detailSetNo).andIsDeletedEqualTo(false);
		List<OrderSetDetailGoods> goodsList = orderSetDetailGoodsMapper.selectByExample(goodsExample);

		NotifyOrderSetQueryResult result = new NotifyOrderSetQueryResult();

		if (!CollectionUtils.isEmpty(goodsList)) {
			List<OrderDetailResult> orderDetailList = orderService.getOrderDetailInfoByOrderNo(
					goodsList.stream().map(item -> item.getOrderNo()).collect(Collectors.toList()));
			result.setListOrderDetails(orderDetailList);
		}
		result.setOrderSet(BeanMapper.map(detailList.get(0), OrderSetDetailDto.class));
		result.setGoodsList(BeanMapper.mapList(goodsList, OrderSetDetailGoodsDto.class));
		return result;
	}

	/**
	 * 更新集单的配送状态
	 * 
	 * @param param
	 */
	@Override
	@Transactional
	public void updateDistributionStatus(DeliveryOrderMqDto param) {
		UpdateOrderSetStatusParam dto = new UpdateOrderSetStatusParam();
		dto.setSfDto(param);
		dto.setUpdateBy("sf_顺丰");
		this.updateStatus(dto);
	}

	/**
	 * 发送门店的集单信息给POS系统进行生产
	 * 
	 * @param shopId
	 * @param isCompensate 是否补偿
	 */
	@Override
	public void sendOrderSetMq(Long shopId, boolean isCompensate) {
		int pageSize = 1000;

		OrderSetDetailExample example = new OrderSetDetailExample();
		// 补偿就查当天全部集单数据
		if(isCompensate) {
			example.createCriteria().andShopIdEqualTo(shopId).andDetailDeliveryDateEqualTo(DateUtil.getDayBeginTime(new Date())).andIsPushEqualTo(Boolean.FALSE);
		} else {
			example.createCriteria().andShopIdEqualTo(shopId).andIsPushEqualTo(Boolean.FALSE)
				.andDetailDeliveryDateEqualTo(DateUtil.getDayBeginTime(new Date()));
		}
		
		// 门店总集单数
		long count = orderSetDetailMapper.countByExample(example);
		if (count > 0) {
			example.setPageSize(pageSize);
			example.setStartRow(0);
			example.setOrderByClause(" detail_set_no asc ");
			List<OrderSetDetail> list = orderSetDetailMapper.selectByExample(example);
			while (!CollectionUtils.isEmpty(list)) {
				processOrderSetMq(list, shopId);
				list = orderSetDetailMapper.selectByExample(example);
			}
		}
	}

	@Override
	public List<OrderSetDetailDto> getOrderSetByOrderNo(String orderNo) {

		List<OrderSetDetailDto> orderSetDetailDtoList = new ArrayList<>();
		OrderSetDetailGoodsExample goodsExample = new OrderSetDetailGoodsExample();
		goodsExample.createCriteria().andOrderNoEqualTo(orderNo);
		List<OrderSetDetailGoods> orderSetDetailGoods = orderSetDetailGoodsMapper.selectByExample(goodsExample);
		if (orderSetDetailGoods.size() > 0) {
			orderSetDetailGoods.stream().forEach(orderSetGoods -> {
				String detailSetNo = orderSetGoods.getDetailSetNo();
				OrderSetDetailExample example = new OrderSetDetailExample();
				example.createCriteria().andDetailSetNoEqualTo(detailSetNo);
				List<OrderSetDetail> orderSetDetails = orderSetDetailMapper.selectByExample(example);
				OrderSetDetail orderSetDetail = orderSetDetails.get(0);
				OrderSetDetailDto orderSetDetailDto = BeanMapper.map(orderSetDetail, OrderSetDetailDto.class);
				orderSetDetailDtoList.add(orderSetDetailDto);
			});
		}
		return orderSetDetailDtoList;
	}
	/**
	 * 查詢需要打印的集单信息
	 */
	@Override
	public OrderSetProductionMsgByPrint getOrderSetMqBySetNo(String setNo) {
		OrderSetProductionMsgByPrint result = new OrderSetProductionMsgByPrint();
		OrderSetDetailExample example = new OrderSetDetailExample();
		example.createCriteria().andDetailSetNoEqualTo(setNo);
		List<OrderSetDetail> orderSetDetailList = orderSetDetailMapper.selectByExample(example);
		if(CollectionUtils.isEmpty(orderSetDetailList))	{
			return null;
		}
		result.setOrderSet(BeanMapper.map(orderSetDetailList.get(0), OrderSetDetailDto.class));
		OrderSetDetailGoodsExample exampleGoods = new OrderSetDetailGoodsExample();
		exampleGoods.createCriteria().andDetailSetNoEqualTo(setNo);
		List<OrderSetDetailGoods> listGoods = orderSetDetailGoodsMapper.selectByExample(exampleGoods);
		if(CollectionUtils.isEmpty(listGoods)) {
			return null;
		}
		result.setGoods(BeanMapper.mapList(listGoods, OrderSetDetailGoodsDto.class));
		return result;
	}

	@Override
	public void pushSfOrderSetMq(ShopInterceptConfigDto configDto) {
		OrderSetDetailExample example = new OrderSetDetailExample();
		// 查当天集单数据
		example.createCriteria().andShopIdEqualTo(configDto.getShopId()).
				andDistributionStatusEqualTo((byte)-1).
				andDetailDeliveryDateEqualTo(DateUtil.getDayBeginTime(new Date())).
				andDeliveryStartTimeEqualTo(configDto.getDeliveryTimeBegin()).
				andDeliveryEndTimeEqualTo(configDto.getDeliveryTimeEnd());
		List<OrderSetDetail> orderSetDetails = orderSetDetailMapper.selectByExample(example);
		// 发送通知顺丰派单的MQ
		processSfOrderSetMq(orderSetDetails);
	}

	private void processSfOrderSetMq(List<OrderSetDetail> list) {
		OrderSetProductionMsg msg = new OrderSetProductionMsg();
		list.stream().forEach(item -> {
			OrderSetDetailGoodsExample example = new OrderSetDetailGoodsExample();
			example.createCriteria().andDetailSetNoEqualTo(item.getDetailSetNo());
			List<OrderSetDetailGoods> details = orderSetDetailGoodsMapper.selectByExample(example);

			msg.setOrderSet(BeanMapper.map(item, OrderSetDetailDto.class));
			if (!CollectionUtils.isEmpty(details)) {
				msg.setGoods(BeanMapper.mapList(details, OrderSetDetailGoodsDto.class));
			}

			MqProducerOrdersetUtil.sendSfOrderSet(msg);
		});
	}

	/**
	 * 处理一批集单发送
	 * 
	 * @param list
	 * @param shopId
	 */
	private void processOrderSetMq(List<OrderSetDetail> list, Long shopId) {
		OrderSetProductionMsg msg = new OrderSetProductionMsg();
		list.stream().forEach(item -> {
			OrderSetDetailGoodsExample example = new OrderSetDetailGoodsExample();
			example.createCriteria().andDetailSetNoEqualTo(item.getDetailSetNo());
			List<OrderSetDetailGoods> details = orderSetDetailGoodsMapper.selectByExample(example);

			msg.setOrderSet(BeanMapper.map(item, OrderSetDetailDto.class));
			if (!CollectionUtils.isEmpty(details)) {
				msg.setGoods(BeanMapper.mapList(details, OrderSetDetailGoodsDto.class));
			}

			item.setIsPush(Boolean.TRUE);
			item.setUpdateTime(null);
			orderSetDetailMapper.updateByPrimaryKeySelective(item);

			MqProducerOrdersetUtil.sendOrderSet(msg);
		});
	}

	/**
	 * 记录操作日志
	 * 
	 * @param param
	 * @param oldOrderSetDetail
	 * @param orderSetDetail
	 */
	private void sendOpLog(UpdateOrderSetStatusParam param, OrderSetDetail oldOrderSetDetail,
			OrderSetDetail newOrderSetDetail) {
		OperationLogDto opLog = OperationLogDto.builder().beforeData(JSONObject.toJSONString(oldOrderSetDetail))
				.afterData(JSONObject.toJSONString(newOrderSetDetail))
				.diffData(CompareDifferenceUtil.compare(oldOrderSetDetail, newOrderSetDetail))
				.operationUser(param.getUpdateBy()).bussinessId(oldOrderSetDetail.getDetailSetNo())
				.modular(OperationModularEnum.ORDER_SET_BF.getCode()).operationType(OperationTypeEnum.UPDATE.getCode())
				.operationTime(new Date()).build();
		opLog.setOperationResume(OrderDeliveryStatus.getNameByValue(newOrderSetDetail.getDistributionStatus()));
		operationLogService.sendMq(opLog);
	}
	
	/**
	 * 处理用户联数
	 * @param data
	 */
	private void processCustomerNumber(List<OrderSetDetailResultDto> data){
		List<String> list = data.stream().map(item -> item.getDetailSetNo()).collect(Collectors.toList());
		
		Map<String,Integer> resultMap = new HashMap<String, Integer>();
		if(CollectionUtils.isEmpty(list)) {
			return;
		}
				
		List<Map<String, Object>> listMap =	orderSetDetailExtMapper.selectCountCustomerOrderBySetNo(list);
		for (Map<String, Object> map : listMap) {
			resultMap.put(map.get("detail_set_no").toString(), Integer.parseInt(map.get("coupletNo").toString()));
		}
		data.stream().forEach(item -> {
			item.setCustomerSum(resultMap.get(item.getDetailSetNo()));
		});
	}

	
}

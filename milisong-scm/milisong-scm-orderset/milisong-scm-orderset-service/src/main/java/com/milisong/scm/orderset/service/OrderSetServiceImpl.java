package com.milisong.scm.orderset.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.farmland.core.db.IdGenerator;
import com.farmland.core.util.BeanMapper;
import com.milisong.scm.base.api.OperationLogService;
import com.milisong.scm.base.constant.OperationModularEnum;
import com.milisong.scm.base.constant.OperationTypeEnum;
import com.milisong.scm.base.dto.mq.OperationLogDto;
import com.milisong.scm.base.utils.CompareDifferenceUtil;
import com.milisong.scm.orderset.api.OrderService;
import com.milisong.scm.orderset.api.OrderSetService;
import com.milisong.scm.orderset.constant.ErrorEnum;
import com.milisong.scm.orderset.constant.LogisticsDeliveryStatus;
import com.milisong.scm.orderset.constant.NoPrefixEnum;
import com.milisong.scm.orderset.constant.OrderSetActionEnum;
import com.milisong.scm.orderset.constant.OrderSetStatusEnum;
import com.milisong.scm.orderset.constant.PrintTypeEnum;
import com.milisong.scm.orderset.domain.Distribution;
import com.milisong.scm.orderset.domain.DistributionExample;
import com.milisong.scm.orderset.domain.DistributionOrderset;
import com.milisong.scm.orderset.domain.DistributionOrdersetExample;
import com.milisong.scm.orderset.domain.OrderSetDetail;
import com.milisong.scm.orderset.domain.OrderSetDetailExample;
import com.milisong.scm.orderset.domain.OrderSetDetailExample.Criteria;
import com.milisong.scm.orderset.domain.OrderSetDetailGoods;
import com.milisong.scm.orderset.domain.OrderSetDetailGoodsExample;
import com.milisong.scm.orderset.dto.mq.SfOrderMqDto;
import com.milisong.scm.orderset.dto.param.InterceptConfigDto;
import com.milisong.scm.orderset.dto.param.OrderSearch4OrderSetParam;
import com.milisong.scm.orderset.dto.param.OrderSetSearchParam;
import com.milisong.scm.orderset.dto.param.OrderUpdateStatusParam;
import com.milisong.scm.orderset.dto.param.UpdateOrderSetStatusParam;
import com.milisong.scm.orderset.dto.result.NotifyOrderSetQueryResult;
import com.milisong.scm.orderset.dto.result.OrderCompanyResult;
import com.milisong.scm.orderset.dto.result.OrderDetailResult;
import com.milisong.scm.orderset.dto.result.OrderResult;
import com.milisong.scm.orderset.dto.result.OrderSetDetailDto;
import com.milisong.scm.orderset.dto.result.OrderSetDetailGoodsDto;
import com.milisong.scm.orderset.dto.result.OrderSetSearchResult;
import com.milisong.scm.orderset.dto.result.OrderSetSearchResultOrder;
import com.milisong.scm.orderset.dto.result.OrderSetStatusQueryResult;
import com.milisong.scm.orderset.dto.result.OrderSetStatusQueryResultItem;
import com.milisong.scm.orderset.dto.result.OrderSetStatusQueryResultItemGoods;
import com.milisong.scm.orderset.mapper.DistributionMapper;
import com.milisong.scm.orderset.mapper.DistributionOrdersetMapper;
import com.milisong.scm.orderset.mapper.OrderSetDetailGoodsExtMapper;
import com.milisong.scm.orderset.mapper.OrderSetDetailGoodsMapper;
import com.milisong.scm.orderset.mapper.OrderSetDetailMapper;
import com.milisong.scm.orderset.mq.OrderSetOrderStatusChangeMsg;
import com.milisong.scm.orderset.mq.OrderSetProductionMsg;
import com.milisong.scm.orderset.result.OrderSetDetailStatusDto;
import com.milisong.scm.orderset.util.ConfigRedisUtil;
import com.milisong.scm.orderset.util.DateUtil;
import com.milisong.scm.orderset.util.MqProducerUtil;
import com.milisong.scm.shop.api.BuildingService;
import com.milisong.scm.shop.dto.building.BuildingDto;
import com.milisong.scm.shop.param.BuildingParam;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderSetServiceImpl implements OrderSetService {
	@Autowired
	@Qualifier(value = "orderService")
	private OrderService orderService;
	@Autowired(required = false)
	private BuildingService buildingService;
	@Autowired
	private OrderSetDetailMapper orderSetDetailMapper;
	@Autowired
	private OrderSetDetailGoodsMapper orderSetDetailGoodsMapper;
	@Autowired
	private OrderSetDetailGoodsExtMapper orderSetDetailGoodsExtMapper;
	@Autowired
	private DistributionMapper distributionMapper;
	@Autowired
	private DistributionOrdersetMapper distributionOrdersetMapper;
	@Autowired
	private OperationLogService operationLogService;
	
	// 楼宇集单可打包的标识
	private static final String ORDERSET_FLAG = "orderset_flag_building:";

	/**
	 * 针对某个门店做集单处理
	 */
	@Override
	@Transactional
	public void executeShopSet(Long shopId, InterceptConfigDto config) {
		BuildingParam param = new BuildingParam();
		param.setShopId(shopId);
		param.setPageNo(1);
		param.setPageSize(50);

		// 查询门店负责的楼宇信息list
		Pagination<BuildingDto> buildingList = buildingService.getBuildingPageByShopId(param);

		while (!CollectionUtils.isEmpty(buildingList.getDataList())) {

			processPageBuilding(buildingList.getDataList(), config, shopId);

			param.setPageNo(param.getPageNo() + 1);
			buildingList = buildingService.getBuildingPageByShopId(param);
		}
	}

	/**
	 * 根据订单号查询集单的处理状态
	 * 
	 * @param orderNo
	 */
	@Override
	public OrderSetStatusQueryResult queryStatusByOrderNo(String orderNo) {
		OrderSetStatusQueryResult result = OrderSetStatusQueryResult.builder().orderNo(orderNo).build();

		// 查询该订单共被拆成了几个子集单
		List<String> detailSetNoList = orderSetDetailGoodsExtMapper.listDetailSetNoByOrderNo(orderNo);
		if (CollectionUtils.isEmpty(detailSetNoList)) {
			// 没有数据说明还没生成集单信息
			return result;
		}

		// 查询子集单信息
		OrderSetDetailExample example = new OrderSetDetailExample();
		example.createCriteria().andDetailSetNoIn(detailSetNoList).andIsDeletedEqualTo(false);
		example.setOrderByClause(" detail_set_no asc ");
		List<OrderSetDetail> list = orderSetDetailMapper.selectByExample(example);

		List<OrderSetStatusQueryResultItem> details = new ArrayList<>(list.size());
		// 循环子集单
		list.stream().forEach(item -> {
			OrderSetStatusQueryResultItem detail = OrderSetStatusQueryResultItem.builder()
					.orderSetDetailNo(item.getDetailSetNo()).orderSetNo(item.getSetNo()).status(item.getStatus())
					.goods(new ArrayList<>()).build();

			OrderSetDetailGoodsExample goodsExample = new OrderSetDetailGoodsExample();
			OrderSetDetailGoodsExample.Criteria goodsCriteria = goodsExample.createCriteria();
			goodsCriteria.andDetailSetNoEqualTo(item.getDetailSetNo()).andIsDeletedEqualTo(false);
			List<OrderSetDetailGoods> goods = orderSetDetailGoodsMapper.selectByExample(goodsExample);
			detail.setGoods(BeanMapper.mapList(goods, OrderSetStatusQueryResultItemGoods.class));

			details.add(detail);
		});

		result.setDetail(details);

		return result;
	}

	/**
	 * 分页查询集单信息
	 * 
	 * @param param
	 * @return
	 */
	@Override
	public Pagination<OrderSetSearchResult> pageSearch(OrderSetSearchParam param) {
		OrderSetDetailExample example = new OrderSetDetailExample();
		Criteria criteria = example.createCriteria();

		if (StringUtils.isNotBlank(param.getDate())) {
			try {
				Date deliveryDate = DateUtils.parseDate(param.getDate(), "yyyy-MM-dd");
				criteria.andDetailDeliveryDateEqualTo(deliveryDate);
			} catch (ParseException e) {
				log.error("日期格式化错误", e);
				throw new RuntimeException(e);
			}
		}

		Pagination<OrderSetSearchResult> result = new Pagination<>();

		if (null != param.getShopId()) {
			criteria.andShopIdEqualTo(param.getShopId());
		}
		if (!CollectionUtils.isEmpty(param.getOrderSetNo())) {
			criteria.andDetailSetNoIn(param.getOrderSetNo());
		}

		long count = orderSetDetailMapper.countByExample(example);
		result.setTotalCount(count);
		result.setPageNo(param.getPageNo());
		result.setPageSize(param.getPageSize());

		if (count > 0) {
			example.setOrderByClause(" create_time asc ");
			example.setPageSize(param.getPageSize());
			example.setStartRow(param.getStartRow());

			List<OrderSetDetail> list = orderSetDetailMapper.selectByExample(example);
			if (!CollectionUtils.isEmpty(list)) {
				List<OrderSetSearchResult> dataList = new ArrayList<>(list.size());

				list.stream().forEach(item -> {
					// 查询订单商品信息
					OrderSetDetailGoodsExample goodsExample = new OrderSetDetailGoodsExample();
					OrderSetDetailGoodsExample.Criteria goodsCriteria = goodsExample.createCriteria();
					goodsCriteria.andDetailSetNoEqualTo(item.getDetailSetNo()).andIsDeletedEqualTo(false);
					List<OrderSetDetailGoods> goods = orderSetDetailGoodsMapper.selectByExample(goodsExample);

					OrderSetSearchResult data = OrderSetSearchResult.builder().buildingId(item.getBuildingId())
							.detailSetId(item.getId()).detailDeliveryAddress(item.getDetailDeliveryAddress())
							.detailDeliveryDate(item.getDetailDeliveryDate()).detailSetNo(item.getDetailSetNo())
							.detailSetNoDescription(item.getDetailSetNoDescription())
							.orderList(BeanMapper.mapList(goods, OrderSetSearchResultOrder.class))
							// .setNo(item.getSetNo())
							.shopId(item.getShopId()).status(item.getStatus())
							.buildingAbbreviation(item.getBuildingAbbreviation()).deliveryFloor(item.getDeliveryFloor())
							.companyAbbreviation(item.getCompanyAbbreviation()).isPrint(item.getIsPrint())
							.goodsSum(item.getGoodsSum()).build();
					dataList.add(data);
				});
				result.setDataList(dataList);
			}
		} else {
			result.setDataList(Collections.emptyList());
		}

		return result;
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

			if (LogisticsDeliveryStatus.DISPATCHED_ORDER.getValue().equals(param.getSfDto().getStatus())
					|| LogisticsDeliveryStatus.RECEIVED_ORDER.getValue().equals(param.getSfDto().getStatus())
					|| LogisticsDeliveryStatus.ARRIVED_SHOP.getValue().equals(param.getSfDto().getStatus())) {
				// 已打包
				orderSetDetail.setStatus(OrderSetStatusEnum.ALREADY_PACKED.getCode());
			} else if (LogisticsDeliveryStatus.COMPLETED.getValue().equals(param.getSfDto().getStatus())) {
				// 已经送达
				orderSetDetail.setStatus(OrderSetStatusEnum.NOTIFIED.getCode());
			} else if (LogisticsDeliveryStatus.RECEIVED_GOODS.getValue().equals(param.getSfDto().getStatus())) {
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

					if(!LogisticsDeliveryStatus.INIT.getValue().equals(dto.getDistributionStatus())) {
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
						MqProducerUtil.sendOrderSetOrderStatusMsg(msg);
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

		DistributionOrdersetExample example = new DistributionOrdersetExample();
		example.createCriteria().andDetailSetNoEqualTo(detailSetNo);
		List<DistributionOrderset> distributionOrderset = distributionOrdersetMapper.selectByExample(example);
		if (!CollectionUtils.isEmpty(distributionOrderset)) {
			result.setDistributionNo(distributionOrderset.get(0).getDistributionNo());
		}
		return result;
	}

	@Override
	public ResponseResult<Object> updatePrintStatus(UpdateOrderSetStatusParam param) {
		log.info("记录打印日志入参:{}", JSONObject.toJSONString(param));
		String setNo = param.getSetNo();
		Integer printType = param.getPrintType();
		OperationLogDto operation = new OperationLogDto();
		String updateBy = param.getUpdateBy();
		operation.setModular(OperationModularEnum.ORDER_SET_LUNCH.getCode());
		operation.setOperationTime(new Date());
		operation.setOperationType("update");
		operation.setOperationUser(param.getUpdateBy());
		operation.setBussinessId(setNo);
		operation.setOperationResume(PrintTypeEnum.getDescByCode(printType) + "日志");
		String diffData = null;
		int row = 0;
		boolean result = false;
		switch (printType) {
		case 1:
			// 拣货单
			result = updateDistributionPrint(setNo, printType, operation, updateBy);
			break;
		case 2:
			// 配送单
			result = updateDistributionPrint(setNo, printType, operation, updateBy);
			break;
		case 3:
			// 集单
			// 获取子集单信息
			OrderSetDetailExample orderSetDetailExample = new OrderSetDetailExample();
			orderSetDetailExample.createCriteria().andDetailSetNoEqualTo(setNo);
			List<OrderSetDetail> listResult = orderSetDetailMapper.selectByExample(orderSetDetailExample);
			if (CollectionUtils.isEmpty(listResult)) {
				return ResponseResult.buildFailResponse(ErrorEnum.NOT_FOND_RESULT.getCode(),
						ErrorEnum.NOT_FOND_RESULT.getDesc());
			}
			OrderSetDetail orderSetDetailA = listResult.get(0);
			if (!orderSetDetailA.getIsPrint()) {
				OrderSetDetail orderSetDetail = BeanMapper.map(orderSetDetailA, OrderSetDetail.class);
				orderSetDetail.setIsPrint(true);
				orderSetDetail.setUpdateBy(updateBy);
				// CompareDifferenceUtil.isDifference(orderSetInfo, orderSetDetail);
				diffData = CompareDifferenceUtil.compare(orderSetDetailA, orderSetDetail);
				operation.setDiffData(diffData);
				operation.setBeforeData(JSONObject.toJSONString(orderSetDetailA));
				operation.setAfterData(JSONObject.toJSONString(orderSetDetail));
				// 修改集单打印状态
				OrderSetDetailExample example = new OrderSetDetailExample();
				example.createCriteria().andDetailSetNoEqualTo(setNo).andIsDeletedEqualTo(false);
				row = orderSetDetailMapper.updateByExample(orderSetDetail, example);
				result = true;
				log.info("修改集单打印状态，受影响行数:[{}]", row);
			}
			break;
		default:
			break;
		}
		if (!result) {
			log.info("操作失败,集单已经打印过小票");
			// return ResponseResult.buildFailResponse();
		}
		operationLogService.sendMq(operation);
		return ResponseResult.buildSuccessResponse();
	}

	@Override
	@Transactional
	public ResponseResult<Object> updateStatusByIds(List<Long> detailIds, String updateBy, Integer status) {
		for (Long long1 : detailIds) {
			UpdateOrderSetStatusParam param = new UpdateOrderSetStatusParam();
			param.setDetailSetId(long1);
			param.setAction(status);
			param.setUpdateBy(updateBy);
			updateStatus(param);
		}
		return null;
	}

	/**
	 * 更新集单的配送状态
	 * 
	 * @param param
	 */
	@Override
	@Transactional
	public void updateDistributionStatus(SfOrderMqDto param) {
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
		int pageSize = 50;

		OrderSetDetailExample example = new OrderSetDetailExample();
		// 补偿就查当天全部集单数据
		if(isCompensate) {
			example.createCriteria().andShopIdEqualTo(shopId).andDetailDeliveryDateEqualTo(DateUtil.getDayBeginTime(new Date()));
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

			MqProducerUtil.sendOrderSet(msg);
		});
	}

	/**
	 * @param setNo
	 * @param printType
	 * @param operation
	 */
	private boolean updateDistributionPrint(String setNo, Integer printType, OperationLogDto operation,
			String updateBy) {
		String diffData = "";
		int row = 0;
		DistributionExample distributionExample = new DistributionExample();
		distributionExample.createCriteria().andDistributionNoEqualTo(setNo).andIsDeletedEqualTo(false);
		List<Distribution> listDistribution = distributionMapper.selectByExample(distributionExample);
		if (CollectionUtils.isEmpty(listDistribution)) {
			log.error("操作失败:{}{}", ErrorEnum.NOT_FOND_RESULT.getCode(), ErrorEnum.NOT_FOND_RESULT.getDesc());
			return false;
		}
		Distribution distribution = listDistribution.get(0);
		Distribution distributionDto = BeanMapper.map(distribution, Distribution.class);
		boolean Identification = false;
		if (printType == 1) {
			if (!distributionDto.getIsPrintPickList()) {
				distribution.setIsPrintPickList(true);
				distribution.setUpdateBy(updateBy);
				Identification = true;
			}
		} else {
			if (!distributionDto.getIsPrintDistribution()) {
				distribution.setIsPrintDistribution(true);
				distribution.setUpdateBy(updateBy);
				Identification = true;
			}
		}
		if (Identification) {
			diffData = CompareDifferenceUtil.compare(distributionDto, distribution);
			operation.setDiffData(diffData);
			operation.setBeforeData(JSONObject.toJSONString(distributionDto));
			operation.setAfterData(JSONObject.toJSONString(distribution));

			row = distributionMapper.updateByExample(distribution, distributionExample);
			log.info("修改配送单打印状态,打印类型{},单号{}.影响行数{}", printType, setNo, row);
		}
		return true;
	}

	/**
	 * 批量处理一些楼宇订单
	 * 
	 * @param buildingList
	 * @param companyConfig
	 */
	private void processPageBuilding(List<BuildingDto> buildingList, InterceptConfigDto config, Long shopId) {
		if (CollectionUtils.isEmpty(buildingList)) {
			return;
		}
		// 循环楼宇
		buildingList.stream().forEach(item -> {
			log.info("-----开始处理楼宇：{}的订单", item.getAbbreviation());

			// 生成小集单
			processBuildingDetail(item, config, shopId);
			log.info("+++++结束处理楼宇：{}的订单", item.getAbbreviation());
		});
	}

	/**
	 * 处理单个楼宇的小集单
	 * 
	 * @param buildingDto
	 * @param companyConfig
	 * @param shopId
	 */
	private void processBuildingDetail(BuildingDto buildingDto, InterceptConfigDto config, Long shopId) {
		log.info("-----开始生成楼宇：{}的子集单", buildingDto.getAbbreviation());

		Date deliveryBeginDate = getDeliveryDate(config.getDeliveryTimeBegin());

		// 查询该楼宇里有订单信息的公司
		OrderSearch4OrderSetParam param = OrderSearch4OrderSetParam.builder()
				.deliveryOfficeBuildingId(buildingDto.getId()).deliveryDate(deliveryBeginDate).build();
		param.setPageNo(1);
		param.setPageSize(50);
		Pagination<OrderCompanyResult> companyPageResult = orderService.pageSearchExistsOrderCompany(param);

		log.info("楼宇：{}里共有{}个公司下了订单", buildingDto.getAbbreviation(), companyPageResult.getTotalCount());

		// 公司订单查询条件
		OrderSearch4OrderSetParam companyParam = new OrderSearch4OrderSetParam();
		companyParam.setDeliveryOfficeBuildingId(buildingDto.getId());
		companyParam.setDeliveryDate(deliveryBeginDate);

		// 循环公司
		while (!CollectionUtils.isEmpty(companyPageResult.getDataList())) {
			for (OrderCompanyResult item : companyPageResult.getDataList()) {
				// 处理单个公司的订单
				processCompanyOrder(buildingDto, item, companyParam);
			}
			param.setPageNo(param.getPageNo() + 1);
			companyPageResult = orderService.pageSearchExistsOrderCompany(param);
		}

		// 删除可打包标识
		RedisCache.delete(ORDERSET_FLAG.concat(String.valueOf(buildingDto.getId())));
		log.info("+++++楼宇：{}的子集单处理完成", buildingDto.getAbbreviation());
	}

	/**
	 * 得到年月日时分秒的配送时间
	 * 
	 * @param deliveryTimeBegin
	 * @return
	 */
	private Date getDeliveryDate(Date deliveryTimeBegin) {
		Calendar cal = Calendar.getInstance();
		Calendar result = Calendar.getInstance();

		result.setTime(deliveryTimeBegin);
		result.set(Calendar.YEAR, cal.get(Calendar.YEAR));
		result.set(Calendar.MONTH, cal.get(Calendar.MONTH));
		result.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR));

		return result.getTime();
	}

	/**
	 * 处理单个公司的集单
	 * 
	 * @param buildingDto
	 * @param item
	 * @param setNoKey
	 * @param companyParam
	 * @param companyConfig
	 */
	private void processCompanyOrder(BuildingDto buildingDto, OrderCompanyResult item,
			OrderSearch4OrderSetParam companyParam) {
		log.info("-----开始处理楼宇：{}里的公司：{}的订单", buildingDto.getAbbreviation(), item.getDeliveryCompany());

		// 单个集单的最大商品金额
		BigDecimal ordersetMaxAmount = ConfigRedisUtil.getOrdersetMaxAmount(item.getShopId());
		// 每个集单的最大顾客数
		Integer ordersetMaxMember = ConfigRedisUtil.getOrdersetMaxMember(item.getShopId());

		// 集单的编号自增的key
		String setNoKey = "ORDER_SET_INCRT:NO:".concat(DateUtil.formatNowDate(DateUtil.YYYYMMDD));
		// 集单的编号描述自增的key
		String setNoDescKey = "ORDER_SET_INCRT:DESC:".concat(String.valueOf(item.getShopId())).concat(":")
				.concat(DateUtil.formatNowDate(DateUtil.YYYYMMDD));

		companyParam.setDeliveryFloor(item.getDeliveryFloor());
		companyParam.setDeliveryCompany(item.getDeliveryCompany());

		// 查询该公司的最新的详细地址
		String address = orderService.queryDeliveryAddressByCompanyInfo(companyParam);
		if (StringUtils.isBlank(address)) {
			throw new RuntimeException("查询公司的最新详细地址为空");
		}
		item.setDeliveryAddress(address);

		// 查询单个公司订单
		List<OrderDetailResult> detailList = orderService.search4OrderSet(companyParam);

		if (!CollectionUtils.isEmpty(detailList)) {
			// 获取该订单的配送开始和结束时间
			OrderResult orderResult = orderService.getOrderInfoByOrderNo(detailList.get(0).getOrderNo());

			// 该公司所有的子集单
			List<List<OrderDetailResult>> allDetail = new ArrayList<>();
			// 单个子集单的数据
			List<OrderDetailResult> goodsList = new ArrayList<>();

			// 楼宇id
			String buildingId = String.valueOf(buildingDto.getId());

			// 遍历订单商品信息
			for (OrderDetailResult detail : detailList) {
				recursiveDetailGoods(detail, goodsList, allDetail, ordersetMaxAmount, buildingId, ordersetMaxMember);
			}
			// 说明还有剩余不满max个的需要单独打一个包
			if (!CollectionUtils.isEmpty(goodsList)) {
				allDetail.add(goodsList);
			}

			// 正式生成子集单
			for (List<OrderDetailResult> detail : allDetail) {
				Long setNoIndex = RedisCache.incrBy(setNoKey, 1);
				RedisCache.expire(setNoKey, 1, TimeUnit.DAYS);
				Long setNoDescIndex = RedisCache.incrBy(setNoDescKey, 1);
				RedisCache.expire(setNoDescKey, 1, TimeUnit.DAYS);

				// 子集单号
				String detailSetNo = generateDetailSetNo(setNoIndex.intValue());
				// 子集单号描述
				String detailSetNoDescription = generateDetailSetNoDescription(setNoDescIndex.intValue());

				// 生成子集单
				generateDetailSetInfo(buildingDto, item, detail, detailSetNo, detailSetNoDescription, orderResult);
			}

			// 订单列表
			List<String> orderNoList = detailList.stream().map(data -> data.getOrderNo()).collect(Collectors.toList());
			// 更新订单处理状态
			this.orderService.updateOrderProcessStatus(orderNoList);
		}
		log.info("+++++处理楼宇：{}里的公司：{}的订单为子集单完成", buildingDto.getAbbreviation(), item.getDeliveryCompany());
	}

	/**
	 * 递归调用来生成子集单
	 * 
	 * @param detail
	 * @param goodsList
	 * @param allDetail
	 * @param oneDetailSetGoods
	 * @param companyMax
	 */
	private void recursiveDetailGoods(OrderDetailResult detail, List<OrderDetailResult> goodsList,
			List<List<OrderDetailResult>> allDetail, BigDecimal ordersetMaxAmount, String buildingId, Integer ordersetMaxMember) {
		// 可打包标识
		RedisCache.set(ORDERSET_FLAG.concat(buildingId), "F");
		// 剩余未被分配的商品
		OrderDetailResult surplus = null;

		// 当前detail的总实际支付金额
		BigDecimal detailSum = detail.getActualPayAmount().multiply(new BigDecimal(detail.getGoodsCount()));
		// 当前集单里的总金额
		BigDecimal nowSum = calculationDetailSetGoodsAmount(goodsList);
		// 当前集单里的顾客数 + 当前订单
		Integer memberCount = calculationDetailSetMemberCount(goodsList, detail);

		// 当前商品实付金额已经超过了子集单的最大装载，则拆单
		if (detailSum.add(nowSum).compareTo(ordersetMaxAmount) > 0 || memberCount.compareTo(ordersetMaxMember)>0) {
			// 如果仅仅是金额超了
			if(memberCount.compareTo(ordersetMaxMember)<=0) {
				// 数量超过一个才计算是否能装下部分
				if(detail.getGoodsCount() > 1) {
					// 当前能装下的记录
					OrderDetailResult now = new OrderDetailResult();
					BeanMapper.copy(detail, now);
					
					// 计算还能装下多少份
					int surplusGoodsCount = calculationMaxGoodsCount(now, ordersetMaxAmount, nowSum);
					
					if(surplusGoodsCount > 0) {
						// 能装下的放入打包队列
						now.setGoodsCount(surplusGoodsCount);
						goodsList.add(now);
						
						// 剩余的数量到递归里去
						detail.setGoodsCount(detail.getGoodsCount() - surplusGoodsCount);
					}
				}
			}
			// 可以打包了
			RedisCache.set(ORDERSET_FLAG.concat(buildingId), "T");
			// 被剩下的只能分配到下一个子集单
			surplus = detail;
		} else {
			goodsList.add(detail);
		}

		// 为T就表示可以打包了
		if ("T".equals(RedisCache.get(ORDERSET_FLAG.concat(buildingId)))) {
			// 加到队列里
			allDetail.add(new ArrayList<>(goodsList));
			// 重置
			goodsList.clear();
		}
		// 有剩余待打包的递归放入下一个包裹中
		if (null != surplus) {
			// 递归调用
			recursiveDetailGoods(surplus, goodsList, allDetail, ordersetMaxAmount, buildingId, ordersetMaxMember);
		} else {
			// 结束递归
			return;
		}
	}

	/**
	 * 计算还能装下多少份当前商品
	 * 
	 * @param detail
	 * @param ordersetMaxAmount
	 * @param nowSum
	 * @return
	 */
	private int calculationMaxGoodsCount(OrderDetailResult detail, BigDecimal ordersetMaxAmount, BigDecimal nowSum) {
		for (int i = 1; i <= detail.getGoodsCount(); i++) {
			if (nowSum.add(detail.getActualPayAmount().multiply(new BigDecimal(i))).compareTo(ordersetMaxAmount) > 0) {
				return i - 1;
			}
		}
		return 0;
	}

	/**
	 * 计算子集单商品金额
	 * 
	 * @param detail
	 * @return
	 */
	private BigDecimal calculationDetailSetGoodsAmount(List<OrderDetailResult> detail) {
		if (CollectionUtils.isEmpty(detail)) {
			return BigDecimal.ZERO;
		}
		BigDecimal count = BigDecimal.ZERO;
		for (OrderDetailResult item : detail) {
			count = count.add(item.getActualPayAmount().multiply(new BigDecimal(item.getGoodsCount())));
		}
		return count;
	}
	
	/**
	 * 计算子集单商品金额
	 * 
	 * @param detail
	 * @return
	 */
	private int calculationDetailSetMemberCount(List<OrderDetailResult> detailList, OrderDetailResult detail) {
		if (CollectionUtils.isEmpty(detailList)) {
			return 0;
		}
		Set<String> set = detailList.stream().map(item -> item.getMobileNo()).collect(Collectors.toSet());
		if(CollectionUtils.isEmpty(set)) {
			return 1;
		}
		set.add(detail.getMobileNo());
		return set.size();
	}

	/**
	 * 生成子集单信息
	 * 
	 * @param buildingDto
	 * @param orderCompany
	 * @param goodsList
	 */
	private void generateDetailSetInfo(BuildingDto buildingDto, OrderCompanyResult orderCompany,
			List<OrderDetailResult> goodsList, String detailSetNo, String detailSetNoDescription,
			OrderResult orderResult) {
		log.info("-----开始保存子集单信息，楼宇：{}", buildingDto.getAbbreviation());

		OrderSetDetail detail = new OrderSetDetail();
		detail.setBuildingId(buildingDto.getId());
		detail.setBuildingAbbreviation(buildingDto.getAbbreviation());
		detail.setCompanyAbbreviation(orderCompany.getDeliveryCompany());
		detail.setDeliveryFloor(orderCompany.getDeliveryFloor());
		detail.setDetailDeliveryAddress(orderCompany.getDeliveryAddress());
		detail.setDetailDeliveryDate(new Date());
		detail.setDetailSetNo(detailSetNo);
		detail.setDetailSetNoDescription(detailSetNoDescription);
		detail.setId(IdGenerator.nextId());
		detail.setIsDeleted(false);
		detail.setShopId(orderCompany.getShopId());
		detail.setStatus(OrderSetStatusEnum.WAITING_FOR_PACKAGING.getCode());
		detail.setIsPrint(false);
		detail.setDeliveryStartTime(orderResult.getDeliveryStartDate());
		detail.setDeliveryEndTime(orderResult.getDeliveryEndDate());

		// redis的自增key
		String key = "ORDER_SET_ORDER_COUPLET_INCRT:".concat(String.valueOf(orderCompany.getShopId())).concat(":")
				.concat(DateUtil.formatNowDate(DateUtil.YYYYMMDD));

		// 得到这个集单里的订单号的客户联
		Map<String, String> map = getOrderCoupletNo(goodsList, key);
		RedisCache.expire(key, 1, TimeUnit.DAYS);

		// 商品数量
		int goodsSum = 0;
		// 实付总金额
		BigDecimal actualPayAmount = BigDecimal.ZERO;

		OrderSetDetailGoods record = null;
		for (OrderDetailResult goods : goodsList) {
			// 根据订单号查询主表信息
			OrderResult orderInfo = orderService.getOrderInfoByOrderNo(goods.getOrderNo());
			if (orderInfo == null) {
				throw new RuntimeException("根据订单号" + goods.getOrderNo() + "未查询到订单信息");
			}

			goodsSum += goods.getGoodsCount();
			actualPayAmount = actualPayAmount.add(goods.getActualPayAmount().multiply(new BigDecimal(goods.getGoodsCount())));

			record = new OrderSetDetailGoods();
			record.setDetailSetNo(detail.getDetailSetNo());
			record.setGoodsCode(goods.getGoodsCode());
			record.setGoodsName(goods.getGoodsName());
			record.setGoodsNumber(goods.getGoodsCount());
			record.setId(IdGenerator.nextId());
			record.setIsDeleted(false);
			record.setOrderNo(goods.getOrderNo());
			record.setUserId(orderInfo.getUserId());
			record.setUserName(orderInfo.getRealName());
			record.setUserPhone(orderInfo.getMobileNo());
			record.setCoupletNo(map.get(goods.getOrderNo()));
			record.setActualPayAmount(goods.getActualPayAmount());

			orderSetDetailGoodsMapper.insertSelective(record);
		}
		detail.setGoodsSum(goodsSum);
		detail.setActualPayAmount(actualPayAmount);
		detail.setDistributionStatus(LogisticsDeliveryStatus.INIT.getValue());
		detail.setIsPush(Boolean.FALSE);
		orderSetDetailMapper.insertSelective(detail);

		log.info("+++++楼宇：{}的子集单保存成功，子集单号：{}", buildingDto.getAbbreviation(), detail.getDetailSetNo());
	}

	/**
	 * 生成集单里订单的订单联的编号
	 * 
	 * @param goodsList
	 * @param key
	 * @return
	 */
	private Map<String, String> getOrderCoupletNo(List<OrderDetailResult> goodsList, String key) {
		Map<String, String> map = new HashMap<>();
		for (OrderDetailResult goods : goodsList) {
			if (map.get(goods.getOrderNo()) == null) {
				map.put(goods.getOrderNo(),
						NoPrefixEnum.ORDER.getCode().concat(leftFillInZero(RedisCache.incrBy(key, 1).intValue(), 4)));
			}
		}
		return map;
	}

	/**
	 * 生成子集单编号
	 * 
	 * @param setNo
	 * @param index
	 * @return
	 */
	private String generateDetailSetNo(int index) {
		return NoPrefixEnum.ORDERSET.getCode().concat(DateUtil.formatNowDate(DateUtil.YYYYMMDD))
				.concat(leftFillInZero(index, 6));
	}

	/**
	 * 生成子集单编号的描述
	 * 
	 * @param setNo
	 * @param index
	 * @return
	 */
	private String generateDetailSetNoDescription(int index) {
		return NoPrefixEnum.ORDERSET.getCode().concat(leftFillInZero(index, 4));
	}

	/**
	 * 左补位0
	 * 
	 * @param value
	 * @param digit
	 * @return
	 */
	private String leftFillInZero(int value, int digit) {
		String v = String.valueOf(value);
		int difference = digit - v.length();
		if (difference <= 0) {
			return v;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < difference; i++) {
			sb.append("0");
		}
		sb.append(v);
		return sb.toString();
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
				.modular(OperationModularEnum.ORDER_SET_LUNCH.getCode()).operationType(OperationTypeEnum.UPDATE.getCode())
				.operationTime(new Date()).build();
		
		if(!LogisticsDeliveryStatus.INIT.getValue().equals(newOrderSetDetail.getDistributionStatus())) {
			opLog.setOperationResume(LogisticsDeliveryStatus.getNameByValue(newOrderSetDetail.getDistributionStatus()));
		} else {
			if (param.getAction().equals(OrderSetActionEnum.BALE.getCode())) {
				// 打包操作
				opLog.setOperationResume("打包");
			} else if (param.getAction().equals(OrderSetActionEnum.DELIVERY.getCode())) {
				// 交配送
				opLog.setOperationResume("交配送");
			} else if (param.getAction().equals(OrderSetActionEnum.NOTIFY.getCode())) {
				// 通知客户
				opLog.setOperationResume("通知顾客");
			}
		}
		operationLogService.sendMq(opLog);
	}
}

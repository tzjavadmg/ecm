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
	
	// ??????????????????????????????
	private static final String ORDERSET_FLAG = "orderset_flag_building:";

	/**
	 * ?????????????????????????????????
	 */
	@Override
	@Transactional
	public void executeShopSet(Long shopId, InterceptConfigDto config) {
		BuildingParam param = new BuildingParam();
		param.setShopId(shopId);
		param.setPageNo(1);
		param.setPageSize(50);

		// ?????????????????????????????????list
		Pagination<BuildingDto> buildingList = buildingService.getBuildingPageByShopId(param);

		while (!CollectionUtils.isEmpty(buildingList.getDataList())) {

			processPageBuilding(buildingList.getDataList(), config, shopId);

			param.setPageNo(param.getPageNo() + 1);
			buildingList = buildingService.getBuildingPageByShopId(param);
		}
	}

	/**
	 * ??????????????????????????????????????????
	 * 
	 * @param orderNo
	 */
	@Override
	public OrderSetStatusQueryResult queryStatusByOrderNo(String orderNo) {
		OrderSetStatusQueryResult result = OrderSetStatusQueryResult.builder().orderNo(orderNo).build();

		// ?????????????????????????????????????????????
		List<String> detailSetNoList = orderSetDetailGoodsExtMapper.listDetailSetNoByOrderNo(orderNo);
		if (CollectionUtils.isEmpty(detailSetNoList)) {
			// ??????????????????????????????????????????
			return result;
		}

		// ?????????????????????
		OrderSetDetailExample example = new OrderSetDetailExample();
		example.createCriteria().andDetailSetNoIn(detailSetNoList).andIsDeletedEqualTo(false);
		example.setOrderByClause(" detail_set_no asc ");
		List<OrderSetDetail> list = orderSetDetailMapper.selectByExample(example);

		List<OrderSetStatusQueryResultItem> details = new ArrayList<>(list.size());
		// ???????????????
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
	 * ????????????????????????
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
				log.error("?????????????????????", e);
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
					// ????????????????????????
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
	 * ?????????????????????
	 * 
	 * @param param
	 */
	@Override
	@Transactional
	public ResponseResult<Object> updateStatus(UpdateOrderSetStatusParam param) {
		log.info("????????????????????????????????????{}", JSONObject.toJSONString(param));
		ResponseResult<Object> result = ResponseResult.buildSuccessResponse();
		
		if (null != param.getSfDto()) {
			param.setDetailSetId(Long.valueOf(param.getSfDto().getDetailSetId()));
		}
		// ?????????????????????
		OrderSetDetail orderSetDetail = orderSetDetailMapper.selectByPrimaryKey(param.getDetailSetId());
		if(null == orderSetDetail) {
			result.setSuccess(false);
			result.setMessage("????????????????????????");
			log.warn("??????id???{}??????????????????", param.getDetailSetId());
			return result;
		}
		
		// ???????????????
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
				// ?????????
				orderSetDetail.setStatus(OrderSetStatusEnum.ALREADY_PACKED.getCode());
			} else if (LogisticsDeliveryStatus.COMPLETED.getValue().equals(param.getSfDto().getStatus())) {
				// ????????????
				orderSetDetail.setStatus(OrderSetStatusEnum.NOTIFIED.getCode());
			} else if (LogisticsDeliveryStatus.RECEIVED_GOODS.getValue().equals(param.getSfDto().getStatus())) {
				// ???????????????
				orderSetDetail.setStatus(OrderSetStatusEnum.IN_DISTRIBUTION.getCode());
			}
			criteria.andStatusEqualTo(oldOrderSetDetail.getStatus());
			criteria.andDistributionStatusEqualTo(oldOrderSetDetail.getDistributionStatus());
		} else {
			// ?????????????????????
			
			Byte oldStatus = null;
			if (param.getAction().equals(OrderSetActionEnum.BALE.getCode())) {
				// ????????????
				oldStatus = OrderSetStatusEnum.WAITING_FOR_PACKAGING.getCode();
				orderSetDetail.setStatus(OrderSetStatusEnum.ALREADY_PACKED.getCode());
			} else if (param.getAction().equals(OrderSetActionEnum.DELIVERY.getCode())) {
				// ?????????
				oldStatus = OrderSetStatusEnum.ALREADY_PACKED.getCode();
				orderSetDetail.setStatus(OrderSetStatusEnum.IN_DISTRIBUTION.getCode());
			} else if (param.getAction().equals(OrderSetActionEnum.NOTIFY.getCode())) {
				// ????????????
				oldStatus = OrderSetStatusEnum.IN_DISTRIBUTION.getCode();
				orderSetDetail.setStatus(OrderSetStatusEnum.NOTIFIED.getCode());
			} else {
				result.setSuccess(false);
				result.setMessage("?????????????????????");
				return result;
			}
			criteria.andStatusEqualTo(oldStatus);
		}

		int ct = orderSetDetailMapper.updateByExampleSelective(orderSetDetail, example);
		if (ct == 0) {
			result.setSuccess(false);
			result.setMessage("??????????????????????????????");
			log.warn("??????id???{}???????????????", param.getDetailSetId());
			return result;
		}

		// ??????????????????
		sendOpLog(param, oldOrderSetDetail, orderSetDetail);

		OrderSetDetailGoodsExample goodsExample = new OrderSetDetailGoodsExample();
		goodsExample.createCriteria().andDetailSetNoEqualTo(orderSetDetail.getDetailSetNo())
				.andIsDeletedEqualTo(false);
		List<OrderSetDetailGoods> list = orderSetDetailGoodsMapper.selectByExample(goodsExample);
		if (!CollectionUtils.isEmpty(list)) {
			Set<String> orderNoSet = list.stream().map(item -> item.getOrderNo()).collect(Collectors.toSet());
			log.info("?????????{}??????????????????????????????:{}", orderSetDetail.getDetailSetNo(), JSONObject.toJSONString(orderNoSet));
			orderNoSet.stream().forEach(orderNo -> {
				// ?????????????????????????????????????????????
				List<OrderSetDetailStatusDto> statusList = orderSetDetailGoodsExtMapper
						.listAllStatusByOrderNo(orderNo);
				log.info("????????????{}?????????list???{}", orderNo, JSONObject.toJSONString(statusList));
				if (!CollectionUtils.isEmpty(statusList)) {
					OrderSetDetailStatusDto dto = statusList.get(0);

					if(!LogisticsDeliveryStatus.INIT.getValue().equals(dto.getDistributionStatus())) {
						// ???????????????????????????
						OrderUpdateStatusParam orderStatusParam = new OrderUpdateStatusParam();
						orderStatusParam.setOrderNo(orderNo);
						orderStatusParam.setDeliveryStatus(dto.getDistributionStatus());
						orderService.updateStatus(orderStatusParam);

						log.info("????????????{}????????????????????????????????????????????????MQ", orderNo);
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
		log.info("????????????????????????:{}", JSONObject.toJSONString(param));
		String setNo = param.getSetNo();
		Integer printType = param.getPrintType();
		OperationLogDto operation = new OperationLogDto();
		String updateBy = param.getUpdateBy();
		operation.setModular(OperationModularEnum.ORDER_SET_LUNCH.getCode());
		operation.setOperationTime(new Date());
		operation.setOperationType("update");
		operation.setOperationUser(param.getUpdateBy());
		operation.setBussinessId(setNo);
		operation.setOperationResume(PrintTypeEnum.getDescByCode(printType) + "??????");
		String diffData = null;
		int row = 0;
		boolean result = false;
		switch (printType) {
		case 1:
			// ?????????
			result = updateDistributionPrint(setNo, printType, operation, updateBy);
			break;
		case 2:
			// ?????????
			result = updateDistributionPrint(setNo, printType, operation, updateBy);
			break;
		case 3:
			// ??????
			// ?????????????????????
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
				// ????????????????????????
				OrderSetDetailExample example = new OrderSetDetailExample();
				example.createCriteria().andDetailSetNoEqualTo(setNo).andIsDeletedEqualTo(false);
				row = orderSetDetailMapper.updateByExample(orderSetDetail, example);
				result = true;
				log.info("??????????????????????????????????????????:[{}]", row);
			}
			break;
		default:
			break;
		}
		if (!result) {
			log.info("????????????,???????????????????????????");
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
	 * ???????????????????????????
	 * 
	 * @param param
	 */
	@Override
	@Transactional
	public void updateDistributionStatus(SfOrderMqDto param) {
		UpdateOrderSetStatusParam dto = new UpdateOrderSetStatusParam();
		dto.setSfDto(param);
		dto.setUpdateBy("sf_??????");
		this.updateStatus(dto);
	}

	/**
	 * ??????????????????????????????POS??????????????????
	 * 
	 * @param shopId
	 * @param isCompensate ????????????
	 */
	@Override
	public void sendOrderSetMq(Long shopId, boolean isCompensate) {
		int pageSize = 50;

		OrderSetDetailExample example = new OrderSetDetailExample();
		// ????????????????????????????????????
		if(isCompensate) {
			example.createCriteria().andShopIdEqualTo(shopId).andDetailDeliveryDateEqualTo(DateUtil.getDayBeginTime(new Date()));
		} else {
			example.createCriteria().andShopIdEqualTo(shopId).andIsPushEqualTo(Boolean.FALSE)
				.andDetailDeliveryDateEqualTo(DateUtil.getDayBeginTime(new Date()));
		}
		
		// ??????????????????
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
	 * ????????????????????????
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
			log.error("????????????:{}{}", ErrorEnum.NOT_FOND_RESULT.getCode(), ErrorEnum.NOT_FOND_RESULT.getDesc());
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
			log.info("???????????????????????????,????????????{},??????{}.????????????{}", printType, setNo, row);
		}
		return true;
	}

	/**
	 * ??????????????????????????????
	 * 
	 * @param buildingList
	 * @param companyConfig
	 */
	private void processPageBuilding(List<BuildingDto> buildingList, InterceptConfigDto config, Long shopId) {
		if (CollectionUtils.isEmpty(buildingList)) {
			return;
		}
		// ????????????
		buildingList.stream().forEach(item -> {
			log.info("-----?????????????????????{}?????????", item.getAbbreviation());

			// ???????????????
			processBuildingDetail(item, config, shopId);
			log.info("+++++?????????????????????{}?????????", item.getAbbreviation());
		});
	}

	/**
	 * ??????????????????????????????
	 * 
	 * @param buildingDto
	 * @param companyConfig
	 * @param shopId
	 */
	private void processBuildingDetail(BuildingDto buildingDto, InterceptConfigDto config, Long shopId) {
		log.info("-----?????????????????????{}????????????", buildingDto.getAbbreviation());

		Date deliveryBeginDate = getDeliveryDate(config.getDeliveryTimeBegin());

		// ??????????????????????????????????????????
		OrderSearch4OrderSetParam param = OrderSearch4OrderSetParam.builder()
				.deliveryOfficeBuildingId(buildingDto.getId()).deliveryDate(deliveryBeginDate).build();
		param.setPageNo(1);
		param.setPageSize(50);
		Pagination<OrderCompanyResult> companyPageResult = orderService.pageSearchExistsOrderCompany(param);

		log.info("?????????{}?????????{}?????????????????????", buildingDto.getAbbreviation(), companyPageResult.getTotalCount());

		// ????????????????????????
		OrderSearch4OrderSetParam companyParam = new OrderSearch4OrderSetParam();
		companyParam.setDeliveryOfficeBuildingId(buildingDto.getId());
		companyParam.setDeliveryDate(deliveryBeginDate);

		// ????????????
		while (!CollectionUtils.isEmpty(companyPageResult.getDataList())) {
			for (OrderCompanyResult item : companyPageResult.getDataList()) {
				// ???????????????????????????
				processCompanyOrder(buildingDto, item, companyParam);
			}
			param.setPageNo(param.getPageNo() + 1);
			companyPageResult = orderService.pageSearchExistsOrderCompany(param);
		}

		// ?????????????????????
		RedisCache.delete(ORDERSET_FLAG.concat(String.valueOf(buildingDto.getId())));
		log.info("+++++?????????{}????????????????????????", buildingDto.getAbbreviation());
	}

	/**
	 * ???????????????????????????????????????
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
	 * ???????????????????????????
	 * 
	 * @param buildingDto
	 * @param item
	 * @param setNoKey
	 * @param companyParam
	 * @param companyConfig
	 */
	private void processCompanyOrder(BuildingDto buildingDto, OrderCompanyResult item,
			OrderSearch4OrderSetParam companyParam) {
		log.info("-----?????????????????????{}???????????????{}?????????", buildingDto.getAbbreviation(), item.getDeliveryCompany());

		// ?????????????????????????????????
		BigDecimal ordersetMaxAmount = ConfigRedisUtil.getOrdersetMaxAmount(item.getShopId());
		// ??????????????????????????????
		Integer ordersetMaxMember = ConfigRedisUtil.getOrdersetMaxMember(item.getShopId());

		// ????????????????????????key
		String setNoKey = "ORDER_SET_INCRT:NO:".concat(DateUtil.formatNowDate(DateUtil.YYYYMMDD));
		// ??????????????????????????????key
		String setNoDescKey = "ORDER_SET_INCRT:DESC:".concat(String.valueOf(item.getShopId())).concat(":")
				.concat(DateUtil.formatNowDate(DateUtil.YYYYMMDD));

		companyParam.setDeliveryFloor(item.getDeliveryFloor());
		companyParam.setDeliveryCompany(item.getDeliveryCompany());

		// ???????????????????????????????????????
		String address = orderService.queryDeliveryAddressByCompanyInfo(companyParam);
		if (StringUtils.isBlank(address)) {
			throw new RuntimeException("???????????????????????????????????????");
		}
		item.setDeliveryAddress(address);

		// ????????????????????????
		List<OrderDetailResult> detailList = orderService.search4OrderSet(companyParam);

		if (!CollectionUtils.isEmpty(detailList)) {
			// ?????????????????????????????????????????????
			OrderResult orderResult = orderService.getOrderInfoByOrderNo(detailList.get(0).getOrderNo());

			// ???????????????????????????
			List<List<OrderDetailResult>> allDetail = new ArrayList<>();
			// ????????????????????????
			List<OrderDetailResult> goodsList = new ArrayList<>();

			// ??????id
			String buildingId = String.valueOf(buildingDto.getId());

			// ????????????????????????
			for (OrderDetailResult detail : detailList) {
				recursiveDetailGoods(detail, goodsList, allDetail, ordersetMaxAmount, buildingId, ordersetMaxMember);
			}
			// ????????????????????????max??????????????????????????????
			if (!CollectionUtils.isEmpty(goodsList)) {
				allDetail.add(goodsList);
			}

			// ?????????????????????
			for (List<OrderDetailResult> detail : allDetail) {
				Long setNoIndex = RedisCache.incrBy(setNoKey, 1);
				RedisCache.expire(setNoKey, 1, TimeUnit.DAYS);
				Long setNoDescIndex = RedisCache.incrBy(setNoDescKey, 1);
				RedisCache.expire(setNoDescKey, 1, TimeUnit.DAYS);

				// ????????????
				String detailSetNo = generateDetailSetNo(setNoIndex.intValue());
				// ??????????????????
				String detailSetNoDescription = generateDetailSetNoDescription(setNoDescIndex.intValue());

				// ???????????????
				generateDetailSetInfo(buildingDto, item, detail, detailSetNo, detailSetNoDescription, orderResult);
			}

			// ????????????
			List<String> orderNoList = detailList.stream().map(data -> data.getOrderNo()).collect(Collectors.toList());
			// ????????????????????????
			this.orderService.updateOrderProcessStatus(orderNoList);
		}
		log.info("+++++???????????????{}???????????????{}???????????????????????????", buildingDto.getAbbreviation(), item.getDeliveryCompany());
	}

	/**
	 * ??????????????????????????????
	 * 
	 * @param detail
	 * @param goodsList
	 * @param allDetail
	 * @param oneDetailSetGoods
	 * @param companyMax
	 */
	private void recursiveDetailGoods(OrderDetailResult detail, List<OrderDetailResult> goodsList,
			List<List<OrderDetailResult>> allDetail, BigDecimal ordersetMaxAmount, String buildingId, Integer ordersetMaxMember) {
		// ???????????????
		RedisCache.set(ORDERSET_FLAG.concat(buildingId), "F");
		// ???????????????????????????
		OrderDetailResult surplus = null;

		// ??????detail????????????????????????
		BigDecimal detailSum = detail.getActualPayAmount().multiply(new BigDecimal(detail.getGoodsCount()));
		// ???????????????????????????
		BigDecimal nowSum = calculationDetailSetGoodsAmount(goodsList);
		// ??????????????????????????? + ????????????
		Integer memberCount = calculationDetailSetMemberCount(goodsList, detail);

		// ???????????????????????????????????????????????????????????????????????????
		if (detailSum.add(nowSum).compareTo(ordersetMaxAmount) > 0 || memberCount.compareTo(ordersetMaxMember)>0) {
			// ???????????????????????????
			if(memberCount.compareTo(ordersetMaxMember)<=0) {
				// ????????????????????????????????????????????????
				if(detail.getGoodsCount() > 1) {
					// ????????????????????????
					OrderDetailResult now = new OrderDetailResult();
					BeanMapper.copy(detail, now);
					
					// ???????????????????????????
					int surplusGoodsCount = calculationMaxGoodsCount(now, ordersetMaxAmount, nowSum);
					
					if(surplusGoodsCount > 0) {
						// ??????????????????????????????
						now.setGoodsCount(surplusGoodsCount);
						goodsList.add(now);
						
						// ??????????????????????????????
						detail.setGoodsCount(detail.getGoodsCount() - surplusGoodsCount);
					}
				}
			}
			// ???????????????
			RedisCache.set(ORDERSET_FLAG.concat(buildingId), "T");
			// ?????????????????????????????????????????????
			surplus = detail;
		} else {
			goodsList.add(detail);
		}

		// ???T????????????????????????
		if ("T".equals(RedisCache.get(ORDERSET_FLAG.concat(buildingId)))) {
			// ???????????????
			allDetail.add(new ArrayList<>(goodsList));
			// ??????
			goodsList.clear();
		}
		// ???????????????????????????????????????????????????
		if (null != surplus) {
			// ????????????
			recursiveDetailGoods(surplus, goodsList, allDetail, ordersetMaxAmount, buildingId, ordersetMaxMember);
		} else {
			// ????????????
			return;
		}
	}

	/**
	 * ???????????????????????????????????????
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
	 * ???????????????????????????
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
	 * ???????????????????????????
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
	 * ?????????????????????
	 * 
	 * @param buildingDto
	 * @param orderCompany
	 * @param goodsList
	 */
	private void generateDetailSetInfo(BuildingDto buildingDto, OrderCompanyResult orderCompany,
			List<OrderDetailResult> goodsList, String detailSetNo, String detailSetNoDescription,
			OrderResult orderResult) {
		log.info("-----???????????????????????????????????????{}", buildingDto.getAbbreviation());

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

		// redis?????????key
		String key = "ORDER_SET_ORDER_COUPLET_INCRT:".concat(String.valueOf(orderCompany.getShopId())).concat(":")
				.concat(DateUtil.formatNowDate(DateUtil.YYYYMMDD));

		// ?????????????????????????????????????????????
		Map<String, String> map = getOrderCoupletNo(goodsList, key);
		RedisCache.expire(key, 1, TimeUnit.DAYS);

		// ????????????
		int goodsSum = 0;
		// ???????????????
		BigDecimal actualPayAmount = BigDecimal.ZERO;

		OrderSetDetailGoods record = null;
		for (OrderDetailResult goods : goodsList) {
			// ?????????????????????????????????
			OrderResult orderInfo = orderService.getOrderInfoByOrderNo(goods.getOrderNo());
			if (orderInfo == null) {
				throw new RuntimeException("???????????????" + goods.getOrderNo() + "????????????????????????");
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

		log.info("+++++?????????{}??????????????????????????????????????????{}", buildingDto.getAbbreviation(), detail.getDetailSetNo());
	}

	/**
	 * ??????????????????????????????????????????
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
	 * ?????????????????????
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
	 * ??????????????????????????????
	 * 
	 * @param setNo
	 * @param index
	 * @return
	 */
	private String generateDetailSetNoDescription(int index) {
		return NoPrefixEnum.ORDERSET.getCode().concat(leftFillInZero(index, 4));
	}

	/**
	 * ?????????0
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
	 * ??????????????????
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
				// ????????????
				opLog.setOperationResume("??????");
			} else if (param.getAction().equals(OrderSetActionEnum.DELIVERY.getCode())) {
				// ?????????
				opLog.setOperationResume("?????????");
			} else if (param.getAction().equals(OrderSetActionEnum.NOTIFY.getCode())) {
				// ????????????
				opLog.setOperationResume("????????????");
			}
		}
		operationLogService.sendMq(opLog);
	}
}

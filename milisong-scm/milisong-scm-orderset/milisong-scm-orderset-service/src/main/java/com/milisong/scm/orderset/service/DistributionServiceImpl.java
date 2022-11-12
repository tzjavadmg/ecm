package com.milisong.scm.orderset.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.milisong.scm.orderset.dto.param.OrderSetReqDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.Pagination;
import com.farmland.core.util.BeanMapper;
import com.milisong.scm.orderset.api.DistributionService;
import com.milisong.scm.orderset.domain.Distribution;
import com.milisong.scm.orderset.domain.DistributionExample;
import com.milisong.scm.orderset.domain.DistributionExample.Criteria;
import com.milisong.scm.orderset.domain.DistributionOrderset;
import com.milisong.scm.orderset.domain.DistributionOrdersetExample;
import com.milisong.scm.orderset.dto.param.DistributionSearchParam;
import com.milisong.scm.orderset.dto.param.OrderSearch4OrderSetParam;
import com.milisong.scm.orderset.dto.result.BuildingBaseInfoResult;
import com.milisong.scm.orderset.dto.result.DistributionOrdersetInfoResult;
import com.milisong.scm.orderset.dto.result.DistributionOrdersetResult;
import com.milisong.scm.orderset.dto.result.DistributionQueryResult;
import com.milisong.scm.orderset.dto.result.OrderSetDetailDto;
import com.milisong.scm.orderset.dto.result.OrderSetDetailGoodsDto;
import com.milisong.scm.orderset.dto.result.OrderSetDetailResultDto;
import com.milisong.scm.orderset.dto.result.PickOrderGoodsSumResult;
import com.milisong.scm.orderset.dto.result.PickOrderPrintQueryResult;
import com.milisong.scm.orderset.mapper.DistributionExtMapper;
import com.milisong.scm.orderset.mapper.DistributionMapper;
import com.milisong.scm.orderset.mapper.DistributionOrdersetExtMapper;
import com.milisong.scm.orderset.mapper.DistributionOrdersetMapper;
import com.milisong.scm.orderset.mapper.OrderSetDetailExtMapper;
import com.milisong.scm.orderset.mapper.OrderSetDetailGoodsExtMapper;
import com.milisong.scm.orderset.util.DateUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class DistributionServiceImpl implements DistributionService{
	@Autowired
	private OrderSetDetailExtMapper orderSetDetailExtMapper;
	@Autowired
	private OrderSetDetailGoodsExtMapper orderSetDetailGoodsExtMapper;
	@Autowired
	private DistributionMapper distributionMapper;
	@Autowired
	private DistributionExtMapper distributionExtMapper;
	@Autowired
	private DistributionOrdersetMapper distributionOrdersetMapper;
	@Autowired
	private DistributionOrdersetExtMapper distributionOrdersetExtMapper;
	@Autowired
	private RestTemplate restTemplatel;

	/**
	 * 分页查询配送单信息
	 * @param param
	 * @return
	 */
	public Pagination<DistributionQueryResult> pageSearch(DistributionSearchParam param) {
		DistributionExample example = new DistributionExample();
		Criteria criteria = example.createCriteria().andShopIdEqualTo(param.getShopId());
		if(StringUtils.isNotBlank(param.getBeginDate())) {
			criteria.andDeliveryDateGreaterThanOrEqualTo(DateUtil.parseDate(param.getBeginDate(), DateUtil.YYYY_MM_DD));
		}
		if(StringUtils.isNotBlank(param.getEndDate())) {
			criteria.andDeliveryDateLessThanOrEqualTo(DateUtil.parseDate(param.getEndDate(), DateUtil.YYYY_MM_DD));
		}
		if(null != param.getIsPrintPickList()) {
			criteria.andIsPrintPickListEqualTo(param.getIsPrintPickList());
		}
		if(null != param.getIsPrintDistribution()) {
			criteria.andIsPrintDistributionEqualTo(param.getIsPrintDistribution());
		}
		if(StringUtils.isNotBlank(param.getDistributionDescription())) {
			criteria.andDistributionDescriptionEqualTo(param.getDistributionDescription());
		}
		
		Pagination<DistributionQueryResult> result = new Pagination<>();
		result.setPageNo(param.getPageNo());
		result.setPageSize(param.getPageSize());
		Map<String,Object> mapParam = new HashMap<String,Object>();
		mapParam.put("deliveryDateStart",DateUtil.parseDate(param.getBeginDate(), DateUtil.YYYY_MM_DD));
		mapParam.put("deliveryDateEnd",DateUtil.parseDate(param.getEndDate(), DateUtil.YYYY_MM_DD));
		mapParam.put("shopId",param.getShopId());
		mapParam.put("startRow",param.getStartRow());
		mapParam.put("pageSize",param.getPageSize());
		mapParam.put("isPrintPickList", param.getIsPrintPickList());
		mapParam.put("isPrintDistribution", param.getIsPrintDistribution());
		long count = distributionMapper.countByExample(example);
		result.setTotalCount(count);
		if(count > 0) {
			
			
			List<Distribution> list = distributionExtMapper.selectByPage(mapParam);
			//List<Distribution> list = distributionMapper.selectByExample(example);
			if(!CollectionUtils.isEmpty(list)) {
				List<DistributionQueryResult> data = BeanMapper.mapList(list, DistributionQueryResult.class);
				Set<String> setNo = data.stream().map(DistributionQueryResult::getDistributionNo).collect(Collectors.toSet());
				Map<String,Integer> mapCount = getCustomerCountByDistributionNos(setNo);
				data.stream().forEach(item -> {
					item.setBuildingList(BeanMapper.mapList(distributionOrdersetExtMapper.selectBuildingByDistributionNo(item.getDistributionNo()), BuildingBaseInfoResult.class));
					item.setCustomerSum(mapCount.get(item.getDistributionNo()));
					item.setOrdersetNoResult(orderSetDetailGoodsExtMapper.getLastOrderNo(item.getDistributionNo()));
				});
				result.setDataList(data);
			}
		} else {
			result.setDataList(Collections.emptyList());
		}
		return result;
	}
	
	private Map<String,Integer> getCustomerCountByDistributionNos(Set<String> no){
		List<String> listNo = new ArrayList<String>();
		Map<String,Integer> mapResult = new HashMap<String,Integer>();
		listNo.addAll(no);
		List<Map<String, Object>>  list = distributionOrdersetExtMapper.customerCountByDistributionNo(listNo);
		for (Map<String, Object> map : list) {
			mapResult.put(map.get("distribution_no").toString(), Integer.parseInt(map.get("count").toString()));
		}
		return mapResult;
	}
	
	/**
	 * 根据配送单号查询里面的集单号list
	 * @param distributionNo
	 * @return
	 */
	public List<DistributionOrdersetResult> listOrderSetNoByDistributionNo(String distributionNo) {
		DistributionOrdersetExample example = new DistributionOrdersetExample();
		example.createCriteria().andDistributionNoEqualTo(distributionNo);
		
		List<DistributionOrderset> list = distributionOrdersetMapper.selectByExample(example);
		if(CollectionUtils.isEmpty(list)) {
			return null;
		}
		return BeanMapper.mapList(list, DistributionOrdersetResult.class);
	}

	@Override
	public PickOrderPrintQueryResult getPickOrderByPrint(String distributionNo) {
		log.info("查询拣货单信息{}",JSONObject.toJSONString(distributionNo));
		List<DistributionOrdersetResult> listResult = listOrderSetNoByDistributionNo(distributionNo);
		PickOrderPrintQueryResult pickOrder = new PickOrderPrintQueryResult();
		pickOrder.setSetNoList(listResult);
		List<String> listOrderSetNo = listResult.stream().map(DistributionOrdersetResult::getDetailSetNo).collect(Collectors.toList());
		List<PickOrderGoodsSumResult> listGoodsCount = orderSetDetailGoodsExtMapper.listGoodsCountGroup(listOrderSetNo);
		pickOrder.setGoodsList(listGoodsCount);
		return pickOrder;
	}

	@Override
	public List<DistributionOrdersetInfoResult> getCustomerOrderByDistributionNo(String distributionNo) {
		List<DistributionOrdersetInfoResult>  listResult = distributionOrdersetExtMapper.getCustomerOrderByDistributionNo(distributionNo);
		Map<String,DistributionOrdersetInfoResult> mapResult = new TreeMap<String,DistributionOrdersetInfoResult>();
		for (DistributionOrdersetInfoResult distributionOrdersetInfoResult : listResult) {
			mapResult.put(distributionOrdersetInfoResult.getCoupletNo(), distributionOrdersetInfoResult);
		}
		//mapResult = listResult.stream().collect(Collectors.toMap(DistributionOrdersetInfoResult::getCoupletNo, DistributionOrdersetInfoResult ->DistributionOrdersetInfoResult));
		for (DistributionOrdersetInfoResult distributionOrdersetInfoResult : listResult) {
			DistributionOrdersetInfoResult detail = mapResult.get(distributionOrdersetInfoResult.getCoupletNo());
			if(detail != null) {
				OrderSetDetailGoodsDto goodsInfo = new OrderSetDetailGoodsDto();
				goodsInfo.setGoodsName(distributionOrdersetInfoResult.getGoodsName());
				goodsInfo.setGoodsNumber(distributionOrdersetInfoResult.getGoodsNumber());
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

	@Override
	public List<String> getOrdersetNoByDistributionNo(String distributionNo) {
		return	distributionOrdersetExtMapper.getOrdersetNoByDistributionNo(distributionNo);
	}

	@Override
	public List<Long> getOrdersetIdByDistributionNo(String distributionNo) {
		return	distributionOrdersetExtMapper.getOrdersetIdByDistributionNo(distributionNo);
	}

	@Override
	public Pagination<OrderSetDetailResultDto> pageSearchOrderSet(OrderSetReqDto dto) {
		log.info("分页条件查询集单信息{}",JSON.toJSONString(dto));
		Pagination<OrderSetDetailResultDto> result = new Pagination<>();
//		result.setPageNo(param.getPageNo());
//		result.setPageSize(param.getPageSize());
//		Map<String,Object> mapParam = new HashMap<String,Object>();
//		if(StringUtils.isNotBlank(param.getBeginDate())) {
//			mapParam.put("deliveryDateStart",DateUtil.parseDate(param.getBeginDate(), DateUtil.YYYY_MM_DD));
//		}
//		if(StringUtils.isNotBlank(param.getEndDate())) {
//			mapParam.put("deliveryDateEnd",DateUtil.parseDate(param.getEndDate(), DateUtil.YYYY_MM_DD));
//		}
//		mapParam.put("shopId",param.getShopId());
//		mapParam.put("startRow",param.getStartRow());
//		mapParam.put("pageSize",param.getPageSize());
//		mapParam.put("building",param.getBuilding());
//		long count =orderSetDetailExtMapper.selectCountOrderSetByPage(mapParam);
//		List<OrderSetDetailDto> list =	orderSetDetailExtMapper.selectListOrderSetByPage(mapParam);
//		List<OrderSetDetailResultDto> listResult = BeanMapper.mapList(list, OrderSetDetailResultDto.class);
//		List<String> orderSetList = list.stream().map(OrderSetDetailDto::getDetailSetNo).collect(Collectors.toList());
//		Map<String,Integer> customerCountMap = getCustomerSumByListSetNo(orderSetList);
//		listResult.stream().forEach(item -> {
//			item.setCustomerSum(customerCountMap.get(item.getDetailSetNo()));
//		});
//		result.setTotalCount(count);
//		result.setDataList(listResult);
		return result;
	}
	
	public Map<String,Integer> getCustomerSumByListSetNo(List<String> list){
		log.info("根据集单号查询有多少顾客单{}",JSON.toJSONString(list));
		Map<String,Integer> resultMap = new HashMap<String, Integer>();
		if(CollectionUtils.isEmpty(list)) {
			return resultMap;
		}
		List<Map<String, Object>> listMap =	orderSetDetailExtMapper.selectCountCustomerOrderBySetNo(list);
		for (Map<String, Object> map : listMap) {
			resultMap.put(map.get("detail_set_no").toString(), Integer.parseInt(map.get("coupletNo").toString()));
		}
		return resultMap;
	}

	@Override
	public List<DistributionOrdersetInfoResult> getCustomerOrderByOrderSetNo(String setNo) {
		log.info("根据集单号查询{}",setNo);
		List<DistributionOrdersetInfoResult>  listResult = distributionOrdersetExtMapper.getCustomerOrderByOrderSetNo(setNo);
		Map<String,DistributionOrdersetInfoResult> mapResult = new TreeMap<String,DistributionOrdersetInfoResult>();
		for (DistributionOrdersetInfoResult distributionOrdersetInfoResult : listResult) {
			mapResult.put(distributionOrdersetInfoResult.getCoupletNo(), distributionOrdersetInfoResult);
		}
		for (DistributionOrdersetInfoResult distributionOrdersetInfoResult : listResult) {
			DistributionOrdersetInfoResult detail = mapResult.get(distributionOrdersetInfoResult.getCoupletNo());
			if(detail != null) {
				OrderSetDetailGoodsDto goodsInfo = new OrderSetDetailGoodsDto();
				goodsInfo.setGoodsName(distributionOrdersetInfoResult.getGoodsName());
				goodsInfo.setGoodsNumber(distributionOrdersetInfoResult.getGoodsNumber());
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

}

package com.milisong.scm.printer.web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.gp.port.Drive;
import com.milisong.scm.printer.Template.jiabo.GainschaBillTemplate;
import com.milisong.scm.printer.Template.jiabo.GainschaBillTemplateByTsc;
import com.milisong.scm.printer.Template.jiabo.GainschaBillTemplateByTscAndBreakfast;
import com.milisong.scm.printer.Template.jiabo.JiaBoEcsPrintTemplate;
import com.milisong.scm.printer.api.PosPrintService;
import com.milisong.scm.printer.dto.OrderDetailInfo;
import com.milisong.scm.printer.request.BuildingDto;
import com.milisong.scm.printer.request.DistributionOrdersetInfoDto;
import com.milisong.scm.printer.request.DistributionOrdersetResult;
import com.milisong.scm.printer.request.DistributionOrdersetResultDto;
import com.milisong.scm.printer.request.NotifyOrderSetQueryResult;
import com.milisong.scm.printer.request.OrderDetailResult;
import com.milisong.scm.printer.request.OrderSetDetailDto;
import com.milisong.scm.printer.request.PickOrderPrintQueryResult;
import com.milisong.scm.printer.request.mq.NotifyOrderSetQueryResultByBreakfast;
import com.milisong.scm.printer.request.mq.OrderSetDetailGoodsDto;
import com.milisong.scm.printer.request.mq.OrderSetProductionMsg;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/print")
@CrossOrigin
public class PrintRest {
	@Autowired
	private GainschaBillTemplate template;
	private static Drive dr = null;
	private static Drive dr2 = null;
//	static {
//		dr = new Drive();
//		//boolean b = dr.opendrive("GP-H80300 Series");
//		boolean b = dr.opendrive("ZH3080(标签)");
////		dr2 = new Drive();
////		b = dr2.opendrive("ZH308I(标签)");
//		if (b)
//			log.info("【ZH3080(标签)】打印机初始化ok");
//			else {
//				System.out.println("【ZH3080(标签)】打印机初始化false");
//			//	dr.opendrive("ZH3080(标签)");
//			}
//	}
	@Autowired
	GainschaBillTemplateByTscAndBreakfast templateTscBreakfast;
	
	@Autowired
	GainschaBillTemplateByTsc templateTsc;
	
	@Autowired 
	PosPrintService  posPrintServiceImpl;
	
	@PostMapping("/print-all-tsc")
	public ResponseResult<String> printAllByTsc(@RequestBody NotifyOrderSetQueryResultByBreakfast notifyOrderSetQueryResult){
		log.info("打印集单联-(标签打印机),{}",JSONObject.toJSONString(notifyOrderSetQueryResult));
		
		OrderSetProductionMsg msg = new OrderSetProductionMsg();
		msg.setGoods(notifyOrderSetQueryResult.getGoodsList());
		msg.setOrderSet(notifyOrderSetQueryResult.getOrderSet());
		msg.setCoupletNo(notifyOrderSetQueryResult.getCoupletNo());
		String payload = JSON.toJSONString(msg);
		posPrintServiceImpl.printOrdeSet(payload,notifyOrderSetQueryResult.getPrintType());
//		Map<String,Object> map = new HashMap<String,Object>();
//		//获取集单联公司楼宇信息
//		OrderSetDetailDto orderSetDetailsInfo = notifyOrderSetQueryResult.getOrderSet();
//		List<OrderSetDetailGoodsDto> listOrderSet = notifyOrderSetQueryResult.getGoodsList();
//		List<OrderDetailInfo> list = new ArrayList<OrderDetailInfo>();
//		Map<String,Integer> goodsCount = new HashMap<String, Integer>();
//		Map<String,Integer> orderGoodsCountMap = new HashMap<String,Integer>();
//		Map<String,List<OrderSetDetailGoodsDto>> customerMap = new LinkedHashMap<String,List<OrderSetDetailGoodsDto>>();
//		map.put("buildingAbbreviation", orderSetDetailsInfo.getBuildingAbbreviation());
//		map.put("deliveryDate", DateFormatUtils.format(orderSetDetailsInfo.getDetailDeliveryDate(), "yyyy-MM-dd"));
//		String detailDeliveryAddress = orderSetDetailsInfo.getDetailDeliveryAddress();
//		String address = detailDeliveryAddress.substring(0, detailDeliveryAddress.length()-orderSetDetailsInfo.getCompanyAbbreviation().length());
//		map.put("detailDeliveryAddress", address);
//		map.put("company", orderSetDetailsInfo.getDeliveryFloor()+"/"+orderSetDetailsInfo.getCompanyAbbreviation());
//		List<OrderDetailResult>  orderDetailResultlis = notifyOrderSetQueryResult.getListOrderDetails();
//		if (!CollectionUtils.isEmpty(orderDetailResultlis)) {
//			for (OrderDetailResult orderDetailResult : orderDetailResultlis) {
//				//统计订单总商品数量
//				Integer orderGoodsCount = orderGoodsCountMap.get(orderDetailResult.getOrderNo());
//				if( orderGoodsCount == null) {
//					orderGoodsCount = 0 ;
//				}
//				orderGoodsCount = orderGoodsCount + orderDetailResult.getGoodsCount();
//				orderGoodsCountMap.put(orderDetailResult.getOrderNo(), orderGoodsCount);
//			}
//		}
//		for (OrderSetDetailGoodsDto orderSetDetailGoodsDto : listOrderSet) {
//			OrderDetailInfo orderInfo = new OrderDetailInfo();
//			orderInfo.setName(orderSetDetailGoodsDto.getUserName());
//			orderInfo.setGoodsName(orderSetDetailGoodsDto.getGoodsName());
//			orderInfo.setPhone(orderSetDetailGoodsDto.getUserPhone());
//			orderInfo.setGoodsCount(orderSetDetailGoodsDto.getGoodsNumber());
//			list.add(orderInfo);
//			Integer count = goodsCount.get(orderSetDetailGoodsDto.getGoodsName());
//			if (count == null ) {
//				count = 0;
//			}
//			//统计商品总数量
//			count = count + orderSetDetailGoodsDto.getGoodsNumber() ;
//			goodsCount.put(orderSetDetailGoodsDto.getGoodsName(),count);
//			// 客户联数据组装
//			//Long userId = orderSetDetailGoodsDto.getUserId();
//			String coupletNo = orderSetDetailGoodsDto.getCoupletNo();
//			List<OrderSetDetailGoodsDto> listOrderInfo = customerMap.get(coupletNo);
//			if(CollectionUtils.isEmpty(listOrderInfo)) {
//				listOrderInfo = new ArrayList<OrderSetDetailGoodsDto>();
//			}
//			listOrderInfo.add(orderSetDetailGoodsDto);
//			customerMap.put(coupletNo, listOrderInfo);
//		}
//		map.put("goodsCountMap", goodsCount);
//		map.put("customerGoodsInfo", list);
//		map.put("setNo", orderSetDetailsInfo.getDetailSetNoDescription());
//		map.put("orderGoodsCountMap", orderGoodsCountMap);
//		byte[]  setOrder =templateTsc.setOrderTemplate(map);
//		log.info("打印集单联");
//		Drive d1 = new Drive();
//		 d1.opendrive("ZH3080(标签)");
//		if(notifyOrderSetQueryResult.getPrintType() == 1) {
//			d1.sendMessage(setOrder);
//		}
//		if(notifyOrderSetQueryResult.getPrintType() == 2) {
//			log.info("打印客户联{}",notifyOrderSetQueryResult.getCoupletNo());
//			List<OrderSetDetailGoodsDto> listCustomer = customerMap.get(notifyOrderSetQueryResult.getCoupletNo());
//		
//			for (OrderSetDetailGoodsDto orderSetDetailGoodsDto : listCustomer) {
//				Integer goodsCounts = orderSetDetailGoodsDto.getGoodsNumber();
//				for (int i = 0; i < goodsCounts; i++) {
//					List<OrderSetDetailGoodsDto> CustomerInfo = new ArrayList<OrderSetDetailGoodsDto>();
//					orderSetDetailGoodsDto.setGoodsNumber(1);
//					CustomerInfo.add(orderSetDetailGoodsDto);
//					byte[]  customer = templateTsc.customerTemplate(CustomerInfo,map);
//					d1.sendMessage(customer);
//				}
//			}
//		}
		return ResponseResult.buildSuccessResponse();
	}

	
	@PostMapping("/print-all-tsc-breakfast")
	public ResponseResult<String> printAllByTscBreakfast(@RequestBody NotifyOrderSetQueryResultByBreakfast notifyOrderSetQueryResult){
		log.info("早餐手动补偿打印参数:{}",JSON.toJSONString(notifyOrderSetQueryResult));
		com.milisong.scm.printer.request.mq.OrderSetDetailDto orderSetDetailsInfo = notifyOrderSetQueryResult.getOrderSet();
		List<OrderSetDetailGoodsDto> listOrderSet = notifyOrderSetQueryResult.getGoodsList();
		OrderSetProductionMsg msg = new OrderSetProductionMsg();
		msg.setGoods(listOrderSet);
		msg.setOrderSet(orderSetDetailsInfo);
		msg.setCoupletNo(notifyOrderSetQueryResult.getCoupletNo());
		String payload = JSON.toJSONString(msg);
		posPrintServiceImpl.printOrdeBreakfastSet(payload,notifyOrderSetQueryResult.getPrintType());
		//		log.info("打印集单联-(标签打印机),{}",JSONObject.toJSONString(notifyOrderSetQueryResult));
//		Map<String,Object> map = new HashMap<String,Object>();
//		//获取集单联公司楼宇信息
//		OrderSetDetailDto orderSetDetailsInfo = notifyOrderSetQueryResult.getOrderSet();
//		List<OrderSetDetailGoodsDto> listOrderSet = notifyOrderSetQueryResult.getGoodsList();
//		List<OrderDetailInfo> list = new ArrayList<OrderDetailInfo>();
//		Map<String,Integer> goodsCount = new HashMap<String, Integer>();
//		Map<String,Integer> orderGoodsCountMap = new HashMap<String,Integer>();
//		Map<String,List<OrderSetDetailGoodsDto>> customerMap = new LinkedHashMap<String,List<OrderSetDetailGoodsDto>>();
//		map.put("buildingAbbreviation", orderSetDetailsInfo.getBuildingAbbreviation());
//		map.put("deliveryDate", DateFormatUtils.format(orderSetDetailsInfo.getDetailDeliveryDate(), "yyyy-MM-dd"));
//		String detailDeliveryAddress = orderSetDetailsInfo.getDetailDeliveryAddress();
//		String address = detailDeliveryAddress.substring(0, detailDeliveryAddress.length()-orderSetDetailsInfo.getCompanyAbbreviation().length());
//		map.put("detailDeliveryAddress", address);
//		map.put("company", orderSetDetailsInfo.getDeliveryFloor()+"/"+orderSetDetailsInfo.getCompanyAbbreviation());
//		List<OrderDetailResult>  orderDetailResultlis = notifyOrderSetQueryResult.getListOrderDetails();
//		if (!CollectionUtils.isEmpty(orderDetailResultlis)) {
//			for (OrderDetailResult orderDetailResult : orderDetailResultlis) {
//				//统计订单总商品数量
//				Integer orderGoodsCount = orderGoodsCountMap.get(orderDetailResult.getOrderNo());
//				if( orderGoodsCount == null) {
//					orderGoodsCount = 0 ;
//				}
//				orderGoodsCount = orderGoodsCount + orderDetailResult.getGoodsCount();
//				orderGoodsCountMap.put(orderDetailResult.getOrderNo(), orderGoodsCount);
//			}
//		}
//		Map<String,OrderDetailInfo> mapOrderDetailInfo = new HashMap<String, OrderDetailInfo>();
//		for (OrderSetDetailGoodsDto orderSetDetailGoodsDto : listOrderSet) {
//			OrderDetailInfo orderInfo = new OrderDetailInfo();
//			orderInfo.setName(orderSetDetailGoodsDto.getUserName());
//			orderInfo.setGoodsName(orderSetDetailGoodsDto.getGoodsName());
//			orderInfo.setPhone(orderSetDetailGoodsDto.getUserPhone());
//			orderInfo.setGoodsCount(orderSetDetailGoodsDto.getGoodsNumber());
//			OrderDetailInfo orderDetailInfo = mapOrderDetailInfo.get(orderSetDetailGoodsDto.getUserName());
//			if(orderDetailInfo == null ) {
//				list.add(orderInfo);
//			}
//			mapOrderDetailInfo.put(orderSetDetailGoodsDto.getUserName(),orderInfo);
//			Integer count = goodsCount.get(orderSetDetailGoodsDto.getGoodsName());
//			if (count == null ) {
//				count = 0;
//			}
//			//统计商品总数量
//			count = count + orderSetDetailGoodsDto.getGoodsNumber() ;
//			goodsCount.put(orderSetDetailGoodsDto.getGoodsName(),count);
//			// 客户联数据组装
//			//Long userId = orderSetDetailGoodsDto.getUserId();
//			String coupletNo = orderSetDetailGoodsDto.getCoupletNo();
//			List<OrderSetDetailGoodsDto> listOrderInfo = customerMap.get(coupletNo);
//			if(CollectionUtils.isEmpty(listOrderInfo)) {
//				listOrderInfo = new ArrayList<OrderSetDetailGoodsDto>();
//			}
//			listOrderInfo.add(orderSetDetailGoodsDto);
//			customerMap.put(coupletNo, listOrderInfo);
//		}
//		map.put("goodsCountMap", goodsCount);
//		map.put("customerGoodsInfo", list);
//		map.put("setNo", orderSetDetailsInfo.getDetailSetNoDescription());
//		map.put("orderGoodsCountMap", orderGoodsCountMap);
//		byte[]  setOrder =templateTscBreakfast.setOrderTemplate(map);
//		log.info("打印集单联");
//		Drive d1 = new Drive();
//		d1.opendrive("ZH3080(标签)");
//		if(notifyOrderSetQueryResult.getPrintType() == 1) {
//			d1.sendMessage(setOrder);
//		}
//		if(notifyOrderSetQueryResult.getPrintType() == 2) {
//			log.info("打印客户联{}",notifyOrderSetQueryResult.getCoupletNo());
//			List<OrderSetDetailGoodsDto> listCustomer = customerMap.get(notifyOrderSetQueryResult.getCoupletNo());
//			byte[]  customer = templateTscBreakfast.customerTemplate(listCustomer,map);
//			d1.sendMessage(customer);
//
//		}
		return ResponseResult.buildSuccessResponse();
	}
	
	@PostMapping("/print-all")
	public ResponseResult<String> printALl(@RequestBody DistributionOrdersetInfoDto distributionOrdersetInfoDto){
		DistributionOrdersetResultDto  distributionOrdersetResultDto = distributionOrdersetInfoDto.getDistributionOrdersetResultDto();
		printDistributedOrder(distributionOrdersetResultDto);
		List<NotifyOrderSetQueryResult> listNotifyOrderSet = distributionOrdersetInfoDto.getOrderSet();
		for (NotifyOrderSetQueryResult notifyOrderSetQueryResult : listNotifyOrderSet) {
			printCollectionOrder(notifyOrderSetQueryResult);
		}
		return ResponseResult.buildSuccessResponse();
	}
	
	@PostMapping("/print-distribution-order")
	public ResponseResult<String> printDistributedOrder(@RequestBody DistributionOrdersetResultDto distributionOrdersetResutlDto){
		log.info("打印配送联,{}",JSONObject.toJSONString(distributionOrdersetResutlDto));
		Map<String,Object> map = new HashMap<String,Object>();
		List<DistributionOrdersetResult> distributionOrdersetResutl = distributionOrdersetResutlDto.getDistributionOrdersetResult();
		List<BuildingDto> buildingDtoList = distributionOrdersetResutlDto.getBuildingDto();
		Map<Long,BuildingDto> buildingMaps = null;
		if(CollectionUtils.isEmpty(distributionOrdersetResutl)) {
			return ResponseResult.buildFailResponse("9999","参数不能为空");
		}
		if(!CollectionUtils.isEmpty(buildingDtoList)) {
			buildingMaps = buildingDtoList.stream().collect(Collectors.toMap(BuildingDto::getId, BuildingDto -> BuildingDto));
			
		}
		map.put("buildingMaps", buildingMaps);
		DistributionOrdersetResult distributionOrdersetResult = distributionOrdersetResutl.get(0);
		Map<Long,Set<String>> mapResult = new HashMap<Long,Set<String>>();
		for (DistributionOrdersetResult distributionOrderset : distributionOrdersetResutl) {
			Long buildingId = distributionOrderset.getBuildingId();
			Set<String> set = mapResult.get(buildingId);
			if(CollectionUtils.isEmpty(set)) {
				set = new HashSet<String>();
			}
			String value = distributionOrderset.getDeliveryFloor()+"层"+"  "+distributionOrderset.getCompanyAbbreviation();
			set.add(value);
			mapResult.put(buildingId, set);
		}
		map.put("distributionDescription", distributionOrdersetResult.getDistributionDescription());
		map.put("deliveryDate",DateFormatUtils.format(distributionOrdersetResult.getDeliveryDate(),"yyyy-MM-dd"));
		map.put("distributionOrdersetResultMap", mapResult);
		byte[]  distributed =template.distributedTemplate(map);
		dr.sendMessage(distributed);
		return ResponseResult.buildSuccessResponse();
	}
	
	@PostMapping("/print-pick-order")
	public ResponseResult<String> printPickOrder(@RequestBody PickOrderPrintQueryResult pickOrderPrintQueryResult){
		log.info("打印拣货单,{}",JSONObject.toJSONString(pickOrderPrintQueryResult));
		if(null == pickOrderPrintQueryResult) {
			return ResponseResult.buildFailResponse("9999","参数不能为空");
		}
		List<DistributionOrdersetResult> distributionOrdersetResultList	= pickOrderPrintQueryResult.getSetNoList();
		DistributionOrdersetResult distributionOrdersetResult = distributionOrdersetResultList.get(0);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("deliveryDate",DateFormatUtils.format(distributionOrdersetResult.getDeliveryDate(),"yyyy-MM-dd"));
		map.put("orderSetNo",distributionOrdersetResultList);
		map.put("distributionDescription", distributionOrdersetResult.getDistributionDescription());
		map.put("goodsList", pickOrderPrintQueryResult.getGoodsList());
		byte[]  pickOrder =template.pickOrderTemplate(map);
		dr.sendMessage(pickOrder);
		return ResponseResult.buildSuccessResponse();
	}
	
	@PostMapping("/print-collection-order")
	public ResponseResult<String> printCollectionOrder(@RequestBody NotifyOrderSetQueryResult notifyOrderSetQueryResult){
		log.info("打印集单联,{}",JSONObject.toJSONString(notifyOrderSetQueryResult));
		Map<String,Object> map = new HashMap<String,Object>();
		//获取集单联公司楼宇信息
		OrderSetDetailDto orderSetDetailsInfo = notifyOrderSetQueryResult.getOrderSet();
		List<OrderSetDetailGoodsDto> listOrderSet = notifyOrderSetQueryResult.getGoodsList();
		List<OrderDetailInfo> list = new ArrayList<OrderDetailInfo>();
		Map<String,Integer> goodsCount = new HashMap<String, Integer>();
		Map<String,Integer> orderGoodsCountMap = new HashMap<String,Integer>();
		Map<String,List<OrderSetDetailGoodsDto>> customerMap = new LinkedHashMap<String,List<OrderSetDetailGoodsDto>>();
		List<DistributionOrdersetResult>  distributionOrdersetResultsList = notifyOrderSetQueryResult.getDistributionOrdersetResultList();
		Map<String,DistributionOrdersetResult> distributionOrdersetResultsMap = new HashMap<String, DistributionOrdersetResult>();
		for (DistributionOrdersetResult distributionOrdersetResult : distributionOrdersetResultsList) {
			distributionOrdersetResultsMap.put(distributionOrdersetResult.getDetailSetNoDescription(), distributionOrdersetResult);
		}
		map.put("distributionOrdersetResultsMap", distributionOrdersetResultsMap);
		map.put("distributionNo",notifyOrderSetQueryResult.getDistributionNo());
		map.put("distributionDescription", distributionOrdersetResultsList.get(0).getDistributionDescription());
		map.put("detailDeliveryDate", DateFormatUtils.format(orderSetDetailsInfo.getDetailDeliveryDate(), "yyyy-MM-dd"));
		map.put("orderSetDetailsInfo", orderSetDetailsInfo);
		map.put("detailDeliveryAddress", orderSetDetailsInfo.getDetailDeliveryAddress());
		List<OrderDetailResult>  orderDetailResultlis = notifyOrderSetQueryResult.getListOrderDetails();
		if (!CollectionUtils.isEmpty(orderDetailResultlis)) {
			for (OrderDetailResult orderDetailResult : orderDetailResultlis) {
				//统计订单总商品数量
				Integer orderGoodsCount = orderGoodsCountMap.get(orderDetailResult.getOrderNo());
				if( orderGoodsCount == null) {
					orderGoodsCount = 0 ;
				}
				orderGoodsCount = orderGoodsCount + orderDetailResult.getGoodsCount();
				orderGoodsCountMap.put(orderDetailResult.getOrderNo(), orderGoodsCount);
			}
		}
		for (OrderSetDetailGoodsDto orderSetDetailGoodsDto : listOrderSet) {
			OrderDetailInfo orderInfo = new OrderDetailInfo();
			orderInfo.setName(orderSetDetailGoodsDto.getUserName());
			orderInfo.setGoodsName(orderSetDetailGoodsDto.getGoodsName());
			orderInfo.setPhone(orderSetDetailGoodsDto.getUserPhone());
			orderInfo.setGoodsCount(orderSetDetailGoodsDto.getGoodsNumber());
			list.add(orderInfo);
			Integer count = goodsCount.get(orderSetDetailGoodsDto.getGoodsName());
			if (count == null ) {
				count = 0;
			}
			//统计商品总数量
			count = count + orderSetDetailGoodsDto.getGoodsNumber() ;
			goodsCount.put(orderSetDetailGoodsDto.getGoodsName(),count);
			// 客户联数据组装
			//Long userId = orderSetDetailGoodsDto.getUserId();
			String coupletNo = orderSetDetailGoodsDto.getCoupletNo();
			List<OrderSetDetailGoodsDto> listOrderInfo = customerMap.get(coupletNo);
			if(CollectionUtils.isEmpty(listOrderInfo)) {
				listOrderInfo = new ArrayList<OrderSetDetailGoodsDto>();
			}
			listOrderInfo.add(orderSetDetailGoodsDto);
			customerMap.put(coupletNo, listOrderInfo);
		}
		map.put("goodsCountMap", goodsCount);
		map.put("detailsList", list);
		map.put("orderSetNo", orderSetDetailsInfo.getDetailSetNoDescription());
		map.put("orderGoodsCountMap", orderGoodsCountMap);
		byte[]  setOrder =template.setOrderTemplate(map);
		log.info("打印集单联");
		dr.sendMessage(setOrder);
		for (String coupletNo : customerMap.keySet()) {
			log.info("打印客户联{}",coupletNo);
			List<OrderSetDetailGoodsDto> listCustomer = customerMap.get(coupletNo);
			byte[]  customer = template.customerTemplate(listCustomer,map);
			dr.sendMessage(customer);
		}
		return ResponseResult.buildSuccessResponse();
	}
	
	/**
	 * 打印集单
	 * @param notifyOrderSetQueryResult
	 * @return
	 */
	@PostMapping("/print-collection-orderset")
	public ResponseResult<String> printCollectionOrderset(@RequestBody NotifyOrderSetQueryResult notifyOrderSetQueryResult){
		log.info("打印集单联,{}",JSONObject.toJSONString(notifyOrderSetQueryResult));
		Map<String,Object> map = new HashMap<String,Object>();
		//获取集单联公司楼宇信息
		OrderSetDetailDto orderSetDetailsInfo = notifyOrderSetQueryResult.getOrderSet();
		List<OrderSetDetailGoodsDto> listOrderSet = notifyOrderSetQueryResult.getGoodsList();
		List<OrderDetailInfo> list = new ArrayList<OrderDetailInfo>();
		Map<String,Integer> goodsCount = new HashMap<String, Integer>();
		Map<String,Integer> orderGoodsCountMap = new HashMap<String,Integer>();
		Map<String,List<OrderSetDetailGoodsDto>> customerMap = new LinkedHashMap<String,List<OrderSetDetailGoodsDto>>();
		List<DistributionOrdersetResult>  distributionOrdersetResultsList = notifyOrderSetQueryResult.getDistributionOrdersetResultList();
		Map<String,DistributionOrdersetResult> distributionOrdersetResultsMap = new HashMap<String, DistributionOrdersetResult>();
		for (DistributionOrdersetResult distributionOrdersetResult : distributionOrdersetResultsList) {
			distributionOrdersetResultsMap.put(distributionOrdersetResult.getDetailSetNoDescription(), distributionOrdersetResult);
		}
		map.put("distributionOrdersetResultsMap", distributionOrdersetResultsMap);
		map.put("distributionNo",notifyOrderSetQueryResult.getDistributionNo());
		map.put("distributionDescription", distributionOrdersetResultsList.get(0).getDistributionDescription());
		map.put("detailDeliveryDate", DateFormatUtils.format(orderSetDetailsInfo.getDetailDeliveryDate(), "yyyy-MM-dd"));
		map.put("orderSetDetailsInfo", orderSetDetailsInfo);
		map.put("detailDeliveryAddress", orderSetDetailsInfo.getDetailDeliveryAddress());
		List<OrderDetailResult>  orderDetailResultlis = notifyOrderSetQueryResult.getListOrderDetails();
		if (!CollectionUtils.isEmpty(orderDetailResultlis)) {
			for (OrderDetailResult orderDetailResult : orderDetailResultlis) {
				//统计订单总商品数量
				Integer orderGoodsCount = orderGoodsCountMap.get(orderDetailResult.getOrderNo());
				if( orderGoodsCount == null) {
					orderGoodsCount = 0 ;
				}
				orderGoodsCount = orderGoodsCount + orderDetailResult.getGoodsCount();
				orderGoodsCountMap.put(orderDetailResult.getOrderNo(), orderGoodsCount);
			}
		}
		for (OrderSetDetailGoodsDto orderSetDetailGoodsDto : listOrderSet) {
			OrderDetailInfo orderInfo = new OrderDetailInfo();
			orderInfo.setName(orderSetDetailGoodsDto.getUserName());
			orderInfo.setGoodsName(orderSetDetailGoodsDto.getGoodsName());
			orderInfo.setPhone(orderSetDetailGoodsDto.getUserPhone());
			orderInfo.setGoodsCount(orderSetDetailGoodsDto.getGoodsNumber());
			list.add(orderInfo);
			Integer count = goodsCount.get(orderSetDetailGoodsDto.getGoodsName());
			if (count == null ) {
				count = 0;
			}
			//统计商品总数量
			count = count + orderSetDetailGoodsDto.getGoodsNumber() ;
			goodsCount.put(orderSetDetailGoodsDto.getGoodsName(),count);
			// 客户联数据组装
			//Long userId = orderSetDetailGoodsDto.getUserId();
			String coupletNo = orderSetDetailGoodsDto.getCoupletNo();
			List<OrderSetDetailGoodsDto> listOrderInfo = customerMap.get(coupletNo);
			if(CollectionUtils.isEmpty(listOrderInfo)) {
				listOrderInfo = new ArrayList<OrderSetDetailGoodsDto>();
			}
			listOrderInfo.add(orderSetDetailGoodsDto);
			customerMap.put(coupletNo, listOrderInfo);
		}
		map.put("goodsCountMap", goodsCount);
		map.put("detailsList", list);
		map.put("orderSetNo", orderSetDetailsInfo.getDetailSetNoDescription());
		map.put("orderGoodsCountMap", orderGoodsCountMap);
		byte[]  setOrder =template.setOrderTemplate(map);
		log.info("打印集单联");
		if(notifyOrderSetQueryResult.getPrintType() == 1) {
			dr.sendMessage(setOrder);
		}
		if(notifyOrderSetQueryResult.getPrintType() == 2) {
			for (String coupletNo : customerMap.keySet()) {
				log.info("打印客户联{}",coupletNo);
				List<OrderSetDetailGoodsDto> listCustomer = customerMap.get(coupletNo);
				byte[]  customer = template.customerTemplate(listCustomer,map);
				dr.sendMessage(customer);
			}
		}
		return ResponseResult.buildSuccessResponse();
	}
	
	
	@PostMapping("/print-order")
	@Deprecated
	public ResponseResult<String> printOrder(@RequestBody NotifyOrderSetQueryResult notifyOrderSetQueryResult) {
		log.info("打印小票:{}",JSONObject.toJSONString(notifyOrderSetQueryResult));
		Map<String,Object> map = new HashMap<String,Object>();
		OrderSetDetailDto orderSetDetailsInfo = notifyOrderSetQueryResult.getOrderSet();
		List<OrderSetDetailGoodsDto> listOrderSet = notifyOrderSetQueryResult.getGoodsList();
		List<OrderDetailInfo> list = new ArrayList<OrderDetailInfo>();
		Map<String,Integer> goodsCount = new HashMap<String, Integer>();
		Map<Long,List<OrderSetDetailGoodsDto>> customerMap = new HashMap<Long,List<OrderSetDetailGoodsDto>>();
		for (OrderSetDetailGoodsDto orderSetDetailGoodsDto : listOrderSet) {
			OrderDetailInfo orderInfo = new OrderDetailInfo();
			orderInfo.setName(orderSetDetailGoodsDto.getUserName());
			orderInfo.setGoodsName(orderSetDetailGoodsDto.getGoodsName());
			orderInfo.setPhone(orderSetDetailGoodsDto.getUserPhone());
			orderInfo.setGoodsCount(orderSetDetailGoodsDto.getGoodsNumber());
			list.add(orderInfo);
			Integer count = goodsCount.get(orderSetDetailGoodsDto.getGoodsName());
			if (count ==null ) {
				count = 0;
			}
			count = count + orderSetDetailGoodsDto.getGoodsNumber() ;
			goodsCount.put(orderSetDetailGoodsDto.getGoodsName(),count);
			// 客户联数据组装
			Long userId = orderSetDetailGoodsDto.getUserId();
			List<OrderSetDetailGoodsDto> listOrderInfo = customerMap.get(userId);
			if(CollectionUtils.isEmpty(listOrderInfo)) {
				listOrderInfo = new ArrayList<OrderSetDetailGoodsDto>();
			}
			listOrderInfo.add(orderSetDetailGoodsDto);
			customerMap.put(userId, listOrderInfo);
		}
		map.put("detailSetNoDescription", orderSetDetailsInfo.getDetailSetNoDescription());
		map.put("detailDeliveryDate", DateFormatUtils.format(orderSetDetailsInfo.getDetailDeliveryDate(), "yyyy-MM-dd"));
		map.put("detailDeliveryAddress", orderSetDetailsInfo.getDetailDeliveryAddress());
		map.put("goodsCountMap", goodsCount);
		map.put("detailsList", list);
		map.put("detailSetNo", orderSetDetailsInfo.getDetailSetNo());
		map.put("detailDeliveryAddress", orderSetDetailsInfo.getDetailDeliveryAddress());
		
		
		JiaBoEcsPrintTemplate template = new JiaBoEcsPrintTemplate();
		//调用门店联
		byte[]  shop =template.shopTemplate(map);
		dr.sendMessage(shop);
		//调用配送联
		byte[]  distributed =template.distributedTemplate(map);
		dr.sendMessage(distributed);
		//调用客户联
		for (Long userId : customerMap.keySet()) {
			List<OrderSetDetailGoodsDto> listCustomer = customerMap.get(userId);
			byte[]  customer = template.customerTemplate(listCustomer,map);
			dr.sendMessage(customer);
		}
		return ResponseResult.buildSuccessResponse();
	}
	
	@RequestMapping("/hello")
	public String hello(HttpServletRequest request) throws UnsupportedEncodingException{
		request.setCharacterEncoding("UTF-8");
		
//		System.out.println("hello123");
//		JiaBoEcsPrintTemplate template = new JiaBoEcsPrintTemplate();
//		byte[]  customer = template.customerTemplate();
//		byte[]  shop = template.shopTemplate();
//		byte[]  distributed = template.distributedTemplate();
//		dr.sendMessage(shop);
//		dr.sendMessage(distributed);
//		dr.sendMessage(customer);
//		JiaboTemplate.shopTemplate();
//		JiaboTemplate.distributedTemplate();
//		JiaboTemplate.customerTemplate();
//		JiaboTemplate.customerTemplate();
//		JiaboTemplate.customerTemplate();
		log.info("test");
		GainschaBillPrintTestTsc();
		return "hello";
	}
	
	public static void main(String[] args) {
//		JiaBoEcsPrintTemplate template = new JiaBoEcsPrintTemplate();
//		byte[]  customer = template.customerTemplate();
//		byte[]  shop = template.shopTemplate();
//		byte[]  distributed = template.distributedTemplate();
//		dr.sendMessage(shop);
//		dr.sendMessage(distributed);
//		dr.sendMessage(customer);
		GainschaBillPrintTestTsc();
	}
	static void GainschaBillPrintTest(){
		Map<String,Object> map = new HashMap<String,Object>();
		GainschaBillTemplate template = new GainschaBillTemplate();
		byte[]  customer = template.pickOrderTemplate(map);
		dr.sendMessage(customer);
	}
	
	static void GainschaBillPrintTestTsc() {
		GainschaBillTemplate template = new GainschaBillTemplate();
		Map<String,Object> map = new HashMap<String,Object>();
		byte[]  customer = template.pickOrderTemplate(map);
		Drive d1 = new Drive();
		d1.opendrive("ZH3080(标签)");
		d1.sendMessage(customer);
		Drive d2 = new Drive();
		d2.opendrive("ZH308I(标签)");
		d2.sendMessage(customer);
	}
	
}

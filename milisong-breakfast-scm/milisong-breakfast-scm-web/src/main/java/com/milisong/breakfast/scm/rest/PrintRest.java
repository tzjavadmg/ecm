package com.milisong.breakfast.scm.rest;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.milisong.breakfast.scm.order.api.OrderService;
import com.milisong.breakfast.scm.order.dto.result.OrderDetailResult;
import com.milisong.breakfast.scm.orderset.api.OrderSetService;
import com.milisong.breakfast.scm.orderset.dto.result.NotifyOrderSetQueryResult;
import com.milisong.breakfast.scm.orderset.dto.result.OrderSetDetailGoodsDto;
import com.milisong.breakfast.scm.orderset.dto.result.OrderSetProductionMsgByPrint;
import com.milisong.breakfast.scm.properties.OssUrlProperties;
import com.milisong.upms.utils.RestClient;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
*@author    created by benny
*@date  2018年9月14日---下午8:21:55
* 打印数据获取层
*/
@Api(tags = "打印数据查询")
@Slf4j
@RestController
@RequestMapping("/print")
public class PrintRest {

	@Autowired(required=false)
	private OrderSetService orderSetService;
 
	@Autowired(required=false)
	private OrderService orderService;
		
	@Autowired
	private OssUrlProperties ossUrlProperties;


	/**
	 * 单独打印集单联、顾客联查询数据接口
	 * @param coupletNo
	 * @param setNo
	 * @param printType
	 * @return
	 */
	@ApiOperation("获取待打印的集单数据")
	@GetMapping("/reprint-order")
	public ResponseResult<NotifyOrderSetQueryResult> reprintOrder(@RequestParam("coupletNo")String coupletNo,@RequestParam("setNo")String setNo,@RequestParam("printType")Integer printType) {
		log.info("BF_顾客联编号{}", coupletNo);
		log.info("BF_集单编号{}", JSON.toJSONString(setNo));
		log.info("BF_打印类型{}", printType);

		NotifyOrderSetQueryResult result = getRePrint(coupletNo,setNo,printType);
		if(result == null) {
			ResponseResult.buildFailResponse();
		}
		result.setCoupletNo(coupletNo);
		result.setPrintType(printType);
		return ResponseResult.buildSuccessResponse(result);
	}

	private NotifyOrderSetQueryResult getRePrint(String coupletNo,String setNo,Integer printType) {
		NotifyOrderSetQueryResult no = new NotifyOrderSetQueryResult();
		String url = ossUrlProperties.getBfReprintInfo();
		log.info("URL{}", url);
        log.info("转换后URL{}", MessageFormat.format(url,setNo,coupletNo,printType));
		String ossResult = RestClient.get(MessageFormat.format(url,setNo,coupletNo,printType));
		log.info("请求oss集单查询接口返回:{}", ossResult);
		OrderSetProductionMsgByPrint  orderInfo = JSONObject.parseObject(ossResult, OrderSetProductionMsgByPrint.class);
		if(null == orderInfo) {
			return no;
		}
		no.setGoodsList(orderInfo.getGoods());
		no.setOrderSet(orderInfo.getOrderSet());
		return no;
	}


	/**
	 * 单独打印集单联、顾客联查询数据接口
	 * @param coupletNo
	 * @param setNo
	 * @param distributionNo
	 * @param printType
	 * @return
	 */
	@ApiOperation("获取待打印的集单数据")
	@GetMapping("/print-order")
	public ResponseResult<NotifyOrderSetQueryResult> printOrder(@RequestParam("coupletNo")@ApiParam("顾客单号")String coupletNo,@RequestParam("setNo")@ApiParam("集单号")String setNo,@RequestParam("printType")@ApiParam("打印类型 1配送单 2顾客联")Integer printType) {
		log.info("单独打印集单联、顾客联查询数据接口:顾客联：{}，集单联：{},打印类型：{}",coupletNo,setNo,printType);
		NotifyOrderSetQueryResult result = getPrintOrderSetBySetNo(setNo);
		if(result == null) {
			ResponseResult.buildFailResponse();
		}
		result.setCoupletNo(coupletNo);
		result.setPrintType(printType);
		return ResponseResult.buildSuccessResponse(result);
	}
	
	private NotifyOrderSetQueryResult getPrintOrderSetBySetNo(String setNo) {
		NotifyOrderSetQueryResult no = new NotifyOrderSetQueryResult();
		log.info("查询集单信息{}", JSON.toJSONString(setNo));
	    String ossResult = RestClient.get(ossUrlProperties.getBforderSetInfo()+setNo);
	    log.info("请求oss集单查询接口返回:{}", ossResult);
		OrderSetProductionMsgByPrint  orderInfo = JSONObject.parseObject(ossResult, OrderSetProductionMsgByPrint.class);
		if(null == orderInfo) {
			return no;
		}
		no.setGoodsList(orderInfo.getGoods());
		no.setOrderSet(orderInfo.getOrderSet());
		return no;
	}

	private NotifyOrderSetQueryResult getOrdersetInfoBysetNoAndDistributionNo(String setNo, String distributionNo) {
		log.info("集单号:{}",setNo);
		NotifyOrderSetQueryResult result = orderSetService.getOrderSetInfoByDetailSetNo(setNo, null);
		log.info("获取集单明细:{}",JSONObject.toJSONString(result));
		List<OrderSetDetailGoodsDto> listResult = result.getGoodsList();
		Set<String> setOrderNo = listResult.stream().map(OrderSetDetailGoodsDto::getOrderNo).collect(Collectors.toSet());
		List<String> listOrderNo = new ArrayList<String>();
		listOrderNo.addAll(setOrderNo);
		List<OrderDetailResult> listOrderDetails = orderService.getOrderDetailInfoByOrderNo(listOrderNo);
		log.info("获取订单明细，订单号{}，查询结果{}",JSONObject.toJSONString(listOrderNo),JSONObject.toJSONString(listOrderDetails));
		Map<String,OrderDetailResult> mapOrderDetails = new HashMap<String,OrderDetailResult>();
		for (OrderDetailResult orderDetailResult : listOrderDetails) {
			mapOrderDetails.put(orderDetailResult.getOrderNo()+"-"+orderDetailResult.getGoodsCode(), orderDetailResult);
		}
		
		for (OrderSetDetailGoodsDto orderSetDetailGoodsDto : listResult) {
			String mapKey = orderSetDetailGoodsDto.getOrderNo()+"-"+orderSetDetailGoodsDto.getGoodsCode();
			OrderDetailResult orderDetail = mapOrderDetails.get(mapKey);
			orderSetDetailGoodsDto.setGoodsPrice(orderDetail.getGoodsPrice());
			orderSetDetailGoodsDto.setGoodsDiscountPrice(orderDetail.getGoodsDiscountPrice());
			orderSetDetailGoodsDto.setOrderTime(orderDetail.getCreateTime());
		}
		result.setDistributionNo(distributionNo);
		result.setListOrderDetails(listOrderDetails);
		result.setOrdersetNo(setNo);
//		List<DistributionOrdersetResult> distributionOrdersetList = distributionService.listOrderSetNoByDistributionNo(distributionNo);
//		result.setDistributionOrdersetResultList(distributionOrdersetList);
		return result;
	}
	
	  
	
	 
}

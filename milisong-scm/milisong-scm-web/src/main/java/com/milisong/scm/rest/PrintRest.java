package com.milisong.scm.rest;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.milisong.scm.dto.DistributionOrdersetInfoDto;
import com.milisong.scm.dto.DistributionOrdersetResultDto;
import com.milisong.scm.orderset.api.DistributionService;
import com.milisong.scm.orderset.api.OrderService;
import com.milisong.scm.orderset.api.OrderSetService;
import com.milisong.scm.orderset.constant.OrderSetActionEnum;
import com.milisong.scm.orderset.dto.param.UpdateOrderSetStatusParam;
import com.milisong.scm.orderset.dto.result.DistributionOrdersetResult;
import com.milisong.scm.orderset.dto.result.NotifyOrderSetQueryResult;
import com.milisong.scm.orderset.dto.result.OrderDetailResult;
import com.milisong.scm.orderset.dto.result.OrderSetDetailGoodsDto;
import com.milisong.scm.orderset.dto.result.PickOrderPrintQueryResult;
import com.milisong.scm.properties.OssUrlProperties;
import com.milisong.scm.shop.api.BuildingService;
import com.milisong.scm.shop.dto.building.BuildingDto;
import com.milisong.upms.utils.RestClient;
import com.milisong.upms.utils.UserInfoUtil;

import lombok.extern.slf4j.Slf4j;

/**
*@author    created by benny
*@date  2018年9月14日---下午8:21:55
* 打印数据获取层
*/
@Slf4j
@RestController
@RequestMapping("/print")
public class PrintRest {

	@Autowired(required=false)
	private OrderSetService orderSetService;
	@Autowired
	private DistributionService distributionService;
	
	@Autowired(required=false)
	private OrderService orderService;
	
	@Autowired
	private BuildingService buildingService;
	
	@Autowired
	private OssUrlProperties ossUrlProperties;
	
	@GetMapping("/save-print-order-log")
	public ResponseResult<String> savePrintOrderLog(@RequestParam("setNo")String setNo,@RequestParam("printType")Integer printType){
		UpdateOrderSetStatusParam param = new UpdateOrderSetStatusParam();
		param.setSetNo(setNo);
		param.setPrintType(printType);
		param.setUpdateBy(UserInfoUtil.buildUpdateBy());
		orderSetService.updatePrintStatus(param);
		return ResponseResult.buildSuccessResponse();
	}

	/**
	 * BOSS端页面补打全部/配送联/顾客联信息
	 * @param coupletNo
	 * @param setNo
	 * @param printType
	 * @return
	 */
	@GetMapping("/reprint-order")
	public ResponseResult<NotifyOrderSetQueryResult> reprintOrder(@RequestParam("coupletNo")String coupletNo,@RequestParam("setNo")String setNo,@RequestParam("printType")Integer printType) {
		log.info("顾客联编号{}", coupletNo);
		log.info("集单编号{}", JSON.toJSONString(setNo));
		log.info("打印类型{}", printType);
		String url = ossUrlProperties.getReprintOrder();
		log.info("URL{}", url);
        String ossResult = RestClient.get(MessageFormat.format(url,setNo,coupletNo,printType));
		log.info("请求oss集单查询接口返回:{}", ossResult);
		NotifyOrderSetQueryResult resultObject = JSONObject.parseObject(ossResult, NotifyOrderSetQueryResult.class);
		resultObject.setCoupletNo(coupletNo);
		resultObject.setPrintType(printType);
		return ResponseResult.buildSuccessResponse(resultObject);
	}

	
	/**
	 * 单独打印集单联、顾客联查询数据接口
	 * @param coupletNo
	 * @param setNo
	 * @param distributionNo
	 * @param printType
	 * @return
	 */
	@GetMapping("/print-order")
	public ResponseResult<NotifyOrderSetQueryResult> printOrder(@RequestParam("coupletNo")String coupletNo,@RequestParam("setNo")String setNo,@RequestParam("distributionNo") String distributionNo,@RequestParam("printType")Integer printType) {
		log.info("单独打印集单联、顾客联查询数据接口:顾客联：{}，集单联：{}配送单：{},打印类型：{}",coupletNo,setNo,distributionNo,printType);
		//NotifyOrderSetQueryResult result = getOrdersetInfoBysetNoAndDistributionNo(setNo, distributionNo);
		log.info("查询集单信息{}", JSON.toJSONString(setNo));
	    String ossResult = RestClient.get(ossUrlProperties.getOrderSetInfo()+setNo);
	    log.info("请求oss集单查询接口返回:{}", ossResult);
	    NotifyOrderSetQueryResult resultObject = JSONObject.parseObject(ossResult, NotifyOrderSetQueryResult.class);
	    
	    resultObject.setCoupletNo(coupletNo);
	    resultObject.setPrintType(printType);
		return ResponseResult.buildSuccessResponse(resultObject);
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
		List<DistributionOrdersetResult> distributionOrdersetList = distributionService.listOrderSetNoByDistributionNo(distributionNo);
		result.setDistributionOrdersetResultList(distributionOrdersetList);
		return result;
	}
	
	/**
	 * 打印配送单 查询
	 * @return
	 */
	@GetMapping("/print-distribution-order")
	public ResponseResult<DistributionOrdersetResultDto> printDistributedOrder(@RequestParam("distributionNo")String distributionNo){
		log.info("打印配送单查询：配送单号{}",distributionNo);
		if(StringUtils.isBlank(distributionNo)) {
			return ResponseResult.buildFailResponse("参数不能为空");
		}
		DistributionOrdersetResultDto distributionOrdersetResultDto = getDistributionOrder(distributionNo);
		return ResponseResult.buildSuccessResponse(distributionOrdersetResultDto);
	}

	private DistributionOrdersetResultDto getDistributionOrder(String distributionNo) {
		List<DistributionOrdersetResult> listResult = distributionService.listOrderSetNoByDistributionNo(distributionNo);
		DistributionOrdersetResultDto distributionOrdersetResultDto = new DistributionOrdersetResultDto();
		distributionOrdersetResultDto.setDistributionOrdersetResult(listResult);
		Set<Long> buildingSet = listResult.stream().map(DistributionOrdersetResult::getBuildingId).collect(Collectors.toSet());
		if(!CollectionUtils.isEmpty(buildingSet)) {
			List<Long> buildingList = new ArrayList<Long>();
			buildingList.addAll(buildingSet);
			List<BuildingDto> listBuildingDto = buildingService.getBuildingInfoByIdSet(buildingList);
			distributionOrdersetResultDto.setBuildingDto(listBuildingDto);
		}
		return distributionOrdersetResultDto;
	}
	
	/**
	 * 打印拣货单 查询
	 */
	@GetMapping("/print-pick-order")
	public ResponseResult<PickOrderPrintQueryResult> printPickOrder(@RequestParam("distributionNo")String distributionNo){
		log.info("打印拣货单查询：配送单号{}",JSONObject.toJSONString(distributionNo));
		PickOrderPrintQueryResult Data = distributionService.getPickOrderByPrint(distributionNo);
		return ResponseResult.buildSuccessResponse(Data);
	}
	
	@GetMapping("/print-all")
	public ResponseResult<DistributionOrdersetInfoDto> printAll(@RequestParam("distributionNo") String distributionNo){
		log.info("打印配送联/集单联/生成联");
		//获取配送联信息
		DistributionOrdersetResultDto distributionOrdersetResultDto = getDistributionOrder(distributionNo);
		//获取所有集单号
		List<String> listOrderset =	distributionService.getOrdersetNoByDistributionNo(distributionNo);
		List<Long> listOrdersetId =	distributionService.getOrdersetIdByDistributionNo(distributionNo);

		List<NotifyOrderSetQueryResult> orderSet = new ArrayList<NotifyOrderSetQueryResult>();
		
		for (String ordersetNo : listOrderset) {
			log.info("number,{}",ordersetNo);
			NotifyOrderSetQueryResult result = getOrdersetInfoBysetNoAndDistributionNo(ordersetNo, distributionNo);
			log.info("集单号--:{}",result.getOrdersetNo());
			orderSet.add(result);
		}
		DistributionOrdersetInfoDto result = new DistributionOrdersetInfoDto();
		result.setDistributionOrdersetResultDto(distributionOrdersetResultDto);
		result.setOrderSet(orderSet);
		log.info("数据组装完成{}",JSON.toJSONString(result));
		// 修改集单状态
		orderSetService.updateStatusByIds(listOrdersetId, UserInfoUtil.buildUpdateBy(),OrderSetActionEnum.BALE.getCode());
		orderSetService.updateStatusByIds(listOrdersetId, UserInfoUtil.buildUpdateBy(),OrderSetActionEnum.DELIVERY.getCode());
		return ResponseResult.buildSuccessResponse(result);
	}
}

package com.milisong.scm.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.milisong.scm.orderset.dto.result.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.milisong.scm.orderset.api.DistributionService;
import com.milisong.scm.orderset.api.OrderService;
import com.milisong.scm.orderset.api.OrderSetService;
import com.milisong.scm.orderset.constant.OrderSetActionEnum;
import com.milisong.scm.orderset.dto.param.OrderSetSearchParam;
import com.milisong.scm.orderset.dto.param.UpdateOrderSetStatusParam;
import com.milisong.upms.utils.UserInfoUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 集单相关的rest接口
 * @author yangzhilong
 *
 */
@Slf4j
@RestController
@RequestMapping("/order-set")
public class OrderSetRest {
	@Autowired(required=false)
	private OrderSetService orderSetService;
	@Autowired
	private DistributionService distributionService;
	@Autowired(required=false)
	private OrderService orderService;
	/**
	 * 分页条件查询集单信息
	 * @param param
	 * @return
	 */
	@PostMapping("/page-list")
	public ResponseResult<Pagination<OrderSetSearchResult>> pageList(@RequestBody OrderSetSearchParam param) {
		try {
			if(StringUtils.isNotBlank(param.getDistributionNo())) {
				List<DistributionOrdersetResult> list = distributionService.listOrderSetNoByDistributionNo(param.getDistributionNo());
				if(!CollectionUtils.isEmpty(list)) {
					param.setOrderSetNo(list.stream().map(item -> item.getDetailSetNo()).collect(Collectors.toList()));
				}
			}
			
			return ResponseResult.buildSuccessResponse(orderSetService.pageSearch(param));
		} catch (Exception e) {
			log.error("调用集单分页查询出现异常", e);
			throw e;
		}
	}

	/**
	 * 修改集单的状态
	 * @return
	 */
	@PostMapping("/update-status")
	public ResponseResult<Object> updateStatus(@RequestBody UpdateOrderSetStatusParam param) {
		if(!OrderSetActionEnum.NOTIFY.getCode().equals(param.getAction())) {
			param.setUpdateBy(UserInfoUtil.buildUpdateBy());
		} else {
			param.setUpdateBy("psy_配送员");
		}
		return orderSetService.updateStatus(param);
	}

	/**
	 * 根据订单号查询集单的状态
	 * @param orderNo
	 * @return
	 */
	@GetMapping("/query-detail-order-set-status")
	public ResponseResult<OrderSetStatusQueryResult> queryDetailOrderSetStatusByOrderNo(@RequestParam("orderNo")String orderNo) {
		return ResponseResult.buildSuccessResponse(orderSetService.queryStatusByOrderNo(orderNo));
	}
	
	/**
	 * 根据子集单好查询子集单信息
	 * @param detailSetNo
	 * @return
	 */
	@GetMapping("/query-detail-order-info")
	public ResponseResult<NotifyOrderSetQueryResult> getDetailSetInfoByDetailNo(@RequestParam(name="detailSetNo",required=false)String detailSetNo, @RequestParam(name="detailSetId",required=false)Long detailSetId) {
		return ResponseResult.buildSuccessResponse(orderSetService.getOrderSetInfoByDetailSetNo(detailSetNo, detailSetId));
	}
	
	@GetMapping("/print-order")
	@Deprecated
	public ResponseResult<NotifyOrderSetQueryResult> printOrder(@RequestParam("setNo")String setNo) {
		log.info("集单号:{}",setNo);
		NotifyOrderSetQueryResult result = orderSetService.getOrderSetInfoByDetailSetNo(setNo, null);
		log.info("获取集单明细:{}",JSONObject.toJSONString(result));
		List<OrderSetDetailGoodsDto> listResult = result.getGoodsList();
		List<String> listOrderNo = listResult.stream().map(OrderSetDetailGoodsDto::getOrderNo).collect(Collectors.toList());
		List<OrderDetailResult> listOrderDetails = orderService.getOrderDetailInfoByOrderNo(listOrderNo);
		log.info("获取订单明细，订单号{}，查询结果",JSONObject.toJSONString(listOrderNo),JSONObject.toJSONString(listOrderDetails));
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
		return ResponseResult.buildSuccessResponse(result);
	}

	@GetMapping("/get-order-set-by-order-no")
	public ResponseResult<List<OrderSetDetailDto>> getOrderSetByOrderNo(@RequestParam("orderNo")String orderNo){
		try {
			List<OrderSetDetailDto> orderSetList = orderSetService.getOrderSetByOrderNo(orderNo);
			return ResponseResult.buildSuccessResponse(orderSetList);
		} catch (Exception e) {
			return ResponseResult.buildFailResponse();
		}
	}

}

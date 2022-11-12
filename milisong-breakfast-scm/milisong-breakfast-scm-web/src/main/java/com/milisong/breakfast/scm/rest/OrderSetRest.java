package com.milisong.breakfast.scm.rest;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.milisong.breakfast.scm.order.constant.OrderTypeEnum;
import com.milisong.breakfast.scm.orderset.dto.param.OrderSetReqDto;
import com.milisong.breakfast.scm.properties.OssUrlProperties;
import com.milisong.upms.utils.RestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.milisong.breakfast.scm.constant.SysConstant;
import com.milisong.breakfast.scm.orderset.api.OrderSetService;
import com.milisong.breakfast.scm.orderset.dto.param.OrderSetSearchParam;
import com.milisong.breakfast.scm.orderset.dto.result.NotifyOrderSetQueryResult;
import com.milisong.breakfast.scm.orderset.dto.result.OrderSetDetailDto;
import com.milisong.breakfast.scm.orderset.dto.result.OrderSetDetailResultDto;
import com.milisong.breakfast.scm.orderset.dto.result.OrdersetInfoResult;
import com.milisong.upms.utils.UserInfoUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 集单相关的rest接口
 * @author yangzhilong
 *
 */
@Api(tags = "集单管理")
@Slf4j
@RestController
@RequestMapping("/order-set")
public class OrderSetRest {
	@Autowired(required=false)
	private OrderSetService orderSetService;
	@Autowired
	private OssUrlProperties ossUrlProperties;
	/**
	 * 集单管理 - 分页查询
	 * @param param
	 * @return
	 */
	@ApiOperation("分页查询集单信息")
	@PostMapping("/page-list")
	public ResponseResult<Pagination<OrderSetDetailResultDto>> pageSearchOrderSet(@RequestBody OrderSetReqDto dto) {
		ResponseResult<String> checkResult = checkShopPermission(dto.getShopId());
		if (!checkResult.isSuccess()) {
			return ResponseResult.buildFailResponse(checkResult.getCode(), checkResult.getMessage());
		}
		dto.setOrderType(OrderTypeEnum.BREAKFAST.getCode());
		log.info("分页条件查询集单信息{}", JSON.toJSONString(dto));
		String ossResult = RestClient.post(ossUrlProperties.getSearchOrderSet(), dto);
		log.info("请求oss集单查询接口返回:{}", ossResult);
		ResponseResult result = JSONObject.parseObject(ossResult, ResponseResult.class);
		if (result.isSuccess()) {
			return result;
		}
		return ResponseResult.buildFailResponse(result.getCode(),result.getMessage());
	}
	
	/**
	 * 集单管理 - 单条集单详情
	 * @param setNo
	 * @return
	 */
	@ApiOperation("根据集单编号查询详情")
	@GetMapping("/detail")
	public ResponseResult<List<OrdersetInfoResult>>  getCustomerOrderByOrderSetNo(@RequestParam("setNo") @ApiParam("集单编号") String setNo){
		String url = ossUrlProperties.getSearchCustomerOrder().concat(setNo);
		String ossResult = RestClient.get(url);
		log.info("请求oss集单详情查询接口返回:{}", ossResult);
		ResponseResult result = JSONObject.parseObject(ossResult, ResponseResult.class);
		if (result.isSuccess()) {
			return result;
		}
		return ResponseResult.buildFailResponse(result.getCode(),result.getMessage());
	}
	private ResponseResult<String> checkShopPermission(Long shopId){
		if(!UserInfoUtil.checkShopPermission(shopId)){
			return ResponseResult.buildFailResponse(SysConstant.SYSTEM_INFO.NO_SHOP_PERMISSION.getCode(),SysConstant.SYSTEM_INFO.NO_SHOP_PERMISSION.getDesc());
		}else{
			return ResponseResult.buildSuccessResponse();
		}
	}
}

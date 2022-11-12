package com.milisong.breakfast.pos.web;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.milisong.breakfast.pos.api.PosProductionService;
import com.milisong.breakfast.pos.constant.OrderSetStatusEnum;
import com.milisong.breakfast.pos.dto.DelayMessageDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 回调服务
 * @author yangzhilong
 *
 */
@Api(tags = "回调服务")
@Slf4j
@RestController
@RequestMapping("/callback")
public class CallbackRest {
	@Autowired
	private PosProductionService posProductionService;
	/**
	 * 延迟队列回调
	 * @param dto
	 */
	@ApiOperation("延迟队列回调")
	@PostMapping("/dq")
	public ResponseResult<?> dqCallback(@RequestBody DelayMessageDto dto) {
		log.info("延迟队列回调：{}", JSONObject.toJSONString(dto));
		if(null == dto || StringUtils.isBlank(dto.getBody())) {
			log.error("延迟队列回调参数异常：{}", JSONObject.toJSONString(dto));
			return ResponseResult.buildFailResponse();
		}
		JSONObject json = JSONObject.parseObject(dto.getBody());
		return posProductionService.updateOrderSetStatusByNo(json.getLong("shopId"), json.getString("detailSetNo"), OrderSetStatusEnum.FINISH_ORDER_3.getValue(),"system_系统");
	}
}

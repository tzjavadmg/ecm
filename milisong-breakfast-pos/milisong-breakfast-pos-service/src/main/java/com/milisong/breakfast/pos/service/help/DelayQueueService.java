package com.milisong.breakfast.pos.service.help;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.milisong.breakfast.pos.dto.DelayMessageDto;
import com.milisong.breakfast.pos.properties.DqProperties;
import com.milisong.breakfast.pos.util.RestClient;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DelayQueueService {
	// 系统类型
	private static final String DQ_SYSTEM_POS = "pos-bf";
	// 模块
	private static final String DQ_MODULE = "production";
	@Autowired
	private DqProperties dqProperties;

	/**
	 * 设置超时时间
	 * 
	 * @param detailSetNo
	 * @param ttl
	 */
	public void setDelay(Long detailSetId, String detailSetNo, Long shopId, long ttl) {
		Map<String, Object> map = new HashMap<>();
		map.put("detailSetNo", detailSetNo);
		map.put("shopId", shopId);

		DelayMessageDto messageDto = new DelayMessageDto();
		messageDto.setSystem(DQ_SYSTEM_POS);
		messageDto.setModule(DQ_MODULE);
		messageDto.setBizId(detailSetId);
		messageDto.setBody(JSONObject.toJSONString(map));
		messageDto.setCallbackUrl(dqProperties.getCallbackUrl());
		messageDto.setTtl(ttl);
		String result = RestClient.postJson(dqProperties.getDelayUrl(), messageDto);
		log.info("调用延迟队列服务的设置过期，参数：{}，返回值：{}", JSONObject.toJSONString(messageDto), result);

		processResult(result);
	}

	/**
	 * 暂停
	 * 
	 * @param detailSetId
	 */
	public long pause(Long detailSetId) {
		DelayMessageDto messageDto = buildDto(detailSetId);
		String result = RestClient.postJson(dqProperties.getPauseUrl(), messageDto);
		log.info("调用延迟队列服务的暂停，参数：{}，返回值：{}", JSONObject.toJSONString(messageDto), result);

		processResult(result);

		String ttl = JSONObject.parseObject(result).getString("data");
		if (StringUtils.isNotBlank(ttl)) {
			return Long.valueOf(ttl);
		}
		return 0L;
	}

	/**
	 * 取消
	 * 
	 * @param detailSetId
	 */
	public void cancel(Long detailSetId) {
		DelayMessageDto messageDto = buildDto(detailSetId);
		String result = RestClient.postJson(dqProperties.getCancelUrl(), messageDto);
		log.info("调用延迟队列服务的取消，参数：{}，返回值：{}", JSONObject.toJSONString(messageDto), result);

		processResult(result);
	}

	/**
	 * 获取过期时间
	 * 
	 * @param detailSetId
	 * @return
	 */
	public long getTtl(Long detailSetId) {
		DelayMessageDto messageDto = buildDto(detailSetId);
		String result = RestClient.postJson(dqProperties.getExpireTimeUrl(), messageDto);
		log.info("调用延迟队列服务的ttl，参数：{}，返回值：{}", JSONObject.toJSONString(messageDto), result);

		processResult(result);

		String ttl = JSONObject.parseObject(result).getString("data");
		if (StringUtils.isNotBlank(ttl)) {
			return Long.valueOf(ttl);
		}
		return 0L;
	}

	/**
	 * 构建dto
	 * 
	 * @param detailSetId
	 * @return
	 */
	private DelayMessageDto buildDto(Long detailSetId) {
		DelayMessageDto messageDto = new DelayMessageDto();
		messageDto.setSystem(DQ_SYSTEM_POS);
		messageDto.setModule(DQ_MODULE);
		messageDto.setBizId(detailSetId);
		return messageDto;
	}

	/**
	 * 处理返回值
	 * 
	 * @param result
	 */
	private void processResult(String result) {
		boolean success = true;
		if (StringUtils.isBlank(result)) {
			success = false;
		} else {
			if (StringUtils.isNotBlank(result)) {
				success = JSONObject.parseObject(result).getBooleanValue("success");
			}
		}
		if (!success) {
			throw new RuntimeException("调用延迟队列服务返回失败：" + result);
		}
	}
}

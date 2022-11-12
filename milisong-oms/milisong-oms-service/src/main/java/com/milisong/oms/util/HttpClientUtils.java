package com.milisong.oms.util;

import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.milisong.oms.dto.DelayMessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class HttpClientUtils {
	
	public static ResponseResult<Integer>  postForDelayQueue(RestTemplate restTemplate, DelayMessageDto messageDto, String postUrl) {
		ResponseResult<Integer> result = ResponseResult.buildSuccessResponse();
		boolean flag = false;
		int index = 1;
		while (!flag) {
			try{
				ResponseEntity<String> responseResult = restTemplate.postForEntity(postUrl, messageDto, String.class);
				log.info("请求延迟队列服务返回结果:{}",responseResult);
				if (responseResult.getStatusCode() == HttpStatus.OK) {
					result = JSONObject.parseObject(responseResult.getBody(), ResponseResult.class) ;
					if (!result.isSuccess()) {
						log.info("result is success:" + result.isSuccess());
						flag = false;
						result.setSuccess(false);
					}else {
						flag = true;
					}
				}else {
					log.info("http code:" + responseResult.getStatusCode());
					flag = false;
					result.setSuccess(false);
				}
				index ++;
				if (!flag) {
					Thread.sleep(3000);
				}
				if (index == 3) {
					break;
				}
			}catch(Exception e) {
				log.error("请求延迟队列服务异常",e);
			}finally{
				index ++;
				if (!flag) {
					try{
						Thread.sleep(3000);
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
				if (index == 3) {
					break;
				}
			}
		}
		return result;

	}

}

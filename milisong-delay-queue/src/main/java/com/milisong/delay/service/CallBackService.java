package com.milisong.delay.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.milisong.delay.dto.MessageDto;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CallBackService {
	@Autowired
	private RestTemplate restTemplate;
	
	public boolean callBack(MessageDto message) {
		boolean flag = false;
		int index = 0;
		while (!flag) {
			try{
				ResponseEntity<String> responseResult = restTemplate.postForEntity(message.getCallbackUrl(), message, String.class);
				log.info("回调业务系统返回结果:{}",responseResult);
				if (responseResult.getStatusCode() == HttpStatus.OK) {
					ResponseResult<String> result = JSONObject.parseObject(responseResult.getBody(), ResponseResult.class) ;
					if (!result.isSuccess()) {
						log.info("result is success:" + result.isSuccess());
						flag = false;
					}else {
						flag = true;
					}
				}else {
					log.info("http code:" + responseResult.getStatusCode());
					flag = false;
				}

			}catch(Exception e) {
				log.error("回调业务系统异常",e);
			}finally {
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
		return flag;
	}

}

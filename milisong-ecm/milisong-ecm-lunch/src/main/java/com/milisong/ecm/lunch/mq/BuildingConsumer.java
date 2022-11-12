/*
package com.milisong.ecm.lunch.mq;



import com.milisong.ecm.common.util.RedisKeyUtils;
import com.milisong.ecm.lunch.goods.api.LbsService;
import com.milisong.ecm.lunch.goods.dto.BuildingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.messaging.Message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.cache.RedisCache;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableBinding(MessageSink.class)
@MessageEndpoint
public class BuildingConsumer {
	@Autowired
	private LbsService lbsService;
	
	@StreamListener(MessageSink.MESSAGE_BUILDING_INTPUT)
	 public void accept(Message<String> message) {
        log.info("接收楼宇消息，参数={}", JSONObject.toJSONString(message));
        try{
        	String messageHeadersId = null;
       		if (message != null && message.getHeaders() != null && message.getHeaders().getId() != null) {
       			messageHeadersId = message.getHeaders().getId().toString();
       		}
       		if (messageHeadersId == null) {
       			log.info("接收消息结束，messageHeadersId=null");
       			return;
       		}

       		String msg = message.getPayload();
       		log.info("receive msg:{}", msg);
       		if (msg == null) {
       			return;
       		}
       		BuildingDto buildDto = JSONObject.parseObject(msg, BuildingDto.class);
            String buildingKey = RedisKeyUtils.getBuildingKey(buildDto.getId());
            log.info("更新楼宇信息:{}", msg);
            RedisCache.set(buildingKey, msg);
            
            //维护楼宇集合信息
            String buildingListKey = RedisKeyUtils.getBuildingListKey();
        	RedisCache.hPut(buildingListKey,buildDto.getId().toString(), JSON.toJSONString(buildDto));

            
            // 保存经纬度数据
            lbsService.saveBuildingLonAndLat(buildDto);
        }catch(Exception e) {
           log.error("接收消息结束，出现异常", e);
       	}
	}
}
*/

package com.milisong.breakfast.scm.goods.utils;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.milisong.breakfast.scm.goods.domain.Goods;
import com.milisong.breakfast.scm.goods.domain.GoodsSchedule;
import com.milisong.breakfast.scm.goods.dto.GoodsInfoMqDto;
import com.milisong.breakfast.scm.goods.dto.GoodsScheduleMqDto;
import com.milisong.breakfast.scm.goods.mq.GoodsCategoryDto;

import lombok.extern.slf4j.Slf4j;

/**
 * MQ生产者帮助类
 * @author benny
 *
 */
@Slf4j
public class MqProducerGoodsUtil {
 
	private static MessageChannel goodsSalableChannel;
	private static MessageChannel goodsScheduleChannel;
	private static MessageChannel goodsCategoryChannel;
	private static MessageChannel goodsBasicChannel;


	public static void setGoodsSalableChannel(MessageChannel goodsSalableChannel) {
		MqProducerGoodsUtil.goodsSalableChannel = goodsSalableChannel;
	}
	
	public static void setGoodsScheduleChannel(MessageChannel goodsScheduleChannel) {
		MqProducerGoodsUtil.goodsScheduleChannel = goodsScheduleChannel;
	}
	
	public static void setGoodsCategoryChannel(MessageChannel goodsCategoryChannel) {
		MqProducerGoodsUtil.goodsCategoryChannel = goodsCategoryChannel;
	}
	
	public static void setGoodsBasicChannel(MessageChannel goodsBasicChannel) {
		MqProducerGoodsUtil.goodsBasicChannel = goodsBasicChannel;
	}

	public static void sendGoodsSalableMsg(Map<String, Map<Date, List<String>>> resultMap) {
		log.info("发送可售商品信息MQ，消息内容：{}", JSONObject.toJSONString(resultMap,SerializerFeature.DisableCircularReferenceDetect));
		goodsSalableChannel.send(MessageBuilder.withPayload(JSON.toJSONString(resultMap,SerializerFeature.DisableCircularReferenceDetect)).build());
	}
	
	public static void sendGoodsScheduleMsg(List<GoodsSchedule> msg) {
		log.info("发送档期信息MQ，消息内容：{}", JSONObject.toJSONString(msg));
		goodsScheduleChannel.send(MessageBuilder.withPayload(JSON.toJSONString(msg)).build());
	}
	
	public static void sendGoodsCategoryMsg(List<GoodsCategoryDto> msg) {
		log.info("发送商品类目信息MQ，消息内容：{}", JSONObject.toJSONString(msg));
		goodsCategoryChannel.send(MessageBuilder.withPayload(JSON.toJSONString(msg)).build());
	}
	
	public static void sendGoodsBasicMsg(GoodsInfoMqDto goodsInfoMqDto) {
		log.info("发送商品信息MQ，消息内容：{}", JSONObject.toJSONString(goodsInfoMqDto));
		goodsBasicChannel.send(MessageBuilder.withPayload(JSON.toJSONString(goodsInfoMqDto)).build());
	}
}

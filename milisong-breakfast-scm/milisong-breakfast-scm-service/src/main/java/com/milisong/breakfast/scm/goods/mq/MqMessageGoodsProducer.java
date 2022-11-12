package com.milisong.breakfast.scm.goods.mq;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

import com.milisong.breakfast.scm.goods.utils.MqProducerGoodsUtil;

@EnableBinding(MqMessageGoodsSource.class)
public class MqMessageGoodsProducer {
 
	@Autowired
    @Output(MqMessageGoodsSource.GOODS_SALABLE_OUTPUT)
    private MessageChannel goodsSalableChannel;
	
	@Autowired
	@Output(MqMessageGoodsSource.GOODS_SCHEDULE_OUTPUT)
	private MessageChannel goodsScheduleChannel;
	
	@Autowired
	@Output(MqMessageGoodsSource.GOODS_CATEGORY_OUTPUT)
	private MessageChannel goodsCategoryChannel;
	
	@Autowired
	@Output(MqMessageGoodsSource.GOODS_BASIC_OUTPUT)
	private MessageChannel goodsBasciChannel;
	
	@PostConstruct
	public void postConstruct() {
		MqProducerGoodsUtil.setGoodsSalableChannel(goodsSalableChannel);
		MqProducerGoodsUtil.setGoodsScheduleChannel(goodsScheduleChannel);
		MqProducerGoodsUtil.setGoodsBasicChannel(goodsBasciChannel);
		MqProducerGoodsUtil.setGoodsCategoryChannel(goodsCategoryChannel);
	}
}

package com.milisong.breakfast.scm.goods.mq;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface MqMessageGoodsSource {

	/**
	 * 可售商品生产
	 */
	String GOODS_SALABLE_OUTPUT = "goods_salable_output";

	@Output(GOODS_SALABLE_OUTPUT)
	MessageChannel goodsSalableOutput();
	
	/**
	 * 档期信息生产
	 */
	String GOODS_SCHEDULE_OUTPUT = "goods_schedule_output";

	@Output(GOODS_SCHEDULE_OUTPUT)
	MessageChannel goodsScheduleOutput();
	
	/**
	 * 商品类目生产
	 */
	String GOODS_CATEGORY_OUTPUT = "goods_category_output";
	
	@Output(GOODS_CATEGORY_OUTPUT)
	MessageChannel goodsCategoryOutput();
	
	/**
	 * 档期信息生产
	 */
	String GOODS_BASIC_OUTPUT = "goods_basic_output";

	@Output(GOODS_BASIC_OUTPUT)
	MessageChannel goodsBasicOutput();
}

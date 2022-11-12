package com.milisong.ecm.lunch.goods.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 门店截单、配送时间配置mq对象
 *
 */
@Getter
@Setter
public class InterceptConfigDto {
	
	private Long id;
	
	//门店code
	private String shopCode;
	
	//订单截单时间
	private String cutOffTime;
	
	//配送开始时间
	private String startTime;
	
	//配送结束时间
	private String endTime;
	
	//最大生产数量
	private Integer maxCapacity;
	
	//是否默认配送设置
	private Boolean isDefault;
}

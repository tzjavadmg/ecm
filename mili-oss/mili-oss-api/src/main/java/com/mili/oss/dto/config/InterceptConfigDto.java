package com.mili.oss.dto.config;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * 截单配置
 * @author yangzhilong
 *
 */
@Getter
@Setter
public class InterceptConfigDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2204608366005957449L;
	// 门店id
	private Long shopId;
	// 第一次集单时间
	private Date firstOrdersetTime;
	// 第二次集单时间
	private Date secondOrdersetTime;
	// 配送开始时间
	private Date deliveryTimeBegin;
	// 配送结束时间
	private Date deliveryTimeEnd;
}

package com.milisong.ecm.lunch.goods.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 全局配置mq对象
 *
 */

@Getter
@Setter
public class GlobalConfigDto {
	
	//分享图片地址
	private String sharePic;
	
	//分享文案
	private String shareText;
	
	//公司同名提醒次数
	private Integer reminderNumberCompany;
	
	//待支付超时时间
	private Integer unPayExpiredTime;
	
	//lbs搜索范围
	private Double lbsDistance;
}

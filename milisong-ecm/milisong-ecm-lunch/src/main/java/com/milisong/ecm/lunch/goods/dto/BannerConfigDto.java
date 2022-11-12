package com.milisong.ecm.lunch.goods.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * banner配置mq对象
 *
 */
@Getter
@Setter
public class BannerConfigDto {
	
	private String shopCode;

	private String picture;
	
	private String linkUrl;
	
	private Byte weight;
}

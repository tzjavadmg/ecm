package com.milisong.breakfast.scm.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 集单服务接口地址
 */
@Data
@ConfigurationProperties(prefix = "oss")
@Component
public class OssUrlProperties {
	/**
	 * 检查集单订单url
	 */
	private String searchOrderSet;
	/**
	 * 查询订单详情
	 */
	private String searchCustomerOrder;
	/**
	 * 查询订单
	 */
	private String searchOrder;
	/**
	 * 查询商品汇总
	 */
	private String seartchReserveGroupInfo;

	/**
	 * 午餐集单打印查询
	 */
	private String orderSetInfo;
	
	/**
	 * 早餐集单打印查询
	 */
	private String bforderSetInfo;

	/**
	 * 新版早餐打印查询
	 */
	private String bfReprintInfo;
	
	/**
	 * 订单已售量查询
	 * 
	 */
	private String goodsCount;
}

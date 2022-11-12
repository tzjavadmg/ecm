package com.mili.oss.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 商品汇总查询结果
 * @author zhaozhonghui
 */
@Getter
@Setter
public class OrderReserveSearchResultDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7615465054257815171L;
	// 门店ID
	private Long shopId;
	// 商品编码
	private String goodsCode;
	// 商品名称
	private String goodsName;
	// 预定日期
	private String reserveDate;
	// 配送日期
	private String deliveryDate;
	// 订购数量
	private Integer number;
}

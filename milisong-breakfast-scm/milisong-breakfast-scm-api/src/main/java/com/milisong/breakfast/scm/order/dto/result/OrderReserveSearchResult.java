package com.milisong.breakfast.scm.order.dto.result;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 预定订单查询结果
 * @author yangzhilong
 *
 */
@Getter
@Setter
public class OrderReserveSearchResult implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7615465054257815171L;
	// 门店ID
	@ApiModelProperty("门店id")
	private Long shopId;
	// 商品编码
	@ApiModelProperty("商品编码")
	private String goodsCode;
	// 商品名称
	@ApiModelProperty("商品名称")
	private String goodsName;
	// 预定日期
	@ApiModelProperty("预定日期")
	private String reserveDate;
	// 配送日期
	@ApiModelProperty("噢诶song日期")
	private String deliveryDate;
	// 订购数量
	@ApiModelProperty("订购数量")
	private Integer number;
}

package com.milisong.breakfast.scm.order.dto.param;

import java.io.Serializable;
import java.util.Date;

import com.farmland.core.api.PageParam;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

/**
 * @author benny
 *
 * 查询商品订单总数
 */
@Getter
@Setter
public class OrderGoodsSumParam extends PageParam implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5734705291131335679L;

	// 门店id
	private Long shopId;

	// 配送日期
	@JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
	private Date deliveryDate;
	
	// 商品编码
	
	private String goodsCode;
	//业务线
	private byte orderType;
}

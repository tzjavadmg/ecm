package com.mili.oss.dto.param;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.farmland.core.api.PageParam;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderGoodsSumParam extends PageParam implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3877411492094916656L;

	// 门店id
	private Long shopId;

	// 配送日期
	@JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
	private Date deliveryDate;
	
	// 商品编码
	
	private String goodsCode;
	
	//公司ID
	private List<Long> companyId;
	
	//业务线
	private byte orderType;
}

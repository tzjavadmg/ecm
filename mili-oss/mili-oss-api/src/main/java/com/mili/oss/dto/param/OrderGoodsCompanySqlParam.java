package com.mili.oss.dto.param;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderGoodsCompanySqlParam {

	// 门店id
	private Long shopId;

	// 配送日期
	private Date deliveryDate;
	
	// 商品编码
	
	private String goodsCode;
	
	//公司ID
	private List<Long> companyId;
	
	//业务线
	private byte orderType;
	
	// 配送日期开始值 yyyy-mm-dd 00:00:00
	private Date beginDeliveryDate;
	
	// 配送日期结束值 yyyy-mm-dd+1 00:00:00
	private Date endDeliveryDate;
}

package com.milisong.scm.orderset.param;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderGoodsCompanySqlParam extends PageInfo{

	private Long shopId;
	
	private String goodsCode;
	
	// 配送日期开始值 yyyy-mm-dd 00:00:00
	private Date beginDeliveryDate;
	
	// 配送日期结束值 yyyy-mm-dd+1 00:00:00
	private Date endDeliveryDate;
}

package com.milisong.breakfast.scm.order.param;

import java.util.Date;

import com.milisong.breakfast.scm.common.param.PageInfo;

import lombok.Getter;
import lombok.Setter;

/**
 * 为集单服务的订单sql查询参数
 * @author yangzhilong
 *
 */
@Getter
@Setter
public class OrderSearch4OrderSetSqlParam extends PageInfo {
	
	// 门店id
	private Long shopId;

	// 公司id
	private Long deliveryCompanyId;
	
	// 订单类型
	private Byte orderType;
	
	// 配送日期开始值 yyyy-mm-dd 00:00:00
	private Date beginDeliveryDate;
	
	// 配送日期结束值 yyyy-mm-dd+1 00:00:00
	private Date endDeliveryDate;
}

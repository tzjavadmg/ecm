package com.milisong.breakfast.scm.order.param;

import java.util.Date;

import com.milisong.breakfast.scm.common.param.PageInfo;

import lombok.Getter;
import lombok.Setter;

/**
 * 查询楼宇里有订单信息的公司的查询参数类
 * @author yangzhilong
 *
 */
@Getter
@Setter
public class OrderCompanySqlParam extends PageInfo{
	// 公司id
	private Long companyId;
	
	// 公司名称
	private String deliveryCompany;
	
	// 配送日期开始值 yyyy-mm-dd 00:00:00
	private Date beginDeliveryDate;
	
	// 配送日期结束值 yyyy-mm-dd+1 00:00:00
	private Date endDeliveryDate;
	
	// 楼层
	private String deliveryFloor;
}

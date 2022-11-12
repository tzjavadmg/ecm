package com.milisong.scm.orderset.param;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * 为集单服务的订单明细sql查询参数
 * @author yangzhilong
 *
 */
@Getter
@Setter
public class OrderDetailSearch4OrderSetSqlParam {
	
	// 楼宇id
	private Long deliveryOfficeBuildingId;
	
	// 公司名称
    private String deliveryCompany;
	
	// 配送日期开始值 yyyy-mm-dd 00:00:00
	private Date beginDeliveryDate;
	
	// 楼层
	private String deliveryFloor;
}

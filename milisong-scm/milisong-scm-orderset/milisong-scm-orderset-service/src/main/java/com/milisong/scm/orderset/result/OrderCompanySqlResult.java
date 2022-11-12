package com.milisong.scm.orderset.result;

import lombok.Getter;
import lombok.Setter;

/**
 * 查询楼宇里有订单信息的公司的结果类
 * @author yangzhilong
 *
 */
@Getter
@Setter
public class OrderCompanySqlResult {
	// 楼宇id
	private Long deliveryOfficeBuildingId;
	// 公司名称
	private String deliveryCompany;
	// 门店id
	private Long shopId;
	// 配送地址
	private String deliveryAddress;
	// 楼层
	private String deliveryFloor;
}

package com.milisong.scm.orderset.dto.result;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 楼宇-公司订单汇总信息结果
 * @author yangzhilong
 *
 */
@Getter
@Setter
public class OrderCompanyResult implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7437216801108297439L;
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

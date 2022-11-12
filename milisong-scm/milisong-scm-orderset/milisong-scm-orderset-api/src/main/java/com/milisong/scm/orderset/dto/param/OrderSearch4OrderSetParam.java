package com.milisong.scm.orderset.dto.param;

import java.io.Serializable;
import java.util.Date;

import com.farmland.core.api.PageParam;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 为集单服务的订单查询参数
 * @author yangzhilong
 *
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSearch4OrderSetParam extends PageParam implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1319129656277950944L;
	
	// 门店id
	private Long shopId;

	// 楼宇id
	private Long deliveryOfficeBuildingId;
	
	// 公司名称
	private String deliveryCompany;
	
	// 配送日期 + 日期
	private Date deliveryDate;
	
	// 楼层
	private String deliveryFloor;
	
	private String beginDate;
	
	private String endDate;
	
	private String building;
}

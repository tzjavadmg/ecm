package com.milisong.breakfast.scm.order.dto.param;

import java.io.Serializable;
import java.util.Date;

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
public class OrderSearch4OrderSetParam implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1319129656277950944L;

	// 公司id
	private Long companyId;
		
	// 配送日期 + 日期
	private Date deliveryDate;
	
	// 取餐点
	private String deliveryRoom;
}

package com.milisong.scm.orderset.param;

import java.io.Serializable;
import java.util.Date;

import com.farmland.core.api.PageParam;

import lombok.Getter;
import lombok.Setter;

/**
 * 预定订单查询条件
 * @author yangzhilong
 *
 */
@Getter
@Setter
public class OrderReserveSearchSqlParam extends PageParam implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7615465054257815171L;
	// 门店id
	private Long shopId;
	// 预定开始日期
	private Date reserveBeginDate;
	// 预定结束日期
	private Date reserveEndDate;
	// 配送开始日期
	private Date deliveryBeginDate;
	// 配送结束日期
	private Date deliveryEndDate;
}

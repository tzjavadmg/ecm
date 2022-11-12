package com.milisong.breakfast.scm.order.dto.param;

import java.io.Serializable;

import com.farmland.core.api.PageParam;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 预定订单查询条件
 * @author yangzhilong
 *
 */
@Getter
@Setter
public class OrderReserveSearchParamDto extends PageParam implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7615465054257815171L;
	// 门店id
	@ApiModelProperty("门店id")
	private Long shopId;
	// 预定日期
	@ApiModelProperty("预定日期")
	private String reserveDate;
	// 配送日期
	@ApiModelProperty("配送日期")
	private String deliveryDate;
}

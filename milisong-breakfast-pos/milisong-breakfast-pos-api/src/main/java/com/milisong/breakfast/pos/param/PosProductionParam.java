package com.milisong.breakfast.pos.param;

import com.farmland.core.api.PageParam;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
*@author    created by benny
*@date  2018年10月26日---下午5:21:44
* 生产管理查询入参
*/
@Getter
@Setter
public class PosProductionParam  extends PageParam{
	@ApiModelProperty("门店id")
	private Long shopId;
	@ApiModelProperty("集单状态 1待生产、2生产中、3已完成")
	private Byte orderStatus;
}

package com.milisong.pos.production.param;

import com.farmland.core.api.PageParam;

import lombok.Data;

/**
*@author    created by benny
*@date  2018年10月26日---下午5:21:44
* 生产管理查询入参
*/
@Data
public class PosProductionParam  extends PageParam{

	private Long shopId;
	
	private Byte orderStatus;
}

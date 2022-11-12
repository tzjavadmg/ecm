package com.milisong.breakfast.scm.goods.param;

import java.util.Date;

import com.farmland.core.api.PageParam;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

/**
*@author    created by benny
*@date  2018年12月5日---下午3:04:11
*
*/
@Getter
@Setter
public class GoosSalableParam extends PageParam{

	private Long shopId;
	@JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
	private Date saleDate;
}

package com.milisong.breakfast.pos.dto.result;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
*@author    created by benny
*@date  2018年10月25日---下午9:12:28
*
*/
@Data
public class OrderSetInfoResult implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4841199691644706683L;
	@ApiModelProperty("集单编号")
	private String detailSetNo;
	@ApiModelProperty("集单号（显示用）")
	private String detailSetNoDescription;
	@ApiModelProperty(hidden=true)
	private String coupletNo;
	@ApiModelProperty(hidden=true)
	private String goodsName;
	@ApiModelProperty(hidden=true)
	private String goodsCode;
	@ApiModelProperty(hidden=true)
	private Integer goodsNumber;
	@ApiModelProperty("剩余时间（秒）")
	private Long outTime;
	
	@ApiModelProperty("集单")
	private List<OrderSetGoodsInfoResult> list;	
}

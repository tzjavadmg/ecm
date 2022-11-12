package com.milisong.breakfast.scm.orderset.dto.result;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 根据集单号获取集单详情的dto
 * 
 * @author yangzhilong
 *
 */
@Data
public class OrdersetInfoResult implements Serializable {

	/**
	* 
	*/
	private static final long serialVersionUID = -3674629418810311820L;
	@ApiModelProperty("集单编号")
	private String detailSetNo;
	@ApiModelProperty("集单编号（显示用）")
	private String detailSetNoDescription;
	@ApiModelProperty("顾客联编号")
	private String coupletNo;
	@ApiModelProperty(value="商品名称",hidden=true)
	private String goodsName;
	@ApiModelProperty(value="商品数量",hidden=true)
	private Integer goodsNumber;
	@ApiModelProperty("商品详情list")
	private List<OrderSetDetailGoodsDto> goodsInfo;
}

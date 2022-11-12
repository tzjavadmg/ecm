package com.milisong.breakfast.scm.goods.param;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商品信息请求dto
 * @author youxia 2018年9月3日
 */
@Data
public class GoodsParam implements Serializable {
	
	private static final long serialVersionUID = -6859505414716743460L;
	
	@ApiModelProperty("商品id")
	private Long id; // 
	
	@ApiModelProperty("商品id")
	private String code; // 商品sku
	
	@ApiModelProperty("商品name")
	private String name; // 商品名称
	
    /**
     * 
     */
	@ApiModelProperty("类目code")
    private String categoryCode;

	@ApiModelProperty("是否为组合品")
	private Boolean isCombo;
    /**
     * 
     */
	@ApiModelProperty("类目名称")
    private String categoryName;
    
	@ApiModelProperty("商品图片")
	private String picture; // 
	
	@ApiModelProperty("建议零售价")
	private BigDecimal advisePrice; // 
	
	@ApiModelProperty("折扣")
	private BigDecimal discount; // 
	/**
     * 商品数量
     */
	@ApiModelProperty("商品数量")
    private Integer goodsCount;
	
	@ApiModelProperty(" 优惠价")
	private BigDecimal preferentialPrice; //
	
	@ApiModelProperty("商品简介")
	private String describe; // 

	@ApiModelProperty("状态")
	private Byte status; // 
	
	@ApiModelProperty("权重")
    private Byte weight; // 
    
	@ApiModelProperty("大图片")
    private String bigPicture; // 
	
	
	private Byte spicy; //辣度 0不辣 1微辣 2中辣 3重辣

	@ApiModelProperty("商品id")
    private String taste; //口味

    // 操作人
    private String updateBy;
    
    @ApiModelProperty("组合单品")
    private List<GoodsCombinationParam> comboGoods;

	@ApiModelProperty("是否新品")
	private Boolean isNewGoods;

	@ApiModelProperty("新品过期时间")
	@JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
	private Date newGoodsExpiredTime;

	@ApiModelProperty("进价(采购进价)")
	private BigDecimal purchasePrice;
}

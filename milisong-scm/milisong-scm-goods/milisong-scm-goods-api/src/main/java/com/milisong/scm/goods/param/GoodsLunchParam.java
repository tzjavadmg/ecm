package com.milisong.scm.goods.param;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoodsLunchParam {

	@ApiModelProperty("商品id")
	private Long id; // 
	
	@ApiModelProperty("商品id")
	private String code; // 商品sku
	
	@ApiModelProperty("商品name")
	private String name; // 商品名称
	
	@ApiModelProperty("类目code")
    private String categoryCode;
	
	@ApiModelProperty("类目名称")
    private String categoryName;
    
	@ApiModelProperty("商品图片")
	private String picture; // 
	
	@ApiModelProperty("建议零售价")
	private BigDecimal advisePrice; // 
	
	@ApiModelProperty("折扣")
	private BigDecimal discount; // 
	
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
 
	@ApiModelProperty("口味")
    private String taste;
    
	@ApiModelProperty("辣度")
	private Byte spicy; //辣度 0不辣 1微辣 2中辣 3重辣

    // 操作人
    private String updateBy;
}

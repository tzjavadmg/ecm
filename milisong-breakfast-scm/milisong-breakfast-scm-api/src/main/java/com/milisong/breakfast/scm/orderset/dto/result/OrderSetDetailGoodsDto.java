package com.milisong.breakfast.scm.orderset.dto.result;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class OrderSetDetailGoodsDto implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 7954539608521895432L;
	@ApiModelProperty(hidden=true)
	private Long id;
	@ApiModelProperty(hidden=true)
    private String detailSetNo;
	@ApiModelProperty(hidden=true)
    private String orderNo;
	@ApiModelProperty(hidden=true)
    private String coupletNo;
	@ApiModelProperty(hidden=true)
    private Long userId;
	@ApiModelProperty(hidden=true)
    private String userName;
	@ApiModelProperty(hidden=true)
    private String userPhone;
	@ApiModelProperty(hidden=true)
    private String goodsCode;

    @ApiModelProperty("商品名称")
    private String goodsName;
    @ApiModelProperty("商品数量")
    private Integer goodsNumber;
    @ApiModelProperty(hidden=true)
    private Boolean isDeleted;
    @ApiModelProperty(hidden=true)
    private String createBy;
    @ApiModelProperty(hidden=true)
    private Date createTime;
    @ApiModelProperty(hidden=true)
    private String updateBy;
    @ApiModelProperty(hidden=true)
    private Date updateTime;
    @ApiModelProperty(hidden=true)
    private BigDecimal goodsPrice;
    @ApiModelProperty(hidden=true)
    private BigDecimal goodsDiscountPrice;
    
    /**
     * 单件商品实付金额
     */
    @ApiModelProperty(hidden=true)
    private BigDecimal actualPayAmount;
    @ApiModelProperty(hidden=true)
    private Date orderTime;
    
    /**
     * 是否组合商品 1是 0否
     */
    @ApiModelProperty(hidden=true)
    private Boolean isCombo;

    /**
     * 组合商品code
     */
    @ApiModelProperty(hidden=true)
    private String comboGoodsCode;

    /**
     * 组合商品名称
     */
    @ApiModelProperty(hidden=true)
    private String comboGoodsName;

    /**
     * 组合商品数量
     */
    @ApiModelProperty(hidden=true)
    private Integer comboGoodsCount;
}
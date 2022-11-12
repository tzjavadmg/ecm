package com.milisong.scm.printer.request.mq;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
	
	private Long id;
	
    private String detailSetNo;
	
    private String orderNo;
	
    private String coupletNo;
	
    private Long userId;
	
    private String userName;
	
    private String userPhone;
	
    private String goodsCode;

    private String goodsName;
    private Integer goodsNumber;
    
    private Boolean isDeleted;
    
    private String createBy;
    
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date createTime;
    
    private String updateBy;
    
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date updateTime;
    
    private BigDecimal goodsPrice;
    
    private BigDecimal goodsDiscountPrice;
    
    /**
     * 单件商品实付金额
     */
    
    private BigDecimal actualPayAmount;
    
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date orderTime;
    
    /**
     * 是否组合商品 1是 0否
     */
    
    private Boolean isCombo;
    /**
     * 0=主餐、1、小菜
     */
    private Byte type;

    /**
     * 组合商品code
     */
    
    private String comboGoodsCode;

    /**
     * 组合商品名称
     */
    
    private String comboGoodsName;

    /**
     * 组合商品数量
     */
    
    private Integer comboGoodsCount;

    
}
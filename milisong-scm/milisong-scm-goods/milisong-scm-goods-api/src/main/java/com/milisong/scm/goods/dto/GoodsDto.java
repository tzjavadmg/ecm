package com.milisong.scm.goods.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 商品信息dto
 * @author youxia 2018年9月2日
 */
@Data
public class GoodsDto implements Serializable {
	
	private static final long serialVersionUID = -6859505414716743460L;
	
	private Long id; // 商品id
	
	private String code; // 商品sku
	
	private String name; // 商品名称
	
	private String picture; // 商品图片
	
	private String pictureUrl; // 商品图片
	
	private BigDecimal advisePrice; // 建议零售价
	
	private BigDecimal discount; // 折扣
	
	private BigDecimal preferentialPrice; // 优惠价
    /**
     * 商品进价
     */
    private BigDecimal buyingPrice;

    /**
     * 包装进价
     */
    private BigDecimal packagingPrice;
	
	private String describe; // 商品简介
	
	@JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
	private Date beginDate; // 开始日期
	 @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
	private Date endDate; // 结束日期
	
	private Byte isLimitedTime; // 是否限定截止时间
	
	private Byte status; // 状态
	
	private String statusStr; // 状态描述
	
	private Byte detailStatus; // 子状态
	
	private Byte weight; // 权重
	 /**
     * 0=主餐、1、小菜
     */
    private Byte type;
	
	private String bigPicture; // 大图片
	
	private String bigPictureUrl; // 大图片
	
	private Byte spicy; //辣度 0不辣 1微辣 2中辣 3重辣

    private String taste; //口味

}

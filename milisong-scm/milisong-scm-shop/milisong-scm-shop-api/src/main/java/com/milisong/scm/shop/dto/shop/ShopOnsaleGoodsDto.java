package com.milisong.scm.shop.dto.shop;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class ShopOnsaleGoodsDto implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8396189825919980162L;

	private Long id;

    private Long shopId;
    
    private String shopCode;

    private String goodsCode;

    private String goodsName;

    private Byte status;
    
    private Integer defaultAvailableVolume;

    private Boolean isDeleted;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;
    
    private Date endDate;
    
    private Date beginDate;
    
}

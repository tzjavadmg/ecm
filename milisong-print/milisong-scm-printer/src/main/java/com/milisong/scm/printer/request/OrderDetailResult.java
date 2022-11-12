package com.milisong.scm.printer.request;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailResult implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -6705662274146881947L;

	private Long id;

    private Long orderId;

    private String orderNo;

    private String goodsCode;

    private String goodsName;

    private BigDecimal goodsPrice;

    private Integer goodsCount;

    private BigDecimal goodsDiscountPrice;

    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date updateTime;
}
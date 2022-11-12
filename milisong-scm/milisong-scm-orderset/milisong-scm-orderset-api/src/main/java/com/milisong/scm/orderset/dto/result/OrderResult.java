package com.milisong.scm.orderset.dto.result;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class OrderResult implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -6953961699864726504L;

	private Long id;

    private String orderNo;

    private Long userId;

    private String realName;

    private String mobileNo;

    private Date deliveryStartDate;

    private Date deliveryEndDate;

    private Long deliveryOfficeBuildingId;

    private String deliveryCompany;

    private String deliveryAddress;

    private String deliveryFloor;

    private String deliveryRoom;

    private String openId;

    private BigDecimal totalAmount;

    private BigDecimal actualPayAmount;

    private Byte orderType;

    private Byte payMode;

    private Date orderDate;

    private Long shopId;

    private Byte orderSource;

    private Byte orderStatus;

    private String payStatus;

    private Date createTime;

    private Date updateTime;
}
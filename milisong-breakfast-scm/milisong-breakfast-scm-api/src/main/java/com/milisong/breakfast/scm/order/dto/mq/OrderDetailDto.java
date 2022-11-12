package com.milisong.breakfast.scm.order.dto.mq;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/8/31 14:57
 */
@Getter
@Setter
public class OrderDetailDto implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 891841635636902239L;

	private Long id;

    private Long orderId;

    private String orderNo;

    private String goodsCode;

    private String goodsName;
    // 原价
    private BigDecimal goodsPrice;

    private Integer goodsCount;
    // 折扣价
    private BigDecimal goodsDiscountPrice;

    private Date createTime;

    private Date updateTime;
    
    // 是否组合商品
    private Boolean isCombo;
    // 组合商品明细
    private List<OrderDetailDto> comboDetails;
}

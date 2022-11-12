package com.milisong.breakfast.pos.dto;

import java.math.BigDecimal;
import java.util.Date;

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
public class OrderDetailDto {

    private Long id;

    private Long orderId;

    private String orderNo;

    private String goodsCode;

    private String goodsName;

    private BigDecimal goodsPrice;

    private Integer goodsCount;

    private BigDecimal goodsDiscountPrice;

    private Date createTime;

    private Date updateTime;
}

package com.milisong.pms.prom.dto;

import com.milisong.pms.prom.param.OrderDeliveryGoodsParam;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author sailor wang
 * @date 2018/12/11 1:23 PM
 * @description
 */
@Data
public class UserCouponQueryParam extends BaseDto implements Serializable {
    private static final long serialVersionUID = 1L;


    private Long userId;

    private Boolean usable;

    private Long userActivityId;

    private Byte userActivityType;

    private Long orderId;

    private Long userCouponId;

    private Integer days;

    private Byte couponType;

    private BigDecimal payAmount;

    private List<Long> userids;

    private Long companyId;

    private Integer pageNo;

    private Integer pageSize;

    private Integer startRow;

    private List<OrderDeliveryGoodsParam> orderGoods;

    // 是否合并可用、不可用券列表
    private Boolean merge;

    // 总积分
    private Integer totalPoints;



}
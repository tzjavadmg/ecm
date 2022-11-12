package com.milisong.oms.dto;


import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyOrderDeliveryDto {

    private Long id;

    //配送单号
    private String deliverNo;

    //状态名称值
    private String statusName;

    //预计配送时间
    private Date deliveryDate;

    private String deliveryDateStr;

    //预计配送时间(毫秒)
    private Long deliveryStartTimeMsec;

    //配送状态：0:配送中;1:待配送;2:备餐中;3:已退款;9:已完成;
    private Byte status;

    //总商品数量
    private Integer totalGoodsCount;

    //周几
    private String weekDay;

    //配送开始时间段
    private String deliveryTimezoneFrom;

    //配送结束时间段
    private String deliveryTimezoneTo;

    //配送完成时间
    private Date deliveryEndTime;

    //结束标识
    private Integer engFlag;

    //订单明细集合
    private List<MyOrderDeliveryGoodsDto> orderDeliveryGoods;

}

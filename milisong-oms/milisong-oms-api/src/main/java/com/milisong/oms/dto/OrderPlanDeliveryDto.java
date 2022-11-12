package com.milisong.oms.dto;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderPlanDeliveryDto {
    private Long id;
    
    //订单id
    private Long orderId;
    
    //配送单号
    private String deliveryNo;
    
    //状态名称值
    private String statusName;
    
    //预计配送时间
    private Date deliveryDate;
    
    //预计配送时间(毫秒)
    private Long deliveryStartTimeMsec;
    
    //配送状态：0:配送中;1:待配送;2:备餐中;3:已退款;9:已完成;
    private Byte status;
    private Byte orderMode;

    //总商品数量
    private Integer totalGoodsCount;
    
    //周几
    private String weekDay;
    
    //配送完成时间
    private Date deliveryEndTime;
    
    //预计送达时间
    private String deliveryTimezoneToStr;
    
    //订单明细集合
    private List<OrderPlanDeliveryGoodsDto> orderPlanGoods;
}

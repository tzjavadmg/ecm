package com.milisong.oms.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDeliveryMqDto {
    /**
     * 配送单编号
     */
    private String deliveryNo;
    
    /**
     * 打包时间
     */
    private Date packageTime;
    
    /**
     * 配送开始时间
     */
    private Date deliveryStartTime;

    /**
     * 配送到达时间
     */
    private Date deliveryEndTime;
    
    /**
     * 配送状态：0:配送中;1:待配送;2:备餐中;3:已取消;4:已退款;9:已完成;
     */
    private Byte status;

    /**
     * 顺送配送状态：0:已派单; 1: 已接单;2: 已到店;3: 已提货;4: 已送达
     */
    private Byte sfStatus;
}

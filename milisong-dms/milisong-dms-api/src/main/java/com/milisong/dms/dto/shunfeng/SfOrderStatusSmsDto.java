package com.milisong.dms.dto.shunfeng;

import lombok.Data;

import java.io.Serializable;

/**
 *  顺丰配送短信
 * @author zhaozhonghui
 * @date 2018-11-09
 */
@Data
public class SfOrderStatusSmsDto implements Serializable {

    private static final long serialVersionUID = 4714054852195476141L;
    /** 开发者Id */
    private String devId;
    /** 订单(子集单)ID */
    private String orderId;
    /** 订单类型 1顺丰订单 2商家订单*/
    private Byte orderType;
    /** 推送时间[秒] */
    private Long pushTime;
}

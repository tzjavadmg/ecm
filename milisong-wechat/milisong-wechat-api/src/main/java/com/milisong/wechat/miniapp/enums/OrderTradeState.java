package com.milisong.wechat.miniapp.enums;

/**
 * 订单交易状态
 *
 * @author sailor wang
 * @date 2018/10/31 5:10 PM
 * @description
 */
public enum OrderTradeState {
    SUCCESS,//支付成功

    REFUND,//转入退款

    NOTPAY,//未支付

    CLOSED,//已关闭

    REVOKED,//已撤销（刷卡支付）

    USERPAYING,//用户支付中

    PAYERROR//支付失败
}
package com.milisong.dms.dto.dada;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhaozhonghui
 * @Description 达达订单状态回调参数封装
 * @date 2018-11-27
 */
@Data
public class DaDaOrderStatusBackReqDto implements Serializable {
    private static final long serialVersionUID = -5447409881570770017L;
    /** 达达配送单号 */
    private String clientId;
    /** 商家订单ID */
    private String orderId;
    /** 订单状态 (待接单＝1 待取货＝2 配送中＝3 已完成＝4 已取消＝5 已过期＝7
     * 指派单=8 妥投异常之物品返回中=9 妥投异常之物品返回完成=10
     * 创建达达运单失败=1000 可参考文末的状态说明） */
    private Byte orderStatus;
    /** 订单取消原因 */
    private String cancelReason;
    /** 订单取消来源(1:达达配送员取消；2:商家主动取消；3:系统或客服取消；0:默认值) */
    private Byte cancelFrom;
    /** 更新时间 时间戳 */
    private Long updateTime;
    /** 签名 */
    private String signature;
    /** 配送员id */
    private Long dmId;
    /** 配送员姓名 */
    private String dmName;
    /** 配送员手机 */
    private String dmMobile;
}

package com.milisong.ecm.common.notify.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <pre>
 *    author  : Administrator
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/12/3   14:07
 *    desc    : 微信支付通知dto
 *    version : v1.0
 * </pre>
 */
@Data
public class WechatPayNotifyDto extends BaseDto implements Serializable {

    private static final long serialVersionUID = 703595073859767481L;
    private Long orderId;

    private String shopCode;

    private String openId;

    private Byte status;

}

package com.milisong.ecm.common.notify.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <pre>
 *    author  : Administrator
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/12/3   16:35
 *    desc    : 定时通知今天订单提醒的dto
 *    version : v1.0
 * </pre>
 */
@Data
public class NotifyTodayOrderTaskDto extends BaseDto implements Serializable {

    private static final long serialVersionUID = 2894358238875810169L;

    private String phone;

    private String shopCode;

}

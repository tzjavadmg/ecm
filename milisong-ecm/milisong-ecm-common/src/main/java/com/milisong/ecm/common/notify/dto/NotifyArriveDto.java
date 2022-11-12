package com.milisong.ecm.common.notify.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <pre>
 *    author  : Tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/10/29   14:53
 *    desc    : 通知送达mq的dto
 *    version : v1.0
 * </pre>
 */
@Data
public class NotifyArriveDto extends BaseDto implements Serializable {

    private static final long serialVersionUID = 115584965999785706L;
    /** 更新时间 */
    private Date updateTime;
    /** 子集单ID */
    private String detailSetId;
    /** 顺丰订单状态 */
    private Byte status;
    /**
     * 订单类型 0早餐，1午餐
     */
    private Byte orderType;
}

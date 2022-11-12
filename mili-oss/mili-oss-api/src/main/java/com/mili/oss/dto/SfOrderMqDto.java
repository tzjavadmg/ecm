package com.mili.oss.dto;

import java.io.Serializable;
import java.util.Date;


import lombok.Getter;
import lombok.Setter;

/**
 * @author zhaozhonghui
 * @date 2018-10-24
 */
@Getter
@Setter
public class SfOrderMqDto implements Serializable {

    private static final long serialVersionUID = 115584965999785706L;
    /** 更新时间 */
    private Date updateTime;
    /** 子集单ID */
    private String detailSetId;
    /** 
     * 顺丰订单状态 
     * {@link LogisticsDeliveryStatus}
     * */
    private Byte status;
}

package com.mili.oss.dto.orderstatus;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @author zhaozhonghui
 * @date 2018-10-24
 */
@Data
public class DeliveryOrderMqDto implements Serializable {

    private static final long serialVersionUID = 115584965999785706L;
    /** 更新时间 */
    private Date updateTime;
    /** 子集单ID */
    private String detailSetId;
    /** 配送订单状态 */
    private Byte status;
}

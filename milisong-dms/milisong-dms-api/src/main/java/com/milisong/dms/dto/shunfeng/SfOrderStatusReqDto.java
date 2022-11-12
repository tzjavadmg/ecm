package com.milisong.dms.dto.shunfeng;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * 查询顺丰订单状态参数
 * @author zhaozhonghui
 * @date 2018-10-22
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SfOrderStatusReqDto implements Serializable {

    private static final long serialVersionUID = 9107834125894232699L;
    private String orderId;

    private Byte orderType;

    private String shopId;

    private Byte shopType;

    private String devId;

    private Long pushTime;
}

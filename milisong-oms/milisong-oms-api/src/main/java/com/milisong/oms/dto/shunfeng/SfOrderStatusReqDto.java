package com.milisong.oms.dto.shunfeng;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author zhaozhonghui
 * @date 2018-10-22
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SfOrderStatusReqDto{

    private String orderId;

    private Byte orderType;

    private String shopId;

    private Byte shopType;
}

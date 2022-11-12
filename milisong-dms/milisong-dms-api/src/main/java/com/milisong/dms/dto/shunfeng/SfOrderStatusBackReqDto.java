package com.milisong.dms.dto.shunfeng;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhaozhonghui
 * @date 2018-10-23
 */
@Data
public class SfOrderStatusBackReqDto implements Serializable {

    private static final long serialVersionUID = -1987550753896286268L;
    private Long sfOrderId;

    private String shopOrderId;

    private String urlIndex;

    private String operatorName;

    private String operatorPhone;

    private String riderLng;

    private String riderLat;

    private Byte orderStatus;

    private String statusDesc;

    private Long pushTime;

    private Long shopId;
}

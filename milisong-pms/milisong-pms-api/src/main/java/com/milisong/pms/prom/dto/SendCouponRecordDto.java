package com.milisong.pms.prom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendCouponRecordDto implements Serializable {
    private Long id;

    private Long operatorId;

    private String operatorName;

    private SendBreakCouponRequest filterCondition;

    private String couponids;

    private String content;

    private Date sendTime;

    private Integer shouldSendNum;

    private Integer actualSendNum;

    private String remark;

    private Byte status;

    private Byte businessLine;
}
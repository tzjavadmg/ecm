package com.milisong.pms.prom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendCouponWaterDto implements Serializable {
    private Long id;

    private Long sendCouponRecordId;

    private Long userId;

    private String userName;

    private String mobileNo;

    private Byte sex;

    private String deliveryCompany;

    private Date sendTime;

    private Byte businessLine;

    private static final long serialVersionUID = 1L;


}
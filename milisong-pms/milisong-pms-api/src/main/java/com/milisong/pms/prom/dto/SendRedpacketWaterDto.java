package com.milisong.pms.prom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author sailor wang
 * @date 2019/2/18 4:35 PM
 * @description
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendRedpacketWaterDto implements Serializable {
    private Long id;

    private Long sendRedpacketRecordId;

    private Long userId;

    private String userName;

    private String mobileNo;

    private Byte sex;

    private String deliveryCompany;

    private Date sendTime;

    private Byte businessLine;
}
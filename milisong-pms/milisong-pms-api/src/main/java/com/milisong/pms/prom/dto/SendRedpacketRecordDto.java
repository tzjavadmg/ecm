package com.milisong.pms.prom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author sailor wang
 * @date 2019/2/18 4:43 PM
 * @description
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendRedpacketRecordDto implements Serializable {
    private Long id;

    private Long operatorId;

    private String operatorName;

    private String filterCondition;

    private String redpacketids;

    private String content;

    private Date sendTime;

    private Integer shouldSendNum;

    private Integer actualSendNum;

    private String remark;

    private Byte status;

    private Byte businessLine;

}
package com.milisong.pms.prom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendRedPacketSearchConditionDto implements Serializable {
    private List<RedPacketDto> redPacketList;

    private List<Long> redpacketids;

    private Date sendTime;

    // 发送所有用户
    private Boolean sendAllUser;

    // 手机号
    private List<String> mobiles;

    // 发送短信
    private String smsMsg;

    // 过滤的用户id
    private List<Long> excludeUserIds;
}
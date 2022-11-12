package com.milisong.pms.prom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author sailor wang
 * @date 2019/1/8 4:00 PM
 * @description
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchSendRedPacketParam {
    // 发放红包
    private List<SendRedPacketConfig> redPackets;

    // 指定用户与sendAll互斥
    private List<Long> userids;

    // 全量推送与userids互斥
    private Boolean sendAll;

    // 短信内容
    private String smsMsg;
}
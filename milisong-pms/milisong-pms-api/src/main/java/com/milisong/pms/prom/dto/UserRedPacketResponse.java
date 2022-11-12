package com.milisong.pms.prom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author sailor wang
 * @date 2018/9/13 下午4:19
 * @description
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRedPacketResponse implements Serializable {
    private static final long serialVersionUID = 210626670625199698L;

    private ActivityUserRedPacketDto currentUserRedPacket;

    private List<ActivityRedPacketActionDto> hackRedPacketRecord;

    private List<ActivityUserRedPacketDto> multiShareRedPacketRecord;// 红包领取记录

    private UserRedPacketDto newRedPacket; // 新人红包

    private UserRedPacketDto receiveRedPacket; // 领取红包

    private List<UserRedPacketDto> usableUserRedPacket;//可用红包列表

    private Boolean overLimit;//领取超限

    private Boolean received;//是否领取过

    private Boolean finished;//是否领取完成

    private Boolean isOver;//活动是否结束

    private Boolean isMy;

    private String masterPic;//活动主图

    private String descript;//活动描述

    private Long userActivityId;

}
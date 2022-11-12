package com.milisong.pms.prom.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author sailor wang
 * @date 2018/9/14 上午11:16
 * @description
 */
@Setter
@Getter
public abstract class HasRedPacketHackParts extends BaseDto{

    // 当天砍红包次数
    Integer hackTimes;

    // 用户是否发起过红包
    ActivityUserRedPacketDto activityUserRedPacket;

    // 是否砍过红包活动
    Boolean hackCurrentRedPacket;

}
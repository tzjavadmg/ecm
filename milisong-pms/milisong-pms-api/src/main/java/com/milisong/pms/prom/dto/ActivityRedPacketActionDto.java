package com.milisong.pms.prom.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sailor wang
 * @date 2018/9/13 下午8:57
 * @description
 */
@Builder
@Data
public class ActivityRedPacketActionDto implements Serializable {
    private static final long serialVersionUID = -3899579027500134877L;

    private Long id;

    private Long userActivityId;

    private Long userId;

    private String openId;

    private String nickName;

    private String headPortraitUrl;

}
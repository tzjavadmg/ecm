package com.milisong.pms.prom.dto.invite;

import com.milisong.pms.prom.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sailor wang
 * @date 2019/3/25 5:40 PM
 * @description
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInviteRequest extends BaseDto {
    /**
     * 老拉新活动id
     */
    private Long activityInviteId;

    private Long originateUserId;

    private Long userId;

    /**
     * 是否是新用户
     */
    private Byte isNew;

    /**
     * 是否领取过新人券
     */
    private Byte receivedNewCoupon;

    /**
     * 是否领取过新人红包
     */
    private Byte receivedNewRedpacket;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像
     */
    private String headPortraitUrl;
}
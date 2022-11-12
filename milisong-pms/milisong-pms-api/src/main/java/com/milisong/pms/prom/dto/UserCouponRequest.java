package com.milisong.pms.prom.dto;

import com.milisong.pms.prom.enums.RedPacketLaunchType;
import lombok.*;

import java.io.Serializable;

/**
 * @author sailor wang
 * @date 2018/9/13 下午4:16
 * @description
 */
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserCouponRequest extends BaseDto implements Serializable {
    private static final long serialVersionUID = 8033980640074857944L;

    private Long activityUserCouponId;

    private Long userId;

    private Byte isNewUser;

    private String openid;

    private String nickName;

    private String headPortraitUrl;

    private RedPacketLaunchType launchType;

    private Byte receivedNewCoupon;

    private Long launchId;

}
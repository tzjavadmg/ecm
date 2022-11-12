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
public class UserCouponResponse implements Serializable {
    private static final long serialVersionUID = 210626670625199698L;

    private ActivityUserCouponDto currentUserCoupon;

    private List<ActivityUserCouponDto> multiShareCouponRecord;// 券领取记录

    private UserCouponDto newCoupon; // 新人券

    private UserCouponDto receiveCoupon; // 领取券

    private List<UserCouponDto> usableUserCoupon;//可用券列表

    private Boolean overLimit;//领取超限

    private Boolean received;//是否领取过

    private Boolean finished;//是否领取完成

    private Boolean isOver;//活动是否结束

    private Boolean isMy;

    private String masterPic;//活动主图

    private String descript;//活动描述

    private Long userActivityId;

    private Boolean receivedRedpacket;//是否领取过红包

}
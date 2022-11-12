package com.milisong.pms.prom.dto.invite;

import com.milisong.pms.prom.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author sailor wang
 * @date 2019/3/25 6:01 PM
 * @description
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInviteRecordDto extends BaseDto {
    private Long id;
    /**
     * 邀请活动实例ID
     */
    private Long activityInviteId;
    /**
     * 发起者用户ID
     */
    private Long originateUserId;
    /**
     * 领取用户ID
     */
    private Long receiveUserId;
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 用户头像
     */
    private String headPortraitUrl;

    /**
     * 券名称
     */
    private String couponName;

    /**
     * 商品code
     */
    private String goodsCode;


    private BigDecimal discount;

    /**
     * 状态
     */
    private Byte status;
    /**
     * 成功邀请时间
     */
    private Date inviteSuccessTime;
    /**
     * 备注
     */
    private String remark;

    private Date createTime;
}
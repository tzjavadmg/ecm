package com.milisong.pms.prom.dto.invite;

import com.milisong.pms.prom.dto.UserCouponDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

/**
 * @author sailor wang
 * @date 2019/3/25 5:40 PM
 * @description
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInviteResponse {
    /**
     * 通过该活动领取的新人券
     */
    private UserCouponDto receiveCoupon;

    /**
     * 是否领取过
     */
    private Boolean received;

    /**
     * 我的邀请详情页面展示图
     */
    private String myInvitePic;

    /**
     * 我的邀新（立即邀请下面的小字文案）
     */
    private String myInviteDesc;

    /**
     * 要新结果数据展示(您已成功邀请x人，成功获取x份早餐)
     */
    private String inviteDataDesc ;

    /**
     * 新人点击转发连接进入 展示图
     */
    private String newbieDisplayPic;

    /**
     * 邀新小程序转发图
     */
    public String inviteTransmitPic;

    /**
     * 邀新小程序转发文案
     */
    public String inviteTransmitText;

    /**
     * 新用户受邀，领券成功，券使用描述
     */
    private String inviteNewbieCouponUseDesc;

    /**
     * 邀新规则
     */
    private String inviteRule ;

    /**
     * 老带新弹层图片
     */
    private String inviteToastPic;

    /**
     * 首次分享标题
     */
    private String inviteFirstShareTitle;

    /**
     * 首次分享文案
     */
    private String inviteFirstShareDesc;

    /**
     * 二次分享标题
     */
    private String inviteSecondShareTitle;

    /**
     * 二次分享文案
     */
    private String inviteSecondShareDesc;

    /**
     * 提醒用户文案
     */
    private String inviteRemarkWord;

    /**
     * 邀请用户记录
     */
    private List<UserInviteRecordDto> inviteRecordList = Collections.emptyList();

    /**
     * 用户的邀新主信息
     */
    private ActivityUserInviteDto userInviteDto;

    /**
     * 首页分享文案
     */
    private String homeShareTitle;

    /**
     * 首页分享图片
     */
    private String homeSharePic;

    /**
     * 活动是否结束
     */
    private Boolean isOver;

    /**
     * 是否是我发起的
     */
    private Boolean isMy;

    /**
     * 是否创建老拉新活动实例
     */
    private Boolean isCreated;

    /**
     * 公司ID
     */
    private Long companyId;
    /**
     * 领取新人券成功
     */
    private Boolean receiveNewCouponFlag;

    /**
     * 领取新人红包成功
     */
    private Boolean receiveNewRedpacketFlag;

    public String getInviteNewbieCouponUseDesc() {
        if (!StringUtils.isEmpty(inviteNewbieCouponUseDesc) && receiveCoupon != null){
            return String.format(inviteNewbieCouponUseDesc,receiveCoupon.getName());
        }
        return inviteNewbieCouponUseDesc;
    }

    public void setInviteNewbieCouponUseDesc(String inviteNewbieCouponUseDesc) {
        this.inviteNewbieCouponUseDesc = inviteNewbieCouponUseDesc;
    }
}
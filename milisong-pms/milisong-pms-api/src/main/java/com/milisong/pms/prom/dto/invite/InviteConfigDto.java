package com.milisong.pms.prom.dto.invite;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * <pre>
 *    author  : tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2019/3/26   10:03
 *    desc    : 邀新配置相关信息
 *    version : v1.0
 * </pre>
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InviteConfigDto implements Serializable {

    private static final long serialVersionUID = 8206603838381073803L;
    //邀新首页入口图
    private String inviteHomeEnterPic ;
    //邀新我的 入口图
    private String inviteMineEnterPic ;
    //我的邀请详情页面展示图
    private String myInvitePic ;
    //我的邀新（立即邀请下面的小字文案）
    private String myInviteDesc ;
    //邀新小程序转发图
    private String inviteTransmitPic ;
    //邀新小程序转发文案
    private String inviteTransmitText ;
    //新人点击转发连接进入 展示图
    private String newbieDisplayPic ;
    //邀新送券数量限制
    private Integer inviteSendCouponValidity ;
    //邀新开始时间
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date inviteStartTime ;
    //邀新结束时间
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date inviteEndTime ;
    //邀新最大获得券数量
    private Integer inviteMaxCouponCount ;
    //邀新 赠送商品json数据
    private String inviteGoodsInfo ;
    //邀新规则
    private String inviteRule ;
    //要新结果数据展示(您已成功邀请x人，成功获取x份早餐)
    private String inviteDataDesc ;
    //邀新活动名称
    private String inviteActivityName ;
    //老拉新邀请活动id
    private Long inviteActivityId ;
    //新用户受邀，领券成功，券使用描述
    private String inviteNewbieCouponUseDesc ;

    //老带新弹层图片
    private String inviteToastPic;
    //首次分享标题
    private String inviteFirstShareTitle;
    //首次分享文案
    private String inviteFirstShareDesc;
    //二次分享标题
    private String inviteSecondShareTitle;
    //二次分享文案
    private String inviteSecondShareDesc;
    //提醒用户文案
    private String inviteRemarkWord;
    
}

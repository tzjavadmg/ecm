package com.milisong.pms.breakfast.service;

import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.milisong.ecm.common.api.ShopConfigService;
import com.milisong.ecm.common.notify.constant.BusinessLineEnum;
import com.milisong.ecm.common.util.RedisKeyUtils;
import com.milisong.pms.prom.api.ActivityUserInviteService;
import com.milisong.pms.prom.dto.invite.ActivityUserInviteDto;
import com.milisong.pms.prom.dto.invite.InviteConfigResponse;
import com.milisong.pms.prom.dto.invite.UserInviteRequest;
import com.milisong.pms.prom.dto.invite.UserInviteResponse;
import com.milisong.ucs.api.UserService;
import com.milisong.ucs.dto.UserDeliveryAddressDto;
import com.milisong.ucs.dto.UserDto;
import com.milisong.ucs.enums.UserSourceEnum;
import com.milisong.ucs.sdk.security.UserContext;
import com.milisong.ucs.sdk.security.UserSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.milisong.ecm.common.notify.constant.BusinessLineEnum.BREAKFAST;

/**
 * @author sailor wang
 * @date 2019/3/25 5:28 PM
 * @description
 */
@Slf4j
@Service
public class RestInviteService {

    @Autowired
    private ActivityUserInviteService inviteService;

    @Autowired
    private UserService userService;

    @Autowired
    private ActivityUserInviteService activityUserInviteService;

    @Autowired
    private ShopConfigService shopConfigService;

    public ResponseResult<ActivityUserInviteDto> createInviteActivity() {
        UserInviteRequest userInviteRequest = new UserInviteRequest();
        initInviteRequest(userInviteRequest);
        userInviteRequest.setOriginateUserId(UserContext.getCurrentUser().getId());
        return activityUserInviteService.createInviteActivity(userInviteRequest);
    }

    /**
     * 查询首页 邀新 入口展示图
     *
     * @return
     */
    public ResponseResult<InviteConfigResponse> queryEntryBannerConfig() {
        ResponseResult<InviteConfigResponse> config = inviteService.getConfig();
        InviteConfigResponse result = config.getData();
        Date inviteStartTime = result.getInviteStartTime();
        Date inviteEndTime = result.getInviteEndTime();
        InviteConfigResponse data = new InviteConfigResponse();
        config.setData(data);
        boolean isOver = checkOverTime(inviteStartTime,inviteEndTime);
        if(isOver){
            data.setIsOver(true);
        }else{
            data.setInviteMineEnterPic(result.getInviteMineEnterPic());
            data.setInviteHomeEnterPic(result.getInviteHomeEnterPic());
            data.setIsOver(false);
        }
        return config;
    }

    /**
     * 点击查看 -> 查询我的邀请信息
     *
     * @return
     */
    public ResponseResult<UserInviteResponse> queryMyInviteInfo() {
        UserInviteRequest userInviteRequest = new UserInviteRequest();
        initInviteRequest(userInviteRequest);
        ResponseResult<UserInviteResponse> responseResult = activityUserInviteService.queryMyInviteInfo(userInviteRequest);
        if (responseResult.getData() != null){
            //小程序分享文案
            responseResult.getData().setHomeShareTitle(shopConfigService.getShareTitle());
            //小程序分享图片
            responseResult.getData().setHomeSharePic(shopConfigService.getSharePicture());
            //用户公司id
            responseResult.getData().setCompanyId(getUserCompanyId(UserContext.getCurrentUser().getId()));
        }
        return responseResult;
    }

    private void initInviteRequest(UserInviteRequest userInviteRequest) {
        UserSession userSession = UserContext.getCurrentUser();
        userInviteRequest.setUserId(userSession.getId());
        userInviteRequest.setBusinessLine(BREAKFAST.getVal());
        userInviteRequest.setNickName(userSession.getNickName());
        userInviteRequest.setHeadPortraitUrl(userSession.getHeadPortraitUrl());
        userInviteRequest.setIsNew(userSession.getIsNew());
        userInviteRequest.setReceivedNewCoupon(userSession.getReceivedNewCoupon());
    }

    public ResponseResult<UserInviteResponse> clickSharedInviteActivity(Long originateUserId) {
        UserInviteRequest userInviteRequest = new UserInviteRequest();
        initInviteRequest(userInviteRequest);
        userInviteRequest.setOriginateUserId(originateUserId);
        ResponseResult<UserInviteResponse> responseResult = activityUserInviteService.queryInviteActivity(userInviteRequest);
        if (responseResult.getData() != null) {
            //小程序分享文案
            responseResult.getData().setHomeShareTitle(shopConfigService.getShareTitle());
            //小程序分享图片
            responseResult.getData().setHomeSharePic(shopConfigService.getSharePicture());
            Boolean receiveNewCouponFlag = responseResult.getData().getReceiveNewCouponFlag();
            Boolean receiveNewRedpacketFlag = responseResult.getData().getReceiveNewRedpacketFlag();
            //通过该链接领取了新人券或者红包
            updateReceivedNewCouponStatus(receiveNewCouponFlag,receiveNewRedpacketFlag);
        }
        return responseResult;
    }

    // 修改用户领取券状态
    private void updateReceivedNewCouponStatus(Boolean receiveNewCoupon,Boolean receiveNewRedpacketFlag) {
        UserSession userSession = UserContext.getCurrentUser();
        UserDto user = new UserDto();
        user.setId(userSession.getId());
        user.setOpenId(userSession.getOpenId());
        if (Boolean.TRUE.equals(receiveNewCoupon)){
            user.setReceivedNewCoupon((byte) 1);
            UserContext.freshUser(userSession.getOpenId(), UserSourceEnum.MINI_APP.getCode(), com.milisong.ucs.enums.BusinessLineEnum.BREAKFAST.getCode(), "receivedNewCoupon", "1");
        }
        if (Boolean.TRUE.equals(receiveNewRedpacketFlag)){
            user.setReceivedNewRedPacket((byte) 1);
            UserContext.freshUser(userSession.getOpenId(), UserSourceEnum.MINI_APP.getCode(), com.milisong.ucs.enums.BusinessLineEnum.BREAKFAST.getCode(), "receivedNewRedPacket", "1");
        }
        user.setUpdateTime(new Date());
        user.setBusinessLine(BREAKFAST.getVal());
        userService.updateUser(user);

    }

    private Long getUserCompanyId(Long originateUserId){
        String key = RedisKeyUtils.getLatestDeliveryAddressKey(originateUserId);
        String s = RedisCache.get(key);
        UserDeliveryAddressDto deliveryAddressDto = JSONObject.parseObject(s, UserDeliveryAddressDto.class);
        if(deliveryAddressDto == null){
            return null;
        }
        return deliveryAddressDto.getDeliveryOfficeBuildingId();
    }

    private boolean checkOverTime(Date startTime,Date endTime){
        Date now = new Date();
        if(startTime == null || endTime == null){
            log.error("没有查询到邀新活动的起始结束时间");
            return true;
        }else if(startTime.after(now) || endTime.before(now)){
            return true;
        }else {
            return false;
        }
    }
}
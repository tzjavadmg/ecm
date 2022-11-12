package com.milisong.pms.breakfast.service;

import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.milisong.pms.prom.api.ActivityService;
import com.milisong.pms.prom.api.BreakfastCouponService;
import com.milisong.pms.prom.dto.*;
import com.milisong.ucs.api.UserService;
import com.milisong.ucs.dto.UserDto;
import com.milisong.ucs.enums.UserSourceEnum;
import com.milisong.ucs.sdk.security.UserContext;
import com.milisong.ucs.sdk.security.UserSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.milisong.ecm.common.notify.constant.BusinessLineEnum.BREAKFAST;
import static com.milisong.pms.prom.enums.CouponEnum.TYPE.DISCOUNT;
import static com.milisong.pms.prom.enums.PromotionResponseCode.CREATE_MULTI_BREAKFAST_SHARE_COUPON_EXCEPTION;
import static com.milisong.pms.prom.enums.RedPacketLaunchType.BREAKFAST_COUPON_ORDER_SHARE;

/**
 * @author sailor wang
 * @date 2018/9/14 下午7:47
 * @description
 */
@Slf4j
@Service
public class RestBreakfastPromotionService {

    @Autowired
    BreakfastCouponService breakfastCouponService;

    @Autowired
    UserService userService;

    @Autowired
    ActivityService activityService;

    /**
     * 早餐券提示
     *
     * @return
     */
    public ResponseResult<UserBreakfastCouponTips> breakfastCouponTips() {
        UserCouponQueryParam queryParam = new UserCouponQueryParam();
        initQueryparam(queryParam);
        return breakfastCouponService.breakfastCouponTips(queryParam);
    }

    /**
     * 可用券列表
     *
     * @return
     */
    public ResponseResult<MyUsableBreakfastCouponDto> usableCoupons(UserCouponQueryParam queryParam) {
        initQueryparam(queryParam);
        return breakfastCouponService.usableCoupons(queryParam);
    }

    public ResponseResult<MyUsableBreakfastCouponDto> settleAccountsDiscount(UserCouponQueryParam queryParam) {
        initQueryparam(queryParam);
        return breakfastCouponService.settleAccountsDiscount(queryParam);
    }

    /**
     * 可用券数量
     *
     * @return
     */
    public ResponseResult<Integer> usableCouponsCount() {
        UserCouponQueryParam queryParam = new UserCouponQueryParam();
        initQueryparam(queryParam);
        queryParam.setUsable(Boolean.TRUE);
        return breakfastCouponService.usableCouponsCount(queryParam);
    }

    /**
     * 我的券列表
     *
     * @return
     */
    public ResponseResult<Pagination<UserCouponDto>> myCoupon(UserCouponQueryParam queryParam) {
        initQueryparam(queryParam);
        return breakfastCouponService.myCoupon(queryParam);
    }

    /**
     * 批量发送用户券
     *
     * @param queryParam
     * @return
     */
    public ResponseResult<Boolean> sendUserCoupon(UserCouponQueryParam queryParam){
        return breakfastCouponService.sendBreakfastCoupon(queryParam);
    }

    private void initQueryparam(UserCouponQueryParam queryParam) {
        queryParam.setUserId(UserContext.getCurrentUser().getId());
        queryParam.setBusinessLine(BREAKFAST.getVal());
    }

    public ResponseResult createMultiShareCoupon(Long orderid) {
        try {
            UserSession userSession = UserContext.getCurrentUser();
            UserCouponRequest couponRequest = UserCouponRequest.builder().userId(userSession.getId()).openid(userSession.getOpenId())
                    .nickName(userSession.getNickName()).headPortraitUrl(userSession.getHeadPortraitUrl())
                    .launchType(BREAKFAST_COUPON_ORDER_SHARE).launchId(orderid).build();
            couponRequest.setBusinessLine(BREAKFAST.getVal());
            return breakfastCouponService.createMultiShareCoupon(couponRequest);
        } catch (Exception e) {
            log.error("", e);
        }
        return ResponseResult.buildFailResponse(CREATE_MULTI_BREAKFAST_SHARE_COUPON_EXCEPTION.code(), CREATE_MULTI_BREAKFAST_SHARE_COUPON_EXCEPTION.message());
    }

    /**
     * 领取多人分享券
     *
     * @param orderid
     * @return
     */
    public ResponseResult<UserCouponResponse> recevieMultiShareCoupon(Long orderid) {
        UserSession userSession = UserContext.getCurrentUser();
        Byte receivedNewCoupon = userSession.getReceivedNewCoupon();
        UserCouponRequest couponRequest = UserCouponRequest.builder().userId(userSession.getId()).openid(userSession.getOpenId())
                .isNewUser(userSession.getIsNew()).nickName(userSession.getNickName()).receivedNewCoupon(receivedNewCoupon)
                .headPortraitUrl(userSession.getHeadPortraitUrl()).launchId(orderid).build();
        couponRequest.setBusinessLine(BREAKFAST.getVal());
        ResponseResult<UserCouponResponse> response = breakfastCouponService.recevieMultiShareCoupon(couponRequest);
        UserCouponResponse userCouponResponse = response.getData();
        Byte isNew = userSession.getIsNew();

        if (userCouponResponse != null && (isNew == null || isNew.intValue() == 0)) {
            Boolean receivedCoupon = false;
            Boolean receivedRedpacket = userCouponResponse.getReceivedRedpacket();
            //新用户领取新人券
            if (userCouponResponse.getNewCoupon() != null && (receivedNewCoupon == null || receivedNewCoupon.intValue() == 0)) {
                receivedCoupon = true;
            }
            updateReceivedNewCouponStatus(userSession.getId(),userSession.getOpenId(),receivedCoupon,receivedRedpacket);
        }
        return response;
    }

    // 修改用户领取券状态
    private void updateReceivedNewCouponStatus(Long userId, String openid,Boolean receivedCoupon,Boolean receivedRedpacket) {
        UserDto user = new UserDto();
        user.setId(userId);
        user.setOpenId(openid);
        if (Boolean.TRUE.equals(receivedCoupon)){
            user.setReceivedNewCoupon((byte) 1);
            UserContext.freshUser(openid, UserSourceEnum.MINI_APP.getCode(), com.milisong.ucs.enums.BusinessLineEnum.BREAKFAST.getCode(), "receivedNewCoupon", "1");
        }
        if (Boolean.TRUE.equals(receivedRedpacket)){
            user.setReceivedNewRedPacket((byte) 1);
            UserContext.freshUser(openid, UserSourceEnum.MINI_APP.getCode(), com.milisong.ucs.enums.BusinessLineEnum.BREAKFAST.getCode(), "receivedNewRedPacket", "1");
        }
        user.setUpdateTime(new Date());
        user.setBusinessLine(BREAKFAST.getVal());
        userService.updateUser(user);
    }

    public ResponseResult<MultiBreakfastCouponConfig> multiShareCouponInfo() {
        ActivityQueryParam queryParam = new ActivityQueryParam();
        queryParam.setCouponType(DISCOUNT);
        queryParam.setBusinessLine(BREAKFAST.getVal());
        return activityService.currentActivityCouponConfig(queryParam);
    }
}
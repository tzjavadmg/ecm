package com.milisong.pms.prom.aspect;

import com.farmland.core.api.ResponseResult;
import com.milisong.pms.prom.api.ActivityUserRedPacketService;
import com.milisong.pms.prom.api.BreakfastCouponService;
import com.milisong.pms.prom.dto.*;
import com.milisong.pms.prom.mapper.UserCouponMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.milisong.pms.prom.enums.CouponEnum.TYPE.NEW_DISCOUNT;
import static com.milisong.pms.prom.enums.UserActivityType.MULTI_SHARE_COUPON;
import static com.milisong.ucs.enums.BusinessLineEnum.BREAKFAST;

/**
 * 新人券
 *
 * @author sailor wang
 * @date 2018/8/31 下午5:40
 * @description
 */
@Slf4j
@Aspect
@Component
public class AfterReceiveNewCouponAspect {

    @Autowired
    private BreakfastCouponService breakfastCouponService;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private ActivityUserRedPacketService activityUserRedPacketService;


    @Pointcut("@annotation(com.milisong.pms.prom.annotation.AfterReceiveNewCoupon)")
    public void receiveCouponCut() {
    }


    @AfterReturning(returning = "rvt", pointcut = "receiveCouponCut()")
    public void after(JoinPoint point, Object rvt) {
        log.info("领取早餐新人券切面");
        Long userId = null;
        Byte isNew = null;
        Byte receivedNewCoupon = null;
        Object[] args = point.getArgs();
        if (args != null) {
            for (Object arg : args) {
                try {
                    if (arg instanceof UserCouponRequest && userId == null) {
                        UserCouponRequest couponRequest = (UserCouponRequest) arg;
                        userId = couponRequest.getUserId();
                        isNew = couponRequest.getIsNewUser();
                        receivedNewCoupon = couponRequest.getReceivedNewCoupon();
                    }
                } catch (Exception e) {
                    log.error("", e);
                }
            }
        }
        log.info("早餐券切面===========================早餐券切面");
        try {
            if (rvt != null && rvt instanceof ResponseResult) {
                ResponseResult responseResult = (ResponseResult) rvt;
                if (responseResult.getData() != null && responseResult.getData() instanceof UserCouponResponse) {
                    UserCouponResponse userCouponResponse = (UserCouponResponse) responseResult.getData();
                    log.info("用户早餐券活动 userCouponResponse -> {}",userCouponResponse);

                    UserCouponDto userCouponDto = null;
                    Long userActivityId = userCouponResponse.getUserActivityId();
                    if ((isNew != null && isNew.intValue() == 1) || (receivedNewCoupon != null && receivedNewCoupon.intValue() == 1)){// 老用户
                        userCouponDto = userCouponMapper.queryUserOptimalCouponByUserIdActivityId(userId,userActivityId,NEW_DISCOUNT.getCode());
                    }else {// 新人券
                        log.info("未领过新人券。。。");
                        UserCouponQueryParam queryParam = new UserCouponQueryParam();
                        queryParam.setUserId(userId);
                        queryParam.setBusinessLine(BREAKFAST.getCode());
                        queryParam.setUserActivityId(userActivityId);
                        queryParam.setUserActivityType(MULTI_SHARE_COUPON.getCode());
                        ResponseResult<UserCouponDto> couponDtoResponseResult = breakfastCouponService.receiveNewCoupon(queryParam);
                        userCouponDto = couponDtoResponseResult.getData();
                    }

                    log.info("多人分享领取新人券 userCouponDto -> {}",userCouponDto);
                    if (userCouponDto != null){
                        // 判断是否通过该活动渠道领取的，如果是则在该活动中展示
                        userCouponResponse.setNewCoupon(userCouponDto);
                    }

                    if (isNew == null || isNew.intValue() == 0){//新人标识
                        ResponseResult<UserRedPacketDto> result = activityUserRedPacketService.receiveNewRedPacket(userId,null);
                        userCouponResponse.setReceivedRedpacket(result.isSuccess() && result.getData()!=null);//领取红包标识
                    }

                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }


}

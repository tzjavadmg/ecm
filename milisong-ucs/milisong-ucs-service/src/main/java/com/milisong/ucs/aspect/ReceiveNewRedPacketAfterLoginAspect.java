package com.milisong.ucs.aspect;

import com.farmland.core.api.ResponseResult;
import com.milisong.pms.prom.api.ActivityUserRedPacketService;
import com.milisong.pms.prom.api.BreakfastCouponService;
import com.milisong.pms.prom.dto.UserCouponDto;
import com.milisong.pms.prom.dto.UserCouponQueryParam;
import com.milisong.ucs.domain.User;
import com.milisong.ucs.dto.UserDto;
import com.milisong.ucs.dto.UserRequest;
import com.milisong.ucs.enums.BusinessLineEnum;
import com.milisong.ucs.mapper.UserMapper;
import com.milisong.ucs.sdk.security.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 登录后领取新人红包
 *
 * @author sailor wang
 * @date 2018/8/31 下午5:40
 * @description
 */
@Slf4j
@Aspect
@Component
public class ReceiveNewRedPacketAfterLoginAspect {
    @Autowired
    UserMapper userMapper;

    @Autowired
    ActivityUserRedPacketService activityUserRedPacketService;

    @Autowired
    BreakfastCouponService couponService;


    @Pointcut("@annotation(com.milisong.ucs.annotation.ReceiveNewRedPacketAfterLogin)")
    public void receiveRedPacketCut() {
    }


    @AfterReturning(returning = "rvt", pointcut = "receiveRedPacketCut()")
    public void after(JoinPoint point, Object rvt) {
        try {
            Object[] args = point.getArgs();
            Integer loginSource = null;//登录源：首页登录、多人活动分享登录（为null或者0是首页登录）
            Byte buzLine = null;
            if (args != null) {
                for (Object arg : args) {
                    try {
                        if (arg instanceof UserRequest) {
                            UserRequest userRequest = (UserRequest) arg;
                            loginSource = userRequest.getLoginSource();
                            buzLine = userRequest.getBusinessLine();
                            log.info("loginSource -> {}, buzLine -> {}", loginSource, buzLine);
                        }
                    } catch (Exception e) {
                        log.error("", e);
                    }
                }
            }
            if (rvt != null && rvt instanceof ResponseResult) {
                ResponseResult responseResult = (ResponseResult) rvt;
                if (responseResult.getData() != null && responseResult.getData() instanceof UserDto) {
                    UserDto userDto = (UserDto) responseResult.getData();

                    if (BusinessLineEnum.LUNCH.getCode().equals(buzLine)) {
                        receivedNewRedPacket(userDto, loginSource);//领取午餐红包
                    } else if (BusinessLineEnum.BREAKFAST.getCode().equals(buzLine)) {
                        receivedNewCoupon(userDto, loginSource, buzLine);//领取早餐红包
                        receivedNewRedPacket(userDto, loginSource);//合并版：早餐小程序领取午餐红包
                    }
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    private void receivedNewRedPacket(UserDto userDto, Integer loginSource) {
        if ((userDto.getIsNew() != null && userDto.getIsNew().intValue() == 0)
                && (userDto.getReceivedNewRedPacket() == null || userDto.getReceivedNewRedPacket().intValue() == 0)
                && (loginSource == null || loginSource == 0)) {
            // 未领取过新人红包，领取新人红包 -> 修改领取红包状态
            ResponseResult result = activityUserRedPacketService.receiveNewRedPacket(userDto.getId(), null);
            if (result.isSuccess()) {
                log.info("领取新人红包成功！");
                User user = new User();
                user.setReceivedNewRedPacket((byte) 1);
                user.setId(userDto.getId());
                user.setUpdateTime(new Date());
                userMapper.updateByPrimaryKeySelective(user);
            } else {
                log.info("领取新人红包失败！");
            }
        }
    }

    private void receivedNewCoupon(UserDto userDto, Integer loginSource, Byte buzLine) {
        if ((userDto.getIsNew() != null && userDto.getIsNew().intValue() == 0)
                && (userDto.getReceivedNewCoupon() == null || userDto.getReceivedNewCoupon().intValue() == 0)
                && (loginSource == null || loginSource == 0)) {
            // 未领取过新人券，领取新人券 -> 修改领取券状态
            UserCouponQueryParam queryParam = new UserCouponQueryParam();
            queryParam.setUserId(userDto.getId());
            queryParam.setBusinessLine(buzLine);
            ResponseResult<UserCouponDto> result = couponService.receiveNewCoupon(queryParam);
            if (result.isSuccess() && result.getData() != null) {
                log.info("领取新人券成功！");
                User user = new User();
                user.setReceivedNewCoupon((byte) 1);
                user.setId(userDto.getId());
                user.setUpdateTime(new Date());
                int row = userMapper.updateByPrimaryKeySelective(user);
                if (row > 0) {
                    UserContext.freshUser(userDto.getOpenId(), userDto.getRegisterSource(), buzLine, "receivedNewCoupon", "1");
                }
            } else {
                log.info("领取新人券失败！");
            }
        }
    }


}

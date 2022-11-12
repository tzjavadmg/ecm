package com.milisong.pms.prom.aspect;

import com.farmland.core.api.ResponseResult;
import com.milisong.pms.prom.api.ActivityRedPacketActionService;
import com.milisong.pms.prom.api.ActivityUserRedPacketService;
import com.milisong.pms.prom.dto.ActivityUserRedPacketDto;
import com.milisong.pms.prom.dto.UserRedPacketRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 砍红包切面
 *
 * @author sailor wang
 * @date 2018/8/31 下午5:40
 * @description
 */
@Slf4j
@Aspect
@Component
public class BeforeCutRedPacketAspect {

    @Autowired
    private ActivityRedPacketActionService activityRedPacketHackService;

    @Autowired
    private ActivityUserRedPacketService activityUserRedPacketService;


    @Pointcut("@annotation(com.milisong.pms.prom.annotation.BeforeCutRedPacket)")
    public void redPacketCut() {
    }

    @Before("redPacketCut()")
    public void before(JoinPoint point) {
        Object[] args = point.getArgs();
        if (args != null) {
            for (Object arg : args) {
                try {
                    if (arg instanceof UserRedPacketRequest) {
                        UserRedPacketRequest redPacketRequest = (UserRedPacketRequest) arg;
                        if (redPacketRequest != null && redPacketRequest.getUserId() != null) {
                            // 用户当天砍红包次数
                            redPacketRequest.setHackTimes(hackTimes(redPacketRequest.getUserId()));

                            ActivityUserRedPacketDto activityUserRedPacket = null;

                            if (redPacketRequest.getActivityUserRedPacketId() == null){
                                // 用户是否发起过砍红包
                                activityUserRedPacket = queryUserRedPacketByUserId(redPacketRequest.getUserId());
                            }else {
                                activityUserRedPacket = queryUserRedPacketById(redPacketRequest.getActivityUserRedPacketId());
                            }

                            if (activityUserRedPacket != null){
                                redPacketRequest.setActivityUserRedPacket(activityUserRedPacket);
                                redPacketRequest.setHackCurrentRedPacket(hasHackActivityRedPacket(redPacketRequest.getUserId(),activityUserRedPacket.getId()));
                            }
                        }
                        return;
                    }
                } catch (Exception e) {
                    log.error("", e);
                }
            }
        }
    }

    private ActivityUserRedPacketDto queryUserRedPacketById(Long activityUserRedPacketId) {
        ResponseResult<ActivityUserRedPacketDto> responseResult = activityUserRedPacketService.queryUserRedPacketById(activityUserRedPacketId);
        return responseResult.getData();
    }

    private ActivityUserRedPacketDto queryUserRedPacketByUserId(Long userId) {
        ResponseResult<ActivityUserRedPacketDto> responseResult = activityUserRedPacketService.queryUserCutRedPacketByUserId(userId);
        return responseResult.getData();
    }

    private int hackTimes(Long userId) {
        ResponseResult<Integer> responseResult = activityRedPacketHackService.hackRedPacketTimes(userId);
        return responseResult.getData();
    }

    private Boolean hasHackActivityRedPacket(Long userId,Long userActivityId){
        ResponseResult<Boolean> responseResult = activityRedPacketHackService.hasHackActivityRedPacket(userId,userActivityId);
        return Boolean.TRUE.equals(responseResult.getData());
    }


}

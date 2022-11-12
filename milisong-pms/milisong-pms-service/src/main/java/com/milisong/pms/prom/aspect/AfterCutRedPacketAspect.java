package com.milisong.pms.prom.aspect;

import com.farmland.core.api.ResponseResult;
import com.milisong.pms.prom.api.ActivityRedPacketActionService;
import com.milisong.pms.prom.dto.ActivityRedPacketActionDto;
import com.milisong.pms.prom.dto.UserRedPacketResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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
public class AfterCutRedPacketAspect {

    @Autowired
    private ActivityRedPacketActionService activityRedPacketHackService;


    @Pointcut("@annotation(com.milisong.pms.prom.annotation.AfterCutRedPacket)")
    public void redPacketCut() {
    }


    @AfterReturning(returning = "rvt", pointcut = "redPacketCut()")
    public void after(JoinPoint point, Object rvt) {
        try {
            if (rvt != null && rvt instanceof ResponseResult) {
                ResponseResult responseResult = (ResponseResult) rvt;
                if (responseResult.getData() != null && responseResult.getData() instanceof UserRedPacketResponse) {
                    UserRedPacketResponse userRedPacketResponse = (UserRedPacketResponse) responseResult.getData();
                    Long userRedPacketId = userRedPacketResponse.getCurrentUserRedPacket().getId();
                    if (userRedPacketId != null) {
                        ResponseResult<List<ActivityRedPacketActionDto>> result = activityRedPacketHackService.hackRedPacketRecord(userRedPacketId);
                        if (result.isSuccess()) {
                            userRedPacketResponse.setHackRedPacketRecord(result.getData());
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }


}

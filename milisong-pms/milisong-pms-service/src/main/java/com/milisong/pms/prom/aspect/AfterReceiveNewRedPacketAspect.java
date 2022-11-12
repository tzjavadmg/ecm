package com.milisong.pms.prom.aspect;

import com.farmland.core.api.ResponseResult;
import com.milisong.pms.prom.api.ActivityUserRedPacketService;
import com.milisong.pms.prom.dto.UserRedPacketDto;
import com.milisong.pms.prom.dto.UserRedPacketRequest;
import com.milisong.pms.prom.dto.UserRedPacketResponse;
import com.milisong.pms.prom.mapper.UserRedPacketMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.milisong.pms.prom.enums.RedPacketType.NEW_REDPACKET;

/**
 * 新人红包
 *
 * @author sailor wang
 * @date 2018/8/31 下午5:40
 * @description
 */
@Slf4j
@Aspect
@Component
public class AfterReceiveNewRedPacketAspect {

    @Autowired
    private ActivityUserRedPacketService activityUserRedPacketService;

    @Autowired
    private UserRedPacketMapper userRedPacketMapper;


    @Pointcut("@annotation(com.milisong.pms.prom.annotation.AfterReceiveNewRedPacket)")
    public void receiveRedPacketCut() {
    }


    @AfterReturning(returning = "rvt", pointcut = "receiveRedPacketCut()")
    public void after(JoinPoint point, Object rvt) {
        log.info("领取新人红包切面");
        Long userId = null;
        Byte isNew = null;
        Object[] args = point.getArgs();
        if (args != null) {
            for (Object arg : args) {
                try {
                    if (arg instanceof UserRedPacketRequest && userId == null) {
                        UserRedPacketRequest redPacketRequest = (UserRedPacketRequest) arg;
                        userId = redPacketRequest.getUserId();
                        isNew = redPacketRequest.getIsNewUser();
                    }
                } catch (Exception e) {
                    log.error("", e);
                }
            }
        }
        log.info("红包切面===========================红包切面");
        try {
            if (rvt != null && rvt instanceof ResponseResult) {
                ResponseResult responseResult = (ResponseResult) rvt;
                if (responseResult.getData() != null && responseResult.getData() instanceof UserRedPacketResponse) {
                    UserRedPacketResponse userRedPacketResponse = (UserRedPacketResponse) responseResult.getData();
                    log.info("用户红包活动 userRedPacketResponse -> {}",userRedPacketResponse);

                    UserRedPacketDto userRedPacketDto = null;
                    Long userActivityId = userRedPacketResponse.getUserActivityId();
                    if (isNew != null && isNew.intValue() == 1){// 老用户
                        List<UserRedPacketDto> redPacketDtoList = userRedPacketMapper.queryUserRedPacket(userId, NEW_REDPACKET.getCode(),null);
                        if (CollectionUtils.isNotEmpty(redPacketDtoList)){
                            log.info("已领过。。。");
                            // 已领过
                            userRedPacketDto = redPacketDtoList.get(0);
                        }
                    }else {// 新人红包
                        log.info("未领过。。。");
                        ResponseResult<UserRedPacketDto> redPacketDtoResponseResult = activityUserRedPacketService.receiveNewRedPacket(userId,userActivityId);
                        userRedPacketDto = redPacketDtoResponseResult.getData();
                    }

                    log.info("多人分享领取新人红包 userRedPacketDto -> {}",userRedPacketDto);
                    if (userRedPacketDto != null){
                        // 判断是否通过该活动渠道领取的，如果是则在该活动中展示
                        if (userRedPacketDto.getUserActivityId() != null && userRedPacketDto.getUserActivityId().equals(userActivityId)){
                            userRedPacketResponse.setNewRedPacket(userRedPacketDto);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }


}

package com.milisong.pms.prom.service;

import com.farmland.core.api.ResponseResult;
import com.farmland.core.db.IdGenerator;
import com.farmland.core.util.BeanMapper;
import com.milisong.pms.prom.api.ActivityRedPacketActionService;
import com.milisong.pms.prom.domain.ActivityRedPacketAction;
import com.milisong.pms.prom.dto.ActivityRedPacketActionDto;
import com.milisong.pms.prom.dto.ActivityUserRedPacketDto;
import com.milisong.pms.prom.mapper.ActivityRedPacketActionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

import static com.milisong.pms.prom.enums.PromotionResponseCode.REQUEST_PARAM_EMPTY;

/**
 * @author sailor wang
 * @date 2018/9/13 下午8:54
 * @description
 */
@Slf4j
@Service
@RestController
public class ActivityRedPacketActionServiceImpl implements ActivityRedPacketActionService {

    @Autowired
    private ActivityRedPacketActionMapper activityRedPacketHackMapper;

    /**
     * 砍红包
     *
     * @param activityUserRedPacket
     * @return
     */
    @Override
    @PostMapping(value = "/v1/ActivityRedPacketActionService/hackUserRedPacket")
    public ResponseResult<ActivityRedPacketActionDto> hackUserRedPacket(@RequestBody ActivityUserRedPacketDto activityUserRedPacket) {
        if (activityUserRedPacket == null || activityUserRedPacket.getId() == null || activityUserRedPacket.getUserId() == null || activityUserRedPacket.getOpenId() == null || activityUserRedPacket.getBusinessLine() == null){
            log.info("砍红包参数为空， activityUserRedPacket -> {}",activityUserRedPacket);
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(),REQUEST_PARAM_EMPTY.message());
        }

        try {
            //TODO 加业务线字段
            ActivityRedPacketAction redPacketHack = new ActivityRedPacketAction();
            redPacketHack.setId(IdGenerator.nextId());
            redPacketHack.setUserActivityId(activityUserRedPacket.getId());
            redPacketHack.setUserId(activityUserRedPacket.getUserId());
            redPacketHack.setOpenId(activityUserRedPacket.getOpenId());
            redPacketHack.setNickName(activityUserRedPacket.getNickName());
            redPacketHack.setHeadPortraitUrl(activityUserRedPacket.getHeadPortraitUrl());
            redPacketHack.setCreateTime(new Date());
            redPacketHack.setUpdateTime(new Date());
            int row = activityRedPacketHackMapper.insertSelective(redPacketHack);
            if (row > 0) {
                ActivityRedPacketActionDto activityRedPacketHack = BeanMapper.map(redPacketHack, ActivityRedPacketActionDto.class);
                return ResponseResult.buildSuccessResponse(activityRedPacketHack);
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return ResponseResult.buildFailResponse();
    }

    /**
     * 获取用户当天砍红包次数
     *
     * @param userId
     * @return
     */
    @Override
    @PostMapping(value = "/v1/ActivityRedPacketActionService/hackRedPacketTimes")
    public ResponseResult<Integer> hackRedPacketTimes(@RequestParam("userId") Long userId) {
        if (userId == null) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(),REQUEST_PARAM_EMPTY.message());
        }
        try {
            int hackRedPacketTimes = activityRedPacketHackMapper.hackRedPacketTimes(userId);
            return ResponseResult.buildSuccessResponse(hackRedPacketTimes);
        } catch (Exception e) {
            log.error("",e);
            return ResponseResult.buildFailResponse();
        }
    }

    /**
     * 用户砍某红包活动的次数
     * @param userId
     * @param activityUserRedPacketId
     * @return
     */
    @Override
    @PostMapping(value = "/v1/ActivityRedPacketActionService/hasHackActivityRedPacket")
    public ResponseResult<Boolean> hasHackActivityRedPacket(@RequestParam("userId") Long userId,@RequestParam("activityUserRedPacketId") Long activityUserRedPacketId) {
        if (userId == null || activityUserRedPacketId == null) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(),REQUEST_PARAM_EMPTY.message());
        }
        try {
            Integer times = activityRedPacketHackMapper.hackActivityRedPacketTimes(userId,activityUserRedPacketId);
            return ResponseResult.buildSuccessResponse(times>0);
        } catch (Exception e) {
            log.error("",e);
            return ResponseResult.buildFailResponse();
        }
    }

    @Override
    @PostMapping(value = "/v1/ActivityRedPacketActionService/hackRedPacketRecord")
    public ResponseResult<List<ActivityRedPacketActionDto>> hackRedPacketRecord(@RequestParam("userRedPacketId") Long userRedPacketId) {
        if (userRedPacketId == null){
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(),REQUEST_PARAM_EMPTY.message());
        }
        try {
            List<ActivityRedPacketActionDto> list = activityRedPacketHackMapper.hackRedPacketRecord(userRedPacketId);
            return ResponseResult.buildSuccessResponse(list);
        } catch (Exception e) {
            log.error("",e);
            return ResponseResult.buildFailResponse();
        }
    }
}
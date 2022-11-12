package com.milisong.pms.prom.api;

import com.farmland.core.api.ResponseResult;
import com.milisong.pms.prom.dto.ActivityRedPacketActionDto;
import com.milisong.pms.prom.dto.ActivityUserRedPacketDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 砍红包接口
 * @author sailor wang
 * @date 2018/9/13 下午8:54
 * @description
 */
@FeignClient("milisong-pms-service")
public interface ActivityRedPacketActionService {

    /**
     * 砍红包
     * @param activityUserRedPacket
     * @return
     */
    @PostMapping(value = "/v1/ActivityRedPacketActionService/hackUserRedPacket")
    ResponseResult<ActivityRedPacketActionDto> hackUserRedPacket(@RequestBody ActivityUserRedPacketDto activityUserRedPacket);

    /**
     * 获取用户当天砍红包次数
     * @param userId
     * @return
     */
    @PostMapping(value = "/v1/ActivityRedPacketActionService/hackRedPacketTimes")
    ResponseResult<Integer> hackRedPacketTimes(@RequestParam("userId") Long userId);

    /**
     * 用户砍某红包活动的次数
     * @param userId
     * @param activityUserRedPacketId
     * @return
     */
    @PostMapping(value = "/v1/ActivityRedPacketActionService/hasHackActivityRedPacket")
    ResponseResult<Boolean> hasHackActivityRedPacket(@RequestParam("userId") Long userId,@RequestParam("activityUserRedPacketId") Long activityUserRedPacketId);

    /**
     * 砍红包记录
     * @param userRedPacketId
     * @return
     */
    @PostMapping(value = "/v1/ActivityRedPacketActionService/hackRedPacketRecord")
    ResponseResult<List<ActivityRedPacketActionDto>> hackRedPacketRecord(@RequestParam("userRedPacketId") Long userRedPacketId);
}
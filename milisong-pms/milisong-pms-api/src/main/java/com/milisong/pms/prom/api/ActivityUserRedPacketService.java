package com.milisong.pms.prom.api;

import com.farmland.core.api.ResponseResult;
import com.milisong.pms.prom.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author sailor wang
 * @date 2018/9/13 上午11:54
 * @description
 */
@FeignClient("milisong-pms-service")
public interface ActivityUserRedPacketService {

    /**
     * 发起红包活动
     *
     * @param redPacketRequest
     * @return
     */
    @PostMapping(value = "/v1/ActivityUserRedPacketService/createUserCutRedPacket")
    ResponseResult<UserRedPacketResponse> createUserCutRedPacket(@RequestBody UserRedPacketRequest redPacketRequest);

    /**
     * 查询该用户有无发起过砍红包
     * @param userId
     * @return
     */
    @PostMapping(value = "/v1/ActivityUserRedPacketService/queryUserCutRedPacketByUserId")
    ResponseResult<ActivityUserRedPacketDto> queryUserCutRedPacketByUserId(@RequestParam("userId") Long userId);

    /**
     * 砍红包
     * @param redPacketRequest
     * @return
     */
    @PostMapping(value = "/v1/ActivityUserRedPacketService/hackUserCutRedPacket")
    ResponseResult<UserRedPacketResponse> hackUserCutRedPacket(@RequestBody UserRedPacketRequest redPacketRequest);


    @PostMapping(value = "/v1/ActivityUserRedPacketService/queryUserRedPacketById")
    ResponseResult<ActivityUserRedPacketDto> queryUserRedPacketById(@RequestParam("activityUserRedPacketId") Long activityUserRedPacketId);

    /**
     * 领取新用户红包
     * @param userId
     * @return
     */
    @PostMapping(value = "/v1/ActivityUserRedPacketService/receiveNewRedPacket")
    ResponseResult<UserRedPacketDto> receiveNewRedPacket(@RequestParam("userId") Long userId,@RequestParam(value = "userActivityId",required = false)  Long userActivityId);

    /**
     * 多人分享红包实例
     * @param redPacketRequest
     * @return
     */
    @PostMapping(value = "/v1/ActivityUserRedPacketService/createMultiShareRedPacket")
    ResponseResult<UserRedPacketResponse> createMultiShareRedPacket(@RequestBody UserRedPacketRequest redPacketRequest) throws Exception;

    @PostMapping(value = "/v1/ActivityUserRedPacketService/recevieMultiShareRedPacket")
    ResponseResult<UserRedPacketResponse> recevieMultiShareRedPacket(@RequestBody UserRedPacketRequest redPacketRequest);

    @PostMapping(value = "/v1/ActivityUserRedPacketService/recevieLCMultiShareRedPacket")
    ResponseResult<LCUserRedPacketResponse> recevieLCMultiShareRedPacket(@RequestBody UserRedPacketRequest redPacketRequest);

    @PostMapping(value = "/v1/ActivityUserRedPacketService/multiShareRedPacketLaunchQuery")
    ResponseResult<RedPacketLaunchResultDto> multiShareRedPacketLaunchQuery(@RequestBody RedPacketLaunchQueryDto redPacketLaunchQuery);
}
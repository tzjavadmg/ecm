package com.milisong.pms.prom.api;

import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.milisong.pms.prom.dto.*;
import com.milisong.pms.prom.enums.RedPacketType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author sailor wang
 * @date 2018/9/14 下午7:06
 * @description
 */
@FeignClient("milisong-pms-service")
public interface UserRedPacketService {

    @PostMapping(value = "/v1/UserRedPacketService/queryUserRedPacket")
    ResponseResult<List<UserRedPacketDto>> queryUserRedPacket(@RequestParam("userId") Long userId, @RequestParam("type") RedPacketType type);

    @PostMapping(value = "/v1/UserRedPacketService/queryByUserRedPacketId")
    ResponseResult<UserRedPacketDto> queryByUserRedPacketId(@RequestParam("userRedPacketId") Long userRedPacketId);

    @PostMapping(value = "/v1/UserRedPacketService/queryUserRedPacketByOrderId")
    ResponseResult<UserRedPacketDto> queryUserRedPacketByOrderId(@RequestParam("orderId") Long orderId);

    @PostMapping(value = "/v1/UserRedPacketService/useUserRedPacket")
    ResponseResult<Boolean> useUserRedPacket(@RequestParam("userRedPacketId") Long userRedPacketId, @RequestParam("orderId") Long orderId);

    @PostMapping(value = "/v1/UserRedPacketService/updateUserRedPacketUseful")
    ResponseResult<Boolean> updateUserRedPacketUseful(@RequestParam("userRedPacketId") Long userRedPacketId);

    @PostMapping(value = "/v1/UserRedPacketService/usableUserRedPacketCount")
    ResponseResult<Integer> usableUserRedPacketCount(@RequestParam("userId") Long userId);

    @PostMapping(value = "/v1/UserRedPacketService/usableUserRedPackets")
    ResponseResult<UserRedPacketResponse> usableUserRedPackets(@RequestParam("userId") Long userId);

    @PostMapping(value = "/v1/UserRedPacketService/userRedPacketList")
    ResponseResult<Pagination<UserRedPacketDto>> userRedPacketList(@RequestBody ActivityQueryParam queryParam);

    @PostMapping(value = "/v1/UserRedPacketService/scanUserRedPacket")
    ResponseResult scanUserRedPacket();

    @PostMapping(value = "/v1/UserRedPacketService/sendRedPacket")
    ResponseResult<Boolean> sendRedPacket();

    @PostMapping(value = "/v1/UserRedPacketService/batchSendRedPacket")
    ResponseResult<String> batchSendRedPacket(@RequestBody BatchSendRedPacketParam sendRedPacketParam);

    @PostMapping(value = "/v1/UserRedPacketService/toastRedPacketConfig")
    ResponseResult<ToastRedPacketConfig> toastRedPacketConfig();

    @PostMapping(value = "/v1/UserRedPacketService/toastUserRedPacket")
    ResponseResult<List<UserRedPacketDto>> toastUserRedPacket(@RequestParam("userId") Long userId, @RequestParam("num") Integer num);

    @PostMapping(value = "/v1/UserRedPacketService/fullReduceConfig")
    ResponseResult<FullReduce> fullReduceConfig();




}
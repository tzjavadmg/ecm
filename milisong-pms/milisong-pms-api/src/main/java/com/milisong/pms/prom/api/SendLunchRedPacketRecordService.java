package com.milisong.pms.prom.api;

import com.farmland.core.api.ResponseResult;
import com.milisong.pms.prom.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author sailor wang
 * @date 2019/1/13 10:37 AM
 * @description
 */
@FeignClient("milisong-pms-service")
public interface SendLunchRedPacketRecordService {

    @PostMapping(value = "/v1/SendLunchRedPacketRecordService/getById")
    ResponseResult<SendRedPacketSearchConditionDto> getById(@RequestParam("sendRedPacketRecordId") Long sendRedPacketRecordId);

    /**
     * 查询红包发送记录
     *
     * @param sendLunchRedPacket
     * @return
     */
    @PostMapping("/v1/SendLunchRedPacketRecordService/querySendRedPacketRecord")
    ResponseResult<Pagenation<SendRedpacketRecordDto>> querySendRedPacketRecord(@RequestBody SendLunchRedPacketRequest sendLunchRedPacket);

    /**
     * 查看红包发放流水
     *
     * @param waterRequest
     * @return
     */
    @PostMapping("/v1/SendLunchRedPacketRecordService/querySendRedPacketWater")
    ResponseResult<Pagenation<SendRedpacketWaterDto>> querySendRedPacketWater(@RequestBody SendRedPacketWaterRequest waterRequest);

    @PostMapping("/v1/SendLunchRedPacketRecordService/scheduleSendRedPacket")
    ResponseResult<Boolean> scheduleSendRedPacket();
}

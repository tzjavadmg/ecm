package com.milisong.pms.prom.api;

import com.farmland.core.api.ResponseResult;
import com.milisong.pms.prom.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 红包
 *
 * @author sailor wang
 * @date 2019/2/18 3:34 PM
 * @description
 */
@FeignClient("milisong-pms-service")
public interface LunchRedPacketService {

    @PostMapping(value = "/v1/LunchRedPacketService/saveOrUpdate")
    ResponseResult<Long> saveOrUpdate(@RequestBody RedPacketDto redPacketDto);

    /**
     * 红包列表
     *
     * @param dto
     * @return
     */
    @PostMapping("/v1/LunchRedPacketService/queryRedpacket")
    ResponseResult<Pagenation<RedPacketDto>> queryRedpacket(@RequestBody RedPacketQueryDto dto);

    @PostMapping("/v1/LunchRedPacketService/queryById")
    ResponseResult<RedPacketDto> queryById(@RequestParam("id") Long id);

    /**
     * 下线红包
     * @param id
     * @return
     */
    @PostMapping("/v1/LunchRedPacketService/offlineRedpacket")
    ResponseResult<Boolean> offlineRedpacket(@RequestParam("id") Long id);

    /**
     * 发放红包
     *
     * @param sendLunchRedPacket
     * @return
     */
    @PostMapping("/v1/LunchRedPacketService/sendLunchRedPacket")
    ResponseResult<Boolean> sendRedPacket(@RequestBody SendLunchRedPacketRequest sendLunchRedPacket);

}
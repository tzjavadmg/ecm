package com.milisong.pms.rest;

import com.farmland.core.api.ResponseResult;
import com.milisong.pms.lunch.service.RestRedPacketService;
import com.milisong.pms.prom.dto.*;
import com.milisong.ucs.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 红包管理
 *
 * @author sailor wang
 * @date 2018/11/7 4:09 PM
 * @description
 */
@Slf4j
@RestController
@RequestMapping("/v1/redpacket/")
public class RedPacketRest {

    @Autowired
    RestRedPacketService restRedPacketService;

    /**
     * 创建修改红包
     *
     * @param redPacketDto
     * @return
     */
    @PostMapping("create-update-redpacket")
    ResponseResult<Long> createOrUpdateRedpacket(@RequestBody RedPacketDto redPacketDto) {
        return restRedPacketService.createOrUpdateRedpacket(redPacketDto);
    }

    /**
     * 红包列表
     *
     * @param queryDto
     * @return
     */
    @PostMapping("redpacket-list")
    ResponseResult<Pagenation<RedPacketDto>> redpacketList(@RequestBody RedPacketQueryDto queryDto) {
        return restRedPacketService.redpacketList(queryDto);
    }

    /**
     * 查询，通过id
     * @param id
     * @return
     */
    @GetMapping("query")
    ResponseResult<RedPacketDto> query(@RequestParam("id")Long id){
        return restRedPacketService.queryById(id);
    }

    /**
     * 下线红包
     * @param id
     * @return
     */
    @GetMapping("offline")
    ResponseResult<Boolean> offline(@RequestParam("id")Long id){
        return restRedPacketService.offline(id);
    }

    /**
     * 发送午餐红包
     *
     * @param sendLunchRedPacket
     * @return
     */
    @PostMapping("send-redpacket")
    ResponseResult<Boolean> sendRedPacket(@RequestBody SendLunchRedPacketRequest sendLunchRedPacket){
        return restRedPacketService.sendRedPacket(sendLunchRedPacket);
    }

    /**
     * 查询午餐红包发送记录
     *
     * @param sendRedPacketRequest
     * @return
     */
    @PostMapping("query-sendredpacket-record")
    ResponseResult<Pagenation<SendRedpacketRecordDto>> querySendRedPacketRecord(@RequestBody SendLunchRedPacketRequest sendRedPacketRequest) {
        return restRedPacketService.querySendRedPacketRecord(sendRedPacketRequest);
    }

    /**
     * 查询红包发送流水
     *
     * @param waterRequest
     * @return
     */
    @PostMapping("query-sendredpacket-water")
    ResponseResult<Pagenation<SendRedpacketWaterDto>> querySendRedPacketWater(@RequestBody SendRedPacketWaterRequest waterRequest) {
        return restRedPacketService.querySendRedPacketWater(waterRequest);
    }

    /**
     * 查询用户
     *
     * @param sendLunchRedPacket
     * @return
     */
    @PostMapping("query-user")
    ResponseResult<com.milisong.ucs.dto.Pagenation<UserDto>> queryUser(@RequestBody SendLunchRedPacketRequest sendLunchRedPacket) {
        return restRedPacketService.queryUser(sendLunchRedPacket);
    }

    /**
     * 根据id获取数据
     *
     * @param sendRecordId
     * @return
     */
    @GetMapping("get-sendredpacket-record")
    ResponseResult<SendRedPacketSearchConditionDto> getById(@RequestParam("id") Long sendRecordId) {
        return restRedPacketService.getById(sendRecordId);
    }

}
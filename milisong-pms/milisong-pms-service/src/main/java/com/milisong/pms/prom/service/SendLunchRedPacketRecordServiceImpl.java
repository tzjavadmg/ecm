package com.milisong.pms.prom.service;

import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.google.common.collect.Lists;
import com.milisong.pms.prom.api.LunchRedPacketService;
import com.milisong.pms.prom.api.SendLunchRedPacketRecordService;
import com.milisong.pms.prom.domain.SendRedpacketRecord;
import com.milisong.pms.prom.dto.*;
import com.milisong.pms.prom.enums.RedPacketEnum;
import com.milisong.pms.prom.mapper.RedPacketMapper;
import com.milisong.pms.prom.mapper.SendRedpacketRecordMapper;
import com.milisong.pms.prom.mapper.SendRedpacketWaterMapper;
import com.milisong.ucs.enums.BusinessLineEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

import static com.milisong.pms.prom.enums.PromotionResponseCode.BUSINESS_LINE_IS_EMPTY;
import static com.milisong.pms.prom.enums.PromotionResponseCode.REQUEST_PARAM_EMPTY;

/**
 * @author sailor wang
 * @date 2019/2/20 4:11 PM
 * @description
 */
@Slf4j
@Service
@RestController
public class SendLunchRedPacketRecordServiceImpl implements SendLunchRedPacketRecordService {
    @Autowired
    SendRedpacketRecordMapper sendRedpacketRecordMapper;

    @Autowired
    SendRedpacketWaterMapper sendRedpacketWaterMapper;

    @Autowired
    RedPacketMapper redPacketMapper;

    @Autowired
    LunchRedPacketService lunchRedPacketService;

    /**
     * 查询红包发放条件
     *
     * @param sendRedPacketRecordId
     * @return
     */
    @Override
    public ResponseResult<SendRedPacketSearchConditionDto> getById(@RequestParam("sendRedPacketRecordId") Long sendRedPacketRecordId) {
        SendRedpacketRecord sendRedpacketRecord = sendRedpacketRecordMapper.selectByPrimaryKey(sendRedPacketRecordId);
        SendRedPacketSearchConditionDto searchCondition = new SendRedPacketSearchConditionDto();
        if (sendRedpacketRecord != null){
            searchCondition = JSONObject.parseObject(sendRedpacketRecord.getFilterCondition(),SendRedPacketSearchConditionDto.class);
            searchCondition.setRedPacketList(redPacketMapper.queryByIds(searchCondition.getRedpacketids()));
        }
        return ResponseResult.buildSuccessResponse(searchCondition);
    }

    /**
     * 查询红包发送记录
     *
     * @param sendLunchRedPacket
     * @return
     */
    @Override
    public ResponseResult<Pagenation<SendRedpacketRecordDto>> querySendRedPacketRecord(@RequestBody SendLunchRedPacketRequest sendLunchRedPacket) {
        if (sendLunchRedPacket.getBusinessLine() == null) {
            return ResponseResult.buildFailResponse(BUSINESS_LINE_IS_EMPTY.code(), BUSINESS_LINE_IS_EMPTY.message());
        }
        if (sendLunchRedPacket.getPageNo() < 0) {
            sendLunchRedPacket.setPageNo(1);
        }
        if (sendLunchRedPacket.getPageSize() < 0) {
            sendLunchRedPacket.setPageSize(10);
        }
        if (sendLunchRedPacket.getPageSize() > 500) {
            sendLunchRedPacket.setPageSize(500);
        }

        Pagenation<SendRedpacketRecordDto> pagenation = new Pagenation<>();
        pagenation.setRowCount(0);
        pagenation.setList(Collections.EMPTY_LIST);
        int rowCount = sendRedpacketRecordMapper.countSendRedpacketRecord(sendLunchRedPacket);
        if (rowCount > 0) {
            List<SendRedpacketRecordDto> list = sendRedpacketRecordMapper.querySendRedpacketRecord(sendLunchRedPacket);
            pagenation.setRowCount(rowCount);
            pagenation.setList(list);
        }
        return ResponseResult.buildSuccessResponse(pagenation);
    }

    /**
     * 查看红包发放流水
     *
     * @param waterRequest
     * @return
     */
    @Override
    public ResponseResult<Pagenation<SendRedpacketWaterDto>> querySendRedPacketWater(@RequestBody SendRedPacketWaterRequest waterRequest) {
        if (waterRequest.getSendRedPacketRecordId() == null || waterRequest.getBusinessLine() == null) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        if (waterRequest.getPageNo() < 0) {
            waterRequest.setPageNo(1);
        }
        if (waterRequest.getPageSize() < 0) {
            waterRequest.setPageSize(10);
        }
        if (waterRequest.getPageSize() > 500) {
            waterRequest.setPageSize(500);
        }

        Pagenation<SendRedpacketWaterDto> pagenation = new Pagenation<>();
        pagenation.setRowCount(0);
        pagenation.setList(Lists.newArrayList());

        int rowCount = sendRedpacketWaterMapper.countSendRedPacketWater(waterRequest);
        if (rowCount > 0) {
            List<SendRedpacketWaterDto> list = sendRedpacketWaterMapper.querySendRedPacketWater(waterRequest);
            pagenation.setRowCount(rowCount);
            pagenation.setList(list);
        }
        return ResponseResult.buildSuccessResponse(pagenation);
    }

    @Override
    public ResponseResult<Boolean> scheduleSendRedPacket() {
        Byte buzLine = BusinessLineEnum.LUNCH.getCode();
        Byte status = RedPacketEnum.SendStatus.WAIT_FOR_SEND.getCode();
        List<SendRedpacketRecord> list = sendRedpacketRecordMapper.querySendRedPacketRecordByStatus(status,buzLine);
        if (CollectionUtils.isNotEmpty(list)){
            for (SendRedpacketRecord sendRedpacketRecord : list){
                try {
                    log.info("定时发送红包 sendRedpacketRecord -> {}",sendRedpacketRecord);
                    SendLunchRedPacketRequest redPacketRequest = JSONObject.parseObject(sendRedpacketRecord.getFilterCondition(),SendLunchRedPacketRequest.class);
                    redPacketRequest.setId(sendRedpacketRecord.getId());
                    lunchRedPacketService.sendRedPacket(redPacketRequest);
                } catch (Exception e) {
                    log.error("",e);
                }
            }
        }else {
            log.info("暂无待发送红包数据。。。");
        }
        return ResponseResult.buildSuccessResponse();
    }
}
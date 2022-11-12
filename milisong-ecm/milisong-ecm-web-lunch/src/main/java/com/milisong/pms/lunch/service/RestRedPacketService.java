package com.milisong.pms.lunch.service;

import com.farmland.core.api.ResponseResult;
import com.farmland.core.util.BeanMapper;
import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.milisong.pms.lunch.enums.BusinessLine;
import com.milisong.pms.prom.api.LunchRedPacketService;
import com.milisong.pms.prom.api.SendLunchRedPacketRecordService;
import com.milisong.pms.prom.dto.*;
import com.milisong.ucs.api.UserService;
import com.milisong.ucs.dto.UserDto;
import com.milisong.upms.dto.UserInfoDto;
import com.milisong.upms.utils.UserInfoUtil;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author sailor wang
 * @date 2018/11/7 4:41 PM
 * @description
 */
@Slf4j
@Service
public class RestRedPacketService {

    @Autowired
    LunchRedPacketService lunchRedPacketService;

    @Autowired
    SendLunchRedPacketRecordService sendLunchRedPacketRecordService;

    @Autowired
    UserService userService;


    public ResponseResult<Long> createOrUpdateRedpacket(RedPacketDto redPacketDto) {
        redPacketDto.setBusinessLine(BusinessLine.LUNCH.getCode());
        return lunchRedPacketService.saveOrUpdate(redPacketDto);
    }

    public ResponseResult<Pagenation<RedPacketDto>> redpacketList(RedPacketQueryDto queryDto) {
        queryDto.setBusinessLine(BusinessLine.LUNCH.getCode());
        return lunchRedPacketService.queryRedpacket(queryDto);
    }

    public ResponseResult<RedPacketDto> queryById(Long id) {
        return lunchRedPacketService.queryById(id);
    }

    public ResponseResult<Boolean> offline(Long id) {
        return lunchRedPacketService.offlineRedpacket(id);
    }

    public ResponseResult<Pagenation<SendRedpacketRecordDto>> querySendRedPacketRecord(SendLunchRedPacketRequest sendRedPacketRequest) {
        sendRedPacketRequest.setBusinessLine(BusinessLine.LUNCH.getCode());
        return sendLunchRedPacketRecordService.querySendRedPacketRecord(sendRedPacketRequest);
    }

    public ResponseResult<Pagenation<SendRedpacketWaterDto>> querySendRedPacketWater(SendRedPacketWaterRequest waterRequest) {
        waterRequest.setBusinessLine(BusinessLine.LUNCH.getCode());
        return sendLunchRedPacketRecordService.querySendRedPacketWater(waterRequest);
    }

    public ResponseResult<Boolean> sendRedPacket(SendLunchRedPacketRequest sendLunchRedPacket) {
        UserInfoDto userInfo = UserInfoUtil.getCurrentUserInfo();
        sendLunchRedPacket.setBusinessLine(BusinessLine.LUNCH.getCode());
        sendLunchRedPacket.setOperatorId(userInfo.getUserNo());
        sendLunchRedPacket.setOperatorName(userInfo.getUserName());
        String mobileText = sendLunchRedPacket.getMobileText();
        if (StringUtil.isNotBlank(mobileText)){
            sendLunchRedPacket.setMobiles(Lists.newArrayList(Splitter.on(CharMatcher.breakingWhitespace()).split(mobileText)));
            sendLunchRedPacket.setMobileText(null);
        }
        lunchRedPacketService.sendRedPacket(sendLunchRedPacket);
        return ResponseResult.buildSuccessResponse(Boolean.TRUE);
    }

    public ResponseResult<com.milisong.ucs.dto.Pagenation<UserDto>> queryUser(SendLunchRedPacketRequest sendLunchRedPacket) {
        sendLunchRedPacket.setBusinessLine(BusinessLine.LUNCH.getCode());
        String mobileText = sendLunchRedPacket.getMobileText();
        if (StringUtil.isNotBlank(mobileText)){
            sendLunchRedPacket.setMobiles(Lists.newArrayList(Splitter.on(CharMatcher.breakingWhitespace()).split(mobileText)));
            sendLunchRedPacket.setMobileText(null);
        }

        if (Boolean.TRUE.equals(sendLunchRedPacket.getSendAllUser())){
            ResponseResult<com.milisong.ucs.dto.Pagenation<UserDto>> responseResult =
                    userService.queryUser(BeanMapper.map(sendLunchRedPacket, com.milisong.ucs.dto.SendLunchRedPacketRequest.class));
            return responseResult;
        }else if (CollectionUtils.isNotEmpty(sendLunchRedPacket.getMobiles())){
            UserDto userDto = new UserDto();
            userDto.setBusinessLine(BusinessLine.LUNCH.getCode());
            userDto.setMobileNos(sendLunchRedPacket.getMobiles());
            ResponseResult<List<UserDto>> result = userService.fetchUserInfoByMobiles(userDto);

            com.milisong.ucs.dto.Pagenation<UserDto> pagenation = new com.milisong.ucs.dto.Pagenation();
            pagenation.setList(result.getData());
            pagenation.setRowCount(CollectionUtils.isEmpty(result.getData())?0:result.getData().size());
            return ResponseResult.buildSuccessResponse(pagenation);
        }
        return ResponseResult.buildSuccessResponse();
    }

    public ResponseResult<SendRedPacketSearchConditionDto> getById(Long sendRecordId) {
        return sendLunchRedPacketRecordService.getById(sendRecordId);
    }

}
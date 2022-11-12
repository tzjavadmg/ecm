package com.milisong.pms.prom.mapper;

import com.milisong.pms.prom.domain.SendRedpacketWater;
import com.milisong.pms.prom.dto.SendRedPacketWaterRequest;
import com.milisong.pms.prom.dto.SendRedpacketWaterDto;

import java.util.List;

public interface SendRedpacketWaterMapper {
    int insert(SendRedpacketWater record);

    int insertSelective(SendRedpacketWater record);

    SendRedpacketWater selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SendRedpacketWater record);

    int updateByPrimaryKey(SendRedpacketWater record);

    int countSendRedPacketWater(SendRedPacketWaterRequest waterRequest);

    List<SendRedpacketWaterDto> querySendRedPacketWater(SendRedPacketWaterRequest waterRequest);

    int insertBatch(List<SendRedpacketWater> sendRedPacketWaters);
}
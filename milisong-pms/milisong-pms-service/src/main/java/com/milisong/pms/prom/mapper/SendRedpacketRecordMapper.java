package com.milisong.pms.prom.mapper;

import com.milisong.pms.prom.domain.SendRedpacketRecord;
import com.milisong.pms.prom.dto.SendLunchRedPacketRequest;
import com.milisong.pms.prom.dto.SendRedpacketRecordDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SendRedpacketRecordMapper {
    int insert(SendRedpacketRecord record);

    int insertSelective(SendRedpacketRecord record);

    SendRedpacketRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SendRedpacketRecord record);

    int updateByPrimaryKey(SendRedpacketRecord record);

    int countSendRedpacketRecord(SendLunchRedPacketRequest sendLunchRedPacket);

    List<SendRedpacketRecordDto> querySendRedpacketRecord(SendLunchRedPacketRequest sendLunchRedPacket);

    List<SendRedpacketRecord> querySendRedPacketRecordByStatus(@Param("status") Byte status,@Param("buzLine") Byte buzLine);
}
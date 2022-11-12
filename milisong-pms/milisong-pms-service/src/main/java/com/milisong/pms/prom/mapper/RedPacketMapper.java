package com.milisong.pms.prom.mapper;

import com.milisong.pms.prom.domain.RedPacket;
import com.milisong.pms.prom.dto.RedPacketDto;
import com.milisong.pms.prom.dto.RedPacketQueryDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RedPacketMapper {
    int insert(RedPacket record);

    int insertSelective(RedPacket record);

    RedPacket selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RedPacket record);

    int updateByPrimaryKey(RedPacket record);

    int countRedpacket(RedPacketQueryDto dto);

    List<RedPacketDto> queryRedpacket(RedPacketQueryDto dto);

    List<RedPacketDto> queryByIds(@Param("redpacketids") List<Long> redpacketids);
}
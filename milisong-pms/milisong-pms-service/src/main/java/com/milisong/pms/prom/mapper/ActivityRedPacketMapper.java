package com.milisong.pms.prom.mapper;

import com.milisong.pms.prom.domain.ActivityRedPacket;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ActivityRedPacketMapper {
    int insert(ActivityRedPacket record);

    int insertSelective(ActivityRedPacket record);

    ActivityRedPacket selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ActivityRedPacket record);

    int updateByPrimaryKey(ActivityRedPacket record);

    int updateByActivityId(ActivityRedPacket actRedPacket);
}
package com.milisong.pms.prom.mapper;

import com.milisong.pms.prom.domain.ActivityTimeSlot;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ActivityTimeSlotMapper {
    int insert(ActivityTimeSlot record);

    int insertSelective(ActivityTimeSlot record);

    ActivityTimeSlot selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ActivityTimeSlot record);

    int updateByPrimaryKey(ActivityTimeSlot record);
}
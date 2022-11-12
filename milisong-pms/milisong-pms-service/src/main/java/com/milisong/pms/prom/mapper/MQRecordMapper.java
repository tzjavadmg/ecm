package com.milisong.pms.prom.mapper;

import com.milisong.pms.prom.domain.MQRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MQRecordMapper {

    int insert(MQRecord record);

    int insertSelective(MQRecord record);

    MQRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MQRecord record);

    int updateByPrimaryKeyWithBLOBs(MQRecord record);

    int updateByPrimaryKey(MQRecord record);
}
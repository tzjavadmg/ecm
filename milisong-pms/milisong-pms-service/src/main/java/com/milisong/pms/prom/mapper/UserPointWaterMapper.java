package com.milisong.pms.prom.mapper;

import com.milisong.pms.prom.domain.UserPointWater;
import com.milisong.pms.prom.dto.UserPointWaterDto;
import com.milisong.pms.prom.dto.UserPointWaterQueryDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface UserPointWaterMapper {
    int insert(UserPointWater record);

    int insertSelective(UserPointWater record);

    UserPointWater selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserPointWater record);

    int updateByPrimaryKey(UserPointWater record);

    Integer queryPointWaterCount(UserPointWaterQueryDto reqDto);

    List<UserPointWater> queryPointWater(UserPointWaterQueryDto reqDto);

    UserPointWater queryForPowerHashBeforeSave(UserPointWaterDto record);
}
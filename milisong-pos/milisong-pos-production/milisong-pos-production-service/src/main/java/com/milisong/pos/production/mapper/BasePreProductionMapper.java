package com.milisong.pos.production.mapper;

import com.milisong.pos.production.domain.PreProduction;
import com.milisong.pos.production.domain.PreProductionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BasePreProductionMapper {
    long countByExample(PreProductionExample example);

    int deleteByPrimaryKey(Long id);

    int insert(PreProduction record);

    int insertSelective(PreProduction record);

    List<PreProduction> selectByExample(PreProductionExample example);

    PreProduction selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") PreProduction record, @Param("example") PreProductionExample example);

    int updateByExample(@Param("record") PreProduction record, @Param("example") PreProductionExample example);

    int updateByPrimaryKeySelective(PreProduction record);

    int updateByPrimaryKey(PreProduction record);
}
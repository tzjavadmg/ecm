package com.milisong.scm.shop.mapper;

import com.milisong.scm.shop.domain.BuildingApply;
import com.milisong.scm.shop.domain.BuildingApplyExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BuildingApplyMapper {
    long countByExample(BuildingApplyExample example);

    int deleteByExample(BuildingApplyExample example);

    int deleteByPrimaryKey(Long id);

    int insert(BuildingApply record);

    int insertSelective(BuildingApply record);

    List<BuildingApply> selectByExample(BuildingApplyExample example);

    BuildingApply selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") BuildingApply record, @Param("example") BuildingApplyExample example);

    int updateByExample(@Param("record") BuildingApply record, @Param("example") BuildingApplyExample example);

    int updateByPrimaryKeySelective(BuildingApply record);

    int updateByPrimaryKey(BuildingApply record);
}
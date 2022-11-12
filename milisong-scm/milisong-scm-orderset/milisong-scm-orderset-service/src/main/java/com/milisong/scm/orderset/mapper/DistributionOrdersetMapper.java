package com.milisong.scm.orderset.mapper;

import com.milisong.scm.orderset.domain.DistributionOrderset;
import com.milisong.scm.orderset.domain.DistributionOrdersetExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DistributionOrdersetMapper {
    long countByExample(DistributionOrdersetExample example);

    int deleteByExample(DistributionOrdersetExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DistributionOrderset record);

    int insertSelective(DistributionOrderset record);

    List<DistributionOrderset> selectByExample(DistributionOrdersetExample example);

    DistributionOrderset selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") DistributionOrderset record, @Param("example") DistributionOrdersetExample example);

    int updateByExample(@Param("record") DistributionOrderset record, @Param("example") DistributionOrdersetExample example);

    int updateByPrimaryKeySelective(DistributionOrderset record);

    int updateByPrimaryKey(DistributionOrderset record);
}
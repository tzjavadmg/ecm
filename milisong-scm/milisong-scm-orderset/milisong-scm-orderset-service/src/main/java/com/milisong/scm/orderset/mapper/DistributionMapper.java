package com.milisong.scm.orderset.mapper;

import com.milisong.scm.orderset.domain.Distribution;
import com.milisong.scm.orderset.domain.DistributionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DistributionMapper {
    long countByExample(DistributionExample example);

    int deleteByExample(DistributionExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Distribution record);

    int insertSelective(Distribution record);

    List<Distribution> selectByExample(DistributionExample example);

    Distribution selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Distribution record, @Param("example") DistributionExample example);

    int updateByExample(@Param("record") Distribution record, @Param("example") DistributionExample example);

    int updateByPrimaryKeySelective(Distribution record);

    int updateByPrimaryKey(Distribution record);
}
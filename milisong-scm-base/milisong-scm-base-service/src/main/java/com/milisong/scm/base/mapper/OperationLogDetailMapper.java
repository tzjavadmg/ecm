package com.milisong.scm.base.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.milisong.scm.base.domain.OperationLogDetail;
import com.milisong.scm.base.domain.OperationLogDetailExample;

public interface OperationLogDetailMapper {
    long countByExample(OperationLogDetailExample example);

    int deleteByExample(OperationLogDetailExample example);

    int deleteByPrimaryKey(Long id);

    int insert(OperationLogDetail record);

    int insertSelective(OperationLogDetail record);

    List<OperationLogDetail> selectByExample(OperationLogDetailExample example);

    OperationLogDetail selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") OperationLogDetail record, @Param("example") OperationLogDetailExample example);

    int updateByExample(@Param("record") OperationLogDetail record, @Param("example") OperationLogDetailExample example);

    int updateByPrimaryKeySelective(OperationLogDetail record);

    int updateByPrimaryKey(OperationLogDetail record);
}
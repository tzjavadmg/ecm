package com.milisong.scm.base.mapper;

import com.milisong.scm.base.domain.OperationLog;
import com.milisong.scm.base.domain.OperationLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OperationLogMapper {
    /**
     *
     * @mbg.generated 2018-12-25
     */
    long countByExample(OperationLogExample example);

    /**
     *
     * @mbg.generated 2018-12-25
     */
    int deleteByExample(OperationLogExample example);

    /**
     *
     * @mbg.generated 2018-12-25
     */
    int deleteByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2018-12-25
     */
    int insert(OperationLog record);

    /**
     *
     * @mbg.generated 2018-12-25
     */
    int insertSelective(OperationLog record);

    /**
     *
     * @mbg.generated 2018-12-25
     */
    List<OperationLog> selectByExampleWithBLOBs(OperationLogExample example);

    /**
     *
     * @mbg.generated 2018-12-25
     */
    List<OperationLog> selectByExample(OperationLogExample example);

    /**
     *
     * @mbg.generated 2018-12-25
     */
    OperationLog selectByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2018-12-25
     */
    int updateByExampleSelective(@Param("record") OperationLog record, @Param("example") OperationLogExample example);

    /**
     *
     * @mbg.generated 2018-12-25
     */
    int updateByExampleWithBLOBs(@Param("record") OperationLog record, @Param("example") OperationLogExample example);

    /**
     *
     * @mbg.generated 2018-12-25
     */
    int updateByExample(@Param("record") OperationLog record, @Param("example") OperationLogExample example);

    /**
     *
     * @mbg.generated 2018-12-25
     */
    int updateByPrimaryKeySelective(OperationLog record);

    /**
     *
     * @mbg.generated 2018-12-25
     */
    int updateByPrimaryKeyWithBLOBs(OperationLog record);

    /**
     *
     * @mbg.generated 2018-12-25
     */
    int updateByPrimaryKey(OperationLog record);
}
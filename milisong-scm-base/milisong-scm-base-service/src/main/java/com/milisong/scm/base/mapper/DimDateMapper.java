package com.milisong.scm.base.mapper;

import com.milisong.scm.base.domain.DimDate;
import com.milisong.scm.base.domain.DimDateExample;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DimDateMapper {
    /**
     *
     * @mbg.generated 2018-12-25
     */
    long countByExample(DimDateExample example);

    /**
     *
     * @mbg.generated 2018-12-25
     */
    int deleteByExample(DimDateExample example);

    /**
     *
     * @mbg.generated 2018-12-25
     */
    int deleteByPrimaryKey(Date date);

    /**
     *
     * @mbg.generated 2018-12-25
     */
    int insert(DimDate record);

    /**
     *
     * @mbg.generated 2018-12-25
     */
    int insertSelective(DimDate record);

    /**
     *
     * @mbg.generated 2018-12-25
     */
    List<DimDate> selectByExample(DimDateExample example);

    /**
     *
     * @mbg.generated 2018-12-25
     */
    DimDate selectByPrimaryKey(Date date);

    /**
     *
     * @mbg.generated 2018-12-25
     */
    int updateByExampleSelective(@Param("record") DimDate record, @Param("example") DimDateExample example);

    /**
     *
     * @mbg.generated 2018-12-25
     */
    int updateByExample(@Param("record") DimDate record, @Param("example") DimDateExample example);

    /**
     *
     * @mbg.generated 2018-12-25
     */
    int updateByPrimaryKeySelective(DimDate record);

    /**
     *
     * @mbg.generated 2018-12-25
     */
    int updateByPrimaryKey(DimDate record);
}
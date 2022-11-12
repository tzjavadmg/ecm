package com.milisong.scm.base.mapper;

import com.milisong.scm.base.domain.Area;
import com.milisong.scm.base.domain.AreaExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AreaMapper {
    /**
     *
     * @mbg.generated 2018-12-25
     */
    long countByExample(AreaExample example);

    /**
     *
     * @mbg.generated 2018-12-25
     */
    int deleteByExample(AreaExample example);

    /**
     *
     * @mbg.generated 2018-12-25
     */
    int deleteByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2018-12-25
     */
    int insert(Area record);

    /**
     *
     * @mbg.generated 2018-12-25
     */
    int insertSelective(Area record);

    /**
     *
     * @mbg.generated 2018-12-25
     */
    List<Area> selectByExample(AreaExample example);

    /**
     *
     * @mbg.generated 2018-12-25
     */
    Area selectByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2018-12-25
     */
    int updateByExampleSelective(@Param("record") Area record, @Param("example") AreaExample example);

    /**
     *
     * @mbg.generated 2018-12-25
     */
    int updateByExample(@Param("record") Area record, @Param("example") AreaExample example);

    /**
     *
     * @mbg.generated 2018-12-25
     */
    int updateByPrimaryKeySelective(Area record);

    /**
     *
     * @mbg.generated 2018-12-25
     */
    int updateByPrimaryKey(Area record);
}
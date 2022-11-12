package com.milisong.scm.base.mapper;

import com.milisong.scm.base.domain.CompanyMealTime;
import com.milisong.scm.base.domain.CompanyMealTimeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CompanyMealTimeMapper {
    /**
     *
     * @mbg.generated 2019-02-20
     */
    long countByExample(CompanyMealTimeExample example);

    /**
     *
     * @mbg.generated 2019-02-20
     */
    int deleteByExample(CompanyMealTimeExample example);

    /**
     *
     * @mbg.generated 2019-02-20
     */
    int deleteByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2019-02-20
     */
    int insert(CompanyMealTime record);

    /**
     *
     * @mbg.generated 2019-02-20
     */
    int insertSelective(CompanyMealTime record);

    /**
     *
     * @mbg.generated 2019-02-20
     */
    List<CompanyMealTime> selectByExample(CompanyMealTimeExample example);

    /**
     *
     * @mbg.generated 2019-02-20
     */
    CompanyMealTime selectByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2019-02-20
     */
    int updateByExampleSelective(@Param("record") CompanyMealTime record, @Param("example") CompanyMealTimeExample example);

    /**
     *
     * @mbg.generated 2019-02-20
     */
    int updateByExample(@Param("record") CompanyMealTime record, @Param("example") CompanyMealTimeExample example);

    /**
     *
     * @mbg.generated 2019-02-20
     */
    int updateByPrimaryKeySelective(CompanyMealTime record);

    /**
     *
     * @mbg.generated 2019-02-20
     */
    int updateByPrimaryKey(CompanyMealTime record);
}
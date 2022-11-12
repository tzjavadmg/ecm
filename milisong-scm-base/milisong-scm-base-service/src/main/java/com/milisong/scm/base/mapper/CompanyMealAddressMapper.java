package com.milisong.scm.base.mapper;

import com.milisong.scm.base.domain.CompanyMealAddress;
import com.milisong.scm.base.domain.CompanyMealAddressExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CompanyMealAddressMapper {
    /**
     * @mbg.generated 2018-12-03
     */
    long countByExample(CompanyMealAddressExample example);

    /**
     * @mbg.generated 2018-12-03
     */
    int deleteByExample(CompanyMealAddressExample example);

    /**
     * @mbg.generated 2018-12-03
     */
    int deleteByPrimaryKey(Long id);

    /**
     * @mbg.generated 2018-12-03
     */
    int insert(CompanyMealAddress record);

    /**
     * @mbg.generated 2018-12-03
     */
    int insertSelective(CompanyMealAddress record);

    /**
     * @mbg.generated 2018-12-03
     */
    List<CompanyMealAddress> selectByExample(CompanyMealAddressExample example);

    /**
     * @mbg.generated 2018-12-03
     */
    CompanyMealAddress selectByPrimaryKey(Long id);

    /**
     * @mbg.generated 2018-12-03
     */
    int updateByExampleSelective(@Param("record") CompanyMealAddress record, @Param("example") CompanyMealAddressExample example);

    /**
     * @mbg.generated 2018-12-03
     */
    int updateByExample(@Param("record") CompanyMealAddress record, @Param("example") CompanyMealAddressExample example);

    /**
     * @mbg.generated 2018-12-03
     */
    int updateByPrimaryKeySelective(CompanyMealAddress record);

    /**
     * @mbg.generated 2018-12-03
     */
    int updateByPrimaryKey(CompanyMealAddress record);
}
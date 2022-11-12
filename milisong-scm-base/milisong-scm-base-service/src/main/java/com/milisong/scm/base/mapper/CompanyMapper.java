package com.milisong.scm.base.mapper;

import com.milisong.scm.base.domain.Company;
import com.milisong.scm.base.domain.CompanyExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CompanyMapper {
    /**
     * @mbg.generated 2018-12-03
     */
    long countByExample(CompanyExample example);

    /**
     * @mbg.generated 2018-12-03
     */
    int deleteByExample(CompanyExample example);

    /**
     * @mbg.generated 2018-12-03
     */
    int deleteByPrimaryKey(Long id);

    /**
     * @mbg.generated 2018-12-03
     */
    int insert(Company record);

    /**
     * @mbg.generated 2018-12-03
     */
    int insertSelective(Company record);

    /**
     * @mbg.generated 2018-12-03
     */
    List<Company> selectByExample(CompanyExample example);

    /**
     * @mbg.generated 2018-12-03
     */
    Company selectByPrimaryKey(Long id);

    /**
     * @mbg.generated 2018-12-03
     */
    int updateByExampleSelective(@Param("record") Company record, @Param("example") CompanyExample example);

    /**
     * @mbg.generated 2018-12-03
     */
    int updateByExample(@Param("record") Company record, @Param("example") CompanyExample example);

    /**
     * @mbg.generated 2018-12-03
     */
    int updateByPrimaryKeySelective(Company record);

    /**
     * @mbg.generated 2018-12-03
     */
    int updateByPrimaryKey(Company record);
}
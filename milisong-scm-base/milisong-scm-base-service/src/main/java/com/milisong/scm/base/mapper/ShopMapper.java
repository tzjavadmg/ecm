package com.milisong.scm.base.mapper;

import com.milisong.scm.base.domain.Shop;
import com.milisong.scm.base.domain.ShopExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ShopMapper {
    /**
     *
     * @mbg.generated 2018-12-25
     */
    long countByExample(ShopExample example);

    /**
     *
     * @mbg.generated 2018-12-25
     */
    int deleteByExample(ShopExample example);

    /**
     *
     * @mbg.generated 2018-12-25
     */
    int deleteByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2018-12-25
     */
    int insert(Shop record);

    /**
     *
     * @mbg.generated 2018-12-25
     */
    int insertSelective(Shop record);

    /**
     *
     * @mbg.generated 2018-12-25
     */
    List<Shop> selectByExample(ShopExample example);

    /**
     *
     * @mbg.generated 2018-12-25
     */
    Shop selectByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2018-12-25
     */
    int updateByExampleSelective(@Param("record") Shop record, @Param("example") ShopExample example);

    /**
     *
     * @mbg.generated 2018-12-25
     */
    int updateByExample(@Param("record") Shop record, @Param("example") ShopExample example);

    /**
     *
     * @mbg.generated 2018-12-25
     */
    int updateByPrimaryKeySelective(Shop record);

    /**
     *
     * @mbg.generated 2018-12-25
     */
    int updateByPrimaryKey(Shop record);
}
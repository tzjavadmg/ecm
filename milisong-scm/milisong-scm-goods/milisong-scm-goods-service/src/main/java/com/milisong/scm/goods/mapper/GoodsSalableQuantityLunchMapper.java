package com.milisong.scm.goods.mapper;

import com.milisong.scm.goods.domain.GoodsSalableQuantityLunch;
import com.milisong.scm.goods.domain.GoodsSalableQuantityLunchExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GoodsSalableQuantityLunchMapper {
    /**
     *
     * @mbg.generated 2018-12-18
     */
    long countByExample(GoodsSalableQuantityLunchExample example);

    /**
     *
     * @mbg.generated 2018-12-18
     */
    int deleteByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2018-12-18
     */
    int insert(GoodsSalableQuantityLunch record);

    /**
     *
     * @mbg.generated 2018-12-18
     */
    int insertSelective(GoodsSalableQuantityLunch record);

    /**
     *
     * @mbg.generated 2018-12-18
     */
    List<GoodsSalableQuantityLunch> selectByExample(GoodsSalableQuantityLunchExample example);

    /**
     *
     * @mbg.generated 2018-12-18
     */
    GoodsSalableQuantityLunch selectByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2018-12-18
     */
    int updateByExampleSelective(@Param("record") GoodsSalableQuantityLunch record, @Param("example") GoodsSalableQuantityLunchExample example);

    /**
     *
     * @mbg.generated 2018-12-18
     */
    int updateByExample(@Param("record") GoodsSalableQuantityLunch record, @Param("example") GoodsSalableQuantityLunchExample example);

    /**
     *
     * @mbg.generated 2018-12-18
     */
    int updateByPrimaryKeySelective(GoodsSalableQuantityLunch record);

    /**
     *
     * @mbg.generated 2018-12-18
     */
    int updateByPrimaryKey(GoodsSalableQuantityLunch record);

	int insertBatchSelective(List<GoodsSalableQuantityLunch> listParam);
}
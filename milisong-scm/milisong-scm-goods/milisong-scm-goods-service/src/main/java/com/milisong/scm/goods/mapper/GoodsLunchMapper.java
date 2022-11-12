package com.milisong.scm.goods.mapper;

import com.milisong.scm.goods.domain.GoodsLunch;
import com.milisong.scm.goods.domain.GoodsLunchExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GoodsLunchMapper {
    /**
     *
     * @mbg.generated 2018-12-19
     */
    long countByExample(GoodsLunchExample example);

    /**
     *
     * @mbg.generated 2018-12-19
     */
    int deleteByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2018-12-19
     */
    int insert(GoodsLunch record);

    /**
     *
     * @mbg.generated 2018-12-19
     */
    int insertSelective(GoodsLunch record);

    /**
     *
     * @mbg.generated 2018-12-19
     */
    List<GoodsLunch> selectByExample(GoodsLunchExample example);

    /**
     *
     * @mbg.generated 2018-12-19
     */
    GoodsLunch selectByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2018-12-19
     */
    int updateByExampleSelective(@Param("record") GoodsLunch record, @Param("example") GoodsLunchExample example);

    /**
     *
     * @mbg.generated 2018-12-19
     */
    int updateByExample(@Param("record") GoodsLunch record, @Param("example") GoodsLunchExample example);

    /**
     *
     * @mbg.generated 2018-12-19
     */
    int updateByPrimaryKeySelective(GoodsLunch record);

    /**
     *
     * @mbg.generated 2018-12-19
     */
    int updateByPrimaryKey(GoodsLunch record);
}
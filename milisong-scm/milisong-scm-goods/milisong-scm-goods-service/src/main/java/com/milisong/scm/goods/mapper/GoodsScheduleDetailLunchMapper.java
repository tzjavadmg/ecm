package com.milisong.scm.goods.mapper;

import com.milisong.scm.goods.domain.GoodsScheduleDetailLunch;
import com.milisong.scm.goods.domain.GoodsScheduleDetailLunchExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GoodsScheduleDetailLunchMapper {
    /**
     *
     * @mbg.generated 2018-12-18
     */
    long countByExample(GoodsScheduleDetailLunchExample example);

    /**
     *
     * @mbg.generated 2018-12-18
     */
    int deleteByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2018-12-18
     */
    int insert(GoodsScheduleDetailLunch record);

    /**
     *
     * @mbg.generated 2018-12-18
     */
    int insertSelective(GoodsScheduleDetailLunch record);

    /**
     *
     * @mbg.generated 2018-12-18
     */
    List<GoodsScheduleDetailLunch> selectByExample(GoodsScheduleDetailLunchExample example);

    /**
     *
     * @mbg.generated 2018-12-18
     */
    GoodsScheduleDetailLunch selectByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2018-12-18
     */
    int updateByExampleSelective(@Param("record") GoodsScheduleDetailLunch record, @Param("example") GoodsScheduleDetailLunchExample example);

    /**
     *
     * @mbg.generated 2018-12-18
     */
    int updateByExample(@Param("record") GoodsScheduleDetailLunch record, @Param("example") GoodsScheduleDetailLunchExample example);

    /**
     *
     * @mbg.generated 2018-12-18
     */
    int updateByPrimaryKeySelective(GoodsScheduleDetailLunch record);

    /**
     *
     * @mbg.generated 2018-12-18
     */
    int updateByPrimaryKey(GoodsScheduleDetailLunch record);
}
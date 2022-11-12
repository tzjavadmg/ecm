package com.milisong.breakfast.scm.goods.mapper;

import com.milisong.breakfast.scm.goods.domain.GoodsSchedule;
import com.milisong.breakfast.scm.goods.domain.GoodsScheduleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GoodsScheduleMapper {
    /**
     *
     * @mbg.generated 2018-12-03
     */
    long countByExample(GoodsScheduleExample example);

    /**
     *
     * @mbg.generated 2018-12-03
     */
    int deleteByExample(GoodsScheduleExample example);

    /**
     *
     * @mbg.generated 2018-12-03
     */
    int deleteByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2018-12-03
     */
    int insert(GoodsSchedule record);

    /**
     *
     * @mbg.generated 2018-12-03
     */
    int insertSelective(GoodsSchedule record);

    /**
     *
     * @mbg.generated 2018-12-03
     */
    List<GoodsSchedule> selectByExample(GoodsScheduleExample example);

    /**
     *
     * @mbg.generated 2018-12-03
     */
    GoodsSchedule selectByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2018-12-03
     */
    int updateByExampleSelective(@Param("record") GoodsSchedule record, @Param("example") GoodsScheduleExample example);

    /**
     *
     * @mbg.generated 2018-12-03
     */
    int updateByExample(@Param("record") GoodsSchedule record, @Param("example") GoodsScheduleExample example);

    /**
     *
     * @mbg.generated 2018-12-03
     */
    int updateByPrimaryKeySelective(GoodsSchedule record);

    /**
     *
     * @mbg.generated 2018-12-03
     */
    int updateByPrimaryKey(GoodsSchedule record);

    /**
     * 批量新增
     * @param listSchedule
     */
    int insertBatchSelective(List<GoodsSchedule> listSchedule);
}
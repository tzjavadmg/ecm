package com.milisong.breakfast.scm.goods.mapper;

import com.milisong.breakfast.scm.goods.domain.GoodsScheduleDetail;
import com.milisong.breakfast.scm.goods.domain.GoodsScheduleDetailExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GoodsScheduleDetailMapper {
    /**
     *
     * @mbg.generated 2018-12-03
     */
    long countByExample(GoodsScheduleDetailExample example);

    /**
     *
     * @mbg.generated 2018-12-03
     */
    int deleteByExample(GoodsScheduleDetailExample example);

    /**
     *
     * @mbg.generated 2018-12-03
     */
    int deleteByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2018-12-03
     */
    int insert(GoodsScheduleDetail record);

    /**
     *
     * @mbg.generated 2018-12-03
     */
    int insertSelective(GoodsScheduleDetail record);

    /**
     *
     * @mbg.generated 2018-12-03
     */
    List<GoodsScheduleDetail> selectByExample(GoodsScheduleDetailExample example);

    /**
     *
     * @mbg.generated 2018-12-03
     */
    GoodsScheduleDetail selectByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2018-12-03
     */
    int updateByExampleSelective(@Param("record") GoodsScheduleDetail record, @Param("example") GoodsScheduleDetailExample example);

    /**
     *
     * @mbg.generated 2018-12-03
     */
    int updateByExample(@Param("record") GoodsScheduleDetail record, @Param("example") GoodsScheduleDetailExample example);

    /**
     *
     * @mbg.generated 2018-12-03
     */
    int updateByPrimaryKeySelective(GoodsScheduleDetail record);

    /**
     *
     * @mbg.generated 2018-12-03
     */
    int updateByPrimaryKey(GoodsScheduleDetail record);
}
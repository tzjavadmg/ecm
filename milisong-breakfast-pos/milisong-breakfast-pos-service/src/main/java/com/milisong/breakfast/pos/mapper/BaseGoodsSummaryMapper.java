package com.milisong.breakfast.pos.mapper;

import com.milisong.breakfast.pos.domain.GoodsSummary;
import com.milisong.breakfast.pos.domain.GoodsSummaryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BaseGoodsSummaryMapper {
    /**
     *
     * @mbg.generated 2018-12-06
     */
    long countByExample(GoodsSummaryExample example);

    /**
     *
     * @mbg.generated 2018-12-06
     */
    int deleteByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2018-12-06
     */
    int insert(GoodsSummary record);

    /**
     *
     * @mbg.generated 2018-12-06
     */
    int insertSelective(GoodsSummary record);

    /**
     *
     * @mbg.generated 2018-12-06
     */
    List<GoodsSummary> selectByExample(GoodsSummaryExample example);

    /**
     *
     * @mbg.generated 2018-12-06
     */
    GoodsSummary selectByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2018-12-06
     */
    int updateByExampleSelective(@Param("record") GoodsSummary record, @Param("example") GoodsSummaryExample example);

    /**
     *
     * @mbg.generated 2018-12-06
     */
    int updateByExample(@Param("record") GoodsSummary record, @Param("example") GoodsSummaryExample example);

    /**
     *
     * @mbg.generated 2018-12-06
     */
    int updateByPrimaryKeySelective(GoodsSummary record);

    /**
     *
     * @mbg.generated 2018-12-06
     */
    int updateByPrimaryKey(GoodsSummary record);
}
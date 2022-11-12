package com.mili.oss.mapper.base;

import com.mili.oss.domain.OrderSet;
import com.mili.oss.domain.OrderSetExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BaseOrderSetMapper {
    /**
     *
     * @mbg.generated 2019-03-12
     */
    long countByExample(OrderSetExample example);

    /**
     *
     * @mbg.generated 2019-03-12
     */
    int deleteByExample(OrderSetExample example);

    /**
     *
     * @mbg.generated 2019-03-12
     */
    int insert(OrderSet record);

    /**
     *
     * @mbg.generated 2019-03-12
     */
    int insertSelective(OrderSet record);

    /**
     *
     * @mbg.generated 2019-03-12
     */
    List<OrderSet> selectByExample(OrderSetExample example);

    /**
     *
     * @mbg.generated 2019-03-12
     */
    OrderSet selectByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2019-03-12
     */
    int updateByExampleSelective(@Param("record") OrderSet record, @Param("example") OrderSetExample example);

    /**
     *
     * @mbg.generated 2019-03-12
     */
    int updateByExample(@Param("record") OrderSet record, @Param("example") OrderSetExample example);

    /**
     *
     * @mbg.generated 2019-03-12
     */
    int updateByPrimaryKeySelective(OrderSet record);

    /**
     *
     * @mbg.generated 2019-03-12
     */
    int updateByPrimaryKey(OrderSet record);
}
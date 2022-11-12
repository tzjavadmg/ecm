package com.mili.oss.mapper.base;

import com.mili.oss.domain.OrderToOrderSet;
import com.mili.oss.domain.OrderToOrderSetExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BaseOrderToOrderSetMapper {
    /**
     *
     * @mbg.generated 2019-02-26
     */
    long countByExample(OrderToOrderSetExample example);

    /**
     *
     * @mbg.generated 2019-02-26
     */
    int deleteByExample(OrderToOrderSetExample example);

    /**
     *
     * @mbg.generated 2019-02-26
     */
    int insert(OrderToOrderSet record);

    /**
     *
     * @mbg.generated 2019-02-26
     */
    int insertSelective(OrderToOrderSet record);

    /**
     *
     * @mbg.generated 2019-02-26
     */
    List<OrderToOrderSet> selectByExample(OrderToOrderSetExample example);

    /**
     *
     * @mbg.generated 2019-02-26
     */
    OrderToOrderSet selectByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2019-02-26
     */
    int updateByExampleSelective(@Param("record") OrderToOrderSet record, @Param("example") OrderToOrderSetExample example);

    /**
     *
     * @mbg.generated 2019-02-26
     */
    int updateByExample(@Param("record") OrderToOrderSet record, @Param("example") OrderToOrderSetExample example);

    /**
     *
     * @mbg.generated 2019-02-26
     */
    int updateByPrimaryKeySelective(OrderToOrderSet record);

    /**
     *
     * @mbg.generated 2019-02-26
     */
    int updateByPrimaryKey(OrderToOrderSet record);
}
package com.mili.oss.mapper.base;

import com.mili.oss.domain.Order;
import com.mili.oss.domain.OrderExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BaseOrderMapper {
    /**
     *
     * @mbg.generated 2019-03-23
     */
    long countByExample(OrderExample example);

    /**
     *
     * @mbg.generated 2019-03-23
     */
    int deleteByExample(OrderExample example);

    /**
     *
     * @mbg.generated 2019-03-23
     */
    int insert(Order record);

    /**
     *
     * @mbg.generated 2019-03-23
     */
    int insertSelective(Order record);

    /**
     *
     * @mbg.generated 2019-03-23
     */
    List<Order> selectByExample(OrderExample example);

    /**
     *
     * @mbg.generated 2019-03-23
     */
    Order selectByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2019-03-23
     */
    int updateByExampleSelective(@Param("record") Order record, @Param("example") OrderExample example);

    /**
     *
     * @mbg.generated 2019-03-23
     */
    int updateByExample(@Param("record") Order record, @Param("example") OrderExample example);

    /**
     *
     * @mbg.generated 2019-03-23
     */
    int updateByPrimaryKeySelective(Order record);

    /**
     *
     * @mbg.generated 2019-03-23
     */
    int updateByPrimaryKey(Order record);
}
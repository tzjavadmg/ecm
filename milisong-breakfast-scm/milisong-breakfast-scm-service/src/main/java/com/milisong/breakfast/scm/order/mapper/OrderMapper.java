package com.milisong.breakfast.scm.order.mapper;

import com.milisong.breakfast.scm.order.domain.Order;
import com.milisong.breakfast.scm.order.domain.OrderExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OrderMapper {
    /**
     *
     * @mbg.generated 2018-12-12
     */
    long countByExample(OrderExample example);

    /**
     *
     * @mbg.generated 2018-12-12
     */
    int deleteByExample(OrderExample example);

    /**
     *
     * @mbg.generated 2018-12-12
     */
    int deleteByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2018-12-12
     */
    int insert(Order record);

    /**
     *
     * @mbg.generated 2018-12-12
     */
    int insertSelective(Order record);

    /**
     *
     * @mbg.generated 2018-12-12
     */
    List<Order> selectByExample(OrderExample example);

    /**
     *
     * @mbg.generated 2018-12-12
     */
    Order selectByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2018-12-12
     */
    int updateByExampleSelective(@Param("record") Order record, @Param("example") OrderExample example);

    /**
     *
     * @mbg.generated 2018-12-12
     */
    int updateByExample(@Param("record") Order record, @Param("example") OrderExample example);

    /**
     *
     * @mbg.generated 2018-12-12
     */
    int updateByPrimaryKeySelective(Order record);

    /**
     *
     * @mbg.generated 2018-12-12
     */
    int updateByPrimaryKey(Order record);
}
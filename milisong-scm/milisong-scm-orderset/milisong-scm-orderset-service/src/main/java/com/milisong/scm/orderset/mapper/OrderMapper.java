package com.milisong.scm.orderset.mapper;

import com.milisong.scm.orderset.domain.Order;
import com.milisong.scm.orderset.domain.OrderExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OrderMapper {
    /**
     *
     * @mbg.generated 2019-03-01
     */
    long countByExample(OrderExample example);

    /**
     *
     * @mbg.generated 2019-03-01
     */
    int deleteByExample(OrderExample example);

    /**
     *
     * @mbg.generated 2019-03-01
     */
    int deleteByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2019-03-01
     */
    int insert(Order record);

    /**
     *
     * @mbg.generated 2019-03-01
     */
    int insertSelective(Order record);

    /**
     *
     * @mbg.generated 2019-03-01
     */
    List<Order> selectByExample(OrderExample example);

    /**
     *
     * @mbg.generated 2019-03-01
     */
    Order selectByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2019-03-01
     */
    int updateByExampleSelective(@Param("record") Order record, @Param("example") OrderExample example);

    /**
     *
     * @mbg.generated 2019-03-01
     */
    int updateByExample(@Param("record") Order record, @Param("example") OrderExample example);

    /**
     *
     * @mbg.generated 2019-03-01
     */
    int updateByPrimaryKeySelective(Order record);

    /**
     *
     * @mbg.generated 2019-03-01
     */
    int updateByPrimaryKey(Order record);
}
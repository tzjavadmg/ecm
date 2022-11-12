package com.milisong.breakfast.scm.order.mapper;

import com.milisong.breakfast.scm.order.domain.OrderDetail;
import com.milisong.breakfast.scm.order.domain.OrderDetailExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OrderDetailMapper {
    /**
     *
     * @mbg.generated 2018-12-12
     */
    long countByExample(OrderDetailExample example);

    /**
     *
     * @mbg.generated 2018-12-12
     */
    int deleteByExample(OrderDetailExample example);

    /**
     *
     * @mbg.generated 2018-12-12
     */
    int deleteByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2018-12-12
     */
    int insert(OrderDetail record);

    /**
     *
     * @mbg.generated 2018-12-12
     */
    int insertSelective(OrderDetail record);

    /**
     *
     * @mbg.generated 2018-12-12
     */
    List<OrderDetail> selectByExample(OrderDetailExample example);

    /**
     *
     * @mbg.generated 2018-12-12
     */
    OrderDetail selectByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2018-12-12
     */
    int updateByExampleSelective(@Param("record") OrderDetail record, @Param("example") OrderDetailExample example);

    /**
     *
     * @mbg.generated 2018-12-12
     */
    int updateByExample(@Param("record") OrderDetail record, @Param("example") OrderDetailExample example);

    /**
     *
     * @mbg.generated 2018-12-12
     */
    int updateByPrimaryKeySelective(OrderDetail record);

    /**
     *
     * @mbg.generated 2018-12-12
     */
    int updateByPrimaryKey(OrderDetail record);
}
package com.milisong.scm.orderset.mapper;

import com.milisong.scm.orderset.domain.OrderDetail;
import com.milisong.scm.orderset.domain.OrderDetailExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OrderDetailMapper {
    /**
     *
     * @mbg.generated 2018-10-23
     */
    long countByExample(OrderDetailExample example);

    /**
     *
     * @mbg.generated 2018-10-23
     */
    int deleteByExample(OrderDetailExample example);

    /**
     *
     * @mbg.generated 2018-10-23
     */
    int deleteByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2018-10-23
     */
    int insert(OrderDetail record);

    /**
     *
     * @mbg.generated 2018-10-23
     */
    int insertSelective(OrderDetail record);

    /**
     *
     * @mbg.generated 2018-10-23
     */
    List<OrderDetail> selectByExample(OrderDetailExample example);

    /**
     *
     * @mbg.generated 2018-10-23
     */
    OrderDetail selectByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2018-10-23
     */
    int updateByExampleSelective(@Param("record") OrderDetail record, @Param("example") OrderDetailExample example);

    /**
     *
     * @mbg.generated 2018-10-23
     */
    int updateByExample(@Param("record") OrderDetail record, @Param("example") OrderDetailExample example);

    /**
     *
     * @mbg.generated 2018-10-23
     */
    int updateByPrimaryKeySelective(OrderDetail record);

    /**
     *
     * @mbg.generated 2018-10-23
     */
    int updateByPrimaryKey(OrderDetail record);
}
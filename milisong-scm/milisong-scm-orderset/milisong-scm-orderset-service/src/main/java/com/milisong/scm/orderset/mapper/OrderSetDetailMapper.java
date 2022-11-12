package com.milisong.scm.orderset.mapper;

import com.milisong.scm.orderset.domain.OrderSetDetail;
import com.milisong.scm.orderset.domain.OrderSetDetailExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OrderSetDetailMapper {
    /**
     *
     * @mbg.generated 2018-10-29
     */
    long countByExample(OrderSetDetailExample example);

    /**
     *
     * @mbg.generated 2018-10-29
     */
    int deleteByExample(OrderSetDetailExample example);

    /**
     *
     * @mbg.generated 2018-10-29
     */
    int deleteByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2018-10-29
     */
    int insert(OrderSetDetail record);

    /**
     *
     * @mbg.generated 2018-10-29
     */
    int insertSelective(OrderSetDetail record);

    /**
     *
     * @mbg.generated 2018-10-29
     */
    List<OrderSetDetail> selectByExample(OrderSetDetailExample example);

    /**
     *
     * @mbg.generated 2018-10-29
     */
    OrderSetDetail selectByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2018-10-29
     */
    int updateByExampleSelective(@Param("record") OrderSetDetail record, @Param("example") OrderSetDetailExample example);

    /**
     *
     * @mbg.generated 2018-10-29
     */
    int updateByExample(@Param("record") OrderSetDetail record, @Param("example") OrderSetDetailExample example);

    /**
     *
     * @mbg.generated 2018-10-29
     */
    int updateByPrimaryKeySelective(OrderSetDetail record);

    /**
     *
     * @mbg.generated 2018-10-29
     */
    int updateByPrimaryKey(OrderSetDetail record);
}
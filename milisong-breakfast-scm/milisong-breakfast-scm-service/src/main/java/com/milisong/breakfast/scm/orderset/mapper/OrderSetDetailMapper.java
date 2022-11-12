package com.milisong.breakfast.scm.orderset.mapper;

import com.milisong.breakfast.scm.orderset.domain.OrderSetDetail;
import com.milisong.breakfast.scm.orderset.domain.OrderSetDetailExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OrderSetDetailMapper {
    /**
     *
     * @mbg.generated 2018-12-19
     */
    long countByExample(OrderSetDetailExample example);

    /**
     *
     * @mbg.generated 2018-12-19
     */
    int deleteByExample(OrderSetDetailExample example);

    /**
     *
     * @mbg.generated 2018-12-19
     */
    int deleteByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2018-12-19
     */
    int insert(OrderSetDetail record);

    /**
     *
     * @mbg.generated 2018-12-19
     */
    int insertSelective(OrderSetDetail record);

    /**
     *
     * @mbg.generated 2018-12-19
     */
    List<OrderSetDetail> selectByExample(OrderSetDetailExample example);

    /**
     *
     * @mbg.generated 2018-12-19
     */
    OrderSetDetail selectByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2018-12-19
     */
    int updateByExampleSelective(@Param("record") OrderSetDetail record, @Param("example") OrderSetDetailExample example);

    /**
     *
     * @mbg.generated 2018-12-19
     */
    int updateByExample(@Param("record") OrderSetDetail record, @Param("example") OrderSetDetailExample example);

    /**
     *
     * @mbg.generated 2018-12-19
     */
    int updateByPrimaryKeySelective(OrderSetDetail record);

    /**
     *
     * @mbg.generated 2018-12-19
     */
    int updateByPrimaryKey(OrderSetDetail record);
}
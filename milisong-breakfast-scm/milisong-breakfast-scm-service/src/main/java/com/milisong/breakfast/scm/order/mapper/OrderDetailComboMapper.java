package com.milisong.breakfast.scm.order.mapper;

import com.milisong.breakfast.scm.order.domain.OrderDetailCombo;
import com.milisong.breakfast.scm.order.domain.OrderDetailComboExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OrderDetailComboMapper {
    /**
     *
     * @mbg.generated 2018-12-12
     */
    long countByExample(OrderDetailComboExample example);

    /**
     *
     * @mbg.generated 2018-12-12
     */
    int deleteByExample(OrderDetailComboExample example);

    /**
     *
     * @mbg.generated 2018-12-12
     */
    int deleteByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2018-12-12
     */
    int insert(OrderDetailCombo record);

    /**
     *
     * @mbg.generated 2018-12-12
     */
    int insertSelective(OrderDetailCombo record);

    /**
     *
     * @mbg.generated 2018-12-12
     */
    List<OrderDetailCombo> selectByExample(OrderDetailComboExample example);

    /**
     *
     * @mbg.generated 2018-12-12
     */
    OrderDetailCombo selectByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2018-12-12
     */
    int updateByExampleSelective(@Param("record") OrderDetailCombo record, @Param("example") OrderDetailComboExample example);

    /**
     *
     * @mbg.generated 2018-12-12
     */
    int updateByExample(@Param("record") OrderDetailCombo record, @Param("example") OrderDetailComboExample example);

    /**
     *
     * @mbg.generated 2018-12-12
     */
    int updateByPrimaryKeySelective(OrderDetailCombo record);

    /**
     *
     * @mbg.generated 2018-12-12
     */
    int updateByPrimaryKey(OrderDetailCombo record);
}
package com.mili.oss.mapper.base;

import com.mili.oss.domain.OrderGoodsCombo;
import com.mili.oss.domain.OrderGoodsComboExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BaseOrderGoodsComboMapper {
    /**
     *
     * @mbg.generated 2019-03-07
     */
    long countByExample(OrderGoodsComboExample example);

    /**
     *
     * @mbg.generated 2019-03-07
     */
    int deleteByExample(OrderGoodsComboExample example);

    /**
     *
     * @mbg.generated 2019-03-07
     */
    int insert(OrderGoodsCombo record);

    /**
     *
     * @mbg.generated 2019-03-07
     */
    int insertSelective(OrderGoodsCombo record);

    /**
     *
     * @mbg.generated 2019-03-07
     */
    List<OrderGoodsCombo> selectByExample(OrderGoodsComboExample example);

    /**
     *
     * @mbg.generated 2019-03-07
     */
    OrderGoodsCombo selectByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2019-03-07
     */
    int updateByExampleSelective(@Param("record") OrderGoodsCombo record, @Param("example") OrderGoodsComboExample example);

    /**
     *
     * @mbg.generated 2019-03-07
     */
    int updateByExample(@Param("record") OrderGoodsCombo record, @Param("example") OrderGoodsComboExample example);

    /**
     *
     * @mbg.generated 2019-03-07
     */
    int updateByPrimaryKeySelective(OrderGoodsCombo record);

    /**
     *
     * @mbg.generated 2019-03-07
     */
    int updateByPrimaryKey(OrderGoodsCombo record);
}
package com.milisong.oms.mapper.base;

import com.milisong.oms.domain.OrderPaymentWater;

public interface BaseOrderPaymentWaterMapper {
    /**
     *
     * @mbg.generated 2019-05-21
     */
    int insert(OrderPaymentWater record);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    int insertSelective(OrderPaymentWater record);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    OrderPaymentWater selectByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    int updateByPrimaryKeySelective(OrderPaymentWater record);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    int updateByPrimaryKey(OrderPaymentWater record);
}
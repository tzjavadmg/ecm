package com.milisong.oms.mapper.base;

import com.milisong.oms.domain.OrderDelivery;

public interface BaseOrderDeliveryMapper {
    /**
     *
     * @mbg.generated 2019-05-21
     */
    int insert(OrderDelivery record);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    int insertSelective(OrderDelivery record);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    OrderDelivery selectByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    int updateByPrimaryKeySelective(OrderDelivery record);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    int updateByPrimaryKey(OrderDelivery record);
}
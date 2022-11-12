package com.milisong.oms.mapper.base;

import com.milisong.oms.domain.OrderDeliveryGoods;

public interface BaseOrderDeliveryGoodsMapper {
    /**
     *
     * @mbg.generated 2019-05-21
     */
    int insert(OrderDeliveryGoods record);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    int insertSelective(OrderDeliveryGoods record);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    OrderDeliveryGoods selectByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    int updateByPrimaryKeySelective(OrderDeliveryGoods record);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    int updateByPrimaryKey(OrderDeliveryGoods record);
}
package com.milisong.oms.mapper.base;

import com.milisong.oms.domain.VirtualOrderDeliveryGoods;

public interface BaseVirtualOrderDeliveryGoodsMapper {
    /**
     *
     * @mbg.generated 2019-05-21
     */
    int insert(VirtualOrderDeliveryGoods record);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    int insertSelective(VirtualOrderDeliveryGoods record);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    VirtualOrderDeliveryGoods selectByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    int updateByPrimaryKeySelective(VirtualOrderDeliveryGoods record);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    int updateByPrimaryKey(VirtualOrderDeliveryGoods record);
}
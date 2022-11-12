package com.milisong.oms.mapper.base;

import com.milisong.oms.domain.VirtualOrderDelivery;

public interface BaseVirtualOrderDeliveryMapper {
    /**
     *
     * @mbg.generated 2019-05-21
     */
    int insert(VirtualOrderDelivery record);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    int insertSelective(VirtualOrderDelivery record);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    VirtualOrderDelivery selectByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    int updateByPrimaryKeySelective(VirtualOrderDelivery record);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    int updateByPrimaryKey(VirtualOrderDelivery record);
}
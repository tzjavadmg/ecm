package com.milisong.oms.mapper.base;

import com.milisong.oms.domain.GroupBuyOrderDelivery;

public interface BaseGroupBuyOrderDeliveryMapper {
    /**
     *
     * @mbg.generated 2019-05-21
     */
    int insert(GroupBuyOrderDelivery record);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    int insertSelective(GroupBuyOrderDelivery record);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    GroupBuyOrderDelivery selectByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    int updateByPrimaryKeySelective(GroupBuyOrderDelivery record);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    int updateByPrimaryKey(GroupBuyOrderDelivery record);
}
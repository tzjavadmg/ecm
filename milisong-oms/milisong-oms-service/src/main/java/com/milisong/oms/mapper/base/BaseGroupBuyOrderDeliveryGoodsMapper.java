package com.milisong.oms.mapper.base;

import com.milisong.oms.domain.GroupBuyOrderDeliveryGoods;

public interface BaseGroupBuyOrderDeliveryGoodsMapper {
    /**
     *
     * @mbg.generated 2019-05-21
     */
    int insert(GroupBuyOrderDeliveryGoods record);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    int insertSelective(GroupBuyOrderDeliveryGoods record);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    GroupBuyOrderDeliveryGoods selectByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    int updateByPrimaryKeySelective(GroupBuyOrderDeliveryGoods record);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    int updateByPrimaryKey(GroupBuyOrderDeliveryGoods record);
}
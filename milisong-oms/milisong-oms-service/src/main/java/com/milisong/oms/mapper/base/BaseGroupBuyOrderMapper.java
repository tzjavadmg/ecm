package com.milisong.oms.mapper.base;

import com.milisong.oms.domain.GroupBuyOrder;

public interface BaseGroupBuyOrderMapper {
    /**
     *
     * @mbg.generated 2019-05-21
     */
    int insert(GroupBuyOrder record);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    int insertSelective(GroupBuyOrder record);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    GroupBuyOrder selectByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    int updateByPrimaryKeySelective(GroupBuyOrder record);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    int updateByPrimaryKey(GroupBuyOrder record);
}
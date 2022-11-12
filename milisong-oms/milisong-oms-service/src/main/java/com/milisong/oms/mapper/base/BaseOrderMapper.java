package com.milisong.oms.mapper.base;

import com.milisong.oms.domain.Order;

public interface BaseOrderMapper {
    /**
     *
     * @mbg.generated 2019-05-21
     */
    int insert(Order record);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    int insertSelective(Order record);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    Order selectByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    int updateByPrimaryKeySelective(Order record);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    int updateByPrimaryKey(Order record);
}
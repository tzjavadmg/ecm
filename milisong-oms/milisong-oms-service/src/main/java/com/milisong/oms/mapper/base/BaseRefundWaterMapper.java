package com.milisong.oms.mapper.base;

import com.milisong.oms.domain.RefundWater;

public interface BaseRefundWaterMapper {
    /**
     *
     * @mbg.generated 2019-05-21
     */
    int insert(RefundWater record);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    int insertSelective(RefundWater record);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    RefundWater selectByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    int updateByPrimaryKeySelective(RefundWater record);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    int updateByPrimaryKey(RefundWater record);
}
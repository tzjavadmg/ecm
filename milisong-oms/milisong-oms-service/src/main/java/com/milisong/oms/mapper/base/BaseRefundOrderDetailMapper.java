package com.milisong.oms.mapper.base;

import com.milisong.oms.domain.RefundOrderDetail;

public interface BaseRefundOrderDetailMapper {
    /**
     *
     * @mbg.generated 2019-05-21
     */
    int insert(RefundOrderDetail record);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    int insertSelective(RefundOrderDetail record);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    RefundOrderDetail selectByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    int updateByPrimaryKeySelective(RefundOrderDetail record);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    int updateByPrimaryKey(RefundOrderDetail record);
}
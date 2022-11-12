package com.milisong.oms.mapper.base;

import com.milisong.oms.domain.RefundOrder;

public interface BaseRefundOrderMapper {
    /**
     *
     * @mbg.generated 2019-05-21
     */
    int insert(RefundOrder record);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    int insertSelective(RefundOrder record);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    RefundOrder selectByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    int updateByPrimaryKeySelective(RefundOrder record);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    int updateByPrimaryKey(RefundOrder record);
}
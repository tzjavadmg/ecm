package com.milisong.delay.mapper.base;

import com.milisong.delay.domain.DelayConsumeEvent;

public interface BaseDelayConsumeEventMapper {
    /**
     *
     * @mbg.generated 2018-11-08
     */
    int insert(DelayConsumeEvent record);

    /**
     *
     * @mbg.generated 2018-11-08
     */
    int insertSelective(DelayConsumeEvent record);

    /**
     *
     * @mbg.generated 2018-11-08
     */
    DelayConsumeEvent selectByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2018-11-08
     */
    int updateByPrimaryKeySelective(DelayConsumeEvent record);

    /**
     *
     * @mbg.generated 2018-11-08
     */
    int updateByPrimaryKeyWithBLOBs(DelayConsumeEvent record);

    /**
     *
     * @mbg.generated 2018-11-08
     */
    int updateByPrimaryKey(DelayConsumeEvent record);
}
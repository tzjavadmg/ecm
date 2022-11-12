package com.milisong.delay.mapper.base;

import com.milisong.delay.domain.DelayProductionEvent;

public interface BaseDelayProductionEventMapper {
    /**
     *
     * @mbg.generated 2018-11-08
     */
    int insert(DelayProductionEvent record);

    /**
     *
     * @mbg.generated 2018-11-08
     */
    int insertSelective(DelayProductionEvent record);

    /**
     *
     * @mbg.generated 2018-11-08
     */
    DelayProductionEvent selectByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2018-11-08
     */
    int updateByPrimaryKeySelective(DelayProductionEvent record);

    /**
     *
     * @mbg.generated 2018-11-08
     */
    int updateByPrimaryKeyWithBLOBs(DelayProductionEvent record);

    /**
     *
     * @mbg.generated 2018-11-08
     */
    int updateByPrimaryKey(DelayProductionEvent record);
}
package com.milisong.oms.mapper.base;

import com.milisong.oms.domain.MqConsumeEvent;

public interface BaseMqConsumeEventMapper {
    /**
     *
     * @mbg.generated 2018-12-06
     */
    int insert(MqConsumeEvent record);

    /**
     *
     * @mbg.generated 2018-12-06
     */
    int insertSelective(MqConsumeEvent record);
}
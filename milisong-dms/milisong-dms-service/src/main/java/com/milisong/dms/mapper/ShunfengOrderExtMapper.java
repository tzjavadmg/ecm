package com.milisong.dms.mapper;

import com.milisong.dms.domain.ShunfengOrder;
import com.milisong.dms.dto.shunfeng.SfOrderRspDto;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ShunfengOrderExtMapper {

    ShunfengOrder selectSfOrderBySetDetailId(@Param("setDetailId") Long setDetailId);

    List<SfOrderRspDto> selectSfOrderByCreateTime(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("businessType") Byte businessType);

    List<SfOrderRspDto> selectUnconfirmedSfOrder(@Param("overTime")Integer overTime);

    List<ShunfengOrder> selectOverTimeSfOrder();
}
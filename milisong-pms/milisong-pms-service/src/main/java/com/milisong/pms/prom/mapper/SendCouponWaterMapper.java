package com.milisong.pms.prom.mapper;

import com.milisong.pms.prom.domain.SendCouponWater;
import com.milisong.pms.prom.dto.SendCouponRecordDto;
import com.milisong.pms.prom.dto.SendCouponRecordWaterRequest;
import com.milisong.pms.prom.dto.SendCouponWaterDto;

import java.util.List;

public interface SendCouponWaterMapper {
    int insert(SendCouponWater record);

    int insertSelective(SendCouponWater record);

    SendCouponWater selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SendCouponWater record);

    int updateByPrimaryKey(SendCouponWater record);

    int insertBatch(List<SendCouponWater> sendCouponWaters);

    int countSendCouponWater(SendCouponRecordWaterRequest recordWaterRequest);

    List<SendCouponWaterDto> querySendCouponWater(SendCouponRecordWaterRequest recordWaterRequest);
}
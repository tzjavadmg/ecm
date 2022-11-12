package com.milisong.pms.prom.mapper;

import com.milisong.pms.prom.domain.SendCouponRecord;
import com.milisong.pms.prom.dto.SendCouponRecordDto;
import com.milisong.pms.prom.dto.SendCouponRecordRequest;
import com.milisong.pms.prom.dto.SendCouponRecordWaterRequest;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface SendCouponRecordMapper {

    int insert(SendCouponRecord record);

    int insertSelective(SendCouponRecord record);

    SendCouponRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SendCouponRecord record);

    int updateByPrimaryKey(SendCouponRecord record);

    int countSendCouponRecord(SendCouponRecordRequest recordRequest);

    List<SendCouponRecordDto> querySendCouponRecord(SendCouponRecordRequest recordRequest);

    List<SendCouponRecord> querySendCouponRecordByStatus(@RequestParam("status") Byte status,@RequestParam("buzLine") Byte buzLine);
}
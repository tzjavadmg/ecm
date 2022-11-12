package com.milisong.pms.prom.service;

import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.util.BeanMapper;
import com.google.common.collect.Lists;
import com.milisong.pms.prom.api.BreakfastCouponService;
import com.milisong.pms.prom.api.CouponService;
import com.milisong.pms.prom.api.SendCouponRecordService;
import com.milisong.pms.prom.api.SendLunchRedPacketRecordService;
import com.milisong.pms.prom.domain.SendCouponRecord;
import com.milisong.pms.prom.dto.*;
import com.milisong.pms.prom.enums.SendCouponStatus;
import com.milisong.pms.prom.mapper.SendCouponRecordMapper;
import com.milisong.pms.prom.mapper.SendCouponWaterMapper;
import com.milisong.ucs.api.UserDeliveryAddressService;
import com.milisong.ucs.enums.BusinessLineEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.milisong.pms.prom.enums.PromotionResponseCode.*;

/**
 * @author sailor wang
 * @date 2019/1/13 10:38 AM
 * @description
 */
@Slf4j
@Service
@RestController
public class SendCouponRecordServiceImpl implements SendCouponRecordService {

    @Autowired
    SendCouponRecordMapper sendCouponRecordMapper;

    @Autowired
    SendCouponWaterMapper sendCouponWaterMapper;

    @Autowired
    BreakfastCouponService breakfastCouponService;

    @Autowired
    CouponService couponService;

    @Autowired
    SendCouponRecordService sendCouponRecordService;

    @Autowired
    UserDeliveryAddressService userDeliveryAddressService;

    @Autowired
    SendLunchRedPacketRecordService sendLunchRedPacketRecordService;

    @Override
    public ResponseResult<SendCouponSearchConditionDto> getById(@RequestParam("sendCouponRecordId") Long sendCouponRecordId) {
        SendCouponRecord sendCouponRecord = sendCouponRecordMapper.selectByPrimaryKey(sendCouponRecordId);
        if (sendCouponRecord != null){
            SendBreakCouponRequest filterCondition = JSONObject.parseObject(sendCouponRecord.getFilterCondition(),SendBreakCouponRequest.class);

            SendCouponSearchConditionDto searchCondition = new SendCouponSearchConditionDto();
            if (CollectionUtils.isNotEmpty(filterCondition.getCouponids())){
                ResponseResult<List<CouponDto>> result = couponService.queryByIds(filterCondition.getCouponids());
                if (result.isSuccess() && CollectionUtils.isNotEmpty(result.getData())){
                    searchCondition.setCouponList(result.getData());
                }
            }
            if (CollectionUtils.isNotEmpty(filterCondition.getDeliveryOfficeBuildingIds())){
                ResponseResult<List<com.milisong.ucs.dto.UserDeliveryAddressDto>> result =
                        userDeliveryAddressService.queryDeliveryCompanyNames(filterCondition.getDeliveryOfficeBuildingIds());
                if (result.isSuccess() && CollectionUtils.isNotEmpty(result.getData())){
                    searchCondition.setCompanyList(BeanMapper.mapList(result.getData(),UserDeliveryAddressDto.class));
                }
            }
            searchCondition.setSendTime(filterCondition.getSendTime());
            searchCondition.setSendAllUser(filterCondition.getSendAllUser());
            searchCondition.setSaveAddrTimeStart(filterCondition.getSaveAddrTimeStart());
            searchCondition.setSaveAddrTimeEnd(filterCondition.getSaveAddrTimeEnd());
            searchCondition.setMobiles(filterCondition.getMobiles());
            searchCondition.setSmsMsg(filterCondition.getSmsMsg());
            searchCondition.setExcludeUserIds(filterCondition.getExcludeUserIds());
            return ResponseResult.buildSuccessResponse(searchCondition);
        }
        return ResponseResult.buildFailResponse(DATA_NOT_EXISTS.code(),DATA_NOT_EXISTS.message());
    }

    @Override
    public ResponseResult<Pagenation<SendCouponRecordDto>> querySendCouponRecord(@RequestBody SendCouponRecordRequest recordRequest) {
        if (recordRequest.getBusinessLine() == null){
            return ResponseResult.buildFailResponse(POINT_BUSINESS_LINE_NULL.code(),POINT_BUSINESS_LINE_NULL.message());
        }
        if (recordRequest.getPageNo() < 0){
            recordRequest.setPageNo(1);
        }
        if (recordRequest.getPageSize() < 0) recordRequest.setPageSize(10);
        if (recordRequest.getPageSize() > 500) recordRequest.setPageSize(500);

        Pagenation<SendCouponRecordDto> pagenation = new Pagenation<>();
        pagenation.setRowCount(0);
        pagenation.setList(Lists.newArrayList());
        int rowCount = sendCouponRecordMapper.countSendCouponRecord(recordRequest);
        if (rowCount > 0){
            List<SendCouponRecordDto> list = sendCouponRecordMapper.querySendCouponRecord(recordRequest);
            pagenation.setRowCount(rowCount);
            pagenation.setList(list);
        }
        return ResponseResult.buildSuccessResponse(pagenation);
    }

    @Override
    public ResponseResult<Pagenation<SendCouponWaterDto>> querySendCouponWater(@RequestBody SendCouponRecordWaterRequest recordWaterRequest) {
        if (recordWaterRequest.getSendCouponRecordId() == null || recordWaterRequest.getBusinessLine() == null){
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(),REQUEST_PARAM_EMPTY.message());
        }
        if (recordWaterRequest.getPageNo() < 0){
            recordWaterRequest.setPageNo(1);
        }
        if (recordWaterRequest.getPageSize() < 0) recordWaterRequest.setPageSize(10);
        if (recordWaterRequest.getPageSize() > 500) recordWaterRequest.setPageSize(500);

        Pagenation<SendCouponWaterDto> pagenation = new Pagenation<>();
        pagenation.setRowCount(0);
        pagenation.setList(Lists.newArrayList());

        int rowCount = sendCouponWaterMapper.countSendCouponWater(recordWaterRequest);
        if (rowCount > 0){
            List<SendCouponWaterDto> list = sendCouponWaterMapper.querySendCouponWater(recordWaterRequest);
            pagenation.setRowCount(rowCount);
            pagenation.setList(list);
        }
        return ResponseResult.buildSuccessResponse(pagenation);
    }

    @Override
    public ResponseResult<Boolean> scheduleSendCoupon() {
        try {
            sendLunchRedPacketRecordService.scheduleSendRedPacket();
        } catch (Exception e) {
            log.error("",e);
        }

        Byte buzLine = BusinessLineEnum.BREAKFAST.getCode();
        Byte status = SendCouponStatus.WAIT_FOR_SEND.getCode();
        List<SendCouponRecord> list = sendCouponRecordMapper.querySendCouponRecordByStatus(status,buzLine);
        if (CollectionUtils.isNotEmpty(list)){
            for (SendCouponRecord sendCouponRecord : list){
                try {
                    log.info("定时发送券 sendCouponRecord -> {}",sendCouponRecord);
                    SendBreakCouponRequest sendBreakCouponRequest = JSONObject.parseObject(sendCouponRecord.getFilterCondition(),SendBreakCouponRequest.class);
                    sendBreakCouponRequest.setId(sendCouponRecord.getId());
                    breakfastCouponService.batchSendBreakfastCoupon(sendBreakCouponRequest);
                } catch (Exception e) {
                    log.error("",e);
                }
            }
        }else {
            log.info("暂无待发送券数据。。。");
        }
        return ResponseResult.buildSuccessResponse();
    }
}
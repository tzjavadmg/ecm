package com.milisong.dms.api;

import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.milisong.dms.dto.orderset.OrderSetProductionMqMsg;
import com.milisong.dms.dto.shunfeng.RiderInfoDto;
import com.milisong.dms.dto.shunfeng.SfOrderCompanyInfoDto;
import com.milisong.dms.dto.shunfeng.SfOrderReqDto;

import java.util.List;
import java.util.Map;

/**
 * @author zhaozhonghui
 * @date 2018-10-22
 */
public interface ShunfengOrderService {

    void convertOrderSetMsg(OrderSetProductionMqMsg orderSetProductionMqMsg,Byte businessType);

    ResponseResult createOrder(SfOrderReqDto dto,Byte businessType);

    void cacheSfOrder(SfOrderReqDto dto, List<SfOrderCompanyInfoDto> companyMealAddressList, String sfOrderId);

    Map<String,Object> sendShunfengBackMsg(String msg);

    ResponseResult testCrestOrder(SfOrderReqDto dto);

    JSONObject postSendMsg(String url, String param);

    void sendSmsMsg(String detailSetId, String mobiles,Byte businessType);

    ResponseResult<RiderInfoDto> getSfOrderDeliveryStatusByDeliveryNo(String deliveryNo,Byte businessType);

    ResponseResult<RiderInfoDto> getSfOrderDeliveryStatusBySetDetailId(Long setDetailId);


}

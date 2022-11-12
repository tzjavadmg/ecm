package com.milisong.dms.api;

import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.milisong.dms.dto.shunfeng.RiderInfoDto;
import com.milisong.dms.dto.shunfeng.SfOrderReqDto;

import java.util.Map;

/**
 * @author zhaozhonghui
 * @date 2018-10-22
 */
public interface DaDaOrderService {

    ResponseResult createOrder(SfOrderReqDto dto);

    Map<String,Object> sendShunfengBackMsg(String msg);

    ResponseResult testCrestOrder(SfOrderReqDto dto);

    JSONObject postSendMsg(String url, String param);

    void sendSmsMsg(String detailSetId, String mobiles);

    String getDetailSetIdBySfOrderId(String sfOrderId);

    ResponseResult<RiderInfoDto> getSfOrderDeliveryStatusByDeliveryNo(String deliveryNo);

    ResponseResult<RiderInfoDto> getSfOrderDeliveryStatusBySetDetailId(Long setDetailId);


}

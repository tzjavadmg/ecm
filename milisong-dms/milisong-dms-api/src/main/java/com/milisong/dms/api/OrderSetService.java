package com.milisong.dms.api;

import com.milisong.dms.dto.httpdto.NotifyOrderSetQueryResult;
import com.milisong.dms.dto.orderset.OrderSetDetailDto;

import java.util.List;

/**
 * @author zhaozhonghui
 * @Description ${Description}
 * @date 2018-12-25
 */
public interface OrderSetService {

    List<OrderSetDetailDto> getOrderSetByOrderNo(String deliveryNo, Byte businessType);

    NotifyOrderSetQueryResult getOrderSetInfoByDetailSetNo(String setDetailNo, Long setDetailId, Byte businessType);

}

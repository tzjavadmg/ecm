package com.milisong.ecm.lunch.order.service;

import com.alibaba.fastjson.JSON;
import com.farmland.core.api.ResponseResult;
import com.milisong.ecm.common.util.WeekDayUtils;
import com.milisong.oms.constant.OrderResponseCode;
import com.milisong.oms.param.VirtualOrderDeliveryParam;
import com.milisong.oms.param.VirtualOrderParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ Description：虚拟订单校验器。不同业务线不同的校验规则
 * @ Author     ：zengyuekang.
 * @ Date       ：2018/12/3 14:47
 */
@Slf4j
@Component
public class VirtualOrderValidatorLC {
    @Resource
    private ShopConfigServiceLC shopConfigServiceLC;

    public ResponseResult<?> validate(VirtualOrderParam virtualOrderParam) {
        log.info("校验虚拟订单参数：{}", JSON.toJSONString(virtualOrderParam));
        List<VirtualOrderDeliveryParam> deliveryParamList = virtualOrderParam.getDeliveries();
        if (CollectionUtils.isEmpty(deliveryParamList)) {
            return ResponseResult.buildFailResponse(OrderResponseCode.V_ORDER_CHECK_NO_GOODS.getCode(), OrderResponseCode.V_ORDER_CHECK_NO_GOODS.getMessage());
        }
        //过虑配送单中没有商品编码的异常数据
        List<VirtualOrderDeliveryParam> filterResultList = deliveryParamList.stream().filter(dailyOrder ->
                CollectionUtils.isNotEmpty(dailyOrder.getDeliveryGoods())
        ).collect(Collectors.toList());
        virtualOrderParam.setDeliveries(filterResultList);

        boolean isConfirm = virtualOrderParam.isConfirm();
        Date now = new Date();
        VirtualOrderDeliveryParam virtualOrderDeliveryParam = filterResultList.get(0);
        //今天最后一次截单时间
        Date cutOffOrderTime = shopConfigServiceLC.getTodayLastCutOffTime(virtualOrderParam.getShopCode());
        //订单中第一天配送日期
        Date firstPreOrderDate = virtualOrderDeliveryParam.getDeliveryDate();
        log.info("======--------------点击【去结算】时生成虚拟订单，截单时间：{}，首个配送日：{}", DateFormatUtils.format(cutOffOrderTime, "yyyy-MM-dd"), DateFormatUtils.format(firstPreOrderDate, "yyyy-MM-dd"), isConfirm);
        if (WeekDayUtils.isToday(firstPreOrderDate) && now.compareTo(cutOffOrderTime) > 0 && !isConfirm) {
            return ResponseResult.buildFailResponse(OrderResponseCode.V_ORDER_CHECK_SKIP_TODAY.getCode(), OrderResponseCode.V_ORDER_CHECK_SKIP_TODAY.getMessage());
        }
        if (WeekDayUtils.isToday(firstPreOrderDate) && now.compareTo(cutOffOrderTime) > 0 && filterResultList.size() == 1) {
            return ResponseResult.buildFailResponse(OrderResponseCode.V_ORDER_CHECK_CUTOFF_TIME_EXPIRED.getCode(), OrderResponseCode.V_ORDER_CHECK_CUTOFF_TIME_EXPIRED.getMessage());
        }
        return ResponseResult.buildSuccessResponse();
    }
}

package com.milisong.dms.mq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface MqMessageShunfengSource {
    // 发送午餐顺丰订单状态
    String SF_STATUS_CHANGE_OUTPUT = "sf_status_change_output";
    // 消费午餐集单信息
    String DMS_ORDER_SET_INPUT = "dms_order_set_input";
    // 消费早餐集单信息
    String DMS_BF_ORDER_SET_INPUT = "dms_bf_order_set_input";
    // 门店信息消费
    String SHOP_INFO_INPUT = "shop_info_input";
    // 楼宇信息消费
    String BUILDING_INFO_INPUT = "building_info_input";

    @Output(SF_STATUS_CHANGE_OUTPUT)
    MessageChannel sfStatusChangeOutput();

    @Input(DMS_ORDER_SET_INPUT)
    MessageChannel sfOrderSetInput();

    @Input(DMS_BF_ORDER_SET_INPUT)
    MessageChannel sfBfOrderSetInput();

    @Input(SHOP_INFO_INPUT)
    MessageChannel shopInfoInput();

    @Input(BUILDING_INFO_INPUT)
    MessageChannel buildingInfoInput();

}

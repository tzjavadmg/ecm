package com.mili.oss.api;

import com.mili.oss.dto.OrderDetailResult;
import com.mili.oss.dto.OrderDto;
import com.mili.oss.dto.OrderRefundDto;
import com.mili.oss.dto.config.InterceptConfigDto;
import com.mili.oss.dto.config.ShopInterceptConfigDto;
import com.mili.oss.dto.orderstatus.DeliveryOrderMqDto;

import java.util.List;

public interface OrderService {

    /**
     * 保存从MQ去接收来的订单信息
     *
     * @param orderDto
     */
    void save(List<OrderDto> orderDtoList);

    /**
     * 订单退款处理
     *
     * @param list
     */
    void refund(List<OrderRefundDto> list);

    /**
     * @param id
     * @param map
     * @param orderType 订单类型 0 早餐、1午餐
     */
    void executeShopSet(Long id, InterceptConfigDto map, byte orderType);

    /**
     * 发送门店的集单信息给POS系统进行生产
     *
     * @param shopId
     * @param isCompensate 是否补偿
     */
    void sendOrderSetMq(Long shopId, boolean isCompensate, byte orderType);

    /**
     * 更新集单的配送状态（顺丰回调MQ调用一级入口）
     *
     * @param param
     */
    void updateDistributionStatus(DeliveryOrderMqDto param);

    /**
     * 根据订单号获取订单信息
     *
     * @param orderNos
     * @return
     */
    List<OrderDetailResult> getOrderDetailInfoByOrderNo(List<String> orderNos);

    /**
     * 推送顺丰订单
     *
     * @param configDto
     */
    void pushSfOrderSetMq(ShopInterceptConfigDto configDto);
    /**
     * 推送午餐顺丰订单
     *
     * @param configDto
     */
    void pushLcSfOrderSetMq(ShopInterceptConfigDto configDto);

    /**
     * 补偿完成集单状态
     */
    void compensateCompletedOrderSetTask();

    void updateOrderShop(Long companyId, Long shopId, String shopCode);
}

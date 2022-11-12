package com.milisong.oms.service.virtual;

import com.alibaba.fastjson.JSON;
import com.farmland.core.api.ResponseResult;
import com.milisong.oms.api.StockService;
import com.milisong.oms.constant.GoodsStockSource;
import com.milisong.oms.constant.OrderResponseCode;
import com.milisong.oms.constant.VirtualOrderStatus;
import com.milisong.oms.domain.VirtualOrder;
import com.milisong.oms.domain.VirtualOrderDelivery;
import com.milisong.oms.domain.VirtualOrderDeliveryGoods;
import com.milisong.oms.mapper.VirtualOrderDeliveryGoodsMapper;
import com.milisong.oms.mapper.VirtualOrderDeliveryMapper;
import com.milisong.oms.mapper.VirtualOrderMapper;
import com.milisong.oms.service.stock.ShopDailyStockBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/11/8 18:40
 */
@Slf4j
@Component
@Transactional(rollbackFor = RuntimeException.class)
public class VirtualOrderTransactionServiceImpl implements VirtualOrderTransactionService {
    @Resource
    private VirtualOrderMapper virtualOrderMapper;
    @Resource
    private VirtualOrderDeliveryMapper virtualOrderDeliveryMapper;
    @Resource
    private VirtualOrderDeliveryGoodsMapper virtualOrderDeliveryGoodsMapper;
    @Resource
    private StockService stockService;

    @Override
    public ResponseResult<?> cancelVirtualOrder(Long orderId) {
        VirtualOrder virtualOrder = virtualOrderMapper.selectByPrimaryKey(orderId);
        Byte orderStatus = virtualOrder.getStatus();
        log.info("---------取消虚拟订单,订单ID{}------虚拟订单状态：{}", orderId, orderStatus);
        if (VirtualOrderStatus.CANCELED.getValue() == orderStatus) {
            return ResponseResult.buildFailResponse(OrderResponseCode.V_ORDER_CANCEL_ERROR_CANCELED.getCode(), OrderResponseCode.V_ORDER_CANCEL_ERROR_CANCELED.getMessage());
        }
        if (VirtualOrderStatus.COMPLETED.getValue() == orderStatus) {
            return ResponseResult.buildFailResponse(OrderResponseCode.V_ORDER_CANCEL_ERROR_COMPLETED.getCode(), OrderResponseCode.V_ORDER_CANCEL_ERROR_COMPLETED.getMessage());
        }
        rollbackStock(virtualOrder);
        return ResponseResult.buildSuccessResponse();
    }

    /**
     * 虚拟订单修改状态并释放库存
     *
     * @param virtualOrder 虚拟订单
     */
    private void rollbackStock(VirtualOrder virtualOrder) {
        log.info("-----------------取消虚拟主单,释放锁定库存：{}", JSON.toJSONString(virtualOrder));
        long orderId = virtualOrder.getId();
        String shopCode = virtualOrder.getShopCode();

        final List<VirtualOrderDelivery> deliveryList = virtualOrderDeliveryMapper.findDeliveryDateListByOrderId(orderId);
        final Set<Long> ids = deliveryList.stream().map(VirtualOrderDelivery::getId).collect(Collectors.toSet());
        final List<VirtualOrderDeliveryGoods> deliveryGoodsList = virtualOrderDeliveryGoodsMapper.batchFindByDeliveryIds(ids);

        List<StockService.ShopDailyStock> dailyGoodsStockList = ShopDailyStockBuilder.buildIncrementVirtualDailyGoodsStocks(GoodsStockSource.CREATE_VIRTUAL_ORDER.getValue(), shopCode, deliveryList, deliveryGoodsList);
        log.info("========释放库存.......订单集合：{}，订单明细集合：{}", JSON.toJSONString(deliveryList), JSON.toJSONString(deliveryGoodsList));
        stockService.lockMultiDayStock(dailyGoodsStockList);
        try {
            updateVirtualOrderStatus(virtualOrder);
            //回滚相关商品库存
            stockService.incrementMultiDayStock(dailyGoodsStockList);
            log.info("==========虚拟订单取消成功==============订单ID：{},回滚库存.......门店编码：{}，库存信息：{}", orderId, shopCode, JSON.toJSONString(dailyGoodsStockList));
        } finally {
            //解锁相关商品库存
            stockService.unlockMultiDayStock(dailyGoodsStockList);
        }
    }

    private void updateVirtualOrderStatus(VirtualOrder virtualOrder) {
        virtualOrder.setStatus(VirtualOrderStatus.CANCELED.getValue());
        virtualOrder.setCancelTime(new Date());
        virtualOrderMapper.updateByPrimaryKeySelective(virtualOrder);
    }
}

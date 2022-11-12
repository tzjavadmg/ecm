package com.milisong.oms.service.virtual;

import com.alibaba.fastjson.JSON;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.distrib.LockProvider;
import com.farmland.core.util.BeanMapper;
import com.milisong.oms.api.StockService;
import com.milisong.oms.api.VirtualOrderService;
import com.milisong.oms.constant.GoodsStockSource;
import com.milisong.oms.constant.OrderResponseCode;
import com.milisong.oms.constant.VirtualOrderStatus;
import com.milisong.oms.domain.VirtualOrder;
import com.milisong.oms.domain.VirtualOrderDelivery;
import com.milisong.oms.domain.VirtualOrderDeliveryGoods;
import com.milisong.oms.dto.VirtualOrderDto;
import com.milisong.oms.mapper.VirtualOrderDeliveryGoodsMapper;
import com.milisong.oms.mapper.VirtualOrderDeliveryMapper;
import com.milisong.oms.mapper.VirtualOrderMapper;
import com.milisong.oms.param.VirtualOrderParam;
import com.milisong.oms.service.DelayQueueHelper;
import com.milisong.oms.service.promotion.PromotionHelper;
import com.milisong.oms.service.stock.ShopDailyStockBuilder;
import com.milisong.oms.util.SysConfigUtils;
import com.milisong.oms.util.DateUtil;
import com.milisong.oms.util.OrderDtoBuilder;
import com.milisong.oms.util.OrderRedisKeyUtils;
import com.milisong.pms.prom.dto.OrderAmountDto;
import com.milisong.pms.prom.param.OrderParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/1 11:07
 */
@Slf4j
@RestController
public class VirtualOrderServiceImpl implements VirtualOrderService {

    @Resource
    private VirtualOrderTransactionService virtualOrderTransactionService;
    @Resource
    private VirtualOrderMapper virtualOrderMapper;
    @Resource
    private VirtualOrderDeliveryMapper virtualOrderDeliveryMapper;
    @Resource
    private VirtualOrderDeliveryGoodsMapper virtualOrderDeliveryGoodsMapper;
    @Resource
    private StockService stockService;
    @Resource
    private VirtualOrderBuilder virtualOrderBuilder;
    @Resource
    private DelayQueueHelper delayQueueHelper;
    @Resource
    private PromotionHelper promotionHelper;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public ResponseResult<?> createVirtualOrder(@RequestBody VirtualOrderParam virtualOrderParam) {
        log.info("------点击【去结算】时------生成虚拟订单，入参：{}", JSON.toJSONString(virtualOrderParam));

        //构建虚拟定单结构体
        VirtualOrderBuilder.VirtualOrderModel virtualOrderModel = virtualOrderBuilder.buildVirtualOrderModel(virtualOrderParam);
        log.info("---------构建虚拟订单模型------{}", JSON.toJSONString(virtualOrderModel));
        final VirtualOrder virtualOrder = virtualOrderModel.getVirtualOrder();
        final List<VirtualOrderDelivery> deliveryList = virtualOrderModel.getDeliveryList();
        final List<VirtualOrderDeliveryGoods> deliveryGoodsList = virtualOrderModel.getDeliveryGoodsList();

        OrderAmountDto orderAmountDto=setOrderAmount(virtualOrder, deliveryList, deliveryGoodsList);
        log.info("------点击【去结算】时------构建虚拟订单数据，订单：{}，订单配送集：{}，订单明细集：{}", JSON.toJSONString(virtualOrder), JSON.toJSONString(deliveryList), JSON.toJSONString(deliveryGoodsList));
        String shopCode = virtualOrderParam.getShopCode();
        List<StockService.ShopDailyStock> dailyGoodsStockList = ShopDailyStockBuilder.buildDecrementVirtualDailyGoodsStocks(GoodsStockSource.CREATE_VIRTUAL_ORDER.getValue(), shopCode, deliveryList, deliveryGoodsList);
        //获得库存锁，批量插入订单和订单明细记录
        log.info("--------@@@@@@@@-------锁定库存：{},需要校验的商品数量信息：{}", shopCode, JSON.toJSONString(dailyGoodsStockList));
        stockService.lockMultiDayStock(dailyGoodsStockList);
        log.info("--------@@@@@@@@----创建虚拟订单----------获取联合锁");
        try {
            Map<String, List<String>> verifyResult = stockService.verifyMultiDayStock(dailyGoodsStockList);
            if (MapUtils.isNotEmpty(verifyResult)) {
                return ResponseResult.buildFailResponse(OrderResponseCode.V_ORDER_CHECK_NO_STOCK.getCode(), verifyResult);
            }
            virtualOrderMapper.insert(virtualOrder);
            log.info("------点击【去结算】时------插入订单信息：{}", JSON.toJSONString(virtualOrder));
            virtualOrderDeliveryMapper.batchSave(deliveryList);
            log.info("------点击【去结算】时------批量插入配送单信息：{}", JSON.toJSONString(deliveryList));
            virtualOrderDeliveryGoodsMapper.batchSave(deliveryGoodsList);
            log.info("------点击【去结算】时------批量插入配送单商品信息：{}", JSON.toJSONString(deliveryGoodsList));
            stockService.decrementMultiDayStock(dailyGoodsStockList);
            VirtualOrderDto virtualOrderDto = virtualOrderBuilder.buildVirtualOrderDto(virtualOrderModel,orderAmountDto);
            log.info("------点击【去结算】时------返回数据：{}", JSON.toJSONString(virtualOrderDto));
            cacheUncompletedVirtualOrder(virtualOrder.getId(), virtualOrder.getOrderDate());
            return ResponseResult.buildSuccessResponse(virtualOrderDto);
        } finally {
            stockService.unlockMultiDayStock(dailyGoodsStockList);
            log.info("--------@@@@@@@@----创建虚拟订单------------离开联合锁");
        }
    }

    private OrderAmountDto setOrderAmount(VirtualOrder virtualOrder, List<VirtualOrderDelivery> deliveryList, List<VirtualOrderDeliveryGoods> deliveryGoodsList) {
        OrderParam orderParam = BeanMapper.map(OrderDtoBuilder.buildOrderDto(virtualOrder, deliveryList, deliveryGoodsList), OrderParam.class);
        log.info("-------虚拟订单----促销计算金额参数--------：{}", JSON.toJSONString(orderParam));
        orderParam.setBusinessLine(virtualOrder.getOrderType());
        OrderAmountDto orderAmountDto = promotionHelper.preCalculate(orderParam);
        log.info("-------虚拟订单----促销计算金额结果--------：{}", JSON.toJSONString(orderAmountDto));
        virtualOrder.setTotalPayAmount(virtualOrder.getTotalDeliveryActualAmount().add(orderAmountDto.getTotalAmount()));
        virtualOrder.setFullReduceAmount(orderAmountDto.getFullReduceAmount());
        return orderAmountDto;
    }

    private void cacheUncompletedVirtualOrder(Long virtualOrderId, Date orderDate) {
        //String orderKey = OrderRedisKeyUtils.getUncompletedVirtualOrderKey(virtualOrderId);
        //RedisCache.setEx(orderKey, DateFormatUtils.format(orderDate, "yyyy-MM-dd HH:mm:ss"), shopConfigService.getUnPayExpiredTime(), TimeUnit.MINUTES);
        delayQueueHelper.pushVirtual(virtualOrderId, orderDate);
    }

    @Override
    public ResponseResult<?> userCancelVirtualOrder(Long orderId) {
        log.info("==========离开结算页自动取消虚拟订单方法==============订单ID：{}", orderId);
        String lockKey = OrderRedisKeyUtils.getCancelVirtualOrderLockKey(orderId);
        Lock lock = LockProvider.getLock(lockKey);
        lock.lock();
        log.info("---------取消虚拟订单------进入虚拟订单锁【{}】，防止并发情况：虚拟订单过期回滚库存时，用户进行支付操作", orderId);
        try {
            virtualOrderTransactionService.cancelVirtualOrder(orderId);
            //String uncompletedVirtualOrderKey = OrderRedisKeyUtils.getUncompletedVirtualOrderKey(orderId);
            //RedisCache.delete(uncompletedVirtualOrderKey);
            delayQueueHelper.removeVirtual(orderId);
            return ResponseResult.buildSuccessResponse();
        } finally {
            lock.unlock();
            log.info("---------取消虚拟订单------离开虚拟订单锁【{}】，防止并发情况：虚拟订单过期回滚库存时，用户进行支付操作", orderId);
        }
    }


    @Override
    public ResponseResult<?> expiredCancelVirtualOrder(Long orderId) {
        String lockKey = OrderRedisKeyUtils.getCancelVirtualOrderLockKey(orderId);
        Lock lock = LockProvider.getLock(lockKey);
        lock.lock();
        log.info("---------过期取消虚拟订单------进入虚拟订单锁【{}】，防止并发情况：虚拟订单过期回滚库存时，用户进行支付操作", orderId);
        try {
            virtualOrderTransactionService.cancelVirtualOrder(orderId);
            return ResponseResult.buildSuccessResponse();
        } finally {
            lock.unlock();
            log.info("---------过期取消虚拟订单------离开虚拟订单锁【{}】，防止并发情况：虚拟订单过期回滚库存时，用户进行支付操作", orderId);
        }
    }

    @Override
    public ResponseResult<?> cancelUnPayVirtualOrder() {
        try {
            Integer unPayExpiredTime = SysConfigUtils.getUnPayExpiredTime();
            if (unPayExpiredTime == null) {
                unPayExpiredTime = 300;
            } else {
                unPayExpiredTime = unPayExpiredTime * 60;
            }
            List<VirtualOrder> virtualOrderList = virtualOrderMapper.getVirtualOrderByStatus(VirtualOrderStatus.UN_PAY.getValue(), unPayExpiredTime);
            for (VirtualOrder virtualOrder : virtualOrderList) {
                Date orderDate = virtualOrder.getOrderDate();
                Date currentDate = new Date();
                if (DateUtil.computeTime(orderDate, currentDate, unPayExpiredTime)) {
                    this.expiredCancelVirtualOrder(virtualOrder.getId());
                }
            }
            return ResponseResult.buildSuccessResponse();
        } catch (Exception e) {
            log.error("取消待生成虚拟订单异常", e);
            return ResponseResult.buildFailResponse();
        }
    }
}

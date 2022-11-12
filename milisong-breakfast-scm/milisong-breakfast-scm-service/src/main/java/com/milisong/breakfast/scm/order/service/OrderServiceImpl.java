package com.milisong.breakfast.scm.order.service;

import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.db.IdGenerator;
import com.farmland.core.distrib.LockProvider;
import com.farmland.core.util.BeanMapper;
import com.milisong.breakfast.scm.common.util.DateUtil;
import com.milisong.breakfast.scm.order.api.OrderService;
import com.milisong.breakfast.scm.order.constant.OrderStatusEnum;
import com.milisong.breakfast.scm.order.domain.*;
import com.milisong.breakfast.scm.order.dto.mq.OrderDetailDto;
import com.milisong.breakfast.scm.order.dto.mq.OrderRefundDto;
import com.milisong.breakfast.scm.order.dto.param.*;
import com.milisong.breakfast.scm.order.dto.result.OrderDetailComboDto;
import com.milisong.breakfast.scm.order.dto.result.OrderDetailResult;
import com.milisong.breakfast.scm.order.dto.result.OrderDto;
import com.milisong.breakfast.scm.order.dto.result.OrderReserveSearchResult;
import com.milisong.breakfast.scm.order.mapper.*;
import com.milisong.breakfast.scm.order.param.OrderGoodsCompanySqlParam;
import com.milisong.breakfast.scm.order.param.OrderReserveSearchSqlParam;
import com.milisong.dms.constant.OrderDeliveryStatus;
import com.milisong.scm.base.api.CompanyService;
import com.milisong.scm.base.api.ShopService;
import com.milisong.scm.base.dto.CompanyDto;
import com.milisong.scm.base.dto.ShopDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service("orderService")
public class OrderServiceImpl implements OrderService {
    @Autowired
    private ShopService shopService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderExtMapper orderExtMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private OrderDetailExtMapper orderDetailExtMapper;
    @Autowired
    private OrderDetailComboMapper orderDetailComboMapper;

    @Override
    public List<OrderDto> search4OrderSet(OrderSearch4OrderSetParam param) {
        OrderExample example = new OrderExample();
        OrderExample.Criteria criteria = example.createCriteria().andDeliveryCompanyIdEqualTo(param.getCompanyId())
                .andDeliveryStartDateEqualTo(param.getDeliveryDate())
                .andOrdersetProcessStatusEqualTo(Boolean.FALSE)
                .andOrderStatusEqualTo(OrderStatusEnum.WAITPACK.getValue().byteValue());
        if (StringUtils.isNotBlank(param.getDeliveryRoom())) {
            criteria.andDeliveryRoomEqualTo(param.getDeliveryRoom());
        } else {
            criteria.andDeliveryRoomIsNull();
        }

        example.setOrderByClause(" actual_pay_amount desc ");

        List<Order> list = orderMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(list)) {
            return BeanMapper.mapList(list, OrderDto.class);
        }
        return null;
    }

    @Override
    public List<OrderDetailComboDto> searchOrderDetailComboList(OrderDetailComboParam param) {
        OrderDetailComboExample example = new OrderDetailComboExample();
        example.createCriteria().andComboGoodsCodeEqualTo(param.getComboGoodsCode()).andOrderNoEqualTo(param.getOrderNo());
        List<OrderDetailCombo> list = orderDetailComboMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(list)) {
            return BeanMapper.mapList(list, OrderDetailComboDto.class);
        }
        return null;
    }

    @Override
    public List<com.milisong.breakfast.scm.order.dto.result.OrderDetailDto> getOrderDetailByOrderNo(String orderNo) {
        OrderDetailExample example = new OrderDetailExample();
        example.createCriteria().andOrderNoEqualTo(orderNo);
        example.setOrderByClause(" actual_pay_amount desc ");
        List<OrderDetail> list = orderDetailMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(list)) {
            return BeanMapper.mapList(list, com.milisong.breakfast.scm.order.dto.result.OrderDetailDto.class);
        }
        return null;
    }

    @Override
    @Transactional
    public void save(List<com.milisong.breakfast.scm.order.dto.mq.OrderDto> orderDtoList) {
        RLock lock = LockProvider.getLock("MQ_ORDER_" + orderDtoList.get(0).getOrderNo());
        try {
            lock.lock(60, TimeUnit.SECONDS);
            for (com.milisong.breakfast.scm.order.dto.mq.OrderDto orderDto : orderDtoList) {
                Order order = orderMapper.selectByPrimaryKey(orderDto.getId());
                if (null == order) {
                    order = BeanMapper.map(orderDto, Order.class);


                    ResponseResult<ShopDto> shopResp = shopService.queryByCode(orderDto.getShopCode());
                    if (null == shopResp || !shopResp.isSuccess() || null == shopResp.getData()) {
                        log.error("根据门店code：{}未能查询到门店信息", orderDto.getShopCode());
                        throw new RuntimeException("根据门店code：" + orderDto.getShopCode() + "未能查询到门店信息");
                    }
                    ShopDto shop = shopResp.getData();
                    order.setShopId(shop.getId());

                    if (StringUtils.isEmpty(order.getDeliveryAddress())) {
                        ResponseResult<CompanyDto> resp = companyService.querySimpleById(order.getDeliveryCompanyId());
                        if (!resp.isSuccess()) {
                            throw new RuntimeException("根据公司id：" + order.getDeliveryCompanyId() + "未查询公司信息");
                        }
                        order.setDeliveryAddress(resp.getData().getDetailAddress().concat(resp.getData().getFloor()).concat("楼").concat(resp.getData().getName()));
                    }

                    if (!CollectionUtils.isEmpty(orderDto.getOrderDetails())) {
                        order.setGoodsSum(processOrderDetail(orderDto.getOrderDetails(), order));
                        order.setSkuSum(calculationOrderSkuSum(orderDto.getOrderDetails()));
                        orderMapper.insertSelective(order);
                    }
                }
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int getOrderGoodsCount(OrderGoodsSumParam param) {
        log.info("统计商品已售总数量参数: {}", JSONObject.toJSONString(param));
        OrderGoodsCompanySqlParam paramSql = new OrderGoodsCompanySqlParam();
        paramSql.setShopId(param.getShopId());
        paramSql.setGoodsCode(param.getGoodsCode());
        paramSql.setBeginDeliveryDate(DateUtil.getDayBeginTime(param.getDeliveryDate()));
        paramSql.setEndDeliveryDate(DateUtil.getDayBeginTime(param.getDeliveryDate(), 1));
        Integer result = orderExtMapper.selectOrderGoodsCountCompany(paramSql);
        if (result == null) {
            return 0;
        }
        log.info("统计商品已售总数量：{}", result);
        return result;
    }

    @Override
    public void updateStatus(OrderUpdateStatusParam param) {
        OrderExample example = new OrderExample();
        example.createCriteria().andOrderNoEqualTo(param.getOrderNo());
        List<Order> list = orderMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(list)) {
            Order order = list.get(0);
            order.setOrderStatus(param.getOrderStatus());
            order.setDeliveryStatus(param.getDeliveryStatus());
            order.setUpdateTime(null);

            if (OrderDeliveryStatus.COMPLETED.getValue().equals(param.getDeliveryStatus())) {
                order.setCompleteTime(new Date());
            }

            orderMapper.updateByPrimaryKeySelective(order);
        }
    }

    @Override
    public List<OrderDetailResult> getOrderDetailInfoByOrderNo(List<String> orderNos) {
        OrderDetailExample example = new OrderDetailExample();
        example.createCriteria().andOrderNoIn(orderNos);
        List<OrderDetail> list = orderDetailMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(list)) {
            return BeanMapper.mapList(list, OrderDetailResult.class);
        }
        return null;
    }

    @Override
    public void refund(List<OrderRefundDto> list) {
        Order record = new Order();
        record.setOrderStatus(OrderStatusEnum.REFUND.getValue().byteValue());

        List<String> orderNoList = list.stream().map(item -> item.getDeliveryNo()).collect(Collectors.toList());

        OrderExample example = new OrderExample();
        example.createCriteria().andOrderNoIn(orderNoList);

        orderMapper.updateByExampleSelective(record, example);
    }

    @Override
    public Pagination<OrderReserveSearchResult> pageSearchReserveOrder(OrderReserveSearchParamDto param) {
        log.info("分页查询门店商品预定总量，参数：{}", JSONObject.toJSONString(param));
        Pagination<OrderReserveSearchResult> result = new Pagination<>();
        result.setPageNo(param.getPageNo());
        result.setPageSize(param.getPageSize());

        OrderReserveSearchSqlParam sqlParam = new OrderReserveSearchSqlParam();
        if (param.getShopId() != null) {
            sqlParam.setShopId(param.getShopId());
        }

        if (StringUtils.isNotBlank(param.getDeliveryDate())) {
            sqlParam.setDeliveryBeginDate(DateUtil.parseDate(param.getDeliveryDate(), DateUtil.YYYY_MM_DD));
            sqlParam.setDeliveryEndDate(
                    DateUtil.getDayBeginTime(DateUtil.parseDate(param.getDeliveryDate(), DateUtil.YYYY_MM_DD), 1));
        }
        if (StringUtils.isNotBlank(param.getReserveDate())) {
            sqlParam.setReserveBeginDate(
                    DateUtil.getDayBeginTime(DateUtil.parseDate(param.getReserveDate(), DateUtil.YYYY_MM_DD)));
            sqlParam.setReserveEndDate(
                    DateUtil.getDayBeginTime(DateUtil.parseDate(param.getReserveDate(), DateUtil.YYYY_MM_DD), 1));
        }

        int count = orderDetailExtMapper.getReserveOrderGroupCount(sqlParam);
        result.setTotalCount(count);
        if (count > 0) {
            sqlParam.setPageNo(param.getPageNo());
            sqlParam.setPageSize(param.getPageSize());

            result.setDataList(BeanMapper.mapList(orderDetailExtMapper.getReserveOrderGroupList(sqlParam),
                    OrderReserveSearchResult.class));
            ;
        } else {
            result.setDataList(Collections.emptyList());
        }
        return result;
    }

    /**
     * 更新订单的集单处理状态
     *
     * @param orderNoList
     */
    @Override
    public void updateOrderProcessStatus(List<String> orderNoList) {
        OrderExample example = new OrderExample();
        example.createCriteria().andOrderNoIn(orderNoList);

        Order order = new Order();
        order.setOrdersetProcessStatus(Boolean.TRUE);
        orderMapper.updateByExampleSelective(order, example);
    }

    @Override
    public List<String> searchCompanyMealAddress(Long companyId) {
        return orderExtMapper.selectMealAddressByCompany(companyId);
    }

    @Override
    public List<String> getTodayAllOrderNo() {
        OrderExample example = new OrderExample();
        example.createCriteria().andDeliveryStartDateGreaterThanOrEqualTo(DateUtil.getDayBeginTime(new Date()))
                .andDeliveryStartDateLessThan(DateUtil.getDayBeginTime(new Date(), 1))
                .andOrderStatusEqualTo(OrderStatusEnum.WAITPACK.getValue().byteValue());

        List<Order> orderList = orderMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(orderList)) {
            return orderList.stream().map(item -> item.getOrderNo()).collect(Collectors.toList());
        }
        return null;
    }


    /**
     * 处理订单商品数据
     *
     * @param orderDetails
     * @param order
     * @return
     */
    private int processOrderDetail(List<OrderDetailDto> orderDetails, Order order) {
        // 商品数量
        int goodsSum = 0;
        for (OrderDetailDto item : orderDetails) {
            goodsSum += item.getGoodsCount();
        }
        // 超过一份才需要做分摊
        if (goodsSum > 1) {
            processManyGoods(orderDetails, order, goodsSum);
        } else {
            for (OrderDetailDto item : orderDetails) {
                OrderDetail detail = BeanMapper.map(item, OrderDetail.class);
                detail.setOrderNo(order.getOrderNo());
                detail.setActualPayAmount(order.getActualPayAmount());
                detail.setDeliveryCostAmount(order.getDeliveryCostAmount());
                detail.setPackageAmount(order.getPackageAmount());
                detail.setRedPacketAmount(order.getRedPacketAmount());
                detail.setCreateTime(null);
                detail.setUpdateTime(null);
                orderDetailMapper.insertSelective(detail);
            }
        }
        // 处理组合商品
        orderDetails.stream().forEach(item -> {
            if (Boolean.TRUE.equals(item.getIsCombo())) {
                item.getComboDetails().stream().forEach(combo -> {
                    OrderDetailCombo record = new OrderDetailCombo();
                    record.setId(IdGenerator.nextId());
                    record.setOrderId(item.getOrderId());
                    record.setOrderNo(order.getOrderNo());
                    record.setGoodsCode(combo.getGoodsCode());
                    record.setGoodsName(combo.getGoodsName());
                    record.setGoodsCount(combo.getGoodsCount());
                    record.setComboGoodsCode(item.getGoodsCode());
                    record.setComboGoodsName(item.getGoodsName());
                    record.setComboGoodsCount(item.getGoodsCount());
                    orderDetailComboMapper.insertSelective(record);
                });
            }
        });
        return goodsSum;
    }

    /**
     * 计算整个订单所有单品的sku的总数
     *
     * @param orderDetails
     * @return
     */
    private int calculationOrderSkuSum(List<OrderDetailDto> orderDetails) {
        int count = 0;
        for (OrderDetailDto item : orderDetails) {
            if (Boolean.TRUE.equals(item.getIsCombo())) {
                for (OrderDetailDto combo : item.getComboDetails()) {
                    count += combo.getGoodsCount();
                }
            } else {
                count += item.getGoodsCount();
            }
        }
        return count;
    }

    /**
     * 订单有多个商品
     *
     * @param orderDetails
     * @param order
     * @param goodsSum
     */
    private void processManyGoods(List<OrderDetailDto> orderDetails, Order order, int goodsSum) {

        // 商品种类数量
        int goodsType = orderDetails.size();
        BigDecimal gs = new BigDecimal(goodsSum);

        // 转换成数据库的对象
        List<OrderDetail> details = new ArrayList<>();
        for (OrderDetailDto item : orderDetails) {
            OrderDetail detail = BeanMapper.map(item, OrderDetail.class);
            detail.setOrderNo(order.getOrderNo());
            detail.setCreateTime(null);
            detail.setUpdateTime(null);
            details.add(detail);
        }

        // 实付金额分摊
        if (null != order.getActualPayAmount()) {
            // 只有一种商品则直接平摊
            if (goodsType == 1) {
                details.get(0).setActualPayAmount(order.getActualPayAmount().divide(gs, 2, RoundingMode.HALF_UP));
            } else {
                BigDecimal now = new BigDecimal(0);
                for (int i = 0; i < goodsType; i++) {
                    OrderDetail item = details.get(i);
                    // 最后一个计算结余
                    if (i == goodsType - 1) {
                        // 剩余实付金额
                        BigDecimal goodsAct = order.getActualPayAmount().subtract(now);
                        item.setActualPayAmount(goodsAct.divide(new BigDecimal(item.getGoodsCount()), 2, RoundingMode.HALF_UP));
                        if (item.getActualPayAmount().compareTo(BigDecimal.ZERO) < 0) {
                            item.setActualPayAmount(BigDecimal.ZERO);
                        }
                    } else {
                        // 单个商品的实付金额
                        BigDecimal goodsAct = item.getGoodsDiscountPrice().divide(order.getDiscountAmount(), 3, RoundingMode.HALF_UP).multiply(order.getActualPayAmount()).setScale(2, RoundingMode.HALF_UP);
                        ;
                        item.setActualPayAmount(goodsAct);
                        // 将单个实付金额*商品数量
                        now = now.add(goodsAct.multiply(new BigDecimal(item.getGoodsCount())));
                    }
                }
            }
        }

        // 打包费
        if (null != order.getPackageAmount()) {
            // 每一份的打包费
            details.forEach(item -> {
                item.setPackageAmount(order.getPackageAmount().divide(gs, 2, RoundingMode.HALF_UP));
            });
        }
        // 配送费
        if (null != order.getDeliveryCostAmount()) {
            // 每一份的配送费
            details.forEach(item -> {
                item.setDeliveryCostAmount(order.getDeliveryCostAmount().divide(gs, 2, RoundingMode.HALF_UP));
            });
        }
        // 红包
        if (null != order.getRedPacketAmount()) {
            // 只有一种商品则直接平摊
            if (goodsType == 1) {
                details.get(0).setRedPacketAmount(order.getRedPacketAmount().divide(gs, 2, RoundingMode.HALF_UP));
            } else {
                BigDecimal now = new BigDecimal(0);
                for (int i = 0; i < goodsType; i++) {
                    OrderDetail item = details.get(i);
                    // 最后一个计算结余
                    if (i == goodsType - 1) {
                        // 剩余红包金额
                        BigDecimal goodsRed = order.getRedPacketAmount().subtract(now);
                        item.setRedPacketAmount(goodsRed.divide(new BigDecimal(item.getGoodsCount()), 2, RoundingMode.HALF_UP));
                        if (item.getRedPacketAmount().compareTo(BigDecimal.ZERO) < 0) {
                            item.setRedPacketAmount(BigDecimal.ZERO);
                        }
                    } else {
                        // 单个商品的红包金额
                        BigDecimal goodsRed = item.getGoodsDiscountPrice().divide(order.getDiscountAmount(), 3, RoundingMode.HALF_UP).multiply(order.getRedPacketAmount()).setScale(2, RoundingMode.HALF_UP);
                        ;
                        item.setRedPacketAmount(goodsRed);
                        // 将单个红包金额*商品数量
                        now = now.add(goodsRed.multiply(new BigDecimal(item.getGoodsCount())));
                    }
                }
            }
        }

        details.stream().forEach(item -> {
            orderDetailMapper.insertSelective(item);
        });
    }

}

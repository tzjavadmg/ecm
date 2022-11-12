package com.milisong.scm.orderset.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.distrib.LockProvider;
import com.farmland.core.util.BeanMapper;
import com.milisong.scm.base.api.CompanyService;
import com.milisong.scm.base.api.ShopService;
import com.milisong.scm.base.dto.ShopBossDto;
import com.milisong.scm.base.dto.ShopDto;
import com.milisong.scm.orderset.api.OrderService;
import com.milisong.scm.orderset.constant.LogisticsDeliveryStatus;
import com.milisong.scm.orderset.constant.OrderStatusEnum;
import com.milisong.scm.orderset.domain.Order;
import com.milisong.scm.orderset.domain.OrderDetail;
import com.milisong.scm.orderset.domain.OrderDetailExample;
import com.milisong.scm.orderset.domain.OrderExample;
import com.milisong.scm.orderset.dto.mq.OrderDetailDto;
import com.milisong.scm.orderset.dto.mq.OrderRefundDto;
import com.milisong.scm.orderset.dto.param.OrderGoodsSumParam;
import com.milisong.scm.orderset.dto.param.OrderReserveSearchParamDto;
import com.milisong.scm.orderset.dto.param.OrderSearch4OrderSetParam;
import com.milisong.scm.orderset.dto.param.OrderSearchParam;
import com.milisong.scm.orderset.dto.param.OrderUpdateStatusParam;
import com.milisong.scm.orderset.dto.result.OrderCompanyResult;
import com.milisong.scm.orderset.dto.result.OrderDetailResult;
import com.milisong.scm.orderset.dto.result.OrderExportResult;
import com.milisong.scm.orderset.dto.result.OrderGoodsResult;
import com.milisong.scm.orderset.dto.result.OrderReserveSearchResultDto;
import com.milisong.scm.orderset.dto.result.OrderResult;
import com.milisong.scm.orderset.dto.result.OrderSearchResult;
import com.milisong.scm.orderset.mapper.OrderDetailExtMapper;
import com.milisong.scm.orderset.mapper.OrderDetailMapper;
import com.milisong.scm.orderset.mapper.OrderExtMapper;
import com.milisong.scm.orderset.mapper.OrderMapper;
import com.milisong.scm.orderset.param.OrderCompanySqlParam;
import com.milisong.scm.orderset.param.OrderDetailSearch4OrderSetSqlParam;
import com.milisong.scm.orderset.param.OrderGoodsCompanySqlParam;
import com.milisong.scm.orderset.param.OrderReserveSearchSqlParam;
import com.milisong.scm.orderset.result.OrderCompanySqlResult;
import com.milisong.scm.orderset.result.OrderDetailSqlResult;
import com.milisong.scm.orderset.util.DateUtil;
import com.milisong.scm.shop.api.BuildingService;
import com.milisong.scm.shop.dto.building.BuildingDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("orderService")
public class OrderServiceImpl implements OrderService {
    @Autowired
    private ShopService shopService;
    @Autowired(required = false)
    private BuildingService buildingService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderExtMapper orderExtMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private OrderDetailExtMapper orderDetailExtMapper;
    
    @Autowired
    private CompanyService companyService;

    @Value("${ecm.shoponsale.orderList-url}")
    private String orderListUrl;

    @Override
    public Pagination<OrderCompanyResult> pageSearchExistsOrderCompany(OrderSearch4OrderSetParam param) {
        Pagination<OrderCompanyResult> result = new Pagination<>();
        result.setPageNo(param.getPageNo());
        result.setPageSize(param.getPageSize());

        OrderCompanySqlParam queryParam = new OrderCompanySqlParam();
        queryParam.setDeliveryOfficeBuildingId(param.getDeliveryOfficeBuildingId());
        queryParam.setPageSize(param.getPageSize());
        queryParam.setStartRow(param.getStartRow());
        queryParam.setBeginDeliveryDate(param.getDeliveryDate());

        long count = orderExtMapper.countCompany(queryParam);
        result.setTotalCount(count);
        if (count > 0) {
            List<OrderCompanySqlResult> list = orderExtMapper.selectOrderCompany(queryParam);
            if (!CollectionUtils.isEmpty(list)) {
                result.setDataList(BeanMapper.mapList(list, OrderCompanyResult.class));
            }
        }
        return result;
    }

    /**
     * 根据公司信息查询详细的最新的配送地址
     *
     * @param param
     * @return
     */
    @Override
    public String queryDeliveryAddressByCompanyInfo(OrderSearch4OrderSetParam param) {
        OrderCompanySqlParam queryParam = new OrderCompanySqlParam();
        queryParam.setDeliveryOfficeBuildingId(param.getDeliveryOfficeBuildingId());
        queryParam.setDeliveryCompany(param.getDeliveryCompany());
        queryParam.setBeginDeliveryDate(param.getDeliveryDate());
        queryParam.setDeliveryFloor(param.getDeliveryFloor());

        return orderExtMapper.selectLastDeliveryAddressByCompanyInfo(queryParam);
    }

    @Override
    public List<OrderDetailResult> search4OrderSet(OrderSearch4OrderSetParam param) {
        OrderDetailSearch4OrderSetSqlParam queryParam = new OrderDetailSearch4OrderSetSqlParam();
        queryParam.setDeliveryOfficeBuildingId(param.getDeliveryOfficeBuildingId());
        queryParam.setBeginDeliveryDate(param.getDeliveryDate());
        queryParam.setDeliveryCompany(param.getDeliveryCompany());
        if (null != param.getDeliveryFloor()) {
            queryParam.setDeliveryFloor(param.getDeliveryFloor());
        }
        List<OrderDetailSqlResult> list = orderDetailExtMapper.getCompanyOrderDetail(queryParam);
        if (!CollectionUtils.isEmpty(list)) {
            return BeanMapper.mapList(list, OrderDetailResult.class);
        }
        return null;
    }

    @Override
    @Transactional
    public void save(List<com.milisong.scm.orderset.dto.mq.OrderDto> orderDtoList) {
        RLock lock = LockProvider.getLock("MQ_ORDER_" + orderDtoList.get(0).getOrderNo());
        try {
            lock.lock(60, TimeUnit.SECONDS);
            for (com.milisong.scm.orderset.dto.mq.OrderDto orderDto : orderDtoList) {
                Order order = orderMapper.selectByPrimaryKey(orderDto.getId());
                if (null == order) {
                    order = BeanMapper.map(orderDto, Order.class);
                    order.setDeliveryCompanyId(orderDto.getDeliveryOfficeBuildingId());
                    order.setDeliveryOfficeBuildingId(null);
                    if (StringUtils.isEmpty(order.getDeliveryAddress())) {
                        ResponseResult<com.milisong.scm.base.dto.CompanyDto> resp = companyService.querySimpleById(order.getDeliveryCompanyId());
                        if (!resp.isSuccess()) {
                            throw new RuntimeException("根据公司id：" + order.getDeliveryCompanyId() + "未查询公司信息");
                        }
                        order.setDeliveryAddress(resp.getData().getDetailAddress().concat(resp.getData().getFloor()).concat("楼").concat(resp.getData().getName()));
                    }
                    
                    ResponseResult<ShopDto> shopResp = shopService.queryByCode(orderDto.getShopCode());
                	if(null == shopResp || !shopResp.isSuccess() || null==shopResp.getData()) {
                		log.error("根据门店code：{}未能查询到门店信息", orderDto.getShopCode());
                        throw new RuntimeException("根据门店code：" + orderDto.getShopCode() + "未能查询到门店信息");
                	}
                	ShopDto shop = shopResp.getData();
                    order.setShopId(shop.getId());

                    if (!CollectionUtils.isEmpty(orderDto.getOrderDetails())) {
                        int goodsSum = processOrderDetail(orderDto.getOrderDetails(), order);
                        order.setGoodsSum(goodsSum);
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
    public OrderResult getOrderInfoByOrderNo(String orderNo) {
        OrderExample example = new OrderExample();
        example.createCriteria().andOrderNoEqualTo(orderNo);
        List<Order> list = orderMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(list)) {
            return BeanMapper.map(list.get(0), OrderResult.class);
        }
        return null;
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
    public Pagination<OrderReserveSearchResultDto> pageSearchReserveOrder(OrderReserveSearchParamDto param) {
        log.info("分页查询门店商品预定总量，参数：{}", JSONObject.toJSONString(param));
        Pagination<OrderReserveSearchResultDto> result = new Pagination<>();
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
                    OrderReserveSearchResultDto.class));
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
    public ResponseResult<Pagination<OrderSearchResult>> pageSearchOrder(OrderSearchParam param) {
        log.info("分页查询订单详情，接收参数:{}", JSONObject.toJSONString(param));
        List<OrderSearchResult> resultList = listSearchOrder(param);
        Long count = orderExtMapper.countOrderByPage(param);
        Pagination<OrderSearchResult> pagination = new Pagination<>();
        pagination.setTotalCount(count);
        pagination.setDataList(resultList);
        pagination.setPageNo(pagination.getPageNo());
        pagination.setPageSize(pagination.getPageSize());
        return ResponseResult.buildSuccessResponse(pagination);
    }

    @Override
    public List<OrderExportResult> exportOrder(OrderSearchParam param) {
        List<OrderSearchResult> resultList = listSearchOrder(param);
        List<OrderExportResult> exportResults = BeanMapper.mapList(resultList, OrderExportResult.class);
        exportResults.stream().forEach(export -> {
            List<OrderGoodsResult> goodsResultList = export.getGoodsResultList();
            String goodsInfo = "";
            if (goodsResultList.size() > 0) {
                StringBuffer sbuffer = new StringBuffer();
                goodsResultList.stream().forEach(goods -> {
                    sbuffer.append(goods.getGoodsName().concat("*").concat(String.valueOf(goods.getGoodsNumber())));
                    sbuffer.append(",");
                });
                int goodsTotalNumber = goodsResultList.stream().mapToInt(OrderGoodsResult::getGoodsNumber).sum();
                export.setGoodsTotalNumber(goodsTotalNumber);

                sbuffer.deleteCharAt(sbuffer.lastIndexOf(","));
                goodsInfo = sbuffer.toString();
            }
            export.setGoodsInfo(goodsInfo);
        });
        return exportResults;
    }
    
	@Override
	public List<String> getTodayAllOrderNo() {
		OrderExample example = new OrderExample();
		example.createCriteria().andDeliveryStartDateGreaterThanOrEqualTo(DateUtil.getDayBeginTime(new Date()))
			.andDeliveryStartDateLessThan(DateUtil.getDayBeginTime(new Date(), 1))
			.andOrderStatusEqualTo(OrderStatusEnum.WAITPACK.getValue().byteValue());
		
		List<Order> orderList = orderMapper.selectByExample(example);
		if(!CollectionUtils.isEmpty(orderList)) {
			return orderList.stream().map(item -> item.getOrderNo()).collect(Collectors.toList());
		}
		return null;
	}

    private List<OrderSearchResult> listSearchOrder(OrderSearchParam param) {
        List<OrderSearchResult> resultList = orderExtMapper.selectOrderByPage(param);
        if (resultList == null || resultList.isEmpty()) {
            return new ArrayList<>();
        }
        ResponseResult<List<ShopBossDto>> shopResp = shopService.getAllShopList();
        if (null == shopResp || !shopResp.isSuccess()) {
            log.warn("调用门店查询结果失败：{}", JSONObject.toJSONString(shopResp));
            throw new RuntimeException("查询所有门店信息失败");
        }
        List<ShopBossDto> shoplist = shopResp.getData();
        List<String> orderNoList = resultList.stream().map(OrderSearchResult::getOrderNo).collect(Collectors.toList());
        OrderDetailExample example = new OrderDetailExample();
        example.createCriteria().andOrderNoIn(orderNoList);
        List<OrderDetail> orderDetails = orderDetailMapper.selectByExample(example);
        List<OrderGoodsResult> orderGoodsResults = orderDetails.stream().map(orderSetGoods -> {
            OrderGoodsResult orderGoodsResult = new OrderGoodsResult();
            orderGoodsResult.setGoodsName(orderSetGoods.getGoodsName());
            orderGoodsResult.setGoodsNumber(orderSetGoods.getGoodsCount());
            orderGoodsResult.setOrderNo(orderSetGoods.getOrderNo());
            return orderGoodsResult;
        }).collect(Collectors.toList());

        resultList.stream().forEach(order -> {
            List<OrderGoodsResult> orderGoodsCollect = orderGoodsResults.stream().filter(orderGoods -> orderGoods.getOrderNo().equals(order.getOrderNo())).collect(Collectors.toList());
            order.setDeliveryStatusStr(LogisticsDeliveryStatus.getNameByValue(order.getDeliveryStatus()));
            shoplist.stream().forEach(shop -> {
                if (order.getShopId().equals(shop.getId())) {
                    order.setShopName(shop.getName());
                }
            });
            order.setGoodsResultList(orderGoodsCollect);
        });
        return resultList;
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
        return goodsSum;
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

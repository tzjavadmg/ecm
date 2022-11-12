package com.mili.oss.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.farmland.core.db.IdGenerator;
import com.farmland.core.distrib.LockProvider;
import com.farmland.core.util.BeanMapper;
import com.mili.oss.algorithm.SortingAlgorithmExecutor;
import com.mili.oss.algorithm.model.GoodsModel;
import com.mili.oss.algorithm.model.GoodsPackModel;
import com.mili.oss.api.OrderService;
import com.mili.oss.constant.*;
import com.mili.oss.domain.*;
import com.mili.oss.domain.OrderSetExample.Criteria;
import com.mili.oss.dto.*;
import com.mili.oss.dto.config.InterceptConfigDto;
import com.mili.oss.dto.config.ShopInterceptConfigDto;
import com.mili.oss.dto.config.ShopSingleConfigDto;
import com.mili.oss.dto.orderstatus.DeliveryOrderMqDto;
import com.mili.oss.dto.orderstatus.UpdateOrderSetStatusParam;
import com.mili.oss.dto.param.OrderUpdateStatusParam;
import com.mili.oss.dto.result.OrderSetDetailStatusDto;
import com.mili.oss.mapper.*;
import com.mili.oss.mq.OrderSetOrderStatusChangeMsg;
import com.mili.oss.mq.OrderSetProductionLunchMsg;
import com.mili.oss.mq.OrderSetProductionMsg;
import com.mili.oss.mq.message.*;
import com.mili.oss.properties.ScmBFResdisProperties;
import com.mili.oss.util.DateUtil;
import com.mili.oss.util.MqProducerUtil;
import com.mili.oss.util.RedisConfigUtil;
import com.mili.oss.util.ScmBFRedisHttpUtils;
import com.milisong.scm.base.api.CompanyService;
import com.milisong.scm.base.api.ShopService;
import com.milisong.scm.base.constant.OperationTypeEnum;
import com.milisong.scm.base.dto.CompanyMealAddressDto;
import com.milisong.scm.base.dto.CompanyPolymerizationDto;
import com.milisong.scm.base.dto.ShopDto;
import com.milisong.scm.base.dto.mq.OperationLogDto;
import com.milisong.scm.base.utils.CompareDifferenceUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.assertj.core.util.Lists;
import org.redisson.api.RLock;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * 订单转集单处理
 *
 * @author benny
 */
@Slf4j
@Service("orderService")
@RestController
public class OrderServiceImpl implements OrderService {
    @Autowired(required = true)
    private ShopService shopService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderGoodsMapper orderGoodsMapper;
    @Autowired
    private OrderSetMapper orderSetMapper;
    @Autowired
    private OrderSetGoodsMapper orderSetGoodsMapper;
    @Autowired
    private CompanyService companyService;

    @Autowired
    private ReprintServiceImpl reprintServiceImpl;
    @Resource
    private OrderToOrderSetMapper orderToOrderSetMapper;
    @Autowired
    private OrderGoodsComboMapper orderGoodsComboMapper;


    // 集单最大金额
    private static final String ORDERSET_MAX_AMOUNT = "ordersetMaxAmount";
    // 集单的最大人数
    private static final String ORDERSET_MAX_MEMBER = "ordersetMaxMember";
    // 集单最小金额
    private static final String ORDERSET_MIN_AMOUNT = "ordersetMinAmount";

    @Autowired
    private ScmBFResdisProperties scmBFResdisProperties;

    @Autowired
    ScmBFRedisHttpUtils scmBFRedisHttpUtils;

    final String SHOP_SINGLE_CONFIG_KEY = "config:shop_single_config:scm:%s";

    @Override
    @Transactional
    public void save(List<OrderDto> orderDtoList) {
        log.info("开始保存订单明细:{}", JSON.toJSONString(orderDtoList));
        RLock lock = LockProvider.getLock("MQ_ORDER_" + orderDtoList.get(0).getOrderNo());
        try {
            lock.lock(60, TimeUnit.SECONDS);
            for (OrderDto orderDto : orderDtoList) {
                Order order = orderMapper.selectByPrimaryKey(orderDto.getId());
                if (null == order) {
                    order = BeanMapper.map(orderDto, Order.class);
                    order.setDeliveryOfficeBuildingId(null);
                    ResponseResult<ShopDto> shop = shopService.queryByCode(orderDto.getShopCode());
                    if (null == shop) {
                        log.error("根据门店code：{}未能查询到门店信息", orderDto.getShopCode());
                        throw new RuntimeException("根据门店code：" + orderDto.getShopCode() + "未能查询到门店信息");
                    }
                    if (StringUtils.isEmpty(order.getDeliveryAddress())) {
                        ResponseResult<com.milisong.scm.base.dto.CompanyDto> resp = companyService.querySimpleById(order.getDeliveryCompanyId());
                        if (!resp.isSuccess()) {
                            throw new RuntimeException("根据公司id：" + order.getDeliveryCompanyId() + "未查询公司信息");
                        }
                        order.setDeliveryAddress(resp.getData().getDetailAddress().concat(resp.getData().getFloor()).concat("楼").concat(resp.getData().getName()));
                    }
                    order.setShopId(shop.getData().getId());

                    if (!CollectionUtils.isEmpty(orderDto.getOrderGoods())) {
                        int goodsSum = processOrderDetail(orderDto.getOrderGoods(), order);
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
    public void refund(List<OrderRefundDto> list) {
        Order record = new Order();
        record.setOrderStatus(OrderStatusEnum.REFUND.getValue().byteValue());

        List<String> orderNoList = list.stream().map(item -> item.getDeliveryNo()).collect(Collectors.toList());

        OrderExample example = new OrderExample();
        example.createCriteria().andOrderNoIn(orderNoList);

        orderMapper.updateByExampleSelective(record, example);
    }


    /**
     * 处理订单商品数据
     *
     * @param orderDetails
     * @param order
     * @return
     */
    private int processOrderDetail(List<OrderGoodsDto> orderDetails, Order order) {
        // 商品数量
        int goodsSum = 0;
        for (OrderGoodsDto item : orderDetails) {
            goodsSum += item.getGoodsCount();
        }
        // 超过一份才需要做分摊
        if (goodsSum > 1) {
            processManyGoods(orderDetails, order, goodsSum);
        } else {
            List<OrderGoods> list = new ArrayList<OrderGoods>();
            for (OrderGoodsDto item : orderDetails) {
                OrderGoods detail = BeanMapper.map(item, OrderGoods.class);
                detail.setOrderNo(order.getOrderNo());
                detail.setActualPayAmount(order.getActualPayAmount().multiply(new BigDecimal(detail.getGoodsCount())));
                detail.setDeliveryCostAmount(order.getDeliveryCostAmount());
                detail.setPackageAmount(order.getPackageAmount());
                detail.setRedPacketAmount(order.getRedPacketAmount());
                list.add(detail);
            }
            orderGoodsMapper.insertBatchSelective(list);
        }

        // 处理组合商品
        orderDetails.stream().forEach(item -> {
            if (Boolean.TRUE.equals(item.getIsCombo())) {
                item.getComboDetails().stream().forEach(combo -> {
                    OrderGoodsCombo record = new OrderGoodsCombo();
                    record.setId(IdGenerator.nextId());
                    record.setOrderId(item.getOrderId());
                    record.setOrderNo(order.getOrderNo());
                    record.setGoodsCode(combo.getGoodsCode());
                    record.setGoodsName(combo.getGoodsName());
                    record.setGoodsCount(combo.getGoodsCount());
                    record.setComboGoodsCode(item.getGoodsCode());
                    record.setComboGoodsName(item.getGoodsName());
                    record.setComboGoodsCount(item.getGoodsCount());
                    orderGoodsComboMapper.insertSelective(record);
                });
            }
        });
        return goodsSum;
    }

    /**
     * 订单有多个商品
     *
     * @param orderDetails
     * @param order
     * @param goodsSum
     */
    private void processManyGoods(List<OrderGoodsDto> orderDetails, Order order, int goodsSum) {

        // 商品种类数量
        int goodsType = orderDetails.size();
        BigDecimal gs = new BigDecimal(goodsSum);

        // 转换成数据库的对象
        List<OrderGoods> details = new ArrayList<>();
        for (OrderGoodsDto item : orderDetails) {
            OrderGoods detail = BeanMapper.map(item, OrderGoods.class);
            detail.setOrderNo(order.getOrderNo());
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
                    OrderGoods item = details.get(i);
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

//        // 打包费
//        if (null != order.getPackageAmount()) {
//            // 每一份的打包费
//            details.forEach(item -> {
//                item.setPackageAmount(order.getPackageAmount().divide(gs, 2, RoundingMode.HALF_UP));
//            });
//        }
//        // 配送费
//        if (null != order.getDeliveryCostAmount()) {
//            // 每一份的配送费
//            details.forEach(item -> {
//                item.setDeliveryCostAmount(order.getDeliveryCostAmount().divide(gs, 2, RoundingMode.HALF_UP));
//            });
//        }
//        // 红包
//        if (null != order.getRedPacketAmount()) {
//            // 只有一种商品则直接平摊
//            if (goodsType == 1) {
//                details.get(0).setRedPacketAmount(order.getRedPacketAmount().divide(gs, 2, RoundingMode.HALF_UP));
//            } else {
//                BigDecimal now = new BigDecimal(0);
//                for (int i = 0; i < goodsType; i++) {
//                    OrderGoods item = details.get(i);
//                    // 最后一个计算结余
//                    if (i == goodsType - 1) {
//                        // 剩余红包金额
//                        BigDecimal goodsRed = order.getRedPacketAmount().subtract(now);
//                        item.setRedPacketAmount(goodsRed.divide(new BigDecimal(item.getGoodsCount()), 2, RoundingMode.HALF_UP));
//                        if (item.getRedPacketAmount().compareTo(BigDecimal.ZERO) < 0) {
//                            item.setRedPacketAmount(BigDecimal.ZERO);
//                        }
//                    } else {
//                        // 单个商品的红包金额
//                        BigDecimal goodsRed = item.getGoodsDiscountPrice().divide(order.getDiscountAmount(), 3, RoundingMode.HALF_UP).multiply(order.getRedPacketAmount()).setScale(2, RoundingMode.HALF_UP);
//                        ;
//                        item.setRedPacketAmount(goodsRed);
//                        // 将单个红包金额*商品数量
//                        now = now.add(goodsRed.multiply(new BigDecimal(item.getGoodsCount())));
//                    }
//                }
//            }
//        }

        details.stream().forEach(item -> {
            item.setActualPayAmount(item.getActualPayAmount().multiply(new BigDecimal(item.getGoodsCount())));
            //
        });
        orderGoodsMapper.insertBatchSelective(details);
    }

    /**
     * 获取 最大金额
     *
     * @param shopId
     * @param orderType
     * @return
     */
    private String getMaxAmount(Long shopId, byte orderType) {
        String cacheOrdersetMaxAmount = "";
        if (OrderTypeEnum.LUNCH.getCode().equals(orderType)) {
            cacheOrdersetMaxAmount = RedisConfigUtil.getShopSingleConfigByKey(shopId, ORDERSET_MAX_AMOUNT);
            if (StringUtils.isBlank(cacheOrdersetMaxAmount)) {
                throw new RuntimeException("出现异常");
            }
        } else {
            String result = scmBFRedisHttpUtils.redisGet(scmBFResdisProperties.getScmBfRedisUrl(), String.format(SHOP_SINGLE_CONFIG_KEY, shopId.toString()), ORDERSET_MAX_AMOUNT).toString();
            ShopSingleConfigDto dto = JSON.parseObject(result, ShopSingleConfigDto.class);
            cacheOrdersetMaxAmount = dto.getConfigValue();
        }
        return cacheOrdersetMaxAmount;
    }

    /**
     * 获取 最大金额
     *
     * @param shopId
     * @param orderType
     * @return
     */
    private String getMinAmount(Long shopId, byte orderType) {
        String cacheOrdersetMinAmount = "0";
        if (OrderTypeEnum.LUNCH.getCode().equals(orderType)) {
            cacheOrdersetMinAmount = RedisConfigUtil.getShopSingleConfigByKey(shopId, ORDERSET_MIN_AMOUNT);
            if (StringUtils.isBlank(cacheOrdersetMinAmount)) {
                log.error("没有获取到最小集单金额{}", cacheOrdersetMinAmount);
            }
        } else {
            String result = scmBFRedisHttpUtils.redisGet(scmBFResdisProperties.getScmBfRedisUrl(), String.format(SHOP_SINGLE_CONFIG_KEY, shopId.toString()), ORDERSET_MIN_AMOUNT).toString();
            if (StringUtils.isBlank(result)) {
                cacheOrdersetMinAmount = "0";
            } else {
                ShopSingleConfigDto dto = JSON.parseObject(result, ShopSingleConfigDto.class);
                cacheOrdersetMinAmount = dto.getConfigValue();
            }

        }
        return cacheOrdersetMinAmount;
    }

    /**
     * 获取最大数量
     *
     * @param shopId
     * @param orderType
     * @return
     */
    private String getMaxMember(Long shopId, byte orderType) {
        String cacheOrdersetMaxMember = "";
        if (OrderTypeEnum.LUNCH.getCode().equals(orderType)) {
            cacheOrdersetMaxMember = RedisConfigUtil.getShopSingleConfigByKey(shopId, ORDERSET_MAX_MEMBER);
            if (StringUtils.isBlank(cacheOrdersetMaxMember)) {
                cacheOrdersetMaxMember = "100";
            }
        } else {
            String result = scmBFRedisHttpUtils.redisGet(scmBFResdisProperties.getScmBfRedisUrl(), String.format(SHOP_SINGLE_CONFIG_KEY, shopId.toString()), ORDERSET_MAX_MEMBER).toString();
            ShopSingleConfigDto dto = JSON.parseObject(result, ShopSingleConfigDto.class);
            cacheOrdersetMaxMember = dto.getConfigValue();
            if (StringUtils.isBlank(cacheOrdersetMaxMember)) {
                cacheOrdersetMaxMember = "100";
            }
        }
        return cacheOrdersetMaxMember;
    }

    @Override
    public void executeShopSet(Long shopId, InterceptConfigDto config, byte orderType) {
        log.info("-----------开始{}集单、门店信息{}和配置参数:{}", OrderTypeEnum.getCodeByDesc(orderType), shopId, JSON.toJSONString(config));
        Date deliveryBeginDate = getDeliveryDate(config.getDeliveryTimeBegin());
        String cacheOrdersetMaxAmount = getMaxAmount(shopId, orderType);
        String cacheOrdersetMinAmount = getMinAmount(shopId, orderType);
        String cacheOrdersetMaxMember = getMaxMember(shopId, orderType);
        // 单个集单的最大商品金额
        BigDecimal ordersetMaxAmount = new BigDecimal(cacheOrdersetMaxAmount);
        // 单个集单的最大人数
        Integer ordersetMaxMember = new Integer(cacheOrdersetMaxMember);
        //集单最小金额
        BigDecimal lastOrderSetMinAmount = new BigDecimal(cacheOrdersetMinAmount);
        // 集单的编号自增的key
        String setNoKey = "ORDER_SET_INCRT:NO:".concat(DateUtil.formatNowDate(DateUtil.YYYYMMDD));
        // 集单的编号描述自增的key
        String setNoDescKey = "ORDER_SET_INCRT:DESC:".concat(String.valueOf(shopId)).concat(":")
                .concat(DateUtil.formatNowDate(DateUtil.YYYYMMDD));

        //根据门店id 获取公司信息及取餐点信息
        ResponseResult<List<CompanyPolymerizationDto>> listCompany = companyService.queryByShopId(shopId);

        log.info("-----------获取到的公司信息和取餐点信息:{}", JSON.toJSON(listCompany));
        if (listCompany.isSuccess()) {
            if (listCompany.getData() != null && listCompany.getData().size() > 0) {
                List<CompanyPolymerizationDto> listCompanyPloymerization = listCompany.getData();
                //遍历公司信息
                for (CompanyPolymerizationDto item : listCompanyPloymerization) {
                    List<CompanyMealAddressDto> listMealAddress = item.getMealAddressList();
                    if (CollectionUtils.isEmpty(listMealAddress)) {
                        log.info("公司无取餐点信息:{}", JSON.toJSONString(item));
                        continue;
                    }
                    log.info("--------------------公司下的聚餐点信息：{}", JSON.toJSONString(listMealAddress));

                    for (CompanyMealAddressDto MealAddress : listMealAddress) {
                        Long mealAddressId = MealAddress.getId();

                        //-----------------------------------------add by zengyk start----------------
                        long start8 = System.currentTimeMillis();
                        log.info("--------------------集单查询商品参数：{}，{}，{}", orderType, mealAddressId, deliveryBeginDate);
                        List<GoodsModel> goodsModelList = orderGoodsMapper.getGoodsModelsByMealAddrIdAndDeliveryTime(orderType, mealAddressId, deliveryBeginDate);
                        log.info("--------------------集单查询商品结果信息：{}", JSON.toJSONString(goodsModelList));
                        long end8 = System.currentTimeMillis();

                        if (CollectionUtils.isEmpty(goodsModelList)) continue;
                        long start0 = System.currentTimeMillis();
                        //TODO 配置值
                        List<GoodsPackModel> listOrderSet = new SortingAlgorithmExecutor(goodsModelList, ordersetMaxAmount, lastOrderSetMinAmount, ordersetMaxMember).calculate();
                        long end0 = System.currentTimeMillis();
                        //-----------------------------------------add by zengyk end----------------
                        long start1 = System.currentTimeMillis();
                        combinationOrderSet(listOrderSet, item, MealAddress, setNoKey, setNoDescKey, orderType);
                        long end1 = System.currentTimeMillis();
                        //更新订单处理状态
//                        List<Long> orderIdList = orderIdAll.stream().map(OrderModel::getOrderId).collect(Collectors.toList());
                        //-----------------------------------------add by zengyk start----------------
                        List<Long> orderIdList = Lists.newArrayList(goodsModelList.stream().map(GoodsModel::getOrderId).collect(Collectors.toSet()));
                        //-----------------------------------------add by zengyk end----------------
                        long start2 = System.currentTimeMillis();
                        updateOrderProcessStatus(orderIdList);
                        long end2 = System.currentTimeMillis();
                    }
                }
            }
        }


        // 订单里面是包含了门店、公司、以及取餐点、配送时段

        //分组、公司id 取餐点id、配送时段，金额倒叙排序

    }

    private void updateOrderProcessStatus(List<Long> orderIdList) {
        OrderExample example = new OrderExample();
        example.createCriteria().andIdIn(orderIdList);

        Order order = new Order();
        order.setOrdersetProcessStatus(Boolean.TRUE);
        orderMapper.updateByExampleSelective(order, example);
    }


    private void combinationOrderSet(List<GoodsPackModel> listOrderSet, CompanyPolymerizationDto item, CompanyMealAddressDto mealAddress, String setNoKey, String setNoDescKey, byte orderType) {
        log.info("开始组合集单信息{}", JSON.toJSONString(listOrderSet));
        if (CollectionUtils.isEmpty(listOrderSet)) {
            return;
        }
        for (GoodsPackModel goodsPackModel : listOrderSet) {

            Long setNoIndex = 0l;
            Long setNoDescIndex = 0l;
            if (OrderTypeEnum.BREAKFAST.getCode().equals(orderType)) {
                Object setNoObj = scmBFRedisHttpUtils.redisinner(scmBFResdisProperties.getScmBfRedisUrlincr(), setNoKey, 1);
                Object setNoDescObj = scmBFRedisHttpUtils.redisinner(scmBFResdisProperties.getScmBfRedisUrlincr(), setNoDescKey, 1);
                setNoIndex = Long.parseLong(setNoObj.toString());
                setNoDescIndex = Long.parseLong(setNoDescObj.toString());

            } else {
                setNoIndex = RedisCache.incrBy(setNoKey, 1);
                setNoDescIndex = RedisCache.incrBy(setNoDescKey, 1);
            }


            RedisCache.expire(setNoKey, 1, TimeUnit.DAYS);
            RedisCache.expire(setNoDescKey, 1, TimeUnit.DAYS);

            // 子集单号
            String detailSetNo = generateDetailSetNo(setNoIndex.intValue(), orderType);
            // 子集单号描述
            String detailSetNoDescription = generateDetailSetNoDescription(setNoDescIndex.intValue(), orderType);
            log.info("------集单类型-{}获得集单编号{}", orderType, detailSetNo);
            log.info("------集单类型-{}获得集单编号{}", orderType, detailSetNoDescription);
            log.info("组合集单：{}", JSON.toJSONString(goodsPackModel));
            Set<Long> orderIdSet = goodsPackModel.getOrderIdSet();
            if (!CollectionUtils.isEmpty(orderIdSet)) {
                //取一条订单id获取订单明细、组合集单信息

                Iterator<Long> it = orderIdSet.iterator();
                Long orderId = it.next();
                OrderSet orderSetDetails = new OrderSet();
                orderSetDetails.setUserSum(goodsPackModel.getUserIdSet().size());
                //FIXME 此处查一次库为了获取配送开始和结束时间，其实可以从门店配置传过来，避免此次数据库交互
                Order order = orderMapper.selectByPrimaryKey(orderId);
                ConvertOrderToOrderSet(order, orderSetDetails, item, mealAddress, detailSetNo, detailSetNoDescription);

                // redis的自增key
                String key = "ORDER_SET_ORDER_COUPLET_INCRT:".concat(String.valueOf(item.getShopId())).concat(":")
                        .concat(DateUtil.formatNowDate(DateUtil.YYYYMMDD));
                List<OrderGoods> listOrderGoods = getOrderGoodsByIds(goodsPackModel.getGoodsModelSet());
                // 得到这个集单里的订单号的客户联
                Map<String, String> map = getOrderCoupletNo(listOrderGoods, key, orderType);
                RedisCache.expire(key, 1, TimeUnit.DAYS);

                // 商品数量
                int goodsSum = 0;
                // 实付总金额
                BigDecimal actualPayAmount = BigDecimal.ZERO;
                OrderSetGoods record = null;
                Map<Long, Integer> goodsCountMap = goodsPackModel.getGoodsModelSet().stream().collect(Collectors.toMap(GoodsModel::getGoodsId, a -> a.getGoodsCount(), (k1, k2) -> k1));
                Map<Long, BigDecimal> goodsAmount = goodsPackModel.getGoodsModelSet().stream().collect(Collectors.toMap(GoodsModel::getGoodsId, a -> a.getTotalAmount(), (k1, k2) -> k1));
                long start0 = System.currentTimeMillis();
                List<OrderSetGoods> orderSetGoodsList = Lists.newArrayList();
                for (OrderGoods orderGoods : listOrderGoods) {
                    // 根据订单号查询主表信息
                    OrderResult orderInfo = getOrderInfoByOrderNo(orderGoods.getOrderNo());
                    if (orderInfo == null) {
                        throw new RuntimeException("根据订单号" + orderGoods.getOrderNo() + "未查询到订单信息");
                    }
                    Integer goodsCount = goodsCountMap.get(orderGoods.getId());
                    goodsSum += goodsCount;
                    if (BooleanUtils.isTrue(orderGoods.getIsCombo())) {
                        List<OrderGoodsCombo> listGoodsCombo = getOrderGoodsCombo(orderInfo, orderGoods);
                        log.info("获取组合品信息{}", JSON.toJSONString(listGoodsCombo));
                        for (OrderGoodsCombo orderGoodsCombo : listGoodsCombo) {
                            record = new OrderSetGoods();
                            record.setDetailSetNo(orderSetDetails.getDetailSetNo());
                            record.setGoodsCode(orderGoodsCombo.getGoodsCode());
                            record.setGoodsName(orderGoodsCombo.getGoodsName());
//                            record.setGoodsNumber(orderGoodsCombo.getGoodsCount());
                            record.setGoodsNumber(orderGoodsCombo.getGoodsCount() / orderGoodsCombo.getComboGoodsCount() * goodsCount);
                            log.info("----------------订单组合商品中单品数量：{},订单组合商品数量：{},本次集单组合商品数量：{}", orderGoodsCombo.getGoodsCount(), orderGoodsCombo.getComboGoodsCount(), goodsCount);
                            record.setId(IdGenerator.nextId());
                            record.setIsDeleted(false);
                            record.setOrderNo(orderGoodsCombo.getOrderNo());
                            record.setUserId(orderInfo.getUserId());
                            record.setComboGoodsCode(orderGoodsCombo.getComboGoodsCode());
                            record.setComboGoodsName(orderGoodsCombo.getComboGoodsName());
                            record.setIsCombo(true);
                            record.setType(orderGoods.getType());
                            record.setComboGoodsCount(goodsCount);
                            record.setUserName(orderInfo.getRealName());
                            record.setUserPhone(orderInfo.getMobileNo());
                            record.setCoupletNo(map.get(orderGoodsCombo.getOrderNo()));
                            record.setActualPayAmount(null);
                            orderSetGoodsList.add(record);
                        }
                    } else {

                        record = new OrderSetGoods();
                        record.setDetailSetNo(orderSetDetails.getDetailSetNo());
                        record.setGoodsCode(orderGoods.getGoodsCode());
                        record.setGoodsName(orderGoods.getGoodsName());
                        record.setGoodsNumber(goodsCount);
                        record.setId(IdGenerator.nextId());
                        record.setIsCombo(false);
                        record.setIsDeleted(false);
                        record.setOrderNo(orderGoods.getOrderNo());
                        record.setUserId(orderInfo.getUserId());
                        record.setUserName(orderInfo.getRealName());
                        record.setUserPhone(orderInfo.getMobileNo());
                        record.setCoupletNo(map.get(orderGoods.getOrderNo()));
                        record.setActualPayAmount(goodsAmount.get(orderGoods.getId()));
                        record.setType(orderGoods.getType());
//                    orderSetGoodsMapper.insertSelective(record);
                        orderSetGoodsList.add(record);
                    }
                }
                log.info("----------------批量插入商品信息：{}", JSON.toJSONString(orderSetGoodsList));
                orderSetGoodsMapper.batchSave(orderSetGoodsList);
                long end0 = System.currentTimeMillis();
                log.info("~~~~~~~~~~~#####~~~~~~~~~~插入集单商品明细时间：{}", end0 - start0);
                orderSetDetails.setGoodsSum(goodsSum);
                orderSetDetails.setActualPayAmount(goodsPackModel.getTotalGoodsAmount());
                orderSetDetails.setDistributionStatus(LogisticsDeliveryStatus.INIT.getValue());
                orderSetDetails.setIsPush(Boolean.FALSE);
                orderSetDetails.setOrderType(orderType);
                log.info("-----------------集单生产完成：{}", JSON.toJSONString(orderSetDetails));
                orderSetMapper.insertSelective(orderSetDetails);
                //插入订单和集单中间表
                saveMtmOrderOrderset(orderSetDetails.getId(), goodsPackModel.getOrderIdSet());
                log.info("+++++楼宇：{}的子集单保存成功，子集单号：{}", item.getName(), orderSetDetails.getDetailSetNo());
            }
        }
    }

    /**
     * 发送门店的集单信息给POS系统进行生产
     *
     * @param shopId
     * @param isCompensate 是否补偿
     */
    @Override
    public void sendOrderSetMq(Long shopId, boolean isCompensate, byte orderType) {
        int pageSize = 10000;
        log.info("-----发送MQ消息：shopId:{},isCompensate:{},orderType:{}", shopId, isCompensate, orderType);
        OrderSetExample example = new OrderSetExample();
        // 补偿就查当天全部集单数据
        if (isCompensate) {
            example.createCriteria().andShopIdEqualTo(shopId).andDetailDeliveryDateEqualTo(DateUtil.getDayBeginTime(new Date())).andOrderTypeEqualTo(orderType);
        } else {
            example.createCriteria().andShopIdEqualTo(shopId).andIsPushEqualTo(Boolean.FALSE)
                    .andDetailDeliveryDateEqualTo(DateUtil.getDayBeginTime(new Date())).andOrderTypeEqualTo(orderType);
        }

        // 门店总集单数
        long count = orderSetMapper.countByExample(example);
        log.info("-----发送MQ消息：集单数量统计结果:{}", count);
        if (count > 0) {
            example.setPageSize(pageSize);
            example.setStartRow(0);
            example.setOrderByClause(" detail_set_no asc ");
            List<OrderSet> list = orderSetMapper.selectByExample(example);
            log.info("-----发送MQ消息：集单信息查询结果:{}", list);
            while (!CollectionUtils.isEmpty(list)) {

                if (OrderTypeEnum.BREAKFAST.getCode().equals(orderType)) {
                    processOrderSetMq(list, shopId);
                } else {
                    processOrderSetMqLunch(list, shopId);
                }
                list = orderSetMapper.selectByExample(example);
            }
        }
    }

    /**
     * 更新集单的配送状态
     *
     * @param param
     */
    @Override
    @Transactional
    public void updateDistributionStatus(DeliveryOrderMqDto param) {
        UpdateOrderSetStatusParam dto = new UpdateOrderSetStatusParam();
        dto.setSfDto(param);
        dto.setUpdateBy("sf_顺丰");
        this.updateStatus(dto);
    }

    @Override
    public List<OrderDetailResult> getOrderDetailInfoByOrderNo(List<String> orderNos) {
        OrderGoodsExample example = new OrderGoodsExample();
        example.createCriteria().andOrderNoIn(orderNos);
        List<OrderGoods> list = orderGoodsMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(list)) {
            return BeanMapper.mapList(list, OrderDetailResult.class);
        }
        return null;
    }

    @Override
    public void pushSfOrderSetMq(ShopInterceptConfigDto configDto) {
        OrderSetExample example = new OrderSetExample();
        // 查当天集单数据
        example.createCriteria().andShopIdEqualTo(configDto.getShopId()).
                andDistributionStatusEqualTo((byte) -1).
                andOrderTypeEqualTo(OrderTypeEnum.BREAKFAST.getCode()).
                andDetailDeliveryDateEqualTo(DateUtil.getDayBeginTime(new Date())).
                andDeliveryStartTimeEqualTo(configDto.getDeliveryTimeBegin()).
                andDeliveryEndTimeEqualTo(configDto.getDeliveryTimeEnd());
        List<OrderSet> orderSetDetails = orderSetMapper.selectByExample(example);
        // 发送通知顺丰派单的MQ
        processSfOrderSetMq(orderSetDetails);
    }

    @Override
    public void pushLcSfOrderSetMq(ShopInterceptConfigDto configDto) {
        OrderSetExample example = new OrderSetExample();
        // 查当天集单数据
        example.createCriteria().andShopIdEqualTo(configDto.getShopId()).
                andDistributionStatusEqualTo((byte) -1).
                andOrderTypeEqualTo(OrderTypeEnum.LUNCH.getCode()).
                andDetailDeliveryDateEqualTo(DateUtil.getDayBeginTime(new Date())).
                andDeliveryStartTimeEqualTo(configDto.getDeliveryTimeBegin()).
                andDeliveryEndTimeEqualTo(configDto.getDeliveryTimeEnd());
        List<OrderSet> orderSetDetails = orderSetMapper.selectByExample(example);
        // 发送通知顺丰派单的MQ
        processSfOrderSetMq(orderSetDetails);
    }

    private void processSfOrderSetMq(List<OrderSet> list) {
        OrderSetProductionMsg msg = new OrderSetProductionMsg();
        list.stream().forEach(item -> {
            OrderSetGoodsExample example = new OrderSetGoodsExample();
            example.createCriteria().andDetailSetNoEqualTo(item.getDetailSetNo());
            List<OrderSetGoods> details = orderSetGoodsMapper.selectByExample(example);

            msg.setOrderSet(BeanMapper.map(item, OrderSetDetailDto.class));
            if (!CollectionUtils.isEmpty(details)) {
                msg.setGoods(BeanMapper.mapList(details, OrderSetDetailGoodsDto.class));
            }

            MqProducerUtil.sendSfOrderSet(msg);
        });
    }

    @Override
    public void compensateCompletedOrderSetTask() {
        // 查询超过"配送结束时间"2小时的未接集单
        List<Long> orderSetDetailIds = orderSetMapper.queryDelayOrderSet();
        if (!CollectionUtils.isEmpty(orderSetDetailIds)) {
            RLock rLock = null;
            try {
                rLock = LockProvider.getLock("compensate_completed_orderset_task");
                rLock.lock();
                log.info("补偿配送超时订单数量 -> {}", orderSetDetailIds.size());
                for (Long orderSetDetailId : orderSetDetailIds) {
                    try {
                        // 初始化参数
                        UpdateOrderSetStatusParam param = initUpdateOrderSetStatusParam(orderSetDetailId);
                        // 更改集单的状态
                        updateStatus(param);
                    } catch (Exception e) {
                        log.error("", e);
                    }
                }
            } catch (Exception e) {
                log.error("", e);
            } finally {
                if (rLock != null) {
                    rLock.unlock();
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderShop(Long companyId, Long shopId, String shopCode) {
        long count = orderMapper.updateOrderShop(companyId, shopId, shopCode);
        log.info("更新了{}条订单中的门店", count);
    }

    private UpdateOrderSetStatusParam initUpdateOrderSetStatusParam(Long orderSetDetailId) {
        DeliveryOrderMqDto param = new DeliveryOrderMqDto();
        param.setDetailSetId(orderSetDetailId.toString());
        // 配送完成
        param.setStatus(OrderSetStatusEnum.NOTIFIED.getCode());
        param.setUpdateTime(new Date());

        UpdateOrderSetStatusParam dto = new UpdateOrderSetStatusParam();
        dto.setSfDto(param);
        dto.setUpdateBy("task_补偿");
        dto.setIsCompensate(Boolean.TRUE);
        return dto;
    }

    /**
     * 更改集单的状态
     *
     * @param param
     */
    @Transactional
    public ResponseResult<Object> updateStatus(UpdateOrderSetStatusParam param) {
        log.info("修改子集单的状态，入参：{}", JSONObject.toJSONString(param));
        ResponseResult<Object> result = ResponseResult.buildSuccessResponse();

        if (null != param.getSfDto()) {
            param.setDetailSetId(Long.valueOf(param.getSfDto().getDetailSetId()));
        }
        // 数据库中的集单
        OrderSet orderSetDetail = orderSetMapper.selectByPrimaryKey(param.getDetailSetId());
        if (null == orderSetDetail) {
            result.setSuccess(false);
            result.setMessage("操作的记录不存在");
            log.warn("集单id：{}未查询到数据", param.getDetailSetId());
            return result;
        }

        // 原来的集单
        OrderSet oldOrderSetDetail = new OrderSet();
        BeanUtils.copyProperties(orderSetDetail, oldOrderSetDetail);

        orderSetDetail.setUpdateBy(param.getUpdateBy());

        OrderSetExample example = new OrderSetExample();
        Criteria criteria = example.createCriteria().andIdEqualTo(param.getDetailSetId()).andIsDeletedEqualTo(false);

        if (null != param.getSfDto()) {
            param.setDetailSetId(Long.valueOf(param.getSfDto().getDetailSetId()));

            orderSetDetail.setDistributionStatus(param.getSfDto().getStatus());

            if (LogisticsDeliveryStatus.DISPATCHED_ORDER.getValue().equals(param.getSfDto().getStatus())
                    || LogisticsDeliveryStatus.RECEIVED_ORDER.getValue().equals(param.getSfDto().getStatus())
                    || LogisticsDeliveryStatus.ARRIVED_SHOP.getValue().equals(param.getSfDto().getStatus())) {
                // 已打包
                orderSetDetail.setStatus(OrderSetStatusEnum.ALREADY_PACKED.getCode());
            } else if (LogisticsDeliveryStatus.COMPLETED.getValue().equals(param.getSfDto().getStatus())) {
                // 已经送达
                orderSetDetail.setStatus(OrderSetStatusEnum.NOTIFIED.getCode());
            } else if (LogisticsDeliveryStatus.RECEIVED_GOODS.getValue().equals(param.getSfDto().getStatus())) {
                // 已取件配送
                orderSetDetail.setStatus(OrderSetStatusEnum.IN_DISTRIBUTION.getCode());
            }
            criteria.andStatusEqualTo(oldOrderSetDetail.getStatus());
            criteria.andDistributionStatusEqualTo(oldOrderSetDetail.getDistributionStatus());
        } else {
            // 兼容老数据接口

            Byte oldStatus = null;
            if (param.getAction().equals(OrderSetActionEnum.BALE.getCode())) {
                // 打包操作
                oldStatus = OrderSetStatusEnum.WAITING_FOR_PACKAGING.getCode();
                orderSetDetail.setStatus(OrderSetStatusEnum.ALREADY_PACKED.getCode());
            } else if (param.getAction().equals(OrderSetActionEnum.DELIVERY.getCode())) {
                // 交配送
                oldStatus = OrderSetStatusEnum.ALREADY_PACKED.getCode();
                orderSetDetail.setStatus(OrderSetStatusEnum.IN_DISTRIBUTION.getCode());
            } else if (param.getAction().equals(OrderSetActionEnum.NOTIFY.getCode())) {
                // 通知客户
                oldStatus = OrderSetStatusEnum.IN_DISTRIBUTION.getCode();
                orderSetDetail.setStatus(OrderSetStatusEnum.NOTIFIED.getCode());
            } else {
                result.setSuccess(false);
                result.setMessage("非法的操作类型");
                return result;
            }
            criteria.andStatusEqualTo(oldStatus);
        }

        int ct = orderSetMapper.updateByExampleSelective(orderSetDetail, example);
        if (ct == 0) {
            result.setSuccess(false);
            result.setMessage("非法操作，状态不一致");
            log.warn("集单id：{}状态不一致", param.getDetailSetId());
            return result;
        }

        // 发送操作日志
        sendOpLog(param, oldOrderSetDetail, orderSetDetail);

        OrderSetGoodsExample goodsExample = new OrderSetGoodsExample();
        goodsExample.createCriteria().andDetailSetNoEqualTo(orderSetDetail.getDetailSetNo())
                .andIsDeletedEqualTo(false);
        List<OrderSetGoods> list = orderSetGoodsMapper.selectByExample(goodsExample);
        if (!CollectionUtils.isEmpty(list)) {
            Set<String> orderNoSet = list.stream().map(item -> item.getOrderNo()).collect(Collectors.toSet());
            log.info("集单号{}里共包含了如下订单号:{}", orderSetDetail.getDetailSetNo(), JSONObject.toJSONString(orderNoSet));
            orderNoSet.stream().forEach(orderNo -> {
                // 查询订单号里的所有的商品的状态
                List<OrderSetDetailStatusDto> statusList = orderSetGoodsMapper
                        .listAllStatusByOrderNo(orderNo);
                log.info("订单号：{}的状态list：{}", orderNo, JSONObject.toJSONString(statusList));
                if (!CollectionUtils.isEmpty(statusList)) {
                    OrderSetDetailStatusDto dto = statusList.get(0);

                    if (!LogisticsDeliveryStatus.INIT.getValue().equals(dto.getDistributionStatus())) {
                        // 更定订单的配送状态
                        OrderUpdateStatusParam orderStatusParam = new OrderUpdateStatusParam();
                        orderStatusParam.setOrderNo(orderNo);
                        orderStatusParam.setDeliveryStatus(dto.getDistributionStatus());
                        orderStatusParam.setIsCompensate(param.getIsCompensate());
                        this.updateOrderStatus(orderStatusParam);

                        log.info("订单号：{}里的商品状态发生了变更，准备发送MQ", orderNo);
                        OrderSetOrderStatusChangeMsg msg = new OrderSetOrderStatusChangeMsg();
                        msg.setDate(new Date());
                        msg.setOrderNo(orderNo);
                        msg.setStatus(dto.getStatus());
                        msg.setSfStatus(dto.getDistributionStatus());
                        msg.setIsCompensate(param.getIsCompensate());
                        MqProducerUtil.sendOrderSetOrderStatusMsg(msg);
                    }
                }
            });
        }

        return result;
    }

    /**
     * 记录操作日志
     *
     * @param param
     * @param oldOrderSetDetail
     * @param newOrderSetDetail
     */
    private void sendOpLog(UpdateOrderSetStatusParam param, OrderSet oldOrderSetDetail,
                           OrderSet newOrderSetDetail) {
        OperationLogDto opLog = OperationLogDto.builder().beforeData(JSONObject.toJSONString(oldOrderSetDetail))
                .afterData(JSONObject.toJSONString(newOrderSetDetail))
                .diffData(CompareDifferenceUtil.compare(oldOrderSetDetail, newOrderSetDetail))
                .operationUser(param.getUpdateBy()).bussinessId(oldOrderSetDetail.getDetailSetNo())
                .modular(ModularEnum.ORDER_SET.getCode()).operationType(OperationTypeEnum.UPDATE.getCode())
                .operationTime(new Date()).build();

        if (!LogisticsDeliveryStatus.INIT.getValue().equals(newOrderSetDetail.getDistributionStatus())) {
            opLog.setOperationResume(LogisticsDeliveryStatus.getNameByValue(newOrderSetDetail.getDistributionStatus()));
        } else {
            if (param.getAction().equals(OrderSetActionEnum.BALE.getCode())) {
                // 打包操作
                opLog.setOperationResume("打包");
            } else if (param.getAction().equals(OrderSetActionEnum.DELIVERY.getCode())) {
                // 交配送
                opLog.setOperationResume("交配送");
            } else if (param.getAction().equals(OrderSetActionEnum.NOTIFY.getCode())) {
                // 通知客户
                opLog.setOperationResume("通知顾客");
            }
        }

        MqProducerUtil.sendOperationLog(opLog);
    }

    private void updateOrderStatus(OrderUpdateStatusParam param) {
        OrderExample example = new OrderExample();
        example.createCriteria().andOrderNoEqualTo(param.getOrderNo());
        List<Order> list = orderMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(list)) {
            Order order = list.get(0);
            order.setOrderStatus(param.getOrderStatus());
            order.setDeliveryStatus(param.getDeliveryStatus());
            if (OrderDeliveryStatus.COMPLETED.getValue().equals(param.getDeliveryStatus())) {
                order.setCompleteTime(new Date());
            }
            if (Boolean.TRUE.equals(param.getIsCompensate())) {
                // 补偿操作，将"预计完成时间" 改为 "订单完成时间"
                order.setCompleteTime(order.getDeliveryEndDate());
            }
            orderMapper.updateByPrimaryKeySelective(order);
        }
    }

    /**
     * 处理一批集单发送
     *
     * @param list
     * @param shopId
     */
    public void processOrderSetMq(List<OrderSet> list, Long shopId) {
        List<WaveOrderMessage> waveOrderMessageList = new ArrayList<>(100);
        List<DeliveryPackageMessage> deliveryPackageMessageList = new ArrayList<>(250);
        Long currentCompanyId = null;
        WaveOrderMessage currentWaveOrderMessage = null;


        OrderSetProductionMsg msg = new OrderSetProductionMsg();
        for (OrderSet item : list) {
            OrderSetGoodsExample example = new OrderSetGoodsExample();
            example.createCriteria().andDetailSetNoEqualTo(item.getDetailSetNo());
            List<OrderSetGoods> details = orderSetGoodsMapper.selectByExample(example);

            msg.setOrderSet(BeanMapper.map(item, OrderSetDetailDto.class));
            if (!CollectionUtils.isEmpty(details)) {
                msg.setGoods(BeanMapper.mapList(details, OrderSetDetailGoodsDto.class));
            }

            item.setIsPush(Boolean.TRUE);
            orderSetMapper.updateByPrimaryKeySelective(item);
            MqProducerUtil.sendOrderSetBF(msg);


//            try {
            //DeliveryPackageMessage deliveryPackageMessage = buildOrderSetMessage(deliveryPackageMessageList, item, details);
            DeliveryPackageMessage deliveryPackageMessage = reprintServiceImpl.rePrintBuildMessage(item, details, 4, "");
            //按公司分波次
            if (!item.getCompanyId().equals(currentCompanyId)) {
                currentWaveOrderMessage = new WaveOrderMessage();
                currentWaveOrderMessage.setCompany(item.getCompanyAbbreviation());
                currentWaveOrderMessage.setCompanyAddr(item.getDetailDeliveryAddress());
                currentWaveOrderMessage.setShopId(shopId.toString());
                currentWaveOrderMessage.setUserPackageCount(deliveryPackageMessage.getUserPackageCount());
                currentWaveOrderMessage.setBusinessLine(item.getOrderType());
                currentCompanyId = item.getCompanyId();
                waveOrderMessageList.add(currentWaveOrderMessage);
            }
            currentWaveOrderMessage.addUserPackageCount(deliveryPackageMessage.getUserPackageCount());
            currentWaveOrderMessage.addDeliveryPackage(deliveryPackageMessage);
//            } catch (Exception e) {
//                log.error("构建新集单消息出错：{}", e.getMessage());
//                e.printStackTrace();
//            }
        }
        WaveOrderDispatcher.dispatch(waveOrderMessageList, getPrinterCounter(shopId));
    }


    /**
     * 处理一批集单发送
     *
     * @param list
     * @param shopId
     */
    public void processOrderSetMqLunch(List<OrderSet> list, Long shopId) {
        List<WaveOrderMessage> waveOrderMessageList = new ArrayList<>(100);
        List<DeliveryPackageMessage> deliveryPackageMessageList = new ArrayList<>(250);
        Long currentCompanyId = null;
        WaveOrderMessage currentWaveOrderMessage = null;

        OrderSetProductionLunchMsg msg = new OrderSetProductionLunchMsg();
        for (OrderSet item : list) {
            OrderSetGoodsExample example = new OrderSetGoodsExample();
            example.createCriteria().andDetailSetNoEqualTo(item.getDetailSetNo());
            List<OrderSetGoods> details = orderSetGoodsMapper.selectByExample(example);

            msg.setOrderSet(BeanMapper.map(item, OrderSetDetailDto.class));
            if (!CollectionUtils.isEmpty(details)) {
                msg.setGoods(BeanMapper.mapList(details, OrderSetDetailGoodsLunchDto.class));
            }

            item.setIsPush(Boolean.TRUE);
            orderSetMapper.updateByPrimaryKeySelective(item);
            MqProducerUtil.sendOrderSet(msg);

//            try {
            //DeliveryPackageMessage deliveryPackageMessage = buildOrderSetMessage(deliveryPackageMessageList, item, details);
            DeliveryPackageMessage deliveryPackageMessage = reprintServiceImpl.rePrintBuildMessageLunch(item, details, 4, "");
            //按公司分波次
            if (!item.getCompanyId().equals(currentCompanyId)) {
                currentWaveOrderMessage = new WaveOrderMessage();
                currentWaveOrderMessage.setCompany(item.getCompanyAbbreviation());
                currentWaveOrderMessage.setCompanyAddr(item.getDetailDeliveryAddress());
                currentWaveOrderMessage.setShopId(shopId.toString());
                currentWaveOrderMessage.setUserPackageCount(deliveryPackageMessage.getUserPackageCount());
                currentWaveOrderMessage.setBusinessLine(item.getOrderType());
                currentCompanyId = item.getCompanyId();
                waveOrderMessageList.add(currentWaveOrderMessage);
            }
            currentWaveOrderMessage.addUserPackageCount(deliveryPackageMessage.getUserPackageCount());
            currentWaveOrderMessage.addDeliveryPackage(deliveryPackageMessage);
            log.info("------------------波次作业，当前公司ID:{},用户打包明细：{}", currentCompanyId, JSON.toJSONString(currentWaveOrderMessage));
//            } catch (Exception e) {
//                log.error("构建新集单消息出错：{}", e);
//            }
        }
        WaveOrderDispatcher.dispatch(waveOrderMessageList, getPrinterCounter(shopId));
    }

    private DeliveryPackageMessage buildOrderSetMessage(List<DeliveryPackageMessage> deliveryPackageMessageList, OrderSet item, List<OrderSetGoods> details) {
        //按顾客联编号分组
        Map<String, List<OrderSetGoods>> goodsByReceiptNo = details.stream().collect(groupingBy(OrderSetGoods::getCoupletNo));
        List<UserPackageMessage> userPackageMessageList = new ArrayList<>(1000);

//                goodsByReceiptNo.entrySet().stream().map((entry ->

        int index = 0;
        for (Map.Entry<String, List<OrderSetGoods>> entry : goodsByReceiptNo.entrySet()) {
            index++;

            UserPackageMessage userPackageMessage = new UserPackageMessage();
            //得到顾客联编号
            String receiptNo = entry.getKey();
            //打包的商品
            List<OrderSetGoods> goodsList = entry.getValue();
            OrderSetGoods first = goodsList.get(0);
            //按套餐/单品分成两组
            Map<Boolean, List<OrderSetGoods>> goodsByIsCombo = goodsList.stream().collect(groupingBy(OrderSetGoods::getIsCombo));
            //按套餐标识分组商品
            List<OrderSetGoods> combos = goodsByIsCombo.get(true);
            List<UserPackageGoodsMessage> comboPackGoodsMessageList = null;

            if (!CollectionUtils.isEmpty(combos)) {
                Map<String, List<OrderSetGoods>> goodsByComboCode = combos.stream().collect(groupingBy(OrderSetGoods::getComboGoodsCode));
                comboPackGoodsMessageList = goodsByComboCode.entrySet().stream().map(entry2 -> {
                    UserPackageGoodsMessage userPackageGoodsMessage = new UserPackageGoodsMessage();

                    List<OrderSetGoods> itemGoodsList = entry2.getValue();
                    OrderSetGoods itemGoods = itemGoodsList.get(0);
                    userPackageGoodsMessage.setGoodsName(itemGoods.getComboGoodsName());
                    userPackageGoodsMessage.setGoodsCount(itemGoods.getComboGoodsCount());
                    userPackageGoodsMessage.setItems(itemGoodsList.stream().map(goods -> {
                        UserPackageGoodsMessage itemPackGoodsMessage = new UserPackageGoodsMessage();
                        itemPackGoodsMessage.setGoodsCount(goods.getGoodsNumber());
                        itemPackGoodsMessage.setGoodsName(goods.getGoodsName());
                        return itemPackGoodsMessage;
                    }).collect(Collectors.toList()));
                    return userPackageGoodsMessage;
                }).collect(Collectors.toList());
            }

            //单品处理
            List<OrderSetGoods> items = goodsByIsCombo.get(false);
            List<UserPackageGoodsMessage> itemPackGoodsMessageList = null;

            if (!CollectionUtils.isEmpty(items)) {
                itemPackGoodsMessageList = items.stream().map(orderGoods -> {
                    UserPackageGoodsMessage userPackageGoodsMessage = new UserPackageGoodsMessage();
                    userPackageGoodsMessage.setGoodsName(orderGoods.getGoodsName());
                    userPackageGoodsMessage.setGoodsCount(orderGoods.getGoodsNumber());
                    return userPackageGoodsMessage;
                }).collect(Collectors.toList());
            }

            userPackageMessage.setMobileNo(first.getUserPhone());
            userPackageMessage.setReceiptNo(receiptNo);
            userPackageMessage.setUserName(first.getUserName());
            userPackageMessage.setItemGoodsList(itemPackGoodsMessageList);
            userPackageMessage.setComboGoodsList(comboPackGoodsMessageList);
            userPackageMessage.setIndex(index);
            userPackageMessageList.add(userPackageMessage);
        }


        DeliveryPackageMessage deliveryPackageMessage = new DeliveryPackageMessage();
        deliveryPackageMessage.setDeliveryNo("#" + Integer.parseInt(item.getDetailSetNoDescription().substring(1)));
        deliveryPackageMessage.setDeliveryDate(DateFormatUtils.format(item.getDetailDeliveryDate(), "yyyy-MM-dd"));
        deliveryPackageMessage.setTakeMealAddr(item.getMealAddress());
        deliveryPackageMessage.setTakeMealStartTime(DateFormatUtils.format(item.getDeliveryStartTime(), "HH:mm"));
        deliveryPackageMessage.setTakeMealEndTime(DateFormatUtils.format(item.getDeliveryEndTime(), "HH:mm"));
        deliveryPackageMessage.setUserPackageList(userPackageMessageList);
        deliveryPackageMessage.setUserPackageCount(userPackageMessageList.size());
        deliveryPackageMessageList.add(deliveryPackageMessage);
        return deliveryPackageMessage;
    }

    Map<String, String> getPrinterCounter(Long shopId) {
//        Map<String, String> printerCounter = new HashMap<>();
//        printerCounter.put("Gprinter  GP-3150TN", "10.18.10.45");
//        printerCounter.put("Gprinter  GP-3150TN_1", "10.18.10.46");
////        printerCounter.put("Gprinter  GP-3150TN_2", "10.18.10.47");
//        return printerCounter;
        Map<String, String> shopPrinter = RedisConfigUtil.getShopPrinter(shopId);
        log.info("----------门店打印机配置：{}", JSON.toJSONString(shopPrinter));
        return shopPrinter;
    }


    /**
     * 生成中间表数据
     *
     * @param id
     * @param orderIdSet
     */
    private void saveMtmOrderOrderset(Long id, Set<Long> orderIdSet) {
        log.info("--------生成集单和订单中间表数据,集单ID：{},订单ID:{} ", id, JSON.toJSONString(orderIdSet));
        List<OrderToOrderSet> orderToOrderSetList = Lists.newArrayList();
        for (Long orderId : orderIdSet) {
            OrderToOrderSet record = new OrderToOrderSet();
            record.setId(IdGenerator.nextId());
            record.setOrderId(orderId);
            record.setOrderSetDetailId(id);
//            baseOrderToOrderSetMapper.insertSelective(record);
            orderToOrderSetList.add(record);
        }

        orderToOrderSetMapper.batchSave(orderToOrderSetList);
    }


    /**
     *
     */
    private OrderResult getOrderInfoByOrderNo(String orderNo) {
        OrderExample example = new OrderExample();
        example.createCriteria().andOrderNoEqualTo(orderNo);
        List<Order> list = orderMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(list)) {
            return BeanMapper.map(list.get(0), OrderResult.class);
        }
        return null;
    }

    /**
     * 根据组合品code查询
     *
     * @param orderInfo
     * @param orderGoods
     * @return
     */
    private List<OrderGoodsCombo> getOrderGoodsCombo(OrderResult orderInfo, OrderGoods orderGoods) {
        OrderGoodsComboExample example = new OrderGoodsComboExample();
        example.createCriteria().andOrderNoEqualTo(orderInfo.getOrderNo())
                .andComboGoodsCodeEqualTo(orderGoods.getGoodsCode())
                .andOrderIdEqualTo(orderInfo.getId());
        List<OrderGoodsCombo> list = orderGoodsComboMapper.selectByExample(example);
        return list;
    }

    /**
     * 生成集单里订单的订单联的编号
     *
     * @param key
     * @return
     */
    private Map<String, String> getOrderCoupletNo(List<OrderGoods> listOrderGoods, String key, byte orderType) {
        Map<String, String> map = new HashMap<>();
        for (OrderGoods goods : listOrderGoods) {
            if (map.get(goods.getOrderNo()) == null) {
                int number = getNumberByOrderType(key, orderType);
                if (OrderTypeEnum.BREAKFAST.getCode().equals(orderType)) {
                    map.put(goods.getOrderNo(),
                            NoPrefixEnum.BFORDER.getCode().concat(leftFillInZero(number, 4)));
                } else {
                    map.put(goods.getOrderNo(),
                            NoPrefixEnum.ORDER.getCode().concat(leftFillInZero(number, 4)));
                }

            }
        }
        return map;
    }

    private int getNumberByOrderType(String key, byte orderType) {
        if (OrderTypeEnum.BREAKFAST.getCode().equals(orderType)) {
            Object obj = scmBFRedisHttpUtils.redisinner(scmBFResdisProperties.getScmBfRedisUrlincr(), key, 1);
            return Integer.parseInt(obj.toString());
        } else {
            return RedisCache.incrBy(key, 1).intValue();
        }
    }


    /**
     * 根据订单商品Id获取明细
     *
     * @param orderGoodsIdSet
     * @return
     */
    private List<OrderGoods> getOrderGoodsByIds(Set<GoodsModel> orderGoodsIdSet) {

        List<Long> listIds = orderGoodsIdSet.stream().map(GoodsModel::getGoodsId).collect(Collectors.toList());

        OrderGoodsExample example = new OrderGoodsExample();
        example.createCriteria().andIdIn(listIds);
        return orderGoodsMapper.selectByExample(example);
    }


    /**
     * 生成子集单编号
     *
     * @param orderType
     * @param index
     * @return
     */
    private String generateDetailSetNo(int index, byte orderType) {
        if (OrderTypeEnum.BREAKFAST.getCode().equals(orderType)) {
            return NoPrefixEnum.BFORDERSET.getCode().concat(DateUtil.formatNowDate(DateUtil.YYYYMMDD))
                    .concat(leftFillInZero(index, 6));
        } else {
            return NoPrefixEnum.ORDERSET.getCode().concat(DateUtil.formatNowDate(DateUtil.YYYYMMDD))
                    .concat(leftFillInZero(index, 6));
        }

    }

    /**
     * 生成子集单编号的描述
     *
     * @param orderType
     * @param index
     * @return
     */
    private String generateDetailSetNoDescription(int index, byte orderType) {
        if (OrderTypeEnum.BREAKFAST.getCode().equals(orderType)) {
            return NoPrefixEnum.BFORDERSET.getCode().concat(leftFillInZero(index, 4));
        } else {
            return NoPrefixEnum.ORDERSET.getCode().concat(leftFillInZero(index, 4));
        }
    }

    /**
     * 左补位0
     *
     * @param value
     * @param digit
     * @return
     */
    private String leftFillInZero(int value, int digit) {
        String v = String.valueOf(value);
        int difference = digit - v.length();
        if (difference <= 0) {
            return v;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < difference; i++) {
            sb.append("0");
        }
        sb.append(v);
        return sb.toString();
    }

    /**
     * 转换订单和和集单信息
     *
     * @param order
     * @param detailSetNoDescription
     * @param detailSetNo
     */
    private void ConvertOrderToOrderSet(Order order, OrderSet detail, CompanyPolymerizationDto item, CompanyMealAddressDto mealAddress, String detailSetNo, String detailSetNoDescription) {
        detail.setDetailDeliveryDate(new Date());
        detail.setId(IdGenerator.nextId());
        detail.setIsDeleted(false);
        detail.setShopId(item.getShopId());
        detail.setStatus(OrderSetStatusEnum.WAITING_FOR_PACKAGING.getCode());
        detail.setIsPrint(false);
        detail.setDeliveryStartTime(order.getDeliveryStartDate());
        detail.setDeliveryEndTime(order.getDeliveryEndDate());
        detail.setDetailSetNo(detailSetNo);
        detail.setDetailSetNoDescription(detailSetNoDescription);
        detail.setDetailDeliveryAddress(item.getDetailAddress());
        detail.setMealAddress(mealAddress.getMealAddress());
        detail.setCompanyId(mealAddress.getCompanyId());
        //detail.setBuildingId(buildingDto.getId());
        detail.setBuildingAbbreviation(item.getBuildingName());
        detail.setCompanyAbbreviation(item.getName());
        detail.setDeliveryFloor(item.getFloor());
    }


    /**
     * 得到年月日时分秒的配送时间
     *
     * @param deliveryTimeBegin
     * @return
     */
    private Date getDeliveryDate(Date deliveryTimeBegin) {
        Calendar cal = Calendar.getInstance();
        Calendar result = Calendar.getInstance();

        result.setTime(deliveryTimeBegin);
        result.set(Calendar.YEAR, cal.get(Calendar.YEAR));
        result.set(Calendar.MONTH, cal.get(Calendar.MONTH));
        result.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR));

        return result.getTime();
    }
}

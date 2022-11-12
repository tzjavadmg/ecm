package com.milisong.breakfast.scm.orderset.service;

import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.farmland.core.db.IdGenerator;
import com.farmland.core.util.BeanMapper;
import com.milisong.breakfast.scm.common.util.DateUtil;
import com.milisong.breakfast.scm.configuration.dto.ShopInterceptConfigDto;
import com.milisong.breakfast.scm.configuration.util.RedisConfigUtil;
import com.milisong.breakfast.scm.order.api.OrderService;
import com.milisong.breakfast.scm.order.dto.param.OrderDetailComboParam;
import com.milisong.breakfast.scm.order.dto.param.OrderSearch4OrderSetParam;
import com.milisong.breakfast.scm.order.dto.result.OrderDetailComboDto;
import com.milisong.breakfast.scm.order.dto.result.OrderDetailDto;
import com.milisong.breakfast.scm.order.dto.result.OrderDetailResult;
import com.milisong.breakfast.scm.order.dto.result.OrderDto;
import com.milisong.breakfast.scm.orderset.constant.NoPrefixEnum;
import com.milisong.breakfast.scm.orderset.constant.OrderSetStatusEnum;
import com.milisong.breakfast.scm.orderset.domain.OrderSetDetail;
import com.milisong.breakfast.scm.orderset.domain.OrderSetDetailGoods;
import com.milisong.breakfast.scm.orderset.mapper.OrderSetDetailGoodsMapper;
import com.milisong.breakfast.scm.orderset.mapper.OrderSetDetailMapper;
import com.milisong.dms.constant.OrderDeliveryStatus;
import com.milisong.scm.base.api.CompanyService;
import com.milisong.scm.base.dto.CompanyDto;
import com.milisong.scm.base.dto.param.CompanySearchDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderSetProcessService {
    @Autowired
    private OrderSetDetailMapper orderSetDetailMapper;
    @Autowired
    private OrderSetDetailGoodsMapper orderSetDetailGoodsMapper;

    @Autowired
    private OrderService orderService;
    @Autowired
    private CompanyService companyService;

    // 公司集单可打包的标识
    private static final String ORDERSET_FLAG = "orderset_flag_company:";
    // 集单最大金额
    private static final String ORDERSET_MAX_AMOUNT = "ordersetMaxAmount";
    // 集单的最大人数
    private static final String ORDERSET_MAX_MEMBER = "ordersetMaxMember";

    /**
     * 针对某个门店做集单处理
     */
    @Transactional
    public void executeShopSet(Long shopId, ShopInterceptConfigDto config) {
        CompanySearchDto param = new CompanySearchDto();
        param.setShopId(shopId);
        param.setPageNo(1);
        param.setPageSize(50);

        // 查询门店负责的公司信息list
        List<CompanyDto> companyList = pageSearchCompany(param);

        while (!CollectionUtils.isEmpty(companyList)) {

            processPageCompany(companyList, config, shopId);

            param.setPageNo(param.getPageNo() + 1);
            companyList = pageSearchCompany(param);
        }
    }

    /**
     * 批量处理一些公司订单
     *
     * @param companyList
     * @param companyConfig
     */
    private void processPageCompany(List<CompanyDto> companyList, ShopInterceptConfigDto config, Long shopId) {
        if (CollectionUtils.isEmpty(companyList)) {
            return;
        }
        // 循环公司
        companyList.stream().forEach(item -> {
            log.info("-----开始处理公司：{}的订单", item.getName());
            processCompanyMealAddress(item, config, shopId);
            log.info("+++++结束处理公司：{}的订单", item.getName());
        });
    }

    /**
     * 处理公司取餐点
     *
     * @param companyDto
     * @param config
     * @param shopId
     */
    private void processCompanyMealAddress(CompanyDto companyDto, ShopInterceptConfigDto config, Long shopId) {
        List<String> list = orderService.searchCompanyMealAddress(companyDto.getId());
        if (CollectionUtils.isEmpty(list)) {
            processCompanyOrder(companyDto, config, shopId);
        } else {
            list.forEach(item -> {
                log.info("---开始处理公司：{}取餐点为：{}的订单", companyDto.getName(), item);
                processCompanyOrder(companyDto, item, config, shopId);
            });
        }
    }

    /**
     * 处理单个公司的集单（无取餐点）
     *
     * @param companyDto
     * @param config
     * @param shopId
     */
    private void processCompanyOrder(CompanyDto companyDto, ShopInterceptConfigDto config, Long shopId) {
        log.info("该公司没有取餐点，进行没有取餐点的处理逻辑");
        processCompanyOrder(companyDto, null, config, shopId);
        log.info("+++++处理公司：{}子集单完成", companyDto.getName());
    }

    /**
     * 处理单个公司的集单（有取餐点）
     *
     * @param companyDto
     * @param config
     * @param shopId
     */
    private void processCompanyOrder(CompanyDto companyDto, String mealAddress, ShopInterceptConfigDto config, Long shopId) {
        Date deliveryBeginDate = getDeliveryDate(config.getDeliveryTimeBegin());

        // 公司订单查询条件
        OrderSearch4OrderSetParam companyParam = new OrderSearch4OrderSetParam();
        companyParam.setCompanyId(companyDto.getId());
        companyParam.setDeliveryDate(deliveryBeginDate);
        if (StringUtils.isNotBlank(mealAddress)) {
            companyParam.setDeliveryRoom(mealAddress);
        }

        String cacheOrdersetMaxAmount = RedisConfigUtil.getShopSingleConfigByKey(shopId, ORDERSET_MAX_AMOUNT);
        if (StringUtils.isBlank(cacheOrdersetMaxAmount)) {
            throw new RuntimeException("");
        }
        String cacheOrdersetMaxMember = RedisConfigUtil.getShopSingleConfigByKey(shopId, ORDERSET_MAX_MEMBER);
        if (StringUtils.isBlank(cacheOrdersetMaxMember)) {
            cacheOrdersetMaxMember = "100";
        }

        // 单个集单的最大商品金额
        BigDecimal ordersetMaxAmount = new BigDecimal(cacheOrdersetMaxAmount);
        // 单个集单的最大人数
        Integer ordersetMaxMember = new Integer(cacheOrdersetMaxMember);

        // 集单的编号自增的key
        String setNoKey = "ORDER_SET_INCRT:NO:".concat(DateUtil.formatNowDate(DateUtil.YYYYMMDD));
        // 集单的编号描述自增的key
        String setNoDescKey = "ORDER_SET_INCRT:DESC:".concat(String.valueOf(shopId)).concat(":")
                .concat(DateUtil.formatNowDate(DateUtil.YYYYMMDD));

        // 查询单个公司订单
        List<OrderDto> detailList = orderService.search4OrderSet(companyParam);

        if (!CollectionUtils.isEmpty(detailList)) {
            // 记录当前任意一个订单信息
            OrderDto orderDto = detailList.get(0);

            // 该公司所有的子集单
            List<List<OrderDetailResult>> allDetail = new ArrayList<>();
            // 单个子集单的数据
            List<OrderDetailResult> goodsList = new ArrayList<>();

            // 剩余未分配的订单里的部分商品
            List<OrderDetailDto> surplusDetails = new ArrayList<>();

            // 公司id
            String company = String.valueOf(companyDto.getId());

            // 遍历订单商品信息
            for (OrderDto detail : detailList) {
                recursiveDetailGoods(detail, goodsList, allDetail, ordersetMaxAmount, company, ordersetMaxMember, surplusDetails, false);
            }
            // 说明还有剩余不满max个的需要单独打一个包
            if (!CollectionUtils.isEmpty(goodsList)) {
                allDetail.add(goodsList);
            }

            // 正式生成子集单
            for (List<OrderDetailResult> detail : allDetail) {
                Long setNoIndex = RedisCache.incrBy(setNoKey, 1);
                RedisCache.expire(setNoKey, 1, TimeUnit.DAYS);
                Long setNoDescIndex = RedisCache.incrBy(setNoDescKey, 1);
                RedisCache.expire(setNoDescKey, 1, TimeUnit.DAYS);

                // 子集单号
                String detailSetNo = generateDetailSetNo(setNoIndex.intValue());
                // 子集单号描述
                String detailSetNoDescription = generateDetailSetNoDescription(setNoDescIndex.intValue());

                // 生成子集单
                generateDetailSetInfo(companyDto, mealAddress, detail, detailSetNo, detailSetNoDescription, orderDto);
            }

            // 订单列表
            List<String> orderNoList = detailList.stream().map(data -> data.getOrderNo()).collect(Collectors.toList());
            // 更新订单处理状态
            this.orderService.updateOrderProcessStatus(orderNoList);
        }
        log.info("+++++处理公司：{}子集单完成", companyDto.getName());
    }

    /**
     * 递归调用来生成子集单
     *
     * @param detail
     * @param goodsList
     * @param allDetail
     * @param oneDetailSetGoods
     * @param companyMax
     */
    private void recursiveDetailGoods(OrderDto detail, List<OrderDetailResult> goodsList,
                                      List<List<OrderDetailResult>> allDetail, BigDecimal ordersetMaxAmount, String companyId,
                                      Integer ordersetMaxMember, List<OrderDetailDto> surplusDetails, boolean isSurplus) {
        // 可打包标识
        RedisCache.set(ORDERSET_FLAG.concat(companyId), "F");
        // 剩余未被分配的商品
        OrderDto surplus = null;
        // 不是剩余的要清除一下
        if (!isSurplus) {
            surplusDetails.clear();
        }

        // 当前集单里的总金额
        BigDecimal nowSum = calculationDetailSetGoodsAmount(goodsList);

        // 当前集单里的顾客数 + 当前订单
        Integer memberCount = calculationDetailSetMemberCount(goodsList, detail);

        // 订单拆完的标识
        boolean surplusBreak = false;

        // 当前订单实付金额已经超过了子集单的最大装载 或者人数超过了阀值
        if (detail.getActualPayAmount().add(nowSum).compareTo(ordersetMaxAmount) > 0
                || memberCount.compareTo(ordersetMaxMember) > 0) {
            // 可以打包了
            RedisCache.set(ORDERSET_FLAG.concat(companyId), "T");
            // 被剩下的只能分配到下一个子集单
            surplus = detail;
            // 金额超了
            if (detail.getActualPayAmount().add(nowSum).compareTo(ordersetMaxAmount) > 0) {
                if (BigDecimal.ZERO.equals(nowSum)) {
                    log.info("当前订单：{}的金额已经超过了金额阀值，需要拆单", detail.getOrderNo());
                    splitOrder(detail, goodsList, allDetail, ordersetMaxAmount, surplusDetails, companyId, isSurplus);
                    log.info("拆单完成后剩余商品数据：{}", JSONObject.toJSONString(surplusDetails));
                } else {
                    // 下一个新订单
                    if (CollectionUtils.isEmpty(surplusDetails)) {
                        log.info("当前订单：{}与之前剩余之和的金额超过了阀值，直接将之前剩余当一个新集单处理");
                        surplusBreak = true;
                    } else {
                        // 上一个订单还有剩
                        log.info("订单：{}与剩余之和的金额超过了阀值，需要再次拆单");
                        BigDecimal surplusAmount = calculatioSurplusDetailsAmount(surplusDetails);
                        log.info("当前剩余商品金额：{}", surplusAmount);
                        if (surplusAmount.compareTo(ordersetMaxAmount) > 0) {
                            log.info("当前剩余商品金额还是超过了阀值，需要再次拆单");
                            splitOrder(detail, goodsList, allDetail, ordersetMaxAmount, surplusDetails, companyId, isSurplus);
                            log.info("再次拆单完成后剩余商品数据：{}", JSONObject.toJSONString(surplusDetails));
                        } else {
                            log.info("订单：{}终于拆完了！！！！", detail.getOrderNo());
                        }
                    }
                }
            } else {
                // 人数超了
                log.info("当前订单因为人数的原因无法装入当前集单，订单号：{}", detail.getOrderNo());
                surplusDetails = orderService.getOrderDetailByOrderNo(detail.getOrderNo());
                if (CollectionUtils.isEmpty(surplusDetails)) {
                    log.warn("订单号：{}没有明细数据", detail.getOrderNo());
                    return;
                }
            }
        } else {
            List<OrderDetailDto> detailList = surplusDetails;
            if (!isSurplus) {
                detailList = orderService.getOrderDetailByOrderNo(detail.getOrderNo());
                if (CollectionUtils.isEmpty(detailList)) {
                    log.warn("订单号：{}没有明细数据", detail.getOrderNo());
                    return;
                }
            }
            detailList.forEach(item -> {
                OrderDetailResult odr = BeanMapper.map(item, OrderDetailResult.class);
                odr.setMobileNo(detail.getMobileNo());
                odr.setUserId(detail.getUserId());
                odr.setRealName(detail.getRealName());
                goodsList.add(odr);
            });
            if (isSurplus) {
                surplusDetails.clear();
            }
        }

        // 为T就表示可以打包了
        if ("T".equals(RedisCache.get(ORDERSET_FLAG.concat(companyId)))) {
            // 加到队列里
            allDetail.add(new ArrayList<>(goodsList));
            // 重置
            goodsList.clear();
        }
        // 剩余的拆完了
        if (surplusBreak) {
            // 递归调用
            recursiveDetailGoods(surplus, goodsList, allDetail, ordersetMaxAmount, companyId, ordersetMaxMember, surplusDetails, false);
        } else {
            // 有剩余待打包的递归放入下一个包裹中
            if (null != surplus || !CollectionUtils.isEmpty(surplusDetails)) {
                isSurplus = true;

                // 说明一个订单超过了最大值的阀值
                if (!CollectionUtils.isEmpty(surplusDetails)) {
                    surplus.setActualPayAmount(calculatioSurplusDetailsAmount(surplusDetails));
                }

                // 递归调用
                recursiveDetailGoods(surplus, goodsList, allDetail, ordersetMaxAmount, companyId, ordersetMaxMember, surplusDetails, true);
            } else {
                // 结束递归
                return;
            }
        }
    }

    /**
     * 计算剩余的金额
     *
     * @param surplusDetails
     * @return
     */
    private BigDecimal calculatioSurplusDetailsAmount(List<OrderDetailDto> surplusDetails) {
        BigDecimal sum = BigDecimal.ZERO;
        for (OrderDetailDto dto : surplusDetails) {
            sum = sum.add(dto.getActualPayAmount().multiply(new BigDecimal(dto.getGoodsCount())));
        }
        return sum;
    }

    /**
     * 订单拆单
     *
     * @param detail
     * @param goodsList
     * @param allDetail
     * @param ordersetMaxAmount
     * @param companyId
     * @param ordersetMaxMember
     */
    private void splitOrder(OrderDto detail, List<OrderDetailResult> goodsList, List<List<OrderDetailResult>> allDetail,
                            BigDecimal ordersetMaxAmount, List<OrderDetailDto> surplusDetails, String companyId, boolean isSurplus) {
        List<OrderDetailDto> detailList = null;
        List<OrderDetailDto> tempSurplusDetails = null;
        if (isSurplus) {
            detailList = surplusDetails;
            tempSurplusDetails = new ArrayList<>();
        } else {
            detailList = orderService.getOrderDetailByOrderNo(detail.getOrderNo());
        }

        // 当前集单里的总金额
        BigDecimal nowSum = BigDecimal.ZERO;

        if (detailList.size() == 1) {
            log.info("该订单里只有一种商品，需要对数量进行处理");
            OrderDetailDto dto = detailList.get(0);
            if (dto.getGoodsCount() == 1 || dto.getActualPayAmount().compareTo(ordersetMaxAmount) > 0) {
                throw new RuntimeException("该订单的单个商品实付金额超过了集单的最大允许金额");
            }

            // 计算数量
            int ct = 0;

            for (int i = 0; i < dto.getGoodsCount(); i++) {
                if (dto.getActualPayAmount().add(nowSum).compareTo(ordersetMaxAmount) <= 0) {
                    nowSum = nowSum.add(dto.getActualPayAmount());
                    ct = i + 1;
                } else {
                    // 将剩余的
                    OrderDetailDto surplus = new OrderDetailDto();
                    BeanUtils.copyProperties(dto, surplus);
                    surplus.setGoodsCount(dto.getGoodsCount() - ct);
                    if (isSurplus) {
                        tempSurplusDetails.add(surplus);
                    } else {
                        surplusDetails.add(surplus);
                    }

                    log.info("该订单：{}只能装下：{}个{}商品，订单中商品数：{}", detail.getOrderNo(), ct, dto.getGoodsName(), dto.getGoodsCount());

                    // 可以打包了
                    RedisCache.set(ORDERSET_FLAG.concat(companyId), "T");

                    break;
                }
            }
            // 加入打包队列
            OrderDetailResult odr = BeanMapper.map(dto, OrderDetailResult.class);
            // 设置数量
            odr.setGoodsCount(ct);
            odr.setMobileNo(detail.getMobileNo());
            odr.setUserId(detail.getUserId());
            odr.setRealName(detail.getRealName());
            goodsList.add(odr);
        } else {
            for (int i = 0; i < detailList.size(); i++) {
                OrderDetailDto dto = detailList.get(i);
                if (dto.getGoodsCount() == 1) {
                    if (dto.getActualPayAmount().add(nowSum).compareTo(ordersetMaxAmount) <= 0) {
                        nowSum = nowSum.add(dto.getActualPayAmount());

                        // 加入打包队列
                        OrderDetailResult odr = BeanMapper.map(dto, OrderDetailResult.class);
                        odr.setMobileNo(detail.getMobileNo());
                        odr.setUserId(detail.getUserId());
                        odr.setRealName(detail.getRealName());
                        goodsList.add(odr);
                    } else {
                        if (isSurplus) {
                            tempSurplusDetails.add(dto);
                        } else {
                            surplusDetails.add(dto);
                        }

                        if (i + 1 < detailList.size()) {
                            for (int k = i + 1; k < detailList.size(); k++) {
                                if (isSurplus) {
                                    tempSurplusDetails.add(detailList.get(k));
                                } else {
                                    surplusDetails.add(detailList.get(k));
                                }
                            }
                        }
                        // 可以打包了
                        RedisCache.set(ORDERSET_FLAG.concat(companyId), "T");

                        break;
                    }
                } else {
                    // 计算数量
                    int ct = 0;
                    // 能否完全装的下
                    boolean isCan = true;
                    for (int j = 0; j < dto.getGoodsCount(); j++) {
                        if (dto.getActualPayAmount().add(nowSum).compareTo(ordersetMaxAmount) <= 0) {
                            nowSum = nowSum.add(dto.getActualPayAmount());
                            ct++;
                        } else {
                            isCan = false;
                            // 可以打包了
                            RedisCache.set(ORDERSET_FLAG.concat(companyId), "T");
                            break;
                        }
                    }
                    if (isCan) {
                        // 加入打包队列
                        OrderDetailResult odr = BeanMapper.map(dto, OrderDetailResult.class);
                        odr.setMobileNo(detail.getMobileNo());
                        odr.setUserId(detail.getUserId());
                        odr.setRealName(detail.getRealName());
                        goodsList.add(odr);
                    } else {
                        if (ct > 0) {
                            // 加入打包队列
                            OrderDetailResult odr = BeanMapper.map(dto, OrderDetailResult.class);
                            odr.setGoodsCount(ct);
                            odr.setMobileNo(detail.getMobileNo());
                            odr.setUserId(detail.getUserId());
                            odr.setRealName(detail.getRealName());
                            goodsList.add(odr);
                        }

                        // 将剩余的放下轮处理
                        OrderDetailDto surplus = new OrderDetailDto();
                        BeanUtils.copyProperties(dto, surplus);
                        surplus.setGoodsCount(dto.getGoodsCount() - ct);
                        if (isSurplus) {
                            tempSurplusDetails.add(surplus);
                        } else {
                            surplusDetails.add(surplus);
                        }
                    }
                }
            }
        }

        // 将本轮处理后剩下的重新放入临时队列中
        if (isSurplus) {
            surplusDetails.clear();
            if (!CollectionUtils.isEmpty(tempSurplusDetails)) {
                surplusDetails.addAll(tempSurplusDetails);
            }
        }
    }

    /**
     * 计算子集单商品金额
     *
     * @param detail
     * @return
     */
    private BigDecimal calculationDetailSetGoodsAmount(List<OrderDetailResult> detail) {
        if (CollectionUtils.isEmpty(detail)) {
            return BigDecimal.ZERO;
        }
        BigDecimal count = BigDecimal.ZERO;
        for (OrderDetailResult item : detail) {
            count = count.add(item.getActualPayAmount().multiply(new BigDecimal(item.getGoodsCount())));
        }
        return count;
    }

    /**
     * 计算子集单商品金额
     *
     * @param detail
     * @return
     */
    private int calculationDetailSetMemberCount(List<OrderDetailResult> detailList, OrderDto detail) {
        if (CollectionUtils.isEmpty(detailList)) {
            return 1;
        }
        Set<String> set = detailList.stream().map(item -> item.getMobileNo()).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(set)) {
            return 1;
        }
        set.add(detail.getMobileNo());
        return set.size();
    }

    /**
     * 生成子集单信息
     *
     * @param buildingDto
     * @param orderCompany
     * @param goodsList
     */
    private void generateDetailSetInfo(CompanyDto companyDto, String mealAddress, List<OrderDetailResult> goodsList, String detailSetNo,
                                       String detailSetNoDescription, OrderDto orderDto) {
        log.info("-----开始保存子集单信息，公司：{}", companyDto.getName());

        OrderSetDetail detail = new OrderSetDetail();
        detail.setCompanyId(companyDto.getId());
        detail.setBuildingAbbreviation(companyDto.getBuildingName());
        detail.setCompanyAbbreviation(companyDto.getName());
        detail.setDeliveryFloor(companyDto.getFloor());
        detail.setDetailDeliveryAddress(orderDto.getDeliveryAddress());
        detail.setDetailDeliveryDate(new Date());
        detail.setDetailSetNo(detailSetNo);
        detail.setDetailSetNoDescription(detailSetNoDescription);
        detail.setId(IdGenerator.nextId());
        detail.setIsDeleted(false);
        detail.setShopId(companyDto.getShopId());
        detail.setStatus(OrderSetStatusEnum.WAITING_FOR_PACKAGING.getCode());
        detail.setIsPrint(false);
        detail.setDeliveryStartTime(orderDto.getDeliveryStartDate());
        detail.setDeliveryEndTime(orderDto.getDeliveryEndDate());
        if (StringUtils.isNotBlank(mealAddress)) {
            detail.setMealAddress(mealAddress);
        }

        // redis的自增key
        String key = "ORDER_SET_ORDER_COUPLET_INCRT:".concat(String.valueOf(companyDto.getShopId())).concat(":")
                .concat(DateUtil.formatNowDate(DateUtil.YYYYMMDD));

        // 得到这个集单里的订单号的客户联
        Map<String, String> map = getOrderCoupletNo(goodsList, key);
        RedisCache.expire(key, 1, TimeUnit.DAYS);

        // 商品数量(单品计数)
        int goodsSum = 0;
        // SKU数量（套餐算1个）
        int skuSum = 0;
        // 实付总金额
        BigDecimal actualPayAmount = BigDecimal.ZERO;

        OrderSetDetailGoods record = null;
        for (OrderDetailResult goods : goodsList) {
            actualPayAmount = actualPayAmount
                    .add(goods.getActualPayAmount().multiply(new BigDecimal(goods.getGoodsCount())));

            skuSum += goods.getGoodsCount();

            // 如果是组合商品
            if (Boolean.TRUE.equals(goods.getIsCombo())) {
                OrderDetailComboParam param = new OrderDetailComboParam();
                param.setComboGoodsCode(goods.getGoodsCode());
                param.setOrderNo(goods.getOrderNo());
                List<OrderDetailComboDto> list = orderService.searchOrderDetailComboList(param);
                if (CollectionUtils.isEmpty(list)) {
                    throw new RuntimeException("执行集单保存操作时发现组合商品没有明细");
                }
                for (OrderDetailComboDto combo : list) {
                    int ct = calculationGoodsNumber(combo, goods.getGoodsCount());
                    goodsSum += ct;

                    record = new OrderSetDetailGoods();
                    record.setDetailSetNo(detail.getDetailSetNo());

                    record.setGoodsCode(combo.getGoodsCode());
                    record.setGoodsName(combo.getGoodsName());
                    record.setGoodsNumber(ct);

                    record.setId(IdGenerator.nextId());
                    record.setIsDeleted(false);
                    record.setOrderNo(goods.getOrderNo());
                    record.setUserId(goods.getUserId());
                    record.setUserName(goods.getRealName());
                    record.setUserPhone(goods.getMobileNo());
                    record.setCoupletNo(map.get(goods.getOrderNo()));
                    //record.setActualPayAmount(goods.getActualPayAmount());

                    record.setIsCombo(Boolean.TRUE);
                    record.setComboGoodsCode(goods.getGoodsCode());
                    record.setComboGoodsName(goods.getGoodsName());
                    record.setComboGoodsCount(goods.getGoodsCount());

                    orderSetDetailGoodsMapper.insertSelective(record);
                }
            } else {
                goodsSum += goods.getGoodsCount();

                record = new OrderSetDetailGoods();
                record.setDetailSetNo(detail.getDetailSetNo());
                record.setGoodsCode(goods.getGoodsCode());
                record.setGoodsName(goods.getGoodsName());
                record.setGoodsNumber(goods.getGoodsCount());
                record.setId(IdGenerator.nextId());
                record.setIsDeleted(false);
                record.setOrderNo(goods.getOrderNo());
                record.setUserId(goods.getUserId());
                record.setUserName(goods.getRealName());
                record.setUserPhone(goods.getMobileNo());
                record.setCoupletNo(map.get(goods.getOrderNo()));
                record.setActualPayAmount(goods.getActualPayAmount());
                record.setIsCombo(Boolean.FALSE);

                orderSetDetailGoodsMapper.insertSelective(record);
            }

        }
        detail.setGoodsSum(goodsSum);
        detail.setSkuSum(skuSum);
        detail.setActualPayAmount(actualPayAmount);
        detail.setDistributionStatus(OrderDeliveryStatus.INIT.getValue());
        detail.setIsPush(Boolean.FALSE);
        orderSetDetailMapper.insertSelective(detail);

        log.info("+++++公司：{}的子集单保存成功，子集单号：{}", companyDto.getName(), detail.getDetailSetNo());
    }

    /**
     * 计算单品的数量
     *
     * @param combo
     * @param goodsCount
     * @return
     */
    private int calculationGoodsNumber(OrderDetailComboDto combo, Integer goodsCount) {
        if (goodsCount.equals(combo.getComboGoodsCount())) {
            return combo.getGoodsCount();
        }
        BigDecimal one = new BigDecimal(combo.getGoodsCount());
        return one.divide(new BigDecimal(combo.getComboGoodsCount())).multiply(new BigDecimal(goodsCount)).intValue();
    }

    /**
     * 生成集单里订单的订单联的编号
     *
     * @param goodsList
     * @param key
     * @return
     */
    private Map<String, String> getOrderCoupletNo(List<OrderDetailResult> goodsList, String key) {
        Map<String, String> map = new HashMap<>();
        for (OrderDetailResult goods : goodsList) {
            if (map.get(goods.getOrderNo()) == null) {
                map.put(goods.getOrderNo(),
                        NoPrefixEnum.ORDER.getCode().concat(leftFillInZero(RedisCache.incrBy(key, 1).intValue(), 4)));
            }
        }
        return map;
    }

    /**
     * 生成子集单编号
     *
     * @param setNo
     * @param index
     * @return
     */
    private String generateDetailSetNo(int index) {
        return NoPrefixEnum.ORDERSET.getCode().concat(DateUtil.formatNowDate(DateUtil.YYYYMMDD))
                .concat(leftFillInZero(index, 6));
    }

    /**
     * 生成子集单编号的描述
     *
     * @param setNo
     * @param index
     * @return
     */
    private String generateDetailSetNoDescription(int index) {
        return NoPrefixEnum.ORDERSET.getCode().concat(leftFillInZero(index, 4));
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
     * 分页查询公司信息
     *
     * @param param
     * @return
     */
    private List<CompanyDto> pageSearchCompany(CompanySearchDto param) {
        ResponseResult<Pagination<CompanyDto>> result = companyService.pageSearch(param);
        if (!result.isSuccess()) {
            throw new RuntimeException("分页查询公司失败：" + result.getMessage());
        }
        return result.getData().getDataList();
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

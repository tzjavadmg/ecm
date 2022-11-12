package com.milisong.pos.production.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.farmland.core.distrib.LockProvider;
import com.farmland.core.util.BeanMapper;
import com.milisong.pos.production.api.PosProductionService;
import com.milisong.pos.production.constant.OrderSetStatusEnum;
import com.milisong.pos.production.constant.ProductionFlagEnum;
import com.milisong.pos.production.constant.RedisKeyConstant;
import com.milisong.pos.production.domain.OrderSetDetail;
import com.milisong.pos.production.domain.OrderSetDetailExample;
import com.milisong.pos.production.domain.OrderSetDetailGoods;
import com.milisong.pos.production.dto.result.OrderSetGoodsInfoResult;
import com.milisong.pos.production.dto.result.OrderSetInfoResult;
import com.milisong.pos.production.mapper.BaseOrderSetDetailMapper;
import com.milisong.pos.production.mapper.OrderSetDetailGoodsMapper;
import com.milisong.pos.production.mapper.OrderSetDetailMapper;
import com.milisong.pos.production.mq.dto.OrderSetDetailDto;
import com.milisong.pos.production.mq.dto.OrderSetDetailGoodsDto;
import com.milisong.pos.production.mq.dto.OrderSetProductionMsg;
import com.milisong.pos.production.param.PosProductionParam;
import com.milisong.pos.production.service.help.DelayQueueService;
import com.milisong.pos.production.util.ConfigRedisUtil;
import com.milisong.pos.production.util.MqSendUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author created by benny
 * @date 2018年10月25日---下午9:24:32
 */
@Slf4j
@Service
public class PosProductionServiceImpl implements PosProductionService {
    @Autowired
    private BaseOrderSetDetailMapper baseOrderSetDetailMapper;
    @Autowired
    private OrderSetDetailGoodsMapper orderSetDetailGoodsMapper;
    @Autowired
    private OrderSetDetailMapper orderSetDetailMapper;
    @Autowired
    private DelayQueueService delayQueueService;

    @Override
    @Transactional
    public Long saveOrderSetInfo(String msg) {
        log.info("保存集单的信息，数据：{}", msg);
        OrderSetProductionMsg msgDto = JSONObject.parseObject(msg, OrderSetProductionMsg.class);

        RLock lock = LockProvider.getLock(RedisKeyConstant.getOrderSetLockKey(msgDto.getOrderSet().getDetailSetNo()));

        try {
            lock.lock(10, TimeUnit.SECONDS);
            OrderSetDetail orderSet = baseOrderSetDetailMapper.selectByPrimaryKey(msgDto.getOrderSet().getId());
            if (null != orderSet) {
                return orderSet.getShopId();
            }

            orderSet = BeanMapper.map(msgDto.getOrderSet(), OrderSetDetail.class);
            orderSet.setStatus(OrderSetStatusEnum.WATTING_ORDER_1.getValue());
            orderSet.setUpdateTime(null);

            List<OrderSetDetailGoods> goods = new ArrayList<>(msgDto.getGoods().size());
            msgDto.getGoods().stream().forEach(item -> {
                OrderSetDetailGoods good = BeanMapper.map(item, OrderSetDetailGoods.class);
                goods.add(good);
            });

            // 先存库
            baseOrderSetDetailMapper.insertSelective(orderSet);
            orderSetDetailGoodsMapper.insertBatchSelective(goods);

            // 入redis队列
            Long shopId = orderSet.getShopId();
            //RedisCache.lLeftPush(RedisKeyConstant.getOrderSetAllListKey(shopId), msg);
            RedisCache.zAdd(RedisKeyConstant.getOrderSetAllListKey(shopId), msg, getSetNo(orderSet));

            return shopId;
        } catch (Throwable e) {
            log.error("保存集单入库和入redis异常", e);
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean checkOrderSetSequence(String msg) {
        log.info("开始校验集单顺序-------------");
        OrderSetProductionMsg msgDto = JSONObject.parseObject(msg, OrderSetProductionMsg.class);
        RLock lock = LockProvider.getLock(RedisKeyConstant.getShopOrderSetSequenceLockKey(msgDto.getOrderSet().getShopId()));

        try {
            lock.lock(10, TimeUnit.SECONDS);
            if (canProduction(msgDto.getOrderSet())) {
                String key = RedisKeyConstant.getShopProducedOrderSetKey(msgDto.getOrderSet().getShopId());
                long now = getSetNo(msgDto.getOrderSet());
                RedisCache.set(key, String.valueOf(now));
                RedisCache.expire(key, 1, TimeUnit.DAYS);
                return true;
            } else {
                log.warn("当前集单：{}顺序不对，需要等待", msgDto.getOrderSet().getDetailSetNo());
                return false;
            }
        } catch (Throwable e) {
            log.error("保存集单入库和入redis异常", e);
            return true;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void notifyProduction(Long shopId) {
        log.info("通知生产，门店id：{}", shopId);

        String productionFlag = RedisKeyConstant.getProductionFlagKey(shopId);
        if (StringUtils.isBlank(productionFlag)) {
            productionFlag = ProductionFlagEnum.RUN.getValue();
            RedisCache.setEx(productionFlag, productionFlag, 1, TimeUnit.DAYS);
        }
        if (ProductionFlagEnum.PAUSE.getValue().equals(productionFlag)) {
            log.warn("门店：{}已设置为生产暂停", shopId);
            return;
        }

        RLock lock = LockProvider.getLock(RedisKeyConstant.getOrderSetProductionLockKey(shopId));

        try {
            lock.lock(10, TimeUnit.SECONDS);
            // 最大产能
            int maxOutput = 10;
            try {
                maxOutput = ConfigRedisUtil.getMaxOutput(shopId);
            } catch (Exception e) {
                log.error("获取最大产能异常", e);
            }

            // 处理最大产能
            processMaxOutput(maxOutput, shopId);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void countdown(String msg) {
        log.info("开始计算超时时间：{}", msg);
        OrderSetProductionMsg msgDto = JSONObject.parseObject(msg, OrderSetProductionMsg.class);
        Long shopId = msgDto.getOrderSet().getShopId();

        RLock lock = LockProvider.getLock(RedisKeyConstant.getShopTimeoutLockKey(shopId));
        try {
            lock.lock(10, TimeUnit.SECONDS);

            // 设置超时时间
            setTimeout(shopId, msgDto);

        } finally {
            lock.unlock();
        }
    }

    @Override
    @Transactional
    public ResponseResult<String> updateOrderSetStatusByNo(Long shopId, String orderSetNo, Byte orderStatus, String updateBy) {
        log.info("生产完成{},{}", orderSetNo, orderStatus);

        RLock lock = LockProvider.getLock(RedisKeyConstant.getShopTimeoutLockKey(shopId));
        try {
            lock.lock(10, TimeUnit.SECONDS);
            int updateResult = updateOrdersetStatus(orderSetNo, orderStatus, updateBy);
            if (0 == updateResult) {
                return ResponseResult.buildSuccessResponse();
            }
            //将订单从生成列表中移除
            List<OrderSetProductionMsg> list = getOrderSetProductionList(shopId);
            log.info("获取到生产中的队列信息{}", JSON.toJSON(list));
            if (!CollectionUtils.isEmpty(list)) {
                OrderSetProductionMsg orderSetDetailDtoToDelete = null;
                //查找出符合条件的 订单
                for (OrderSetProductionMsg orderSetProductionMsg : list) {
                    //log.info("遍历生产队列中的订单{}",JSON.toJSON(orderSetProductionMsg));
                    OrderSetDetailDto orderSetDetailDto = orderSetProductionMsg.getOrderSet();
                    if (orderSetDetailDto.getDetailSetNo().equals(orderSetNo)) {
                        orderSetDetailDtoToDelete = orderSetProductionMsg;
                        break;
                    }
                }
                //查找就去移除
                log.info("需要移除的信息{}", JSON.toJSONString(orderSetDetailDtoToDelete));
                if (orderSetDetailDtoToDelete != null) {
                    log.info("---------------key:{},value:{}", RedisKeyConstant.getOrderSetLineUpListKey(shopId), transformJsonToSpecialStr(JSON.toJSONString(orderSetDetailDtoToDelete)));
                    RedisCache.lRemove(RedisKeyConstant.getOrderSetLineUpListKey(shopId), 0, this.transformJsonToSpecialStr(JSON.toJSONString(orderSetDetailDtoToDelete)));
                    RedisCache.incrBy(RedisKeyConstant.getOrderSetLineUpCountKey(shopId), orderSetDetailDtoToDelete.getOrderSet().getGoodsSum() * -1);
                    RedisCache.delete(RedisKeyConstant.getOrderSetTimeoutTtlKey(shopId, orderSetNo));

                    // 设置已经生产到集单
                    long now = getSetNo(orderSetDetailDtoToDelete.getOrderSet()) + 1;
                    RedisCache.set(RedisKeyConstant.getShopProducedOrderSetKey(orderSetDetailDtoToDelete.getOrderSet().getShopId()), String.valueOf(now));

                    if (!"system_系统".equals(updateBy) && isRun(shopId)) {
                        delayQueueService.cancel(orderSetDetailDtoToDelete.getOrderSet().getId());
                    }
                }
            }
            // 获取下一个集单
            OrderSetProductionMsg dto = getFirstOrderSet(shopId);
            if (null != dto) {
                this.setTimeout(shopId, dto);
            }
            // 通知继续生产
            this.notifyProduction(shopId);

            return ResponseResult.buildSuccessResponse();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Pagination<OrderSetInfoResult> getOrderSetListByStatus(PosProductionParam posProductionParam) {
        Pagination<OrderSetInfoResult> page = new Pagination<OrderSetInfoResult>();
        //判断是否取生产中的订单
        if (posProductionParam.getOrderStatus().equals(OrderSetStatusEnum.PRODUCTION_ORDER_2.getValue())) {
            page.setDataList(getOrderSetForProductionQueue(posProductionParam.getShopId()));
            return page;
        }
        log.info("获取待生产or生产完成的数据{}", JSON.toJSON(posProductionParam));
        Map<String, Object> mapParam = new HashMap<String, Object>();
        mapParam.put("status", posProductionParam.getOrderStatus());
        mapParam.put("shopId", posProductionParam.getShopId());
        mapParam.put("startRow", posProductionParam.getStartRow());
        mapParam.put("pageSize", posProductionParam.getPageSize());
        log.info("查询参数{}", JSON.toJSONString(mapParam));
        int count = orderSetDetailMapper.getCountByShopIdAndStatus(mapParam);
        page.setPageNo(posProductionParam.getPageNo());
        page.setPageSize(posProductionParam.getPageSize());
        page.setTotalCount(count);
        List<OrderSetInfoResult> list = orderSetDetailMapper.getListByShopIdAndStatus(mapParam);
        //log.info("查询门店生产数据结果{}",JSON.toJSONString(list));
        Map<String, OrderSetInfoResult> mapResult = new LinkedHashMap<String, OrderSetInfoResult>();
        for (OrderSetInfoResult orderSetInfoResult : list) {
            mapResult.put(orderSetInfoResult.getDetailSetNo(), orderSetInfoResult);
        }
        for (OrderSetInfoResult orderSetInfoResult : list) {
            OrderSetInfoResult info = mapResult.get(orderSetInfoResult.getDetailSetNo());
            if (info != null) {
                OrderSetGoodsInfoResult goodsInfo = new OrderSetGoodsInfoResult();
                goodsInfo.setGoodsName(orderSetInfoResult.getGoodsName());
                goodsInfo.setGoodsCode(orderSetInfoResult.getGoodsCode());
                goodsInfo.setGoodsSum(orderSetInfoResult.getGoodsNumber());
                goodsInfo.setCoupletNo(orderSetInfoResult.getCoupletNo());
                List<OrderSetGoodsInfoResult> listGoods = info.getList();
                if (CollectionUtils.isEmpty(listGoods)) {
                    listGoods = new ArrayList<OrderSetGoodsInfoResult>();
                }
                listGoods.add(goodsInfo);
                info.setList(listGoods);
            }
        }
        list.clear();
        list.addAll(mapResult.values());
        page.setDataList(list);
        return page;
    }

    @Override
    public void pauseOrRestart(Long shopId, String opUser) {
        log.info("门店：{}被{}进行暂停或者恢复生产", shopId, opUser);

        RLock lock = LockProvider.getLock(RedisKeyConstant.getProductionFlagLockKey(shopId));
        try {
            lock.lock(10, TimeUnit.SECONDS);

            String productionFlag = RedisCache.get(RedisKeyConstant.getProductionFlagKey(shopId));
            if (StringUtils.isBlank(productionFlag)) {
                productionFlag = ProductionFlagEnum.RUN.getValue();
                RedisCache.setEx(productionFlag, productionFlag, 1, TimeUnit.DAYS);
            }
            log.info("门店：{}当前的生产状态为：{}", shopId, ProductionFlagEnum.getNameByValue(productionFlag));

            if (ProductionFlagEnum.PAUSE.getValue().equals(productionFlag)) {
                log.info("暂停--->生产");
                RedisCache.set(RedisKeyConstant.getProductionFlagKey(shopId), ProductionFlagEnum.RUN.getValue());

                OrderSetProductionMsg item = getFirstOrderSet(shopId);
                if (null != item) {
                    String ttlKey = RedisKeyConstant.getOrderSetTimeoutTtlKey(shopId, item.getOrderSet().getDetailSetNo());
                    // 暂停前的ttl
                    long pauseTtl = Long.valueOf(RedisCache.get(ttlKey));
                    log.info("记录集单：{}的暂停前的ttl为：{}", item.getOrderSet().getDetailSetNo(), pauseTtl);
                    delayQueueService.setDelay(item.getOrderSet().getId(), item.getOrderSet().getDetailSetNo(), shopId, pauseTtl);
                    RedisCache.setEx(ttlKey, String.valueOf(pauseTtl), pauseTtl, TimeUnit.SECONDS);
                }
                this.notifyProduction(shopId);
            } else {
                log.info("生产--->暂停");
                RedisCache.set(RedisKeyConstant.getProductionFlagKey(shopId), ProductionFlagEnum.PAUSE.getValue());

                OrderSetProductionMsg item = getFirstOrderSet(shopId);
                if (null != item) {
                    String ttlKey = RedisKeyConstant.getOrderSetTimeoutTtlKey(shopId, item.getOrderSet().getDetailSetNo());
                    long redisTtl = RedisCache.getExpire(ttlKey, TimeUnit.SECONDS);
                    long ttl = delayQueueService.getTtl(item.getOrderSet().getId());
                    if (redisTtl != ttl) {
                        log.info("本地redis的超时时间与延迟队列的不一致，以本地redis的为准，值为：{}", redisTtl);
                        ttl = redisTtl;
                    }

                    if (ttl > 0) {
                        log.info("记录集单：{}的暂停时的ttl为：{}", item.getOrderSet().getDetailSetNo(), ttl);
                        RedisCache.setEx(ttlKey, String.valueOf(ttl), 30, TimeUnit.DAYS);
                        delayQueueService.pause(item.getOrderSet().getId());
                    }
                }
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Integer getShopProductionStatus(Long shopId) {
        String status = RedisKeyConstant.getProductionFlagKey(shopId);
        String value = RedisCache.get(status);
        if (null == value) {
            return 1;
        }
        log.info("获取门店生产状态shopId:{},status{}", shopId, value);
        return Integer.valueOf(value);
    }

    /**
     * 获取门店生产队列中的集单
     *
     * @param shopId
     * @return
     */
    private List<OrderSetProductionMsg> getOrderSetProductionList(Long shopId) {
        Long lenth = RedisCache.lLen(RedisKeyConstant.getOrderSetLineUpListKey(shopId));
        List<String> productionOrderSet = RedisCache.lRange(RedisKeyConstant.getOrderSetLineUpListKey(shopId), 0L, lenth - 1);
        if (!CollectionUtils.isEmpty(productionOrderSet)) {
            List<OrderSetProductionMsg> list = new ArrayList<OrderSetProductionMsg>();
            //获取当前门店中所有生产中的订单
            for (String string : productionOrderSet) {
                OrderSetProductionMsg obj = JSONObject.parseObject(this.transformSpecialStrToJson(string), OrderSetProductionMsg.class);
                list.add(obj);
            }
            return list;
        }
        return null;
    }

    /**
     * 获取门店生产队列中的第一个
     *
     * @param shopId
     * @return
     */
    private OrderSetProductionMsg getFirstOrderSet(Long shopId) {
        Long length = RedisCache.lLen(RedisKeyConstant.getOrderSetLineUpListKey(shopId));
        if (length > 0) {
            String item = RedisCache.lIndex(RedisKeyConstant.getOrderSetLineUpListKey(shopId), -1);
            if (StringUtils.isNotBlank(item)) {
                return JSONObject.parseObject(this.transformSpecialStrToJson(item), OrderSetProductionMsg.class);
            }
        }
        return null;
    }

    private List<OrderSetInfoResult> getOrderSetForProductionQueue(Long shopId) {
        //log.info("获取门店生产中的队列数据，门店ID：{}",shopId);
        List<OrderSetInfoResult> resultInfo = new ArrayList<OrderSetInfoResult>();
        List<OrderSetProductionMsg> list = getOrderSetProductionList(shopId);
        if (CollectionUtils.isEmpty(list)) {
            return resultInfo;
        }
        int index = 0;
        for (OrderSetProductionMsg orderSetProductionMsg : list) {
            index++;
            OrderSetInfoResult order = new OrderSetInfoResult();
            OrderSetDetailDto ordersetDetailDto = orderSetProductionMsg.getOrderSet();
            order.setDetailSetNo(ordersetDetailDto.getDetailSetNo());
            order.setDetailSetNoDescription(ordersetDetailDto.getDetailSetNoDescription());
            Long outTime = 0L;
            if (index == list.size()) {
                outTime = getOrderSetTtl(shopId, ordersetDetailDto.getDetailSetNo(), ordersetDetailDto.getId());
            }
            order.setOutTime(outTime);
            List<OrderSetGoodsInfoResult> goodsInfo = new ArrayList<OrderSetGoodsInfoResult>();
            List<OrderSetDetailGoodsDto> listGoods = orderSetProductionMsg.getGoods();
            for (OrderSetDetailGoodsDto orderSetDetailGoodsDto : listGoods) {
                OrderSetGoodsInfoResult orderSetGoodsInfo = new OrderSetGoodsInfoResult();
                orderSetGoodsInfo.setCoupletNo(orderSetDetailGoodsDto.getCoupletNo());
                orderSetGoodsInfo.setGoodsCode(orderSetDetailGoodsDto.getGoodsCode());
                orderSetGoodsInfo.setGoodsName(orderSetDetailGoodsDto.getGoodsName());
                orderSetGoodsInfo.setGoodsSum(orderSetDetailGoodsDto.getGoodsNumber());
                goodsInfo.add(orderSetGoodsInfo);
            }
            order.setList(goodsInfo);

            resultInfo.add(order);
        }
        Collections.reverse(resultInfo);
        return resultInfo;
    }

    /**
     * 得到集单的过期时间
     *
     * @param shopId
     * @param detailSetNo
     * @return
     */
    private long getOrderSetTtl(Long shopId, String detailSetNo, Long detailSetId) {
        String productionFlag = RedisCache.get(RedisKeyConstant.getProductionFlagKey(shopId));
        if (StringUtils.isBlank(productionFlag) || ProductionFlagEnum.RUN.getValue().equals(productionFlag)) {
            return delayQueueService.getTtl(detailSetId);
        } else {
            String val = RedisCache.get(RedisKeyConstant.getOrderSetTimeoutTtlKey(shopId, detailSetNo));
            if (StringUtils.isNotBlank(val)) {
                return Long.valueOf(val);
            }
        }
        return 0;
    }

    /**
     * 计算本次集单的最终超时时间（秒）
     *
     * @param shopId
     * @param msgDto
     * @return
     */
    private int calculationOrderSetTimeoutSecond(Long shopId, OrderSetProductionMsg msgDto) {
        // 单份饭的操作时间(秒)
        int singleMealTime = 24;
        try {
            String singleMealTimeStr = ConfigRedisUtil.getShopSingleConfigByKey(shopId, "singleMealTime");
            if (StringUtils.isNotBlank(singleMealTimeStr)) {
                singleMealTime = Integer.valueOf(singleMealTimeStr);
            }
        } catch (Exception e) {
            log.error("获取单份饭的操作时间的配置信息异常", e);
        }
        // 单个集单的基础时间(秒)
        int benchmarkTime = 4;
        try {
            String benchmarkTimeStr = ConfigRedisUtil.getShopSingleConfigByKey(shopId, "benchmarkTime");
            if (StringUtils.isNotBlank(benchmarkTimeStr)) {
                benchmarkTime = Integer.valueOf(benchmarkTimeStr);
            }
        } catch (Exception e) {
            log.error("获取每个集单的基础操作时间的配置信息异常", e);
        }

        // 生产这个集单需要的时间秒
        int requiredSecond = singleMealTime * msgDto.getOrderSet().getGoodsSum();
        // 生产+打印的超时时间秒
        int timeOutSecond = benchmarkTime + requiredSecond;

        return timeOutSecond;
    }

    /**
     * 将json的双引号转成特殊字符串
     *
     * @param json
     * @return
     */
    private String transformJsonToSpecialStr(String json) {
        if (StringUtils.isNotBlank(json)) {
            return json.replaceAll("\"", "&@");
        }
        return json;
    }

    /**
     * 将特殊字符的json转成正常的json
     *
     * @param specialStr
     * @return
     */
    private String transformSpecialStrToJson(String specialStr) {
        if (StringUtils.isNotBlank(specialStr)) {
            return specialStr.replaceAll("&@", "\"");
        }
        return specialStr;
    }

    /**
     * 处理最大产能（递归调用）
     *
     * @param maxOutput
     */
    private void processMaxOutput(int maxOutput, Long shopId) {
        // String msg = RedisCache.lIndex(RedisKeyConstant.getOrderSetAllListKey(shopId), -1);
        Set<String> msgs = RedisCache.zRange(RedisKeyConstant.getOrderSetAllListKey(shopId), 0, 0);
        //if(StringUtils.isBlank(msg)) {
        if (CollectionUtils.isEmpty(msgs)) {
            return;
        }
        String msg = (String) msgs.toArray()[0];

        OrderSetProductionMsg msgDto = JSONObject.parseObject(msg, OrderSetProductionMsg.class);
        // 当前集单的值
        int currentOrderSet = msgDto.getOrderSet().getGoodsSum();

        // 当前的生产中的值
        String now = RedisCache.get(RedisKeyConstant.getOrderSetLineUpCountKey(shopId));
        int nowValue = 0;
        if (StringUtils.isNotBlank(now)) {
            nowValue = Integer.valueOf(now);
        }
        log.info("门店:{}当前正在生产的数量：{}", shopId, nowValue);
        log.info("门店:{}当前集单里的商品数：{}", shopId, currentOrderSet);

        // 当前集单没超过最大产能
        if (currentOrderSet + nowValue <= maxOutput) {
            // 更新状态
            updateOrdersetStatus(msgDto.getOrderSet().getDetailSetNo(), OrderSetStatusEnum.PRODUCTION_ORDER_2.getValue(), "system");
            // 当前生产数+
            RedisCache.incrBy(RedisKeyConstant.getOrderSetLineUpCountKey(shopId), currentOrderSet);
            // 推入正在生产的队列
            RedisCache.lLeftPush(RedisKeyConstant.getOrderSetLineUpListKey(shopId), transformJsonToSpecialStr(msg));
            // 删除等待生产队列
            //RedisCache.lRightPop(RedisKeyConstant.getOrderSetAllListKey(shopId));
            //RedisCache.zRemoveRange(RedisKeyConstant.getOrderSetAllListKey(shopId), 0, 0);
            RedisCache.zRemoveRangeByScore(RedisKeyConstant.getOrderSetAllListKey(shopId), getSetNo(msgDto.getOrderSet()), getSetNo(msgDto.getOrderSet()));

            // 发MQ消息给打印机和后续的处理流程
            MqSendUtil.sendOrderSetPrintMsg(msgDto);

            // 递归调用
            processMaxOutput(maxOutput, shopId);
        } else {
            log.info("当前门店：{}已经超过最大产能，需要等待", shopId);
            return;
        }
    }

    /**
     * 设置超时时间
     *
     * @param shopId
     * @param msgDto
     */
    private void setTimeout(Long shopId, OrderSetProductionMsg msgDto) {
        OrderSetProductionMsg first = this.getFirstOrderSet(shopId);
        log.info("门店：{}当前需要设置超时时间的集单为：{}", shopId, msgDto.getOrderSet().getDetailSetNo());
        if (null != first) {
            log.info("当前门店：{}生产中第一个集单：{}", shopId, first.getOrderSet().getDetailSetNo());
        }
        if (null != first && first.getOrderSet().getId().equals(msgDto.getOrderSet().getId())) {
            if (!RedisCache.hasKey(RedisKeyConstant.getOrderSetTimeoutTtlKey(shopId, msgDto.getOrderSet().getDetailSetNo()))) {
                // 计算最终超时时间（秒）
                int timeOut = calculationOrderSetTimeoutSecond(shopId, msgDto);
                log.info("集单：{}的理论过期时间(秒)：{}", msgDto.getOrderSet().getDetailSetNo(), timeOut);
                if (isRun(shopId)) {
                    delayQueueService.setDelay(msgDto.getOrderSet().getId(), msgDto.getOrderSet().getDetailSetNo(), shopId, timeOut);
                    RedisCache.setEx(RedisKeyConstant.getOrderSetTimeoutTtlKey(shopId, msgDto.getOrderSet().getDetailSetNo()), String.valueOf(timeOut), timeOut, TimeUnit.SECONDS);
                } else {
                    RedisCache.setEx(RedisKeyConstant.getOrderSetTimeoutTtlKey(shopId, msgDto.getOrderSet().getDetailSetNo()), String.valueOf(timeOut), 30, TimeUnit.DAYS);
                }

            }
        }
    }

    /**
     * 修改集单的处理状态
     *
     * @param orderSetNo
     * @param orderStatus
     * @param updateBy
     * @return
     */
    private int updateOrdersetStatus(String orderSetNo, Byte orderStatus, String updateBy) {
        OrderSetDetail record = new OrderSetDetail();
        record.setStatus(orderStatus);
        record.setUpdateBy(updateBy);
        OrderSetDetailExample orderSetDetailExample = new OrderSetDetailExample();
        orderSetDetailExample.createCriteria().andDetailSetNoEqualTo(orderSetNo).andStatusNotEqualTo(orderStatus);

        int updateResult = baseOrderSetDetailMapper.updateByExampleSelective(record, orderSetDetailExample);
        return updateResult;
    }

    /**
     * 是否生产中
     *
     * @param shopId
     * @return
     */
    private boolean isRun(Long shopId) {
        String productionFlag = RedisCache.get(RedisKeyConstant.getProductionFlagKey(shopId));
        if (StringUtils.isBlank(productionFlag) || ProductionFlagEnum.RUN.getValue().equals(productionFlag)) {
            return true;
        }
        return false;
    }

    /**
     * 转换到集单号到数字
     *
     * @param orderSet
     * @return
     */
    private long getSetNo(OrderSetDetail orderSet) {
        String setNo = orderSet.getDetailSetNoDescription().substring(1, orderSet.getDetailSetNoDescription().length());
        return Long.valueOf(setNo);
    }

    /**
     * 转换到集单号到数字
     *
     * @param orderSet
     * @return
     */
    private long getSetNo(OrderSetDetailDto orderSet) {
        String setNo = orderSet.getDetailSetNoDescription().substring(1, orderSet.getDetailSetNoDescription().length());
        return Long.valueOf(setNo);
    }

    /**
     * 计算该集单是否可以生产（顺序问题）
     *
     * @param orderSet
     * @return
     */
    private boolean canProduction(OrderSetDetailDto orderSet) {
        String key = RedisKeyConstant.getShopProducedOrderSetKey(orderSet.getShopId());
        String value = RedisCache.get(key);
        if (StringUtils.isBlank(value)) {
            value = "1";
        }
        long before = Long.valueOf(value);
        long now = getSetNo(orderSet);
        if (now > before) {
            return false;
        }
        return true;
    }
}

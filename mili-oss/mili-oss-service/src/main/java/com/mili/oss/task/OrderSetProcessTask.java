package com.mili.oss.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.util.BeanMapper;
import com.mili.oss.api.ConfigService;
import com.mili.oss.api.OrderService;
import com.mili.oss.constant.OrderTypeEnum;
import com.mili.oss.dto.config.InterceptConfigDto;
import com.mili.oss.dto.config.ShopInterceptConfigDto;
import com.mili.oss.mq.RedisMqDto;
import com.mili.oss.util.DateUtil;
import com.mili.oss.util.RedisConfigUtil;
import com.milisong.scm.base.api.ShopService;
import com.milisong.scm.base.dto.ShopDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 集单处理的定时任务
 *
 * @author yangzhilong
 */
@Slf4j
@RestController
@RequestMapping("/orderset")
public class OrderSetProcessTask {
    // 线程池
    private static ExecutorService pool = null;
    // 线程池最大大小
    private static final int MAX_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    //
    @Autowired
    private OrderService orderService;
    @Autowired(required = false)
    private ShopService shopService;
    @Autowired
    private ConfigService configService;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${scm.sys.scmBfRedisUrl}")
    private String scmBfRedisUrl;

    @GetMapping("/task")
    public void run() {
        start(OrderTypeEnum.LUNCH.getCode());
    }

    @GetMapping("/task-breakfast")
    public void runBreakfast() {
        start(OrderTypeEnum.BREAKFAST.getCode());
    }

    private void start(Byte orderType) {
        ResponseResult<List<ShopDto>> shopResp = shopService.getShopList();
        if (null == shopResp || !shopResp.isSuccess()) {
            log.error("查询门店列表失败：{}", JSONObject.toJSONString(shopResp));
            throw new RuntimeException("查询门店列表失败");
        }
        // 查询所有的门店
        List<ShopDto> shopList = shopResp.getData();
        if (CollectionUtils.isEmpty(shopList)) {
            log.warn("查询有效的门店list时为空");
            return;
        }
        // 计算实际需要的线程池大小
        int poolSize = (shopList.size() > MAX_POOL_SIZE) ? MAX_POOL_SIZE : shopList.size();

        // 计算时间
        Date nowTime = DateUtil.getNowDateTime();
        if (null == nowTime) {
            return;
        }

        // 循序门店->判断是否是否到截单时间
        shopList.stream().forEach(item -> {
            // 取门店的截单配置
            List<ShopInterceptConfigDto> configList = null;
            try {
                if (OrderTypeEnum.BREAKFAST.getCode().equals(orderType)) {
                    configList = configService.queryHttpInterceptConfigByShopId(item.getId());
                } else {
                    configList = configService.queryInterceptConfigByShopId(item.getId());
                }
            } catch (Exception e2) {
                log.error("获取截单配置异常", e2);
                return;
            }

            // 如果门店配置为空则忽略
            if (CollectionUtils.isEmpty(configList)) {
                log.error("门店：{}未查询截单配置信息", item.getId());
                return;
            }

            configList.stream().forEach(config -> {
                boolean run = false;
                if (calculationCanStart(nowTime, config.getFirstOrdersetTime())) {
                    log.info("开始执行第一次集单，shopId：{}", item.getId());
                    run = true;
                }
                if (run) {
                    // 到了截单时间则调用service的方法去执行集单逻辑
                    if (null == pool || pool.isShutdown() || pool.isTerminated()) {
                        pool = Executors.newFixedThreadPool(poolSize);
                    }
                    pool.submit(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // 执行集单
                                orderService.executeShopSet(item.getId(), BeanMapper.map(config, InterceptConfigDto.class), orderType);
                                // 发门店生产MQ
                                orderService.sendOrderSetMq(item.getId(), false, orderType);
                            } catch (Exception e) {
                                log.error("执行门店" + item.getId() + "的集单操作时错误", e);
                            }
                        }
                    });

                }
            });
        });
    }

    /**
     * 把线程池停掉,释放资源
     */
    @GetMapping("/destroy-pool")
    public void destroyPool() {
        if (null != pool) {
            pool.shutdown();
        }
    }

    /**
     * 门店生产的MQ补偿
     *
     * @param shopId
     * @return
     */
    @GetMapping("/compensate-send-mq")
    public ResponseResult<String> compensateSendMq(@RequestParam(name = "shopId", required = false) Long shopId) {
        log.info("补偿门店生产的集单，shopId：{}", shopId);
        if (null == shopId) {
//			orderSetService.sendOrderSetMq(shopId, true);
        } else {
            ResponseResult<List<ShopDto>> shopList = shopService.getShopList();
            if (!CollectionUtils.isEmpty(shopList.getData())) {
                shopList.getData().stream().forEach(item -> {
                    log.info("开始补偿门店：{}", item.getId());
//					orderSetService.sendOrderSetMq(item.getId(), true);
                });
            }
        }
        return ResponseResult.buildSuccessResponse();
    }

    @GetMapping("/push")
    public void push() {
        ResponseResult<List<ShopDto>> shopResp = shopService.getShopList();
        if (null == shopResp || !shopResp.isSuccess()) {
            log.error("查询门店列表失败：{}", JSONObject.toJSONString(shopResp));
            throw new RuntimeException("查询门店列表失败");
        }

        // 查询所有的门店
        List<ShopDto> shopList = shopResp.getData();
        if (CollectionUtils.isEmpty(shopList)) {
            log.warn("查询有效的门店list时为空");
            return;
        }

        // 计算实际需要的线程池大小
        int poolSize = (shopList.size() > MAX_POOL_SIZE) ? MAX_POOL_SIZE : shopList.size();

        // 计算时间
        Date nowTime = DateUtil.getNowDateTime();
        if (null == nowTime) {
            return;
        }

        // 循序门店->判断是否是否到派单时间
        shopList.stream().forEach(item -> {
            // 取门店的截单配置
            List<ShopInterceptConfigDto> configList = null;
            try {
//				configList = configService.queryInterceptConfigByShopId(item.getId());
                configList = getShopInterceptConfig(item.getId());
            } catch (Exception e2) {
                log.error("获取派单配置异常", e2);
                return;
            }

            // 如果门店配置为空则忽略
            if (CollectionUtils.isEmpty(configList)) {
                log.error("门店：{}未查询派单配置信息", item.getId());
                return;
            }

            configList.stream().forEach(config -> {
                boolean run = false;
                if (calculationCanStart(nowTime, config.getDispatchTime())) {
                    log.info("开始执行派单，shopId：{}", item.getId());
                    run = true;
                }
                if (run) {
                    // 到了截单时间则调用service的方法去执行集单逻辑
                    if (null == pool || pool.isShutdown() || pool.isTerminated()) {
                        pool = Executors.newFixedThreadPool(poolSize);
                    }
                    pool.submit(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                orderService.pushSfOrderSetMq(BeanMapper.map(config, ShopInterceptConfigDto.class));
                            } catch (Exception e) {
                                log.error("执行门店" + item.getId() + "的派单操作时错误", e);
                            }
                        }
                    });

                }
            });
        });
    }


    @GetMapping("/lc-send-order")
    public void lcSendOrder() {
        ResponseResult<List<ShopDto>> shopResp = shopService.getShopList();
        if (null == shopResp || !shopResp.isSuccess()) {
            log.error("查询门店列表失败：{}", JSONObject.toJSONString(shopResp));
            throw new RuntimeException("查询门店列表失败");
        }

        // 查询所有的门店
        List<ShopDto> shopList = shopResp.getData();
        if (CollectionUtils.isEmpty(shopList)) {
            log.warn("查询有效的门店list时为空");
            return;
        }

        // 计算实际需要的线程池大小
        int poolSize = (shopList.size() > MAX_POOL_SIZE) ? MAX_POOL_SIZE : shopList.size();

        // 计算时间
        Date nowTime = DateUtil.getNowDateTime();
        if (null == nowTime) {
            return;
        }

        // 循序门店->判断是否是否到派单时间
        shopList.stream().forEach(item -> {
            // 取门店的截单配置
            List<ShopInterceptConfigDto> configList = null;
            try {
                configList = configService.queryInterceptConfigByShopId(item.getId());
            } catch (Exception e2) {
                log.error("获取派单配置异常", e2);
                return;
            }

            // 如果门店配置为空则忽略
            if (CollectionUtils.isEmpty(configList)) {
                log.error("门店：{}未查询派单配置信息", item.getId());
                return;
            }

            configList.stream().forEach(config -> {
                boolean run = false;
                if (calculationCanStart(nowTime, config.getDispatchTime())) {
                    log.info("开始执行派单，shopId：{}", item.getId());
                    run = true;
                }
                if (run) {
                    // 到了截单时间则调用service的方法去执行集单逻辑
                    if (null == pool || pool.isShutdown() || pool.isTerminated()) {
                        pool = Executors.newFixedThreadPool(poolSize);
                    }
                    pool.submit(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                orderService.pushLcSfOrderSetMq(BeanMapper.map(config, ShopInterceptConfigDto.class));
                            } catch (Exception e) {
                                log.error("执行门店" + item.getId() + "的派单操作时错误", e);
                            }
                        }
                    });

                }
            });
        });
    }

    /**
     * 补偿完成集单状态
     * PS:针对不接单导致集单没有配送的情况进行补偿
     *
     * @return
     */
    @GetMapping("/compensate-completed-orderset")
    public void compensateCompletedOrderSet() {
        orderService.compensateCompletedOrderSetTask();
    }

    /**
     * 计算时间是否可以进行集单操作了
     *
     * @param finishTime
     * @return
     */
    private boolean calculationCanStart(Date nowTime, Date finishTime) {
        if (null == finishTime) {
            return false;
        }
        if (nowTime.compareTo(finishTime) == 0) {
            return true;
        }

        return false;
    }

    /**
     * http请求获取门店截单配置
     *
     * @param shopId
     * @return
     */
    private List<ShopInterceptConfigDto> getShopInterceptConfig(Long shopId) {
        RedisMqDto redisMqDto = new RedisMqDto();
        String key = RedisConfigUtil.findShopInterceptConfigKey(shopId);
        redisMqDto.setHash(false);
        redisMqDto.setKey(key);
        String resultStr = restTemplate.postForObject(scmBfRedisUrl, redisMqDto, String.class);
        ResponseResult result = JSONObject.parseObject(resultStr, ResponseResult.class);
        if (!result.isSuccess() || null == result.getData()) {
            return Collections.EMPTY_LIST;
        }
        List<ShopInterceptConfigDto> shopInterceptConfigDtos = JSONArray.parseArray(result.getData().toString(), ShopInterceptConfigDto.class);
        return shopInterceptConfigDtos;
    }
}

package com.milisong.dms.service;

import com.alibaba.fastjson.JSONObject;
import com.farmland.core.cache.RedisCache;
import com.farmland.core.db.IdGenerator;
import com.farmland.core.distrib.LockProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.milisong.dms.api.OrderSetService;
import com.milisong.dms.api.ShopService;
import com.milisong.dms.api.ShunfengOrderService;
import com.milisong.dms.api.ShunfengOrderTaskService;
import com.milisong.dms.constant.OrderDeliveryStatus;
import com.milisong.dms.constant.ShunfengStatusConstant;
import com.milisong.dms.constant.SmsTemplateCodeConstant;
import com.milisong.dms.domain.ShunfengDeliveryLog;
import com.milisong.dms.domain.ShunfengDeliveryLogExample;
import com.milisong.dms.domain.ShunfengOrder;
import com.milisong.dms.dto.httpdto.NotifyOrderSetQueryResult;
import com.milisong.dms.dto.httpdto.ShopDto;
import com.milisong.dms.dto.orderset.OrderSetDetailDto;
import com.milisong.dms.dto.shunfeng.*;
import com.milisong.dms.mapper.ShunfengDeliveryLogMapper;
import com.milisong.dms.mapper.ShunfengOrderExtMapper;
import com.milisong.dms.mapper.ShunfengOrderMapper;
import com.milisong.dms.mq.MqMessageShunfengProducer;
import com.milisong.dms.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zhaozhonghui
 * @date 2018-10-26
 */
@Slf4j
@Service
public class ShunfengOrderTaskServiceImpl implements ShunfengOrderTaskService {
    @Resource
    private ShunfengDeliveryLogMapper shunfengDeliveryLogMapper;
    @Resource
    private ShunfengOrderMapper shunfengOrderMapper;
    @Resource
    private ShunfengOrderExtMapper shunfengOrderExtMapper;
    @Autowired
    private ShunfengOrderService shunfengOrderService;
    @Autowired
    private MqMessageShunfengProducer shunfengProducer;
    @Autowired
    private SmsTemplate smsTemplate;
    @Autowired
    private EmailTemplate emailTemplate;
    @Autowired
    private ShopService shopService;
    @Autowired
    private SfOrderBaseDto sfOrderBaseDto;
    @Autowired
    private OrderSetService orderSetService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkSfOrder() {
        RLock lock = LockProvider.getLock(ShunfengRedisKeyUtil.getSfTaskLock());
        boolean flag = lock.tryLock();
        if (!flag) {
            log.warn("???????????????????????????");
            return;
        }
        try {
            Date dayBeging = DateUtils.getDayBeging();
            Date dayEnd = DateUtils.getDayEnd();
            // ?????? ?????? ???????????? ?????? ??????????????????????????????
            ShunfengDeliveryLogExample shunfengDeliveryLogExample = new ShunfengDeliveryLogExample();
            shunfengDeliveryLogExample.createCriteria().andTriggeringTimeBetween(dayBeging, dayEnd).andStatusEqualTo((byte) 1).andTypeEqualTo((byte) 1);
            List<ShunfengDeliveryLog> sfDeliveryLogs = shunfengDeliveryLogMapper.selectByExample(shunfengDeliveryLogExample);
            if (sfDeliveryLogs.size() > 0) {
                log.info("???????????????????????????????????????...");
                sfDeliveryLogs.stream().forEach(deliveryLog -> {
                    String param = deliveryLog.getParam();
                    JSONObject jsonParam = JSONObject.parseObject(param);
                    SfOrderReqDto sfOrderReqDto = JSONObject.parseObject(jsonParam.toJSONString(), SfOrderReqDto.class);

                    sfOrderReqDto.setPushTime(System.currentTimeMillis() / 1000);  // ??????????????????
                    List<SfOrderCompanyInfoDto> companyDtoList = sfOrderReqDto.getCompanyDtoList();
                    sfOrderReqDto.setCompanyDtoList(null);
                    String jsonStr;
                    try {
                        // ??????????????????
                        jsonStr = JsonUtil.toUnderlineJSONString(sfOrderReqDto);
                    } catch (JsonProcessingException e) {
                        log.error("???????????? ??????????????????????????????!", e);
                        return;
                    }
                    String url = deliveryLog.getDeliveryUrl();
                    JSONObject result = shunfengOrderService.postSendMsg(url, jsonStr);
                    Integer errorCode = result.getInteger("error_code");
                    if (errorCode.equals(0)) {
                        deliveryLog.setStatus((byte) 0);
                        JSONObject sfResult = result.getJSONObject("result");
                        String sfOrderId = sfResult.getString("sf_order_id");
                        // ??????????????????
                        try {
                            ShunfengOrder shunfengOrder = new ShunfengOrder();
                            shunfengOrder.setId(IdGenerator.nextId());
                            shunfengOrder.setBusinessType(deliveryLog.getBusinessType());
                            shunfengOrder.setDistributeTime(new Date());
                            shunfengOrder.setDeliveryDate(new Date());
                            shunfengOrder.setDeliveryAddress(sfOrderReqDto.getReceive().getUserAddress());
                            shunfengOrder.setSfOrderId(Long.valueOf(sfOrderId));
                            shunfengOrder.setShopOrderId(Long.valueOf(sfOrderReqDto.getShopOrderId()));
                            shunfengOrder.setExpectTime(new Date(sfOrderReqDto.getExpectTime() * 1000));
                            shunfengOrderMapper.insertSelective(shunfengOrder);

                            // ??????mq
                            DeliveryOrderMqDto orderMqDto = new DeliveryOrderMqDto();
                            orderMqDto.setUpdateTime(new Date());
                            orderMqDto.setDetailSetId(sfOrderReqDto.getShopOrderId());
                            orderMqDto.setStatus(OrderDeliveryStatus.DISPATCHED_ORDER.getValue());
                            orderMqDto.setOrderType(deliveryLog.getBusinessType());
                            shunfengProducer.sendShunfengBackMsg(orderMqDto);

                            // ??????????????????
                            shunfengOrderService.cacheSfOrder(sfOrderReqDto, companyDtoList, sfOrderId);
                        } catch (Exception e) {
                            log.error("???????????????????????????????????????????????????!", e);
                        }
                    } else {
                        deliveryLog.setStatus((byte) 2);
                        if (!StringUtils.isBlank(sfOrderReqDto.getShopOrderId())) {
                            shunfengOrderService.sendSmsMsg(sfOrderReqDto.getShopOrderId(), sfOrderBaseDto.getNotifyMobile(), deliveryLog.getBusinessType());
                        } else {
                            log.error("??????????????????????????????");
                        }
                    }
                    deliveryLog.setResult(JSONObject.toJSONString(result));
                    shunfengDeliveryLogMapper.updateByPrimaryKeySelective(deliveryLog);
                });
                log.info("???????????????????????????????????????...");
            }
        } finally {
            if (lock.isLocked()) {
                lock.unlock();
            }
        }

    }

    @Override
    public void updateOrderStatus2Finish() {
        Date now = new Date();
        List<SfOrderRspDto> lunchOrderMqDtos = shunfengOrderExtMapper.selectSfOrderByCreateTime(DateUtils.getLastDayBeging(now), DateUtils.getDayBeging(), OrderDeliveryStatus.BusinessType.LUNCH.getValue());
        if (lunchOrderMqDtos.size() > 0) {
            log.info("?????????????????????????????????????????????...");
            lunchOrderMqDtos.stream().forEach(order -> {
                DeliveryOrderMqDto lunch = new DeliveryOrderMqDto();
                lunch.setStatus(OrderDeliveryStatus.COMPLETED.getValue());
                lunch.setUpdateTime(new Date());
                lunch.setDetailSetId(order.getDetailSetId());
                shunfengProducer.sendShunfengBackMsg(lunch);        // ??????????????????

                ShunfengOrder shunfengOrder = new ShunfengOrder();
                shunfengOrder.setId(order.getId());
                shunfengOrder.setArrivedTime(new Date());
                shunfengOrderMapper.updateByPrimaryKeySelective(shunfengOrder);
            });
        }
        List<SfOrderRspDto> breakFastOrderMqDtos = shunfengOrderExtMapper.selectSfOrderByCreateTime(DateUtils.getLastDayBeging(now), DateUtils.getDayBeging(), OrderDeliveryStatus.BusinessType.BREAKFAST.getValue());
        breakFastOrderMqDtos.stream().forEach(order -> {
            DeliveryOrderMqDto breakfast = new DeliveryOrderMqDto();
            breakfast.setStatus(OrderDeliveryStatus.COMPLETED.getValue());
            breakfast.setUpdateTime(new Date());
            breakfast.setDetailSetId(order.getDetailSetId());
            breakfast.setOrderType(OrderDeliveryStatus.BusinessType.BREAKFAST.getValue());
            shunfengProducer.sendShunfengBackMsg(breakfast);   // ??????????????????

            ShunfengOrder shunfengOrder = new ShunfengOrder();
            shunfengOrder.setId(order.getId());
            shunfengOrder.setArrivedTime(new Date());
            shunfengOrderMapper.updateByPrimaryKeySelective(shunfengOrder);
        });
        log.info("?????????????????????????????????????????????...");
    }

    private Date getSFOrderFinishTime(String detailSetId) {
        String sfOrderKey = ShunfengRedisKeyUtil.getSfOrderKey(detailSetId);
        Object obj = RedisCache.hGet(sfOrderKey, ShunfengRedisKeyUtil.SF_HASH_KEY);
        if (null != obj) {
            SfOrderRedisDto sfOrderRedisDto = JSONObject.parseObject(obj.toString(), SfOrderRedisDto.class);
            SfOrderStatusReqDto sfOrderReqDto = new SfOrderStatusReqDto();
            sfOrderReqDto.setShopId(sfOrderRedisDto.getShopId());
            sfOrderReqDto.setShopType(ShunfengStatusConstant.ShopType.ORTHER);
            if (sfOrderBaseDto.getTestFlag()) {
                sfOrderReqDto.setShopId("3243279847393");
                sfOrderReqDto.setShopType(ShunfengStatusConstant.ShopType.SHUNFENG);
            }
            sfOrderReqDto.setOrderId(sfOrderRedisDto.getSfOrderId());
            sfOrderReqDto.setOrderType(ShunfengStatusConstant.SfOrderType.SF_ORDER);
            sfOrderReqDto.setDevId(sfOrderBaseDto.getDevId());
            sfOrderReqDto.setPushTime(System.currentTimeMillis() / 1000);

        }
        return null;
    }

    @Override
    public void checkUnconfirmedSfOrder() {
        RLock lock = LockProvider.getLock(ShunfengRedisKeyUtil.getSfTaskLock());
        boolean flag = lock.tryLock();
        if (!flag) {
            log.warn("???????????????????????????");
            return;
        }
        try {
            // sql????????????????????????????????????
            List<SfOrderRspDto> sfOrderRspDtos = shunfengOrderExtMapper.selectUnconfirmedSfOrder(sfOrderBaseDto.getOverTime() * 60);
            if (sfOrderRspDtos.size() > 0) {
                sfOrderRspDtos.stream().forEach(sfOrder -> {
                    String sfOrderKey = ShunfengRedisKeyUtil.getSfOrderKey(sfOrder.getDetailSetId());
                    if (RedisCache.hasKey(sfOrderKey)) {
                        log.info("????????????????????????????????????...");
                        Object obj = RedisCache.hGet(sfOrderKey, ShunfengRedisKeyUtil.SF_HASH_KEY);
                        SfOrderRedisDto sfOrderRedisDto = JSONObject.parseObject(obj.toString(), SfOrderRedisDto.class);
                        String  shopId = sfOrderRedisDto.getShopId();
                        ShopDto shopDto = getShopById(shopId);
                        if (null != shopDto) {
                            // ?????????????????????????????????
                            notifyStaff(shopDto.getName(), sfOrderRedisDto.getDescription(), sfOrder.getDistributeTime(), sfOrder.getBusinessType());
                            ShunfengOrder shunfengOrder = new ShunfengOrder();
                            shunfengOrder.setId(sfOrder.getId());
                            shunfengOrder.setNotifyStatus((byte) 1);
                            shunfengOrderMapper.updateByPrimaryKeySelective(shunfengOrder);

                        }
                    }
                });
                log.info("????????????????????????????????????...");
            }
        } finally {
            if (lock.isLocked()) {
                lock.unlock();
            }
        }

    }

    @Override
    public void checkOverTimeSfOrder() {
        List<ShunfengOrder> sforderList = shunfengOrderExtMapper.selectOverTimeSfOrder();
        if (!sforderList.isEmpty()) {
            log.info("???????????????????????????????????????...");
            sforderList.stream().forEach(sfOrder -> {
                ShopDto shop;
                String sfOrderKey = ShunfengRedisKeyUtil.getSfOrderKey(String.valueOf(sfOrder.getShopOrderId()));
                Object obj = RedisCache.hGet(sfOrderKey, ShunfengRedisKeyUtil.SF_HASH_KEY);
                if (null != obj) {
                    SfOrderRedisDto sfOrderRedisDto = JSONObject.parseObject(obj.toString(), SfOrderRedisDto.class);
                    shop = getShopById(sfOrderRedisDto.getShopId());
                    if (null != shop) {
                        notifyOverTimeMsg(shop.getName(), sfOrderRedisDto.getDescription(), sfOrder);
                    }
                } else {
                    NotifyOrderSetQueryResult result = orderSetService.getOrderSetInfoByDetailSetNo(null, sfOrder.getShopOrderId(), sfOrder.getBusinessType());
                    if (null == result) {
                        return;
                    }
                    OrderSetDetailDto orderSet = result.getOrderSet();
                    shop = getShopById(String.valueOf(orderSet.getShopId()));
                    if (null != shop) {
                        notifyOverTimeMsg(shop.getName(), orderSet.getDetailSetNoDescription(), sfOrder);
                    }
                }
            });
            log.info("???????????????????????????????????????...");
        }
    }

//    @Override
//    public void runDelayTask(Integer delayMinute) {
//        String todayKey = ShunfengRedisKeyUtil.DELAY_LIST_TIME_KEY_PREFIX.concat(DateUtils.format(new Date(), "yyyyMMdd"));
//        Set<String> set = RedisCache.keys(todayKey.concat("*"));
//        if (null != set && !set.isEmpty()) {
//            set.stream().forEach(item -> {
//                String value = RedisCache.get(item);
//                log.info("????????????????????????:{},???????????????{}", item, value);
//
//                if (canStart(value, delayMinute)) {
//                    // ??????list???key
//                    String comppleteKey = ShunfengRedisKeyUtil.DELAY_LIST_KEY_PREFIX.concat(item.substring(item.indexOf(":") + 1));
//                    Long lenth = RedisCache.lLen(comppleteKey);
//                    List<String> list = RedisCache.lRange(comppleteKey, 0L, lenth - 1);
//                    if (null != list && !list.isEmpty()) {
//                        list.stream().forEach(data -> {
//                            shunfengOrderService.createOrder(JSONObject.parseObject(data, SfOrderReqDto.class), OrderDeliveryStatus.BusinessType.BREAKFAST.getValue());
//                        });
//                    }
//                }
//            });
//        }
//    }

    /**
     * ???????????????????????????
     *
     * @param value
     * @param delayMinute
     * @return
     */
    private boolean canStart(String value, Integer delayMinute) {
        Date date = DateUtils.toDate(value, "yyyyMMddHHmm");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        Calendar now = Calendar.getInstance();
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);

        now.add(Calendar.MINUTE, delayMinute * -1);

        return cal.compareTo(now) == 0;
    }

    private ShopDto getShopById(String shopId) {
        String shopKey = ShunfengRedisKeyUtil.getShopKey();
        if (RedisCache.hasKey(shopKey) && RedisCache.hGet(shopKey, shopId) != null) {
            Object obj = RedisCache.hGet(shopKey, shopId);
            ShopDto shopDto = JSONObject.parseObject(obj.toString(), ShopDto.class);
            if (null != shopDto) {
                return shopDto;
            } else {
                shopDto = shopService.getShopById(shopId);
                if (null == shopDto) {
                    log.warn("??????????????????????????????ID:{}", shopId);
                    return null;
                }
                return shopDto;
            }
        } else {
            ShopDto shopDto = shopService.getShopById(shopId);
            if (null == shopDto) {
                log.warn("??????????????????????????????ID:{}", shopId);
                return null;
            }
            RedisCache.hPut(shopKey, shopId, JSONObject.toJSONString(shopDto));
            return shopDto;
        }
    }

    // ????????????????????????
    private void notifyOverTimeMsg(String shopName, String description, ShunfengOrder sfOrder) {
        sendOverTimeSms(shopName, description, sfOrder);
        sendOverTimeMail(shopName, description, sfOrder);
        sfOrder.setNotifyStatus((byte) 2);
        shunfengOrderMapper.updateByPrimaryKeySelective(sfOrder);
    }

    // ?????????????????????
    private void notifyStaff(String shopName, String description, Date pushTime, Byte businessType) {
        sendUnconfirmedSms(shopName, description, pushTime, businessType);
        sendUnconfirmedMail(shopName, description, pushTime);
    }

    /**
     * ????????????????????????
     */
    private void sendUnconfirmedSms(String shopName, String description, Date pushTime, Byte businessType) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String pushTimeStr = dateFormat.format(pushTime);
        String templateCode = SmsTemplateCodeConstant.SHUNFENG_OVERTIME_NOTYFI;
        List<String> mobileList = Arrays.asList(sfOrderBaseDto.getNotifyMobile().split(","));
        List<String> params = new ArrayList<>();
        params.add(shopName);
        params.add(description);
        params.add(pushTimeStr);
        String singNo = "2";
        if (businessType.equals(OrderDeliveryStatus.BusinessType.BREAKFAST.getValue())) {
            singNo = "3";
        }
        smsTemplate.sendMsg(mobileList, templateCode, singNo, params);
    }

    /**
     * ????????????????????????
     */
//    @Override
    public void sendUnconfirmedMail(String shopName, String description, Date pushTime) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String pushTimeStr = dateFormat.format(pushTime);
        String title = "????????????????????????";
        String text = "???" + shopName + "???????????????" + description + "????????????????????????????????????" + pushTimeStr + "????????????????????????";
        sendMail(text, title, sfOrderBaseDto.getNotifyMail());
    }

    /**
     * ????????????????????????
     */
    private void sendOverTimeSms(String shopName, String description, ShunfengOrder sfOrder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String expectTimeStr = dateFormat.format(sfOrder.getExpectTime());
        String templateCode = SmsTemplateCodeConstant.SHUNFENG_DELIVERY_TIMEOUT;
        List<String> mobileList = Arrays.asList(sfOrderBaseDto.getNotifyMobile().split(","));
        List<String> params = new ArrayList<>();
        params.add(shopName);
        params.add(description);
        params.add(expectTimeStr);
        params.add(sfOrder.getTransporterName());
        params.add(sfOrder.getTransporterPhone());
        String singNo = "2";
        if (sfOrder.getBusinessType().equals(OrderDeliveryStatus.BusinessType.BREAKFAST.getValue())) {
            singNo = "3";
        }
        smsTemplate.sendMsg(mobileList, templateCode, singNo, params);
    }

    /**
     * ????????????????????????
     */
    public void sendOverTimeMail(String shopName, String description, ShunfengOrder sfOrder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String expectTimeStr = dateFormat.format(sfOrder.getExpectTime());
        String title = "????????????????????????";
        String text = "???" + shopName + "???????????????" + description + "?????????????????????????????????" + expectTimeStr + "????????????????????????" + sfOrder.getTransporterName() + "," + sfOrder.getTransporterPhone() + "?????????????????????";
        sendMail(text, title, sfOrderBaseDto.getNotifyMail());
    }

    private void sendMail(String body, String title, String sendTo) {
        Map<String, Object> map = new HashMap<>();
        map.put("body", body);
        map.put("sendTo", sendTo);
        map.put("title", title);
        emailTemplate.sendEmail(map);
    }

}

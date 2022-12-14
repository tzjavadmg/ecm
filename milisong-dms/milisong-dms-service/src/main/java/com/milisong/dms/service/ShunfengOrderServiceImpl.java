package com.milisong.dms.service;

import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.farmland.core.db.IdGenerator;
import com.farmland.core.distrib.LockProvider;
import com.farmland.core.util.BeanMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.milisong.dms.api.BuildingService;
import com.milisong.dms.api.OrderSetService;
import com.milisong.dms.api.ShunfengOrderService;
import com.milisong.dms.constant.OrderDeliveryStatus;
import com.milisong.dms.constant.ShunfengStatusConstant;
import com.milisong.dms.constant.SmsTemplateCodeConstant;
import com.milisong.dms.domain.ShunfengDeliveryLog;
import com.milisong.dms.domain.ShunfengOrder;
import com.milisong.dms.domain.ShunfengOrderExample;
import com.milisong.dms.dto.httpdto.BuildingDto;
import com.milisong.dms.dto.httpdto.NotifyOrderSetQueryResult;
import com.milisong.dms.dto.httpdto.OrderDetailResult;
import com.milisong.dms.dto.httpdto.OrderSetDetailGoodsDto;
import com.milisong.dms.dto.orderset.OrderSetDetailDto;
import com.milisong.dms.dto.orderset.OrderSetDetailGoodsMqDto;
import com.milisong.dms.dto.orderset.OrderSetDetailMqDto;
import com.milisong.dms.dto.orderset.OrderSetProductionMqMsg;
import com.milisong.dms.dto.shop.AddressDetailInfo;
import com.milisong.dms.dto.shunfeng.*;
import com.milisong.dms.mapper.ShunfengDeliveryLogMapper;
import com.milisong.dms.mapper.ShunfengOrderExtMapper;
import com.milisong.dms.mapper.ShunfengOrderMapper;
import com.milisong.dms.mq.MqMessageShunfengProducer;
import com.milisong.dms.util.*;
import com.milisong.scm.base.api.CompanyService;
import com.milisong.scm.base.dto.CompanyMealAddressDto;
import com.milisong.scm.base.dto.CompanyPolymerizationDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author zhaozhonghui
 * @date 2018-10-22
 */
@Slf4j
@Service
public class ShunfengOrderServiceImpl implements ShunfengOrderService {

    @Autowired
    private SfOrderBaseDto sfOrderBaseDto;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private MqMessageShunfengProducer shunfengProducer;
    @Resource
    private ShunfengDeliveryLogMapper shunfengDeliveryLogMapper;
    @Resource
    private ShunfengOrderMapper shunfengOrderMapper;
    @Resource
    private ShunfengOrderExtMapper shunfengOrderExtMapper;
    @Resource
    private SmsTemplate smsTemplate;
    @Resource
    private OrderSetService orderSetService;
    @Resource
    private BuildingService buildingService;
    @Autowired
    private CompanyService companyService;
    @Resource
    private UrlUtils urlUtils;
    private String POINT = ",";

    @Override
    public void convertOrderSetMsg(OrderSetProductionMqMsg orderSetProductionMsg, Byte businessType) {

        List<OrderSetDetailGoodsMqDto> goodsList = orderSetProductionMsg.getGoods();
        Set<String> userNameSet = goodsList.stream().map(OrderSetDetailGoodsMqDto::getUserName).collect(Collectors.toSet());
        OrderSetDetailGoodsMqDto goodsDto = goodsList.get(0);
        OrderSetDetailMqDto orderSet = orderSetProductionMsg.getOrderSet();
        SfOrderReqDto sfOrderReqDto = new SfOrderReqDto();
        SfOrderReceiveDto receiveDto = new SfOrderReceiveDto();
        receiveDto.setUserName(goodsDto.getUserName());
        receiveDto.setUserPhone(goodsDto.getUserPhone());

        AddressDetailInfo address = getCompany(orderSet.getCompanyId(), orderSet.getMealAddress());
        if (null == address) {
            log.error("?????????????????????,??????ID:{}", orderSet.getCompanyId());
            return;
        }
        List<SfOrderCompanyInfoDto> companyInfoDtos = address.getCompanyInfoDtos().stream().filter(company -> company.getCompanyMealAddress().equals(orderSet.getMealAddress())).collect(Collectors.toList());
        sfOrderReqDto.setCompanyDtoList(companyInfoDtos);
        sfOrderReqDto.setLbsType(ShunfengStatusConstant.LbsType.GAODE);

        // ????????????????????????????????????????????????????????????????????????????????????????????????
        if (sfOrderBaseDto.getTestFlag()) {
            receiveDto.setUserLat("40.002349");
            receiveDto.setUserLng("116.339392");
        } else {
            if (address != null) {
                receiveDto.setUserLat(address.getUserLat());
                receiveDto.setUserLng(address.getUserLng());
            } else {
                log.error("??????????????????????????????ID:{}", orderSet.getId());
            }
        }
        receiveDto.setUserAddress(orderSet.getDetailDeliveryAddress() + " " + (orderSet.getCompanyAbbreviation() == null ? "" : orderSet.getCompanyAbbreviation()) + " " + (orderSet.getMealAddress() == null ? "" : orderSet.getMealAddress()));
        sfOrderReqDto.setReceive(receiveDto);

        if (orderSet.getDeliveryEndTime() != null && orderSet.getDetailDeliveryDate() != null) {
            Date detailDeliveryDate = orderSet.getDetailDeliveryDate();
            Long expectTime = (detailDeliveryDate.getTime() / 1000) + (orderSet.getDeliveryEndTime().getTime() / 1000);
            sfOrderReqDto.setExpectTime(expectTime);
        }

        sfOrderReqDto.setShopId(String.valueOf(orderSet.getShopId()));
        sfOrderReqDto.setShopType(ShunfengStatusConstant.ShopType.ORTHER);
        sfOrderReqDto.setShopOrderId(String.valueOf(orderSet.getId()));
        sfOrderReqDto.setOrderSource(ShunfengStatusConstant.OrderSource.MILISONG);
        sfOrderReqDto.setOrderSequence(orderSet.getDetailSetNoDescription().substring(1));
        sfOrderReqDto.setPayType(ShunfengStatusConstant.PayType.ONLINE);
        sfOrderReqDto.setOrderTime(orderSet.getCreateTime().getTime() / 1000);
        sfOrderReqDto.setExpectTime(DateUtils.time2Date(orderSet.getDeliveryEndTime()).getTime() / 1000);
        sfOrderReqDto.setIsAppoint(ShunfengStatusConstant.AppointStatus.NORMAL);


        SfOrderDetail sfOrderDetail = new SfOrderDetail();
        sfOrderDetail.setProductNum(orderSet.getGoodsSum());
        List<SfORderProductDetail> productDetailList = new ArrayList<>();
        for (OrderSetDetailGoodsMqDto goods : goodsList) {
            SfORderProductDetail productDetail = new SfORderProductDetail();
            productDetail.setProductName(goods.getGoodsName());
            productDetail.setProductNum(goods.getGoodsNumber());
            productDetailList.add(productDetail);
        }

        sfOrderDetail.setTotalPrice(orderSet.getActualPayAmount().multiply(new BigDecimal(100)).longValue()); // ????????????(???)
        sfOrderDetail.setProductType(ShunfengStatusConstant.ProductType.FOOD);
        sfOrderDetail.setProductTypeNum(1);     // ???????????????
        sfOrderDetail.setProductDetail(productDetailList);
        if (OrderDeliveryStatus.BusinessType.LUNCH.getValue() == businessType.byteValue()) {
            sfOrderDetail.setWeightGram(sfOrderDetail.getProductNum() * sfOrderBaseDto.getLunchWeight());
        } else {
            sfOrderDetail.setWeightGram(sfOrderDetail.getProductNum() * sfOrderBaseDto.getBfWeight());
        }

        sfOrderReqDto.setOrderDetail(sfOrderDetail);
        if (!userNameSet.isEmpty()) {
            sfOrderReqDto.setRemark(userNameSet.toString());
        }
        // ????????? ????????????
//        if (OrderDeliveryStatus.BusinessType.BREAKFAST.getValue() == businessType.byteValue()) {
//            log.info("?????????????????????????????????????????????");
//            pushRedisList(orderSetProductionMsg.getOrderSet(), sfOrderReqDto);
//        } else {
//            createOrder(sfOrderReqDto, businessType);
//        }
        createOrder(sfOrderReqDto, businessType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult createOrder(SfOrderReqDto dto, Byte businessType) {
        // ??????????????????
        log.info("?????????????????????????????????:{}", JSONObject.toJSONString(dto));

        String url = sfOrderBaseDto.getCreateOrderUrl();
        dto.setDevId(sfOrderBaseDto.getDevId());
        dto.setVersion(sfOrderBaseDto.getVersion());
        dto.setPushTime(System.currentTimeMillis() / 1000);
        SfOrderDetail orderDetail = dto.getOrderDetail();
        dto.setOrderDetail(checkGoodsWeight(orderDetail));
        if (sfOrderBaseDto.getTestFlag()) {
            dto.setShopId("3243279847393");
            dto.setShopType(ShunfengStatusConstant.ShopType.SHUNFENG);
        }

        SfOrderReqDto cacheDto = BeanMapper.map(dto, SfOrderReqDto.class);
        Long expectTime = dto.getExpectTime() * 1000;
        dto.setDetailId(null);
        dto.setCompanyDtoList(null);
        // ???????????????????????????????????????5000 ?????? 4500-5000?????????
        String jsonStr;
        try {
            // ??????????????????
            jsonStr = JsonUtil.toUnderlineJSONString(dto);
        } catch (JsonProcessingException e) {
            log.error("??????????????????????????????!{}", e);
            return ResponseResult.buildFailResponse("9999", e.getMessage());
        }

        // ????????????????????????????????????
        JSONObject result = postSendMsg(url, jsonStr);
        Integer errorCode = result.getInteger("error_code");

        // ??????????????????
        ShunfengDeliveryLog deliveryLog = new ShunfengDeliveryLog();
        deliveryLog.setId(IdGenerator.nextId());
        deliveryLog.setDeliveryStatus(OrderDeliveryStatus.DISPATCHED_ORDER.getValue());
        deliveryLog.setDeliveryUrl(url);
        deliveryLog.setParam(JSONObject.toJSONString(cacheDto));
        deliveryLog.setResult(JSONObject.toJSONString(result));
        deliveryLog.setType((byte) 1);
        deliveryLog.setTriggeringTime(new Date());
        deliveryLog.setBusinessType(businessType);
        if (errorCode.equals(0)) {
            deliveryLog.setStatus((byte) 0); // ????????????????????????
            JSONObject jsonResult = result.getJSONObject("result");
            String shopOrderId = jsonResult.getString("shop_order_id");
            if (!StringUtils.isBlank(shopOrderId) && shopOrderId.equals(dto.getShopOrderId())) {
                String sfOrderId = jsonResult.getString("sf_order_id");
                if (!StringUtils.isBlank(sfOrderId)) {
                    try {

                        // ??????????????????
                        ShunfengOrder shunfengOrder = new ShunfengOrder();
                        shunfengOrder.setId(IdGenerator.nextId());
                        shunfengOrder.setBusinessType(businessType);
                        shunfengOrder.setDistributeTime(new Date());
                        shunfengOrder.setDeliveryDate(new Date());
                        shunfengOrder.setDeliveryAddress(dto.getReceive().getUserAddress());
                        shunfengOrder.setSfOrderId(Long.valueOf(sfOrderId));
                        shunfengOrder.setShopOrderId(Long.valueOf(dto.getShopOrderId()));
                        shunfengOrder.setExpectTime(new Date(expectTime));
                        shunfengOrderMapper.insertSelective(shunfengOrder);
                    } catch (Exception e) {
                        log.error("??????????????????????????????!", e);
                    }
                    // ???redis ?????????????????????????????????????????????
                    String sfOrderIdKey = ShunfengRedisKeyUtil.getSfOrderIdKey(dto.getShopOrderId());
                    RedisCache.setEx(sfOrderIdKey, sfOrderId, 1, TimeUnit.DAYS);
                    // ????????????sf????????????
                    cacheSfOrder(cacheDto, cacheDto.getCompanyDtoList(), sfOrderId);
                    // ??????MQ
                    DeliveryOrderMqDto orderMqDto = new DeliveryOrderMqDto();
                    orderMqDto.setUpdateTime(new Date());
                    orderMqDto.setDetailSetId(dto.getShopOrderId());
                    orderMqDto.setStatus(OrderDeliveryStatus.DISPATCHED_ORDER.getValue());
                    orderMqDto.setOrderType(businessType);
                    shunfengProducer.sendShunfengBackMsg(orderMqDto);
                }
            }
        } else {
            deliveryLog.setStatus((byte) 1); // ????????????????????????
            sendSmsMsg(dto.getShopOrderId(), sfOrderBaseDto.getNotifyMobile(), businessType);
        }
        // ????????????
        try {
            shunfengDeliveryLogMapper.insertSelective(deliveryLog);
        } catch (Exception e) {
            log.error("???????????????????????????????????????", e);
        }
        return ResponseResult.buildSuccessResponse(result);
    }

    private SfOrderDetail checkGoodsWeight(SfOrderDetail orderDetail) {
        Integer weightGram = orderDetail.getWeightGram();
        if (weightGram > 3000) {
            Random random = new Random();
            weightGram = random.nextInt(3000) % (3000 - 2500) + 2500;
            orderDetail.setWeightGram(weightGram);
        }
        return orderDetail;
    }

    @Override
    public void cacheSfOrder(SfOrderReqDto dto, List<SfOrderCompanyInfoDto> companyMealAddressList, String sfOrderId) {
        String sfOrderKey = ShunfengRedisKeyUtil.getSfOrderKey(dto.getShopOrderId());
        SfOrderRedisDto sfOrderRedisDto = new SfOrderRedisDto();
        sfOrderRedisDto.setCompanyInfoDtos(companyMealAddressList);
        sfOrderRedisDto.setDescription(dto.getOrderSequence());
        sfOrderRedisDto.setShopId(dto.getShopId());
        sfOrderRedisDto.setSfOrderId(sfOrderId);
        sfOrderRedisDto.setDetailSetId(dto.getShopOrderId());
        sfOrderRedisDto.setSendUserMobile(dto.getReceive().getUserPhone());
        RedisCache.hPut(sfOrderKey, ShunfengRedisKeyUtil.SF_HASH_KEY, JSONObject.toJSONString(sfOrderRedisDto));
        RedisCache.expire(sfOrderKey, 1, TimeUnit.DAYS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> sendShunfengBackMsg(String msg) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("error_code", 0);
        resultMap.put("error_msg", "success");
        SfOrderStatusBackReqDto backReqDto;
        try {
            msg = camelCaseName(msg);
            backReqDto = JSONObject.parseObject(msg, SfOrderStatusBackReqDto.class);
        } catch (Exception e) {
            log.error("???????????????????????????????????????:{}", e);
            return resultMap;
        }
        // ??????MQ
        DeliveryOrderMqDto orderMqDto = new DeliveryOrderMqDto();
        if (StringUtils.isBlank(backReqDto.getShopOrderId())) {
            log.error("???????????????ID?????????????????????:{}", backReqDto.getSfOrderId());
        }
        orderMqDto.setDetailSetId(backReqDto.getShopOrderId());
        orderMqDto.setUpdateTime(new Date());
        Byte status = convertOrderStatus(backReqDto.getOrderStatus());
        if (null == status) {
            log.error("?????????????????????");
            return resultMap;
        }
        orderMqDto.setStatus(status);
        // ??????????????????
        RLock lock = LockProvider.getLock(ShunfengRedisKeyUtil.getSfOrderStatusLockKey(backReqDto.getSfOrderId(), backReqDto.getOrderStatus()));
        try {
            lock.lock(3, TimeUnit.SECONDS);
            // ??????????????????
            ShunfengOrderExample shunfengOrderExample = new ShunfengOrderExample();
            shunfengOrderExample.createCriteria().andSfOrderIdEqualTo(backReqDto.getSfOrderId());
            List<ShunfengOrder> shunfengOrders = shunfengOrderMapper.selectByExample(shunfengOrderExample);
            if (shunfengOrders.size() == 0) {
                log.error("?????????????????????????????????,??????ID:{},??????????????????:{}", backReqDto.getSfOrderId(), JSONObject.toJSONString(backReqDto));
                return resultMap;
            }
            ShunfengOrder shunfengOrder = shunfengOrders.get(0);
            shunfengOrder.setTransporterName(backReqDto.getOperatorName());
            shunfengOrder.setTransporterPhone(backReqDto.getOperatorPhone());
            Date updateTime = new Date();
            if (backReqDto.getPushTime() != null) {
                updateTime = new Date(backReqDto.getPushTime() * 1000);
            }
            if (ShunfengStatusConstant.SfDeliveryStatus.CONFIRM.equals(backReqDto.getOrderStatus())) {  // ???????????????
                shunfengOrder.setConfirmTime(updateTime);
            } else if (ShunfengStatusConstant.SfDeliveryStatus.DISPATCHING.equals(backReqDto.getOrderStatus())) {   // ?????????
                shunfengOrder.setAchieveGoodTime(updateTime);
                // ???????????????????????? ?????????????????????
                sendSfSmsToUser(backReqDto, shunfengOrder.getBusinessType());
            } else if (ShunfengStatusConstant.SfDeliveryStatus.ARRIVE_SHOP.equals(backReqDto.getOrderStatus())) {   // ???????????????
                shunfengOrder.setArrivedShopTime(updateTime);
                // ???????????? ???????????????
                sendSmsToRider(backReqDto, shunfengOrder.getBusinessType());
            } else if (ShunfengStatusConstant.SfDeliveryStatus.FINISH.equals(backReqDto.getOrderStatus())) {         // ????????????
                shunfengOrder.setArrivedTime(updateTime);
            }
            shunfengOrderMapper.updateByPrimaryKeySelective(shunfengOrder);

            // ????????????????????????mq
            orderMqDto.setOrderType(shunfengOrder.getBusinessType());
            shunfengProducer.sendShunfengBackMsg(orderMqDto);

            // ??????????????????
            ShunfengDeliveryLog deliveryLog = new ShunfengDeliveryLog();
            deliveryLog.setId(IdGenerator.nextId());
            deliveryLog.setDeliveryStatus(status);
            deliveryLog.setParam(JSONObject.toJSONString(backReqDto));
            deliveryLog.setResult(JSONObject.toJSONString(resultMap));
            deliveryLog.setType((byte) 2);
            deliveryLog.setBusinessType(shunfengOrder.getBusinessType());
            if (ShunfengStatusConstant.SfDeliveryStatus.CANCEL.equals(backReqDto.getOrderStatus())) {
                log.error("????????????????????????:{}", backReqDto.getStatusDesc());
                // ???????????????????????????????????????????????????
                sendSmsMsg(backReqDto.getShopOrderId(), sfOrderBaseDto.getNotifyMobile(), shunfengOrder.getBusinessType());
                deliveryLog.setDeliveryStatus(OrderDeliveryStatus.CANCEL.getValue());
            }
            shunfengDeliveryLogMapper.insertSelective(deliveryLog);
        } catch (Exception e) {
            log.error("??????????????????/??????????????????????????????!", e);
        } finally {
            /*
             * ??????????????????????????????????????????????????????????????????????????????????????????????????????
             * ?????????????????????????????????????????????????????????
             */
            if (lock.isLocked()) {
                lock.unlock();
            }
        }
        return resultMap;
    }

    private void sendSfSmsToUser(SfOrderStatusBackReqDto backReqDto, Byte businessType) {

        SfOrderStatusSmsDto sfOrderStatusSmsDto = new SfOrderStatusSmsDto();
        sfOrderStatusSmsDto.setDevId(sfOrderBaseDto.getDevId());
        sfOrderStatusSmsDto.setOrderId(String.valueOf(backReqDto.getSfOrderId()));
        sfOrderStatusSmsDto.setOrderType(ShunfengStatusConstant.SfOrderType.SF_ORDER);
        sfOrderStatusSmsDto.setPushTime(System.currentTimeMillis() / 1000);
        try {
            String msg = JsonUtil.toUnderlineJSONString(sfOrderStatusSmsDto);
            JSONObject result = postSendMsg(sfOrderBaseDto.getRiderH5View(), msg);
            // ????????????
            ShunfengDeliveryLog shunfengDeliveryLog = new ShunfengDeliveryLog();
            shunfengDeliveryLog.setId(IdGenerator.nextId());
            shunfengDeliveryLog.setBusinessType(businessType);
            shunfengDeliveryLog.setTriggeringTime(new Date());
            shunfengDeliveryLog.setParam(msg);
            shunfengDeliveryLog.setResult(JSONObject.toJSONString(result));
            shunfengDeliveryLog.setDeliveryUrl(sfOrderBaseDto.getRiderH5View());
            shunfengDeliveryLog.setDeliveryStatus(OrderDeliveryStatus.RECEIVED_GOODS.getValue());
            shunfengDeliveryLog.setType((byte) 1);
            Integer errorCode = result.getInteger("error_code");
            if (errorCode.equals(0)) {
                JSONObject jsonResult = result.getJSONObject("result");
                String sfOrderIdStr = jsonResult.getString("sf_order_id");
                if (sfOrderIdStr.equals(sfOrderStatusSmsDto.getOrderId())) {
                    String riderUrl = jsonResult.getString("url");
                    log.info("????????????????????????h5:{}", riderUrl);
                    String shortUrl = urlUtils.compressToShortUrl(riderUrl);
                    sendSmsToAllUser(backReqDto.getShopOrderId(), shortUrl, backReqDto, businessType);

                    shunfengDeliveryLog.setStatus(ShunfengStatusConstant.SfRequestStatus.SUCCESS);
                }
            } else {
                shunfengDeliveryLog.setStatus(ShunfengStatusConstant.SfRequestStatus.FAILD);
            }

            try {
                shunfengDeliveryLogMapper.insertSelective(shunfengDeliveryLog);
            } catch (Exception e) {
                log.error("????????????????????????????????????:", e);
            }

        } catch (JsonProcessingException e) {
            log.error("????????????????????????????????????,??????ID:{}", backReqDto.getSfOrderId());
            log.error("????????????????????????????????????,????????????:", e);
        }
    }

    @Override
    public ResponseResult testCrestOrder(SfOrderReqDto dto) {
        dto = testCreateSfOrder(dto);
//        smsTemplate.sendMsg("18652242292",SmsTemplateCodeConstant.SHUNFENG_NOTIFY_RIDER,"3");
        ResponseResult result = createOrder(dto, (byte) 0);
        return result;
//        return null;
    }

    @Override
    public JSONObject postSendMsg(String url, String param) {
        HttpHeaders headers = new HttpHeaders();
        String jsonStr = param.concat("&").concat(sfOrderBaseDto.getDevId()).concat("&").concat(sfOrderBaseDto.getKey());
        log.info("????????????param:{}", jsonStr);
        String sign = encryptSign(jsonStr);
        url = url.concat(sign);
        log.info("????????????url================={}", url);
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<Object> entity = new HttpEntity<>(param, headers);
        JSONObject result = restTemplate.postForObject(url, entity, JSONObject.class);
        log.info("????????????????????????:{}", result);
        return result;
    }

    /**
     * ????????????????????????
     *
     * @param underscoreName
     * @return
     */
    public static String camelCaseName(String underscoreName) {
        StringBuilder result = new StringBuilder();
        if (underscoreName != null && underscoreName.length() > 0) {
            boolean flag = false;
            for (int i = 0; i < underscoreName.length(); i++) {
                char ch = underscoreName.charAt(i);
                if ("_".charAt(0) == ch) {
                    flag = true;
                } else {
                    if (flag) {
                        result.append(Character.toUpperCase(ch));
                        flag = false;
                    } else {
                        result.append(ch);
                    }
                }
            }
        }
        return result.toString();
    }

    private String encryptSign(String param) {
        String sign = null;
        final Base64.Encoder encoder = Base64.getEncoder();
        try {
            String str = MD5Util.MD5Encode(param, "utf-8");
            final byte[] textByte = str.getBytes("UTF-8");
            sign = encoder.encodeToString(textByte);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sign;
    }

    private SfOrderReqDto testCreateSfOrder(SfOrderReqDto dto) {
        long minute = System.currentTimeMillis() / 1000;
        dto.setShopId("3243279847393");
        dto.setShopOrderId("01" + minute);
        dto.setPayType((byte) 1);
        dto.setOrderTime(minute - 2 * 60);
        dto.setPushTime(minute);
        dto.setExpectTime(minute + 2 * 60 * 60);
        dto.setIsAppoint((byte) 0);
        dto.setOrderSource("?????????");

        SfOrderReceiveDto sfOrderReceiveDto = new SfOrderReceiveDto();
        sfOrderReceiveDto.setUserName("??????");
        sfOrderReceiveDto.setUserPhone("15921557120");
        sfOrderReceiveDto.setUserAddress("?????????B12");
        sfOrderReceiveDto.setUserLat("40.002349");
        sfOrderReceiveDto.setUserLng("116.339392");
        dto.setReceive(sfOrderReceiveDto);

        SfOrderDetail sfOrderDetail = new SfOrderDetail();
        sfOrderDetail.setTotalPrice(1000L);
        sfOrderDetail.setWeightGram(420);
        sfOrderDetail.setProductType((byte) 1);
        sfOrderDetail.setProductNum(1);
        sfOrderDetail.setProductTypeNum(1);

        SfORderProductDetail sfORderProductDetail = new SfORderProductDetail();
        sfORderProductDetail.setProductName("??????");
        sfORderProductDetail.setProductNum(1);
        sfOrderDetail.setProductDetail(Arrays.asList(sfORderProductDetail));
        dto.setOrderDetail(sfOrderDetail);
        dto.setOrderSequence(dto.getShopOrderId());
        dto.setDetailId("1234567");
        return dto;
    }

    private Byte convertOrderStatus(Byte sourceStatus) {
        Byte status = null;
        if (ShunfengStatusConstant.SfDeliveryStatus.CONFIRM.equals(sourceStatus)) {  // ???????????????
            status = OrderDeliveryStatus.RECEIVED_ORDER.getValue();
        } else if (ShunfengStatusConstant.SfDeliveryStatus.DISPATCHING.equals(sourceStatus)) {   // ?????????
            status = OrderDeliveryStatus.RECEIVED_GOODS.getValue();
        } else if (ShunfengStatusConstant.SfDeliveryStatus.ARRIVE_SHOP.equals(sourceStatus)) {   // ???????????????
            status = OrderDeliveryStatus.ARRIVED_SHOP.getValue();
        } else if (ShunfengStatusConstant.SfDeliveryStatus.FINISH.equals(sourceStatus)) {         // ????????????
            status = OrderDeliveryStatus.COMPLETED.getValue();
        }
        return status;
    }

    /**
     * ???????????? ??????????????????????????????
     *
     * @param detailSetId
     * @param mobiles
     */
    @Override
    public void sendSmsMsg(String detailSetId, String mobiles, Byte businessType) {
        NotifyOrderSetQueryResult detailSetResult = orderSetService.getOrderSetInfoByDetailSetNo(null, Long.valueOf(detailSetId), businessType);
        if (null != detailSetResult) {
            OrderSetDetailDto orderSet = detailSetResult.getOrderSet();
            List<OrderDetailResult> orderDetails = detailSetResult.getListOrderDetails();
            StringBuffer orderBuffer = new StringBuffer();
            if (null != orderDetails && orderDetails.size() > 0) {
                orderDetails.stream().forEach(detail -> {
                    orderBuffer.append(detail.getOrderNo()).append(POINT);
                });
            } else {
                log.info("??????????????????,??????ID:{}", detailSetId);
            }
            orderBuffer.deleteCharAt(orderBuffer.lastIndexOf(POINT));

            List<String> params = new ArrayList<>();
            if (null != orderSet) {
                params.add(orderSet.getDetailSetNo());
            } else {
                params.add(detailSetId);
            }
            params.add(orderBuffer.toString());
            List<String> mobileList = Arrays.asList(mobiles.split(POINT));
            String templateCode = SmsTemplateCodeConstant.SHUNFENG_CANCEL;
            String signNo = "2";
            if (OrderDeliveryStatus.BusinessType.BREAKFAST.getValue() == businessType) {
                signNo = "3";
            }
            smsTemplate.sendMsg(mobileList, templateCode, signNo, params);
        } else {
            log.error("??????????????????????????????ID:{}", detailSetId);
        }
    }


    @Override
    public ResponseResult<RiderInfoDto> getSfOrderDeliveryStatusByDeliveryNo(String deliveryNo, Byte businessType) {
        if (null == businessType) {
            businessType = OrderDeliveryStatus.BusinessType.LUNCH.getValue();
        }
        log.info("???????????????????????????????????????:{}", deliveryNo);
        List<RiderInfoDto> riderInfoDtoList = new ArrayList<>();
        List<OrderSetDetailDto> orderSetDetailDtoList = orderSetService.getOrderSetByOrderNo(deliveryNo, businessType);
        if (orderSetDetailDtoList.size() > 0) {
            orderSetDetailDtoList.stream().forEach(orderSetDetailDto -> {
                Long orderSetId = orderSetDetailDto.getId();
                String detailSetNoDescription = orderSetDetailDto.getDetailSetNoDescription();

                ShunfengOrderExample example = new ShunfengOrderExample();
                example.createCriteria().andShopOrderIdEqualTo(orderSetId);
                List<ShunfengOrder> shunfengOrders = shunfengOrderMapper.selectByExample(example);
                if (shunfengOrders.size() > 0) {
                    ShunfengOrder shunfengOrder = shunfengOrders.get(0);
                    RiderInfoDto riderInfoDto = buildRiderInfo(shunfengOrder, detailSetNoDescription);
                    riderInfoDtoList.add(riderInfoDto);
                }
            });
        }
        log.info("??????c?????????????????????:{}", JSONObject.toJSONString(riderInfoDtoList));
        if (riderInfoDtoList.isEmpty()) {
            log.error("?????????????????????????????????,orderId:{}", deliveryNo);
            return ResponseResult.buildSuccessResponse();
        }
        return ResponseResult.buildSuccessResponse(riderInfoDtoList.get(0));
    }

    @Override
    public ResponseResult<RiderInfoDto> getSfOrderDeliveryStatusBySetDetailId(Long setDetailId) {
        ShunfengOrder shunfengOrder = shunfengOrderExtMapper.selectSfOrderBySetDetailId(setDetailId);
        if (null == shunfengOrder) {
            log.warn("????????????????????????,??????Id:{}", setDetailId);
            return ResponseResult.buildFailResponse("?????????????????????????????????");
        }
        NotifyOrderSetQueryResult orderSetInfoByDetailSetNo = orderSetService.getOrderSetInfoByDetailSetNo(null, setDetailId, shunfengOrder.getBusinessType());
        if (null != orderSetInfoByDetailSetNo && null != orderSetInfoByDetailSetNo.getOrderSet()) {
            OrderSetDetailDto orderSet = orderSetInfoByDetailSetNo.getOrderSet();
            String orderSequence = orderSet.getDetailSetNoDescription();
            RiderInfoDto riderInfoDto = buildRiderInfo(shunfengOrder, orderSequence);
            return ResponseResult.buildSuccessResponse(riderInfoDto);
        } else {
            log.error("????????????????????????,??????Id:{}", setDetailId);
            return ResponseResult.buildFailResponse("???????????????????????????");
        }
    }

    // ?????????????????? ??????????????????
    private void sendSmsToAllUser(String setDetailId, String riderUrl, SfOrderStatusBackReqDto backReqDto, Byte businessType) {
        NotifyOrderSetQueryResult detailSetResult = orderSetService.getOrderSetInfoByDetailSetNo(null, Long.valueOf(setDetailId), businessType);
        String templateCode = SmsTemplateCodeConstant.SF_BF_ARRIVE_NOTIFY;
        List<String> params = new ArrayList<>();
        params.add(backReqDto.getOperatorName());
        params.add(backReqDto.getOperatorPhone());
        params.add(riderUrl);
        List<String> list = new ArrayList<>();
        if (null != detailSetResult) {
            List<OrderSetDetailGoodsDto> detailGoodsList = detailSetResult.getGoodsList();
            if (null != detailGoodsList && detailGoodsList.size() > 0) {
                Set<String> mobileList = detailGoodsList.stream().map(detail -> detail.getUserPhone()).collect(Collectors.toSet());
                String sfOrderKey = ShunfengRedisKeyUtil.getSfOrderKey(setDetailId);
                String mobile = "";
                if (RedisCache.hasKey(sfOrderKey)) {
                    Object obj = RedisCache.hGet(sfOrderKey, ShunfengRedisKeyUtil.SF_HASH_KEY);
                    SfOrderRedisDto sfOrderRedisDto = JSONObject.parseObject(obj.toString(), SfOrderRedisDto.class);
                    mobile = sfOrderRedisDto.getSendUserMobile();
                }
                log.info("????????????????????????????????????{}", mobile);
                if (!StringUtils.isBlank(mobile)) {
                    mobileList.remove(mobile);
                    log.info("????????????????????????:{}", mobileList.toString());
                }
                list.addAll(mobileList);
            } else {
                log.info("??????????????????,??????ID:{}", setDetailId);
            }
            String signNo = "2";
            if (OrderDeliveryStatus.BusinessType.BREAKFAST.getValue() == businessType) {
                signNo = "3";
                templateCode = SmsTemplateCodeConstant.SF_ARRIVE_NOTIFY;
            }
            smsTemplate.sendMsg(list, templateCode, signNo, params);
        } else {
            log.error("??????????????????????????????ID:{}", setDetailId);
        }
    }

    // ???????????????c????????????????????????
    private RiderInfoDto buildRiderInfo(ShunfengOrder order, String orderSquence) {
        RiderInfoDto riderInfoDto = new RiderInfoDto();
        riderInfoDto.setTransporterName(order.getTransporterName());
        riderInfoDto.setTransporterPhone(order.getTransporterPhone());
        riderInfoDto.setTakeNo(orderSquence.substring(1));
        List<ShunFengStatusDto> shunFengStatusDtoList = new ArrayList<>();
        for (int i = 0; i <= 4; i++) {
            byte status = (byte) i;
            Date date;
            ShunFengStatusDto sfStatusDto = new ShunFengStatusDto();
            sfStatusDto.setStatus(status);
            sfStatusDto.setStatusName(OrderDeliveryStatus.getNameByValue(status));
            if (status == OrderDeliveryStatus.DISPATCHED_ORDER.getValue()) {
                date = order.getDistributeTime();
            } else if (status == OrderDeliveryStatus.RECEIVED_ORDER.getValue()) {
                date = order.getConfirmTime();
            } else if (status == OrderDeliveryStatus.ARRIVED_SHOP.getValue()) {
                date = order.getArrivedShopTime();
            } else if (status == OrderDeliveryStatus.RECEIVED_GOODS.getValue()) {
                date = order.getAchieveGoodTime();
            } else if (status == OrderDeliveryStatus.COMPLETED.getValue()) {
                date = order.getArrivedTime();
            } else {
                date = order.getCreateTime();
            }
//            mm-dd hh:mm
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
            if (null != date) {
                sfStatusDto.setCompleteTime(simpleDateFormat.format(date));
            }
            shunFengStatusDtoList.add(sfStatusDto);
        }
        riderInfoDto.setList(shunFengStatusDtoList);
        return riderInfoDto;
    }

    private void sendSmsToRider(SfOrderStatusBackReqDto backReqDto, Byte businessLine) {
        String sfOrderKey = ShunfengRedisKeyUtil.getSfOrderKey(backReqDto.getShopOrderId());
        if (RedisCache.hasKey(sfOrderKey)) {
            Object obj = RedisCache.hGet(sfOrderKey, ShunfengRedisKeyUtil.SF_HASH_KEY);
            SfOrderRedisDto sfOrderRedisDto = JSONObject.parseObject(obj.toString(), SfOrderRedisDto.class);

            List<String> params = new ArrayList<>();
            List<SfOrderCompanyInfoDto> companyInfoDtos = sfOrderRedisDto.getCompanyInfoDtos();
            if (null == companyInfoDtos || companyInfoDtos.isEmpty()) {
                log.warn("??????????????????! ??????ID:{}", backReqDto.getShopOrderId());
                return;
            }
            SfOrderCompanyInfoDto companyInfoDto = companyInfoDtos.get(0);
            String companyName = companyInfoDto.getCompanyName();
            String companyMealAddress = companyInfoDto.getCompanyMealAddress();
            String mealAddressPicture = companyInfoDto.getMealAddressPicture();
            String shortUrl = urlUtils.compressToShortUrl(mealAddressPicture);

            params.add(sfOrderRedisDto.getDescription());
            params.add(companyName);
            params.add(companyMealAddress);
            params.add(shortUrl);
            String templateCode = SmsTemplateCodeConstant.SHUNFENG_NOTIFY_RIDER;
            String signNo = "2";
            if (OrderDeliveryStatus.BusinessType.BREAKFAST.getValue() == businessLine) {
                signNo = "3";
            }
            if (sfOrderBaseDto.getTestFlag()) {
                smsTemplate.sendMsg(Arrays.asList("18116246668", "19916708402", "15921557120"), templateCode, signNo, params);
            } else {
                smsTemplate.sendMsg(Arrays.asList(backReqDto.getOperatorPhone()), templateCode, signNo, params);
            }
        }
    }

    private AddressDetailInfo getBuilding(Long id) {
        String buildingKey = ShunfengRedisKeyUtil.getBuildingKey();
        Object obj = RedisCache.hGet(buildingKey, String.valueOf(id));

        BuildingDto building;
        if (null != obj) {
            building = JSONObject.parseObject(obj.toString(), BuildingDto.class);
        } else {
            building = buildingService.getBuildingById(id);
            if (null == building) {
                return null;
            }
            RedisCache.hPut(buildingKey, String.valueOf(id), JSONObject.toJSONString(building));
        }

        AddressDetailInfo address = new AddressDetailInfo();
        address.setUserLng(String.valueOf(building.getLon()));
        address.setUserLat(String.valueOf(building.getLat()));
        return address;
    }

    private AddressDetailInfo getCompany(Long id, String meadAddress) {
        // ?????????redis??????????????? ??????????????????????????????????????????????????????
        ResponseResult<CompanyPolymerizationDto> companyResult = companyService.queryAllById(id);
        if (!companyResult.isSuccess() || companyResult.getData() == null) {
            return null;
        }
        CompanyPolymerizationDto company = companyResult.getData();
        AddressDetailInfo address = new AddressDetailInfo();
        address.setUserLat(String.valueOf(company.getLatGaode()));
        address.setUserLng(String.valueOf(company.getLonGaode()));

        List<CompanyMealAddressDto> mealAddressList = company.getMealAddressList();
        List<SfOrderCompanyInfoDto> companyInfoList = new ArrayList<>();
        if (null != mealAddressList && !mealAddressList.isEmpty()) {
            Map<Boolean, List<CompanyMealAddressDto>> collectMap = mealAddressList.stream()
                    .filter(mealAddress -> (mealAddress.getMealAddress().equals(meadAddress)))
                    .collect(Collectors.partitioningBy(CompanyMealAddressDto::getIsDeleted));
            if (collectMap.get(false).size() > 0) { // ?????????????????????????????? ????????????
                CompanyMealAddressDto mealAddress = collectMap.get(false).get(0);
                SfOrderCompanyInfoDto sfOrderCompanyInfoDto = new SfOrderCompanyInfoDto();
                sfOrderCompanyInfoDto.setCompanyId(company.getId());
                sfOrderCompanyInfoDto.setCompanyName(company.getName());
                sfOrderCompanyInfoDto.setCompanyMealAddress(mealAddress.getMealAddress());
                sfOrderCompanyInfoDto.setMealAddressPicture(mealAddress.getCompletePicture());
                companyInfoList.add(sfOrderCompanyInfoDto);
            } else if (collectMap.get(true).size() > 0) { // ???????????????????????????????????????
                CompanyMealAddressDto mealAddress = collectMap.get(true).get(0);
                SfOrderCompanyInfoDto sfOrderCompanyInfoDto = new SfOrderCompanyInfoDto();
                sfOrderCompanyInfoDto.setCompanyId(company.getId());
                sfOrderCompanyInfoDto.setCompanyName(company.getName());
                sfOrderCompanyInfoDto.setCompanyMealAddress(mealAddress.getMealAddress());
                sfOrderCompanyInfoDto.setMealAddressPicture(mealAddress.getCompletePicture());
                companyInfoList.add(sfOrderCompanyInfoDto);
            }
        }
        address.setCompanyInfoDtos(companyInfoList);
        return address;
    }
}

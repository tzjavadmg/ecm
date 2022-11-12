package com.milisong.ecm.common.notify.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.milisong.ecm.common.notify.api.NotifyService;
import com.milisong.ecm.common.notify.api.SmsService;
import com.milisong.ecm.common.notify.constant.BusinessLineEnum;
import com.milisong.ecm.common.notify.constant.OrderRestEnum;
import com.milisong.ecm.common.notify.dto.*;
import com.milisong.ecm.common.notify.properties.NotifyProperties;
import com.milisong.ecm.common.notify.util.IvrCacheUtils;
import com.milisong.ecm.common.notify.util.NotifyCacheUtil;
import com.milisong.ecm.common.util.DateUtils;
import com.milisong.ecm.common.util.RedisValueUtil;
import com.milisong.ecm.common.util.RedisKeyUtils;
import com.milisong.oms.api.GroupBuyOrderService;
import com.milisong.oms.api.OrderService;
import com.milisong.oms.constant.DeliveryStatus;
import com.milisong.oms.constant.OrderStatus;
import com.milisong.oms.constant.OrderUserSource;
import com.milisong.oms.constant.PaymentStatus;
import com.milisong.oms.dto.OrderDeliveryDto;
import com.milisong.oms.dto.OrderDeliveryGoodsDto;
import com.milisong.oms.dto.OrderDto;
import com.milisong.pms.prom.dto.groupbuy.GroupBuyWechatMsgMessage;
import com.milisong.ucs.api.UserService;
import com.milisong.ucs.dto.UserDto;
import com.milisong.wechat.miniapp.api.MiniAppService;
import com.milisong.wechat.miniapp.dto.MiniMsgTemplateDto;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <pre>
 *    author  : Tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/9/20   22:51
 *    desc    : 通知业务实现
 *    version : v1.0
 * </pre>
 */
@Slf4j
@RestController
public class NotifyServiceImpl implements NotifyService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private MiniAppService miniAppService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private GroupBuyOrderService groupBuyOrderService;

    @Resource
    private SmsService smsService;

    @Autowired
    private UserService userService;

    @Autowired
    private NotifyProperties notifyProperties;

    //早9点通知用户今日已订餐
    private static final String TODAY_ORDER_NOTIFY_SMS = "MILISONG_ORDER_NOTIFY";
    private static final String[] MILISONG_ORDER_ARRIVE_NOTIFY = {
            "MILISONG_BF_ORDER_ARRIVE_NOTIF",//早饭通知
            "MILISONG_ORDER_ARRIVE_NOTIFY",//午饭通知
    };
    //给我方人员发送反馈短信
    private static final String MILISONG_USER_FEEDBACK = "MILISONG_USER_FEEDBACK";

    private static final String[] MILISONG_USER_EVALUATION = {
            "MILISONG_BF_USER_EVALUATION",//早饭问卷
            "MILISONG_USER_EVALUATION",//午饭问卷
    };

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat dateFormatDateTime = new SimpleDateFormat("HH:mm");

    /**
     * @param dto
     * @return
     */
    @Override
    public ResponseResult<Boolean> notifyCustomer(@RequestBody NotifyArriveDto dto) {
        Long detailSetId = Long.parseLong(dto.getDetailSetId());
        //过滤状态
        if (!"4".equals(dto.getStatus().toString())) {
            return ResponseResult.buildFailResponse(OrderRestEnum.ORDER_NOTIFY_STATUS_ILLEGAL.getCode(), OrderRestEnum.ORDER_NOTIFY_STATUS_ILLEGAL.getDesc());
        }
        if (NotifyCacheUtil.checkIfSendNotify(detailSetId)) {
            log.info("------配送通知---已经通知过了，不做再次通知--{}", JSON.toJSONString(dto));
            return ResponseResult.buildSuccessResponse();
        }
        log.info("-----通知配送----单号:{}", detailSetId);
        ResponseResult<Map<String, Object>> orderDetailRes = getOrderDetail(detailSetId);
        if (!orderDetailRes.isSuccess()) {
            return ResponseResult.buildFailResponse(orderDetailRes.getCode(), orderDetailRes.getMessage());
        }
        if (orderDetailRes.getData() == null || orderDetailRes.getData().size() == 0) {
            return ResponseResult.buildFailResponse(OrderRestEnum.ORDER_UNION_ORDER_NULL.getCode(), OrderRestEnum.ORDER_UNION_ORDER_NULL.getDesc());
        }
        // TODO 进行微信服务通知
        TreeMap<String, Set<String>> orders = (TreeMap<String, Set<String>>) orderDetailRes.getData().get("orders");
        //判断订单业务类型
        String firstOrderNo = orders.keySet().iterator().next();
        Byte businessLine = getOrderByOrderNo(firstOrderNo).getOrderType();
        dto.setBusinessLine(businessLine);
        WechatArriveNotifyDto notifyDto = new WechatArriveNotifyDto();
        notifyDto.setBusinessLine(dto.getBusinessLine());
        notifyDto.setAddress((String) orderDetailRes.getData().get("detailDeliveryAddress"));
        notifyDto.setMap(orders);
        this.sendWeiXinDeliveryMsg(notifyDto);
        String unionOrderNo = orderDetailRes.getData().get("detailSetNo").toString();
        //TODO 发短信或者ivr通知
        ResponseResult<String> sendNotifyResult = notifySmsOrIvr(orderDetailRes.getData(), unionOrderNo, businessLine);
        if (!sendNotifyResult.isSuccess()) {
            return ResponseResult.buildFailResponse(sendNotifyResult.getCode(), sendNotifyResult.getMessage());
        } else {
            NotifyCacheUtil.cacheSendNotify(detailSetId);
        }
        return ResponseResult.buildSuccessResponse(true);
    }

    @Override
    public boolean sendNotifySms(@RequestBody NotifySmsDto dto) {
        Collection<String> mobiles = dto.getMobiles();
        if (mobiles == null || mobiles.size() == 0) {
            mobiles = new ArrayList<>();
            mobiles.add(dto.getMobile());
        }
        ArrayList<String> mobileList = new ArrayList<>();
        mobileList.addAll(mobiles);
        SmsSendDto smsSendDto = new SmsSendDto();
        smsSendDto.setMobile(mobileList);
        smsSendDto.setParams(new ArrayList<>());
        smsSendDto.setBusinessLine(dto.getBusinessLine());
        smsSendDto.setTemplateCode(MILISONG_ORDER_ARRIVE_NOTIFY[dto.getBusinessLine()]);
        ResponseResult<String> result = smsService.sendMsg(smsSendDto);
        return result.isSuccess();
    }

    @Override
    public boolean sendNotifyIvr(@RequestBody NotifyIvrDto dto) {
        Collection<String> mobileList = dto.getMobiles();
        String orderNo = dto.getOrderNo();
        if (mobileList == null || mobileList.size() == 0) {
            return false;
        }
        List<String> list = new ArrayList<>();
        mobileList.stream().forEach(o -> {
            if (!list.contains(o)) {
                list.add(o);
            } else {
                log.info("ivr电话拨打，检测到手机号在同一个配送单已经拨打过了，不再拨打---{}", o);
            }
        });
        if (list.size() == 0) {
            return true;
        }
        //检测30分钟内是否已经拨过电话？如果是，那就不再拨打
        Map<String, Object> params = new HashMap<>();
        params.put("mobiles", list);
        String uuid = IvrCacheUtils.generateUUID(orderNo, 1);
        params.put("uuid", uuid);
        params.put("callType", notifyProperties.getIvrTemplate());
        log.info("请求拨打IVR电话：参数={}", params);
        ResponseResult result = sendPost(params, notifyProperties.getIvrUrl());
        if (!result.isSuccess()) {
            log.warn("请求拨打IVR电话失败：{}", result);
            return false;
        }
        //缓存拨号信息，用于超时未接通重试
        IvrCacheUtils.cacheMobileIvrMsg(redisTemplate, list, orderNo, 1);
        return true;
    }

    @Override
    public boolean sendNotifyRetryIvr(@RequestBody NotifyIvrDto dto) {
        String orderNoKey = dto.getOrderNoKey();
        String value = dto.getValue();
        String mobile = dto.getMobile();
        // 解析vlaue，提取orderNo ，count
        String orderNo = orderNoKey.split(":")[1];
        Integer count = Integer.valueOf(value.split("-")[0]);
        Map<String, Object> params = new HashMap<>();
        List<String> mobileList = new ArrayList<>();
        mobileList.add(mobile);
        params.put("mobiles", mobileList);
        String uuid = IvrCacheUtils.generateUUID(orderNo, count + 1);
        params.put("uuid", uuid);
        params.put("callType", notifyProperties.getIvrTemplate());
        log.info("请求拨打IVR电话：参数={}", params);
        ResponseResult result = sendPost(params, notifyProperties.getIvrUrl());
        if (!result.isSuccess()) {
            log.info("请求重试拨打IVR电话失败：{}", result);
            return false;
        }
        return true;
    }

    @Override
    public boolean updateIvrResult(@RequestBody NotifyIvrResponseDto dto) {
        IvrCacheUtils.updateIvrResult(redisTemplate, dto.getUuid(), dto.getMobile(), dto.getSuccess());
        return true;
    }

    // 调用微信通知,发送送达通知
    @Override
    public boolean sendWeiXinDeliveryMsg(@RequestBody WechatArriveNotifyDto dto) {
        if (dto.getBusinessLine().equals(BusinessLineEnum.BREAKFAST.getVal())) {
            return sendWeiXinDeliveryMsgBreakfast(dto);
        } else if (dto.getBusinessLine().equals(BusinessLineEnum.LUNCH.getVal())) {
            return sendWeiXinDeliveryMsgLunch(dto);
        }
        return true;
    }

    // 调用微信通知
    @Override
    public void sendWeiXinPayMsg(@RequestBody WechatPayNotifyDto dto) {
        OrderDto orderDto = getOrderById(dto.getOrderId());
        if (!checkOrderSource(orderDto.getOrderType(), orderDto.getOrderSource())) {
            return;
        }
        dto.setBusinessLine(orderDto.getOrderType());
        if (dto.getBusinessLine().equals(BusinessLineEnum.BREAKFAST.getVal())) {
            sendWeiXinPayMsgBreakfast(dto, orderDto);
        } else if (dto.getBusinessLine().equals(BusinessLineEnum.LUNCH.getVal())) {
            sendWeiXinPayMsgLunch(dto, orderDto);
        }
    }

    private void sendWeiXinPayMsgLunch(WechatPayNotifyDto dto, OrderDto orderDto) {
        Long orderId = dto.getOrderId();
        String openId = dto.getOpenId();
        Byte status = dto.getStatus();
        String key = "wechat_notify_form_id:" + orderId;
        if (RedisCache.hasKey(key)) {
            Map<String, String> map = concatDeliveryDate(orderDto);
            MiniMsgTemplateDto miniMsgTemplateDto = new MiniMsgTemplateDto();
            String notifyWCUrl = "pages/order-info/index?orderId=";
            if (orderDto.getOrderSource() != null && orderDto.getOrderSource().equals(OrderUserSource.WECHAT_MINI_BF.getValue())) {
                miniMsgTemplateDto.setBusinessLine(BusinessLineEnum.BREAKFAST.getVal());
                notifyWCUrl = "pages/lunch/order-info/index?orderId=";
            } else {
                miniMsgTemplateDto.setBusinessLine(dto.getBusinessLine());
            }
            String formId = RedisCache.get(key);
            miniMsgTemplateDto.setFormId(formId);
            miniMsgTemplateDto.setOpenId(openId);
            String dateStr = map.get("deliveryDate");
            String orderNo = map.get("orderNo");
            String orderDate = map.get("orderDate");
            String amount = map.get("amount");
            String days = map.get("days");
            String remark = "预订" + days + "日午餐将于指定时间送达，详情可查看米立送小程序.";
            List<String> dataList = new ArrayList<>();
            if (status.equals(PaymentStatus.SUCCEED.getValue())) {
                String msg = "预订" + days + "天的午餐(" + dateStr + ")";
                dataList.add(msg);
                dataList.add(orderNo);
                dataList.add(amount);
                dataList.add(orderDate);
                dataList.add(remark);
                miniMsgTemplateDto.setMsgId(notifyProperties.getReceiveTemplateId());
            } else if (status.equals(PaymentStatus.FAILED.getValue())) {
                Integer min = 5;
                String msg = "您的预订订单未支付，" + min + "分钟内未完成支付，订单将自动取消。";
                String content = "预订" + days + "天午餐(" + dateStr + ")。";
                dataList.add(msg);
                dataList.add(content);
                dataList.add(amount);
                dataList.add(orderNo);
                dataList.add(orderDate);
                miniMsgTemplateDto.setMsgId(notifyProperties.getPayFailTemplateId());
            }
            miniMsgTemplateDto.setData(dataList);
            miniMsgTemplateDto.setTransferPage(notifyWCUrl.concat(orderId.toString()));
            log.info("调用微信通知:{}", JSONObject.toJSONString(miniMsgTemplateDto));
            miniAppService.sendTemplateMsg(miniMsgTemplateDto);
            RedisCache.delete(key);
        }
    }

    private void sendWeiXinPayMsgBreakfast(WechatPayNotifyDto dto, OrderDto orderDto) {
        String key = "wechat_notify_form_id:" + dto.getOrderId();
        if (RedisCache.hasKey(key)) {
            Map<String, String> map = concatDeliveryDate(orderDto);
            MiniMsgTemplateDto miniMsgTemplateDto = new MiniMsgTemplateDto();

            String formId = RedisCache.get(key);
            miniMsgTemplateDto.setFormId(formId);
            miniMsgTemplateDto.setOpenId(dto.getOpenId());
            miniMsgTemplateDto.setBusinessLine(dto.getBusinessLine());
            String dateStr = map.get("deliveryDate");
            String orderNo = map.get("orderNo");
            String orderDate = map.get("orderDate");
            String amount = map.get("amount");
            String days = map.get("days");
            List<String> dataList = new ArrayList<>();
            if (dto.getStatus().equals(PaymentStatus.SUCCEED.getValue())) {
                String remark = "如遇异常联系 " + notifyProperties.getContactPhone();
                String msg = "预订" + days + "天早餐(" + dateStr + ")";
                dataList.add(msg);
                dataList.add(orderNo);
                dataList.add(amount);
                dataList.add(orderDate);
                dataList.add(remark);
                miniMsgTemplateDto.setMsgId(notifyProperties.getBfReceiveTemplateId());
            } else if (dto.getStatus().equals(PaymentStatus.FAILED.getValue())) {
                Integer min = 5;
                String msg = "您的预订" + days + "天早餐订单未支付，" + min + "分钟内未完成支付，订单将自动取消。";
                String content = "预订" + days + "天早餐(" + dateStr + ")。";
                dataList.add(msg);
                dataList.add(content);
                dataList.add(amount);
                dataList.add(orderNo);
                dataList.add(orderDate);
                miniMsgTemplateDto.setMsgId(notifyProperties.getBfPayFailTemplateId());
            }
            miniMsgTemplateDto.setData(dataList);
            miniMsgTemplateDto.setTransferPage("pages/order-info/index?orderId=".concat(dto.getOrderId().toString()));
            log.info("调用微信通知:{}", JSONObject.toJSONString(miniMsgTemplateDto));
            miniAppService.sendTemplateMsg(miniMsgTemplateDto);
            RedisCache.delete(key);
        }
    }

    @Override
    public ResponseResult<String> notifyTodayOrderTask(@RequestBody NotifyTodayOrderTaskDto dto) {
        ResponseResult<List<OrderDeliveryDto>> responseResult = orderService.getOrderListByDeliveryDate(dto.getBusinessLine(), "", System.currentTimeMillis());
        if (responseResult.getData() == null || responseResult.getData().size() == 0) {
            return ResponseResult.buildSuccessResponse();
        }

        Map<String, NotifySmsMsgDto> msgDetail = dealSendTodayOrderSms(responseResult.getData());
        if (msgDetail == null || msgDetail.size() == 0) {
            return ResponseResult.buildSuccessResponse();
        }
        Set<Map.Entry<String, NotifySmsMsgDto>> entrySet = msgDetail.entrySet();
        SmsSendDto smsSendDto = new SmsSendDto();
        smsSendDto.setTemplateCode(TODAY_ORDER_NOTIFY_SMS);
        entrySet.stream().forEach(o -> {
            String mo = getMobileNo(o.getKey());
            List<String> mobileList = new ArrayList<>();
            mobileList.add(mo);
            smsSendDto.setMobile(mobileList);
            NotifySmsMsgDto value = o.getValue();
            if (value != null) {
                smsSendDto.setParams(value.getParams());
            } else {
                return;
            }
            ResponseResult<String> result = smsService.sendMsg(smsSendDto);
            log.info("发送短信接口返回:{}", JSONObject.toJSONString(result));
        });
        return ResponseResult.buildSuccessResponse();
    }

    private String getMobileNo(String key) {
        int index = key.lastIndexOf("-");

        return key.substring(index + 1);
    }

    private Map<String, NotifySmsMsgDto> dealSendTodayOrderSms(List<OrderDeliveryDto> data) {
        if (data == null || data.size() == 0) {
            return null;
        }
        HashMap<String, NotifySmsMsgDto> map = new HashMap<>();
        data.stream().forEach(o -> {
            String key = o.getOrderId().toString().concat("-").concat(o.getMobileNo());
            //key格式:orderId-mobileNo
            List<String> params = new ArrayList<>();
            ResponseResult<OrderDto> order = orderService.getOrderById(o.getOrderId());
            List<OrderDeliveryDto> deliveries = order.getData().getDeliveries();
            String param1 = "";
            String param2 = "";
            String param3 = "";
            Byte orderSource = order.getData().getOrderSource();
            if (orderSource == null || orderSource.equals(OrderUserSource.WECHAT_MINI_LC.getValue())) {
                param3 = "米立送";
            } else {
                param3 = "米立早餐";
            }
            for (OrderDeliveryDto o2 : deliveries) {
                if (!checkSendOrderNotify(order.getData(), o2)) {
                    continue;
                }
                if (o2.getId().equals(o.getId())) {
                    List<OrderDeliveryGoodsDto> deliveryGoods = o2.getDeliveryGoods();
                    param1 = deliveryGoods.get(0).getGoodsName();
                    param2 = dateFormatDateTime.format(o2.getDeliveryTimezoneFrom()).concat("-").concat(dateFormatDateTime.format(o2.getDeliveryTimezoneTo()));
                    if (deliveryGoods.size() > 1) {
                        Integer count = 0;
                        for (OrderDeliveryGoodsDto o3 : deliveryGoods) {
                            count += o3.getGoodsCount();
                        }
                        param1 = param1.concat("等" + count + "件商品");
                    }
                    break;
                }
            }
            if (param1.length() == 0 || param2.length() == 0) {
                return;
            }
            params.add(param1);
            params.add(param2);
            params.add(param3);
            NotifySmsMsgDto msgDto = new NotifySmsMsgDto();
            msgDto.setMobileNo(key);
            msgDto.setParams(params);
            msgDto.setOrderSource(orderSource);
            map.put(key, msgDto);
        });
        return map;
    }

    private static Integer getGoodsCount(String str) {
        String reg = "\\d+";
        Pattern compile = Pattern.compile(reg);
        Matcher matcher = compile.matcher(str);
        int count = 0;
        while (matcher.find()) {
            count = Integer.parseInt(matcher.group());
        }
        return count;
    }

    private static String getNewCountGoodsName(String str, int newCount, int oldCount) {
        int index = str.lastIndexOf(String.valueOf(oldCount));
        System.out.println("---" + index);
        String substring = str.substring(0, index);
        return substring.concat(String.valueOf(newCount)).concat(str.substring(index + String.valueOf(oldCount).length(), str.length()));
    }

    /**
     * 检验是否要发送通知
     *
     * @param orderDto
     * @param deliveryDto
     * @return true-发送，false-不发送
     */
    private boolean checkSendOrderNotify(OrderDto orderDto, OrderDeliveryDto deliveryDto) {
        OrderDto data = orderDto;
        if (!(Byte.parseByte(data.getStatus().toString()) == OrderStatus.PAID.getValue())) {
            return false;
        }
        boolean checkResult = orderDto.getMobileNo() != null && (deliveryDto.getStatus().equals(DeliveryStatus.GETTING_READY.getValue())
                || deliveryDto.getStatus().equals(DeliveryStatus.PACKAGED.getValue())
                || deliveryDto.getStatus().equals(DeliveryStatus.DELIVERING.getValue()));
        return checkResult;
    }

    /**
     * @param dto
     * @RequestParam("orderId") Long orderId
     * 发送待支付通知
     */
    @Override
    public void notifyWaitPay(@RequestBody WechatPayNotifyDto dto) {
        ResponseResult<?> result = orderService.getOrderById(dto.getOrderId());
        OrderDto data = (OrderDto) result.getData();
        if (result.getData() == null) {
            log.warn("---订单未查询到---{}", dto.getOrderId());
            return;
        }
        Long userId = data.getUserId();
        UserDto userDto = new UserDto();
        userDto.setId(userId);
        ResponseResult<UserDto> userInfo = userService.getUserInfo(userDto);
        WechatPayNotifyDto notifyDto = new WechatPayNotifyDto();
        notifyDto.setOrderId(dto.getOrderId());
        notifyDto.setBusinessLine(dto.getBusinessLine());
        notifyDto.setShopCode(data.getShopCode());
        notifyDto.setOpenId(userInfo.getData().getOpenId());
        notifyDto.setStatus(PaymentStatus.FAILED.getValue());
        sendWeiXinPayMsg(notifyDto);
    }

    @Override
    public void dealRetryIvr() {
        Set<String> ivrKeys = IvrCacheUtils.getMobileIvrKeys(redisTemplate);
        if (ivrKeys == null || ivrKeys.size() == 0) {
            return;
        }
        ivrKeys.stream().forEach(o -> {
            Map<String, String> map = IvrCacheUtils.getIvrByKey(redisTemplate, o);
            if (map == null || map.size() == 0) {
                return;
            }
            if (IvrCacheUtils.checkIfComplate(map)) {
                IvrCacheUtils.deleteIvrByKey(redisTemplate, o);
                return;
            }
            Set<Map.Entry<String, String>> entrySet = map.entrySet();
            entrySet.stream().forEach(o1 -> {
                String checkResult = IvrCacheUtils.checkIfNeedRetry(o1.getValue());
                if (checkResult.equals(IvrCacheUtils.IVRENUM.GO_RETRY.getCode())) {
                    //retry
                    NotifyIvrDto dto = new NotifyIvrDto();
                    dto.setBusinessLine(BusinessLineEnum.LUNCH.getVal());
                    dto.setMobile(o1.getKey());
                    dto.setOrderNoKey(o);
                    dto.setValue(o1.getValue());
                    sendNotifyRetryIvr(dto);
                    //update cache
                    IvrCacheUtils.updateNotifyIvr(redisTemplate, o1.getKey(), o, o1.getValue());
                } else if (checkResult.equals(IvrCacheUtils.IVRENUM.GO_SMS.getCode())) {
                    //send sms
                    NotifySmsDto dto = new NotifySmsDto();
                    dto.setBusinessLine(BusinessLineEnum.LUNCH.getVal());
                    dto.setMobile(o1.getKey());
                    sendNotifySms(dto);
                    //update cache
                    IvrCacheUtils.updateNotifyIvr2Sms(redisTemplate, o1.getKey(), o);
                }
            });
        });
    }

    @Override
    public boolean sendFeedbackNotifySms(@RequestBody SendFeedbackNotifyDto dto) {
        Map<Integer, Object> params = dto.getMap();
        List<String> mobiles = dto.getMobiles();
        TreeMap treeMap = new TreeMap(Comparator.comparingInt(o -> (int) o));
        treeMap.putAll(params);
        Collection values = treeMap.values();
        //去重
        Set<String> set = new HashSet<>(values);
        List<String> list = new ArrayList<>(set);
        SmsSendDto smsSendDto = new SmsSendDto();
        smsSendDto.setBusinessLine(dto.getBusinessLine());
        smsSendDto.setMobile(mobiles);
        smsSendDto.setTemplateCode(MILISONG_USER_FEEDBACK);
        smsSendDto.setParams(list);
        ResponseResult<String> result = smsService.sendMsg(smsSendDto);
        return result.isSuccess();
    }

    @Override
    public boolean sendFeedbackInvestigateSms(@RequestBody FeedbackInvestigateSmsDto dto) {
        Map<String, List<String>> map = dto.getMap();
        String templateCode = MILISONG_USER_EVALUATION[dto.getBusinessLine()];
        if (map == null || map.size() == 0) {
            return true;
        }
        Set<Map.Entry<String, List<String>>> entrySet = map.entrySet();
        SmsSendDto smsSendDto = new SmsSendDto();
        smsSendDto.setTemplateCode(templateCode);
        smsSendDto.setAdvFlag(true);
        entrySet.stream().forEach(o -> {
            List<String> listPa = o.getValue();
            ArrayList<String> listMo = new ArrayList<>();
            listMo.add(o.getKey());
            smsSendDto.setMobile(listMo);
            smsSendDto.setParams(listPa);
            smsSendDto.setBusinessLine(dto.getBusinessLine());
            smsService.sendMsg(smsSendDto);
        });
        return true;
    }

    @Override
    public void sendAdvertiseSms(@RequestBody AdvertiseSmsDto dto) {
        Map<String, String> map = dto.getMap();
        String templateCode = "COMMON_TEMPLATE";
        if (map == null || map.size() == 0) {
            return;
        }
        SmsSendDto smsSendDto = new SmsSendDto();
        smsSendDto.setAdvFlag(true);
        smsSendDto.setTemplateCode(templateCode);
        map.entrySet().stream().forEach(o -> {
            ArrayList<String> listMo = new ArrayList<>();
            ArrayList<String> listPa = new ArrayList<>();
            listMo.add(o.getKey());
            listPa.add(o.getValue());
            smsSendDto.setMobile(listMo);
            smsSendDto.setParams(listPa);
            smsSendDto.setBusinessLine(dto.getBusinessLine());
            smsService.sendMsg(smsSendDto);
        });
        return;
    }


    /**
     * 【发团 & 参团成功】：拼团进度通知
     *
     * @return
     */
    @Override
    public Boolean sendWeiXinJoinGroupBuy(GroupBuyWechatMsgMessage dto) {
        log.info("【发团 & 参团成功】：拼团进度通知 入参{}", dto);

        String formId = RedisKeyUtils.getJoinGroupBuyFormIdValue(dto.getJoinUserList().get(0).getOpenId());
        if (StringUtils.isBlank(formId)) {
            String key = "wechat_notify_prepay_id:" + dto.getJoinUserList().get(0).getOrderId();
            log.info("拼团formId为空，使用支付prepayId={}", key);
            formId = RedisCache.get(key);
        }

        MiniMsgTemplateDto miniMsgTemplateDto = new MiniMsgTemplateDto();
        miniMsgTemplateDto.setBusinessLine(dto.getBusinessLine());
        miniMsgTemplateDto.setMsgId(notifyProperties.getJoinGroupBuyTemplateId());
        miniMsgTemplateDto.setFormId(formId);
        miniMsgTemplateDto.setOpenId(dto.getJoinUserList().get(0).getOpenId());
        Map<Integer, String> data = new TreeMap<>();
        data.put(1, getGoodsName(dto.getGoodsCode(), dto.getJoinUserList().get(0).getOrderId()));
        data.put(2, dto.getPrice() == null ? "" : dto.getPrice().toString() + "元");
        data.put(3, dto.getLeftNum() == null ? "" : dto.getLeftNum().toString() + "人");
        data.put(4, DateUtils.secondToTime(dto.getSeconds()));
        miniMsgTemplateDto.setData(new ArrayList<>(data.values()));
        miniMsgTemplateDto.setTransferPage("pages/breakfast/group-buy-join/index?id=".concat(dto.getUserGroupBuyId().toString()));

        log.info("【发团 & 参团成功】：拼团进度通知  调用微信入参{}", JSON.toJSONString(miniMsgTemplateDto));
        ResponseResult responseResult = miniAppService.sendTemplateMsg(miniMsgTemplateDto);
        if (!responseResult.isSuccess()) {
            log.error("【发团 & 参团成功】：拼团进度通知失败{}", JSON.toJSONString(responseResult));
            return false;
        }
        return true;
    }


    /**
     * 【拼团超时，失败退款】：拼团失败通知
     *
     * @return
     */
    @Override
    public Boolean sendWeiXinJoinGroupBuyFailed(GroupBuyWechatMsgMessage dto) {
        log.info("【拼团超时，失败退款】：拼团失败通知 入参{}", dto);
        MiniMsgTemplateDto miniMsgTemplateDto = new MiniMsgTemplateDto();
        miniMsgTemplateDto.setBusinessLine(dto.getBusinessLine());
        miniMsgTemplateDto.setMsgId(notifyProperties.getJoinGroupBuyFailedTemplateId());
        Map<Integer, String> data = new TreeMap<>();
        data.put(1, getGoodsName(dto.getGoodsCode(), dto.getJoinUserList().get(0).getOrderId()));
        data.put(2, dto.getPrice() == null ? "" : dto.getPrice().toString() + "元");
        data.put(3, dto.getFailReason());
        data.put(4, dto.getWarnTips());
        miniMsgTemplateDto.setData(new ArrayList<>(data.values()));
        miniMsgTemplateDto.setTransferPage("pages/breakfast/group-buy-join/index?id=".concat(dto.getUserGroupBuyId().toString()));

        if (dto.getJoinUserList() != null && dto.getJoinUserList().size() > 0) {
            dto.getJoinUserList().forEach(joinUser -> {

                String formId = RedisKeyUtils.getJoinGroupBuyFormIdValue(joinUser.getOpenId());
                if (StringUtils.isBlank(formId)) {
                    String key = "wechat_notify_prepay_id:" + joinUser.getOrderId();
                    log.info("拼团formId为空，使用支付key={},value={}", key, RedisCache.get(key));
                    formId = RedisCache.get(key);
                }

                miniMsgTemplateDto.setFormId(formId);
                miniMsgTemplateDto.setOpenId(joinUser.getOpenId());
                log.info("【拼团超时，失败退款】：拼团失败通知  调用微信入参{}", JSON.toJSONString(miniMsgTemplateDto));
                ResponseResult responseResult = miniAppService.sendTemplateMsg(miniMsgTemplateDto);
                if (!responseResult.isSuccess()) {
                    log.error("【拼团超时，失败退款】：拼团失败通知失败{}", JSON.toJSONString(responseResult));
                }
            });
        }
        return true;
    }


    /**
     * 【拼团时间提醒（在剩余30min的时候提醒）进度提示】：拼团待成团提醒
     *
     * @return
     */
    @Override
    public Boolean sendWeiXinJoinGroupBuyTimeRemind(GroupBuyWechatMsgMessage dto) {
        log.info("【拼团时间提醒（在剩余30min的时候提醒）进度提示】：拼团待成团提醒 入参{}", dto);
        MiniMsgTemplateDto miniMsgTemplateDto = new MiniMsgTemplateDto();
        miniMsgTemplateDto.setBusinessLine(dto.getBusinessLine());
        miniMsgTemplateDto.setMsgId(notifyProperties.getJoinGroupBuyTimeRemindTemplateId());
        Map<Integer, String> data = new TreeMap<>();
        data.put(1, getGoodsName(dto.getGoodsCode(), dto.getJoinUserList().get(0).getOrderId()));
        data.put(2, DateUtils.secondToTime(dto.getSeconds()));
        data.put(3, dto.getWarnTips());
        data.put(4, dto.getLeftNum() == null ? "" : dto.getLeftNum().toString() + "人");
        miniMsgTemplateDto.setData(new ArrayList<>(data.values()));
        miniMsgTemplateDto.setTransferPage("pages/breakfast/group-buy-join/index?id=".concat(dto.getUserGroupBuyId().toString()));

        if (dto.getJoinUserList() != null && dto.getJoinUserList().size() > 0) {
            dto.getJoinUserList().forEach(joinUser -> {

                String formId = RedisKeyUtils.getJoinGroupBuyFormIdValue(joinUser.getOpenId());
                if (StringUtils.isBlank(formId)) {
                    String key = "wechat_notify_prepay_id:" + joinUser.getOrderId();
                    log.info("拼团formId为空，使用支付key={},value={}", key, RedisCache.get(key));
                    formId = RedisCache.get(key);
                }

                miniMsgTemplateDto.setFormId(formId);
                miniMsgTemplateDto.setOpenId(joinUser.getOpenId());
                log.info("【拼团时间提醒（在剩余30min的时候提醒）进度提示】：拼团待成团提醒  调用微信入参{}", JSON.toJSONString(miniMsgTemplateDto));
                ResponseResult responseResult = miniAppService.sendTemplateMsg(miniMsgTemplateDto);
                if (!responseResult.isSuccess()) {
                    log.error("【拼团时间提醒（在剩余30min的时候提醒）进度提示】：拼团待成团提醒失败{}", JSON.toJSONString(responseResult));
                }
            });
        }
        return true;
    }


    /**
     * 【仅提示团长第一个参团和倒数第二个参团进度】：拼团进度通知
     *
     * @return
     */
    @Override
    public Boolean sendWeiXinJoinGroupBuySchedule(GroupBuyWechatMsgMessage dto) {
        log.info("【仅提示团长第一个参团和倒数第二个参团进度】：拼团进度通知 入参{}", dto);

        String formId = RedisKeyUtils.getJoinGroupBuyFormIdValue(dto.getJoinUserList().get(0).getOpenId());
        if (StringUtils.isBlank(formId)) {
            String key = "wechat_notify_prepay_id:" + dto.getJoinUserList().get(0).getOrderId();
            log.info("拼团formId为空，使用支付prepayId={},value={}", key, RedisCache.get(key));
            formId = RedisCache.get(key);
        }

        MiniMsgTemplateDto miniMsgTemplateDto = new MiniMsgTemplateDto();
        miniMsgTemplateDto.setBusinessLine(dto.getBusinessLine());
        miniMsgTemplateDto.setMsgId(notifyProperties.getJoinGroupBuyScheduleTemplateId());
        miniMsgTemplateDto.setFormId(formId);
        miniMsgTemplateDto.setOpenId(dto.getJoinUserList().get(0).getOpenId());
        Map<Integer, String> data = new TreeMap<>();
        data.put(1, getGoodsName(dto.getGoodsCode(), dto.getJoinUserList().get(0).getOrderId()));
        data.put(2, dto.getPrice() == null ? "" : dto.getPrice() + "元");
        data.put(3, "已有" + dto.getJoinedNum() + "人参与");
        data.put(4, DateUtils.secondToTime(dto.getSeconds()));
        miniMsgTemplateDto.setData(new ArrayList<>(data.values()));
        miniMsgTemplateDto.setTransferPage("pages/breakfast/group-buy-join/index?id=".concat(dto.getUserGroupBuyId().toString()));

        log.info("【仅提示团长第一个参团和倒数第二个参团进度】：拼团进度通知  调用微信入参{}", JSON.toJSONString(miniMsgTemplateDto));
        ResponseResult responseResult = miniAppService.sendTemplateMsg(miniMsgTemplateDto);
        if (!responseResult.isSuccess()) {
            log.error("【仅提示团长第一个参团和倒数第二个参团进度】：拼团进度通知失败{}", JSON.toJSONString(responseResult));

        }
        return true;
    }


    /**
     * 拼团成功通知
     *
     * @return
     */
    @Override
    public Boolean sendWeiXinJoinGroupBuySuccess(GroupBuyWechatMsgMessage dto) {
        log.info("【拼团成功】：拼团成功通知 入参{}", dto);
        MiniMsgTemplateDto miniMsgTemplateDto = new MiniMsgTemplateDto();
        miniMsgTemplateDto.setBusinessLine(dto.getBusinessLine());
        miniMsgTemplateDto.setMsgId(notifyProperties.getJoinGroupBuySuccessTemplateId());
        Map<Integer, String> data = new TreeMap<>();

        ResponseResult result = groupBuyOrderService.getOrderById(dto.getJoinUserList().get(0).getOrderId());
        log.info("拼团订单信息：{}", JSONObject.toJSONString(result));
        OrderDto orderDto = (OrderDto) result.getData();
        String deliveryDate = "";
        if (orderDto != null && orderDto.getDeliveries().get(0) != null && orderDto.getDeliveries().get(0).getDeliveryDate() != null) {
            deliveryDate = orderDto.getDeliveries().get(0).getDeliveryDate().substring(5);
            deliveryDate = deliveryDate.replace("-", ".");
            deliveryDate = "【" + deliveryDate + "送达】";
        }
        data.put(1, deliveryDate + RedisValueUtil.getGoodsName(dto.getGoodsCode()));
        data.put(2, dto.getPrice() == null ? "" : dto.getPrice() + "元");
        data.put(3, orderDto == null ? "" : DateUtils.toHHmm(orderDto.getDeliveries().get(0).getDeliveryTimezoneFrom()) + " - "
                + DateUtils.toHHmm(orderDto.getDeliveries().get(0).getDeliveryTimezoneTo()));
        data.put(4, orderDto == null ? "" : orderDto.getTakeMealsAddrName());

        miniMsgTemplateDto.setData(new ArrayList<>(data.values()));
        miniMsgTemplateDto.setTransferPage("pages/breakfast/group-buy-join/index?id=".concat(dto.getUserGroupBuyId().toString()));

        if (dto.getJoinUserList() != null && dto.getJoinUserList().size() > 0) {
            dto.getJoinUserList().forEach(joinUser -> {

                String formId = RedisKeyUtils.getJoinGroupBuyFormIdValue(joinUser.getOpenId());
                if (StringUtils.isBlank(formId)) {
                    String key = "wechat_notify_prepay_id:" + joinUser.getOrderId();
                    log.info("拼团formId为空，使用支付key={},value={}", key, RedisCache.get(key));
                    formId = RedisCache.get(key);
                }

                miniMsgTemplateDto.setFormId(formId);
                miniMsgTemplateDto.setOpenId(joinUser.getOpenId());
                log.info("【拼团成功】：拼团成功通知  调用微信入参{}", JSON.toJSONString(miniMsgTemplateDto));
                ResponseResult responseResult = miniAppService.sendTemplateMsg(miniMsgTemplateDto);
                if (!responseResult.isSuccess()) {
                    log.error("【拼团成功】：拼团成功通知失败{}", JSON.toJSONString(responseResult));
                }
            });
        }
        return true;
    }


    private String getGoodsName(String goodsCode, Long orderId) {
        ResponseResult result = groupBuyOrderService.getOrderById(orderId);
        log.info("拼团订单信息：{}", JSONObject.toJSONString(result));
        OrderDto orderDto = (OrderDto) result.getData();
        String deliveryDate = "";
        if (orderDto != null && orderDto.getDeliveries().get(0) != null && orderDto.getDeliveries().get(0).getDeliveryDate() != null) {
            deliveryDate = orderDto.getDeliveries().get(0).getDeliveryDate().substring(5);
            deliveryDate = deliveryDate.replace("-", ".");
            deliveryDate = "【" + deliveryDate + "送达】";
        }
        return deliveryDate + RedisValueUtil.getGoodsName(goodsCode);
    }


    /**
     * 组合订单送货日期
     *
     * @param orderDto
     * @return
     */
    private Map<String, String> concatDeliveryDate(OrderDto orderDto) {
        String point = ",";
        SimpleDateFormat formatDatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat formatMonth = new SimpleDateFormat("MM月dd日");
        SimpleDateFormat formatMonthDay = new SimpleDateFormat("yyyy-MM-dd");
        List<OrderDeliveryDto> dailyOrders = orderDto.getDeliveries();
        StringBuffer sb = new StringBuffer();
        if (dailyOrders.size() > 0) {
            List<String> dateStrList = dailyOrders.stream().map(OrderDeliveryDto::getDeliveryDate).collect(Collectors.toList());
            List<Date> dataList = new ArrayList<>();
            dateStrList.stream().forEach((o1) -> {
                try {
                    dataList.add(formatMonthDay.parse(o1));
                } catch (ParseException e) {
                    log.error("解析时间字段出错", e);
                }
            });
            dataList.sort(Comparator.comparingLong(Date::getTime));
            dataList.stream().forEach(date -> {
                String dateStr = formatMonth.format(date);
                sb.append(dateStr).append(point);
            });
        }
        String deliveryDate = "";
        if (sb.length() > 0) {
            deliveryDate = sb.substring(0, sb.length() - 1);
        }
        Map<String, String> map = new HashMap<>();
        map.put("days", String.valueOf(orderDto.getTotalOrderDays()));
        map.put("deliveryDate", deliveryDate);
        map.put("orderNo", orderDto.getOrderNo());
        map.put("orderDate", formatDatetime.format(orderDto.getOrderDate()));
        map.put("amount", String.valueOf(orderDto.getTotalPayAmount()) + "元");
        return map;
    }

    private ResponseResult<?> sendPost(Object obj, String url) {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        String result = "{}";
        if (obj != null) {
            result = JSON.toJSONString(obj);
        }
        HttpEntity<String> formEntity = new HttpEntity<>(result, headers);
        ResponseResult responseResult = restTemplate.postForObject(url, formEntity, ResponseResult.class);
        if (!responseResult.isSuccess()) {
            return ResponseResult.buildFailResponse(responseResult.getCode(), responseResult.getMessage());
        } else {
            return responseResult;
        }
    }

    private String getUserOpenId(String orderNo, List<OrderDto> orderDtos) {
        for (OrderDto o : orderDtos) {
            if (o.getOrderNo().equals(orderNo.split("_")[0])) {
                Long userId = o.getUserId();
                UserDto userDto = new UserDto();
                userDto.setId(userId);
                ResponseResult<UserDto> userInfo = userService.getUserInfo(userDto);
                if (userInfo == null || userInfo.getData() == null) {
                    return null;
                }
                return userInfo.getData().getOpenId();
            }
        }
        return null;
    }

    private String getPrepayId(String orderNo, List<OrderDto> orderDtos) {
        Long orderId = 0L;
        orderNo = orderNo.split("_")[0];
        for (OrderDto o : orderDtos) {
            if (o.getOrderNo().equals(orderNo)) {
                orderId = o.getId();
                break;
            }
        }
        String key = "wechat_notify_prepay_id:" + orderId;
        String s = RedisCache.get(key);
        return s;
    }

    private String getDisplayName(String orderNo, Map<String, Set<String>> map) {
        final String mainOrderNo = orderNo.split("_")[0];
        StringBuilder sb = new StringBuilder();
        if (map == null || map.size() == 0) {
            sb.append("未知");
            return sb.toString();
        } else {
            Set<Map.Entry<String, Set<String>>> entrySet = map.entrySet();
            entrySet.stream().forEach(o -> {
                String key = o.getKey();
                if (key.startsWith(mainOrderNo)) {
                    Set<String> value = o.getValue();
                    value.stream().forEach(o1 -> sb.append(o1).append(","));
                }
            });
        }
        String displayName = sb.toString().substring(0, sb.length() - 1);
        return displayName;
    }

    /**
     * 根据集单号，获取集单中的订单信息(订单号)
     *
     * @param detailSetIds
     * @return
     */
    private ResponseResult<Map<String, Object>> getOrderDetail(Long detailSetIds) {
        HashMap<String, Object> map = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        params.put("detailSetId", detailSetIds);
        JSONObject res = restTemplate.getForObject(notifyProperties.getScmUnionOrderUrl(), JSONObject.class, params);
        Boolean success = res.getBoolean("success");
        JSONObject data = res.getJSONObject("data");
        log.info("----{}", JSON.toJSONString(res));
        if (!success) {
            return ResponseResult.buildFailResponse(res.getString("code"), res.getString("message"));
        }
        if (data == null) {
            return ResponseResult.buildFailResponse(OrderRestEnum.ORDER_UNION_ORDER_NULL.getCode(), OrderRestEnum.ORDER_UNION_ORDER_NULL.getDesc());
        }
        JSONArray goodsList = data.getJSONArray("goodsList");
        if (goodsList == null || goodsList.size() == 0) {
            return ResponseResult.buildFailResponse(OrderRestEnum.ORDER_UNION_ORDER_GET_FAILED.getCode(), OrderRestEnum.ORDER_UNION_ORDER_GET_FAILED.getDesc());
        }
        Set<String> mobiles = new HashSet<>();
        Map<String, Set<String>> goodsNames = new TreeMap<>();
        for (int i = 0; i < goodsList.size(); i++) {
            mobiles.add(goodsList.getJSONObject(i).getString("userPhone"));
            goodsNames.putIfAbsent(goodsList.getJSONObject(i).getString("orderNo"), new HashSet<>());
            goodsNames.get(goodsList.getJSONObject(i).getString("orderNo")).add(goodsList.getJSONObject(i).getString("goodsName"));
        }
        String detailSetNo = data.getJSONObject("orderSet").getString("detailSetNo");
        Integer status = data.getJSONObject("orderSet").getInteger("status");
        String mainUnionOrderNo = data.getString("distributionNo");
        String detailDeliveryAddress = data.getJSONObject("orderSet").getString("detailDeliveryAddress");
        map.put("detailSetNo", detailSetNo);
        map.put("orders", goodsNames);
        map.put("mobiles", mobiles);
        map.put("status", status);
        map.put("mainUnionOrderNo", mainUnionOrderNo);
        map.put("detailDeliveryAddress", detailDeliveryAddress);
        return ResponseResult.buildSuccessResponse(map);
    }

    @Data
    private static class UnionOrderDetailNotifyDto {
        private String detailSetNo;
        private Map<String, Set<String>> orders;
        private Set<String> mobiles;
        private Integer status;
        private String mainUnionOrderNo;
        private String detailDeliveryAddress;
    }

    /**
     * http查询scm返回订单数据dto
     */
    @Data
    private static class OrderScmDto {
        private String userPhone;
        private String orderNo;
        private String goodsName;
    }

    private ResponseResult<String> notifySmsOrIvr(Map<String, Object> data, String unionOrderNo, Byte businessLine) {
        Set<String> smsMobiles = new HashSet<>();
        Set<String> ivrMobiles = new HashSet<>();
        TreeMap<String, Set<String>> orders = (TreeMap<String, Set<String>>) data.get("orders");
        List<String> list = new ArrayList<>();
        orders.keySet().stream().forEach(o -> {
            if (o.contains("_")) {
                list.add(o.split("_")[0]);
            } else {
                list.add(o);
            }
        });
        List<OrderDto> orderDtos = new ArrayList<>();
        list.stream().forEach(o -> {
            ResponseResult<OrderDto> orderByOrderNo = orderService.getOrderByOrderNo(o);
            OrderDto orderDto = orderByOrderNo.getData();
            if (orderByOrderNo != null) {
                if (checkOrderSource(orderDto.getOrderType(), orderDto.getOrderSource())) {
                    orderDtos.add(orderByOrderNo.getData());
                }
            }
        });
        //      查询用户配送地址表中的唯一一条，拿取到配送通知方式，决定通知方式,通知类型 0:电话,1:短信
        orderDtos.stream().forEach(o -> {
            Byte notifyType = o.getNotifyType();
            if (notifyType == null || notifyType == 1) {
                smsMobiles.add(o.getMobileNo());
            } else {
                ivrMobiles.add(o.getMobileNo());
            }
        });
        //TODO 给用户发送短信通知
        if (smsMobiles != null && smsMobiles.size() > 0) {
            NotifySmsDto dto = new NotifySmsDto();
            dto.setMobiles(smsMobiles);
            dto.setBusinessLine(businessLine);
            boolean notifySms = this.sendNotifySms(dto);
            if (!notifySms) {
                log.warn("---短信通知失败---");
                return ResponseResult.buildFailResponse(OrderRestEnum.ORDER_SMS_NOTIFY_FAILED.getCode(), OrderRestEnum.ORDER_SMS_NOTIFY_FAILED.getDesc());
            }
        }
        //TODO IVR电话通知
        if (ivrMobiles != null && ivrMobiles.size() > 0) {
            NotifyIvrDto notifyIvrDto = new NotifyIvrDto();
            notifyIvrDto.setMobiles(ivrMobiles);
            notifyIvrDto.setOrderNo(unionOrderNo);
            notifyIvrDto.setBusinessLine(businessLine);
            boolean notifyIvr = this.sendNotifyIvr(notifyIvrDto);
            if (!notifyIvr) {
                log.warn("---ivr通知失败---");
                NotifySmsDto notifySmsDto = new NotifySmsDto();
                notifySmsDto.setMobiles(ivrMobiles);
                notifySmsDto.setBusinessLine(businessLine);
                boolean notifyRetrySms = this.sendNotifySms(notifySmsDto);
                if (notifyRetrySms) {
                    return ResponseResult.buildFailResponse(OrderRestEnum.ORDER_IVR_NOTIFY_RETRY_FAILED.getCode(), OrderRestEnum.ORDER_IVR_NOTIFY_RETRY_FAILED.getDesc());
                }
            }
        }
        return ResponseResult.buildSuccessResponse();
    }

    private boolean sendWeiXinDeliveryMsgLunch(WechatArriveNotifyDto dto) {
        Map<String, Set<String>> map = dto.getMap();
        MiniMsgTemplateDto miniMsgTemplateDto = new MiniMsgTemplateDto();
//            miniMsgTemplateDto.setBusinessLine(dto.getBusinessLine());
        miniMsgTemplateDto.setMsgId(notifyProperties.getTakeFoodTemplateId());
        Map<Integer, String> data = new TreeMap<>();
        data.put(3, dateFormat.format(new Date()).concat(" 中午"));
        data.put(6, notifyProperties.getContactPhone());
        data.put(5, "请记得尽快取餐，遇到异常可以及时联系我们~");
        //查询订单号，订单地址，用户openId，prepay_id;
        Set<String> orderNos = map.keySet();
        List<OrderDto> orderDtos = new ArrayList<>();
        for (String orderNo : orderNos) {
            ResponseResult<OrderDto> orderByOrderNo = orderService.getOrderByOrderNo(orderNo.split("_")[0]);
            OrderDto order = orderByOrderNo.getData();
            if (order != null) {
                if (checkOrderSource(order.getOrderType(), order.getOrderSource())) {
                    orderDtos.add(order);
                }
            }
        }
        Set<String> hasSendOrderNo = new HashSet<>();
        for (String orderNo : orderNos) {
            if (chechHashSendWeixinMsg(orderNo, hasSendOrderNo)) {
                continue;
            }
            //TODO 从redis中拿formid或者prepayid
            miniMsgTemplateDto.setFormId(getPrepayId(orderNo, orderDtos));
            miniMsgTemplateDto.setOpenId(getUserOpenId(orderNo, orderDtos));
            Byte orderSource = getOrderSource(orderNo, orderDtos);
            String notifyWCUrl = "pages/order-info/index?orderId=";
            if (orderSource.equals(OrderUserSource.WECHAT_MINI_BF.getValue())) {
                miniMsgTemplateDto.setBusinessLine(BusinessLineEnum.BREAKFAST.getVal());
                notifyWCUrl = "pages/lunch/order-info/index?orderId=";
            }
            if (miniMsgTemplateDto.getOpenId() == null) {
                continue;
            }
            data.put(1, getDisplayName(orderNo, map));
            data.put(4, getMainOrderNo(orderNo));
            data.put(2, getTakeMealAddr(orderNo, orderDtos));
            miniMsgTemplateDto.setData(new ArrayList<>(data.values()));
            miniMsgTemplateDto.setTransferPage(notifyWCUrl.concat(getMainOrderId(orderNo, orderDtos).toString()));
            log.info("----发送送餐到达通知---{}", JSON.toJSONString(miniMsgTemplateDto));
            ResponseResult responseResult = miniAppService.sendTemplateMsg(miniMsgTemplateDto);
            if (!responseResult.isSuccess()) {
                log.warn("---微信通知取餐失败----{}", JSON.toJSONString(responseResult));
            }
        }
        return true;
    }

    private Byte getOrderSource(String orderNo, List<OrderDto> orderDtos) {
        OrderDto orderDto = null;
        for (OrderDto o : orderDtos) {
            if (o.getOrderNo().equals(orderNo.split("_")[0])) {
                orderDto = o;
                break;
            }
        }
        if (orderDto == null) {
            return OrderUserSource.WECHAT_MINI_LC.getValue();
        }
        if (orderDto.getOrderSource() != null && orderDto.getOrderSource().equals(OrderUserSource.WECHAT_MINI_BF.getValue())) {
            return OrderUserSource.WECHAT_MINI_BF.getValue();
        } else {
            return OrderUserSource.WECHAT_MINI_LC.getValue();
        }

    }

    private String getTakeMealAddr(String orderNo, List<OrderDto> orderDtos) {
        String result = null;
        for (OrderDto o : orderDtos) {
            if (o.getOrderNo().equals(orderNo.split("_")[0])) {
                result = o.getTakeMealsAddrName();
                break;
            }
        }
        if (result == null) {
            result = "公司前台";
        }
        return result;
    }

    private boolean sendWeiXinDeliveryMsgBreakfast(WechatArriveNotifyDto dto) {
        Map<String, Set<String>> map = dto.getMap();
        MiniMsgTemplateDto miniMsgTemplateDto = new MiniMsgTemplateDto();
        miniMsgTemplateDto.setBusinessLine(dto.getBusinessLine());
        miniMsgTemplateDto.setMsgId(notifyProperties.getBfTakeFoodTemplateId());
        Map<Integer, String> data = new TreeMap<>();
        data.put(6, notifyProperties.getContactPhone());
        data.put(5, "元气早餐，要及时享用哦~");
        //查询订单号，订单地址，用户openId，prepay_id;
        Set<String> orderNos = map.keySet();
        List<OrderDto> orderDtos = new ArrayList<>();
        for (String orderNo : orderNos) {
            ResponseResult<OrderDto> orderByOrderNo = orderService.getOrderByOrderNo(orderNo.split("_")[0]);
            OrderDto order = orderByOrderNo.getData();
            if (order != null) {
                if (checkOrderSource(order.getOrderType(), order.getOrderSource())) {
                    orderDtos.add(order);
                }
            }
        }
        Set<String> hasSendOrderNo = new HashSet<>();
        for (String orderNo : orderNos) {
            if (chechHashSendWeixinMsg(orderNo, hasSendOrderNo)) {
                continue;
            }
            //TODO 从redis中拿formid或者prepayid
            miniMsgTemplateDto.setFormId(getPrepayId(orderNo, orderDtos));
            miniMsgTemplateDto.setOpenId(getUserOpenId(orderNo, orderDtos));
            if (miniMsgTemplateDto.getOpenId() == null) {
                continue;
            }
            data.put(2, getTakeFoodAddr(orderNo, orderDtos));
            data.put(1, getDisplayName(orderNo, map));
            data.put(3, getSendArriveTime(orderNo, orderDtos));
            data.put(4, getMainOrderNo(orderNo));
            miniMsgTemplateDto.setData(new ArrayList<>(data.values()));
            miniMsgTemplateDto.setTransferPage("pages/order-info/index?orderId=".concat(getMainOrderId(orderNo, orderDtos).toString()));
            log.info("----发送送餐到达通知---{}", JSON.toJSONString(miniMsgTemplateDto));
            ResponseResult responseResult = miniAppService.sendTemplateMsg(miniMsgTemplateDto);
            if (!responseResult.isSuccess()) {
                log.warn("---微信通知取餐失败----{}", JSON.toJSONString(responseResult));
            }
        }
        return true;
    }

    private String getTakeFoodAddr(String orderNo, List<OrderDto> orderDtos) {
        if (orderNo.contains("_")) {
            orderNo = orderNo.split("_")[0];
        }
        String result = null;
        for (OrderDto o : orderDtos) {
            if (o.getOrderNo().equals(orderNo)) {
                result = o.getTakeMealsAddrName();
                break;
            }
        }

        return result == null ? "米立早餐取餐点" : result;
    }

    private String getSendArriveTime(String deliveryNo, List<OrderDto> orderDtos) {
        String orderNo = null;
        if (deliveryNo.contains("_")) {
            orderNo = deliveryNo.split("_")[0];
        }
        OrderDto result = null;
        for (OrderDto o : orderDtos) {
            if (o.getOrderNo().equals(orderNo)) {
                result = o;
            }
        }
        List<OrderDeliveryDto> deliveries = result.getDeliveries();
        OrderDeliveryDto deliveryDto = null;
        for (OrderDeliveryDto dto : deliveries) {
            if (dto.getDeliveryNo().equals(deliveryNo)) {
                deliveryDto = dto;
            }
        }
        return dateFormatDateTime.format(deliveryDto.getDeliveryTimezoneFrom()).concat("-").concat(dateFormatDateTime.format(deliveryDto.getDeliveryTimezoneTo()));
    }


    private Long getMainOrderId(String orderNo, List<OrderDto> orderDtos) {
        if (orderNo.contains("_")) {
            orderNo = orderNo.split("_")[0];
        }
        for (OrderDto o : orderDtos) {
            if (o.getOrderNo().equals(orderNo)) {
                return o.getId();
            }
        }
        return 0L;
    }

    private boolean chechHashSendWeixinMsg(String orderNo, Set<String> hasSendOrderNo) {
        orderNo = orderNo.split("_")[0];
        if (hasSendOrderNo.contains(orderNo)) {
            return true;
        } else {
            hasSendOrderNo.add(orderNo);
            return false;
        }
    }

    private String getMainOrderNo(String orderNo) {
        if (orderNo.contains("_")) {
            return orderNo.split("_")[0];
        } else {
            return orderNo;
        }
    }

    private OrderDto getOrderById(Long orderId) {
        ResponseResult<OrderDto> orderById = orderService.getOrderById(orderId);
        OrderDto orderDto = orderById.getData();
        return orderDto;
    }

    private OrderDto getOrderByOrderNo(String orderNo) {
        if (orderNo.contains("_")) {
            orderNo = orderNo.split("_")[0];
        }
        ResponseResult<OrderDto> orderById = orderService.getOrderByOrderNo(orderNo);
        OrderDto orderDto = orderById.getData();
        return orderDto;
    }

    private boolean checkOrderSource(Byte orderType, Byte orderSource) {
        log.info("---订单类型和当前应用 校验,orderType={},orderSource={}", orderType, orderSource);
        Byte currentType = notifyProperties.getBusinessLine();
        if (currentType.equals(BusinessLineEnum.LUNCH.getVal())) {
            if (orderType.equals(BusinessLineEnum.LUNCH.getVal())) {
                if (orderSource == null
                        || orderSource.equals(OrderUserSource.WECHAT_MINI_LC.getValue())) {
                    return true;
                }
            }
        }
        if (currentType.equals(BusinessLineEnum.BREAKFAST.getVal())) {
            if (orderSource.equals(OrderUserSource.WECHAT_MINI_BF.getValue())) {
                return true;
            }
        }
        log.info("---订单类型和当前应用不匹配，校验失败,orderType={},orderSource={}", orderType, orderSource);
        return false;
    }

    @Data
    public static class NotifySmsMsgDto {
        private String mobileNo;

        private List<String> params;

        private Byte orderSource;
    }
}
package com.mili.oss.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.farmland.core.util.BeanMapper;
import com.mili.oss.api.OrderInsideService;
import com.mili.oss.constant.LogisticsDeliveryStatus;
import com.mili.oss.constant.OrderStatusEnum;
import com.mili.oss.constant.OrderTypeEnum;
import com.mili.oss.domain.*;
import com.mili.oss.dto.OrderGoodsResult;
import com.mili.oss.dto.OrderReserveSearchResultDto;
import com.mili.oss.dto.OrderSearchResult;
import com.mili.oss.dto.OrderSearchResultPos;
import com.mili.oss.dto.param.*;
import com.mili.oss.dto.result.ShopGoodsCount;
import com.mili.oss.mapper.OrderGoodsComboMapper;
import com.mili.oss.mapper.OrderGoodsMapper;
import com.mili.oss.mapper.OrderMapper;
import com.mili.oss.util.DateUtil;
import com.mili.oss.util.SmsTemplate;
import com.milisong.scm.base.api.CompanyService;
import com.milisong.scm.base.api.ShopService;
import com.milisong.scm.base.dto.ShopBossDto;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author zhaozhonghui
 * @Description
 * @date 2019-02-27
 */
@Slf4j
@RestController
@RefreshScope
public class OrderInsideServiceImpl implements OrderInsideService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderGoodsMapper orderGoodsMapper;
    @Autowired
    private ShopService shopService;
    @Autowired
    private OrderGoodsComboMapper orderGoodsComboMapper;
    @Autowired
    private SmsTemplate smsTemplate;
    @Value("${sms.out-of-stock-msg}")
    private String outOfStockMsg;
    @Value("${sms.send-time}")
    private String sendMsgTime;


    @Override
    public ResponseResult<Pagination<OrderSearchResult>> pageSearchOrder(@RequestBody OrderSearchParam orderSearchParam) {
        if (null == orderSearchParam.getOrderType()) {
            orderSearchParam.setOrderType(OrderTypeEnum.LUNCH.getCode());
        }
        Pagination<OrderSearchResult> pagination = new Pagination<>();
        List<OrderSearchResult> results = listSearchOrder(orderSearchParam);
        Long count = orderMapper.selectOrderCount(orderSearchParam);
        pagination.setTotalCount(count);
        pagination.setPageNo(orderSearchParam.getPageNo());
        pagination.setPageSize(orderSearchParam.getPageSize());
        pagination.setDataList(results);
        return ResponseResult.buildSuccessResponse(pagination);
    }

    @Override
    public ResponseResult<Pagination<OrderReserveSearchResultDto>> pageSearchReserveGroupInfoList(@RequestBody OrderReserveSearchParamDto param) {
        log.info("分页查询门店商品预定总量，参数：{}", JSONObject.toJSONString(param));
        Pagination<OrderReserveSearchResultDto> result = new Pagination<>();
        result.setPageNo(param.getPageNo());
        result.setPageSize(param.getPageSize());

        long count = orderGoodsMapper.getReserveOrderGroupCount(param);
        result.setTotalCount(count);
        if (count > 0) {
            result.setDataList(BeanMapper.mapList(orderGoodsMapper.getReserveOrderGroupList(param),
                    OrderReserveSearchResultDto.class));
        } else {
            result.setDataList(Collections.emptyList());
        }
        return ResponseResult.buildSuccessResponse(result);
    }

    @Override
    public ResponseResult<List<String>> getTodayOrderList() {
        OrderExample example = new OrderExample();
        example.createCriteria().andDeliveryStartDateGreaterThanOrEqualTo(DateUtil.getDayBeginTime(new Date()))
                .andDeliveryStartDateLessThan(DateUtil.getDayBeginTime(new Date(), 1))
                .andOrderStatusEqualTo(OrderStatusEnum.WAITPACK.getValue().byteValue());

        List<Order> orderList = orderMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(orderList)) {
            List<String> collect = orderList.stream().map(item -> item.getOrderNo()).collect(Collectors.toList());
            return ResponseResult.buildSuccessResponse(collect);
        }
        return null;
    }

    private List<OrderSearchResult> listSearchOrder(OrderSearchParam param) {
        List<OrderSearchResult> resultList = orderMapper.selectOrderPage(param);
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
        OrderGoodsExample example = new OrderGoodsExample();
        example.createCriteria().andOrderNoIn(orderNoList);
        List<OrderGoods> orderDetails = orderGoodsMapper.selectByExample(example);
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

    @Override
    public ResponseResult<Pagination<OrderSearchResultPos>> getOrderSetPage(@RequestBody OrderSearchParamPos param) {
        if (completeCoupletNo(param)) {
            return ResponseResult.buildFailResponse("9999","单号传入格式不对，请传入单号后几位数字！");
        }
        Pagination<OrderSearchResultPos> pagination = new Pagination<>();
        List<OrderSearchResultPos> results = listSearchOrderPos(param);
        Long count = orderMapper.selectOrderCountPos(param);
        pagination.setTotalCount(count);
        pagination.setPageNo(param.getPageNo());
        pagination.setPageSize(param.getPageSize());
        pagination.setDataList(results);
        return ResponseResult.buildSuccessResponse(pagination);
    }
    // 补全顾客联、配送单号
    private boolean completeCoupletNo(@RequestBody OrderSearchParamPos param) {
        List<String> coupletNoList = param.getCoupletNo();
        List<String> newCoupletNoList = new ArrayList<>();
        if (null != coupletNoList && !coupletNoList.isEmpty()) {
            for (String coupletNo : coupletNoList) {
                try {
                    coupletNo = String.valueOf(Integer.valueOf(coupletNo));
                } catch (Exception e) {
                    log.warn("顾客联格式化失败:{}", coupletNo);
                    return true;
                }
                // 补全顾客联 早餐D开头 午餐A开头
                if (param.getOrderType() != null){
                    String prefix = "A";
                    if (param.getOrderType().equals(OrderTypeEnum.BREAKFAST.getCode())) {
                        prefix = "D";
                    }
                    // 补全成A0006格式的顾客联
                    coupletNo = buildCoupleNo(coupletNo, prefix);
                    newCoupletNoList.add(coupletNo);
                } else {
                    String bfCoupletNo = coupletNo;
                    // 午餐
                    String prefix = "A";
                    // 补全成A0006格式的顾客联
                    coupletNo = buildCoupleNo(coupletNo, prefix);
                    newCoupletNoList.add(coupletNo);
                    // 早餐
                    prefix = "D";
                    bfCoupletNo = buildCoupleNo(bfCoupletNo, prefix);
                    newCoupletNoList.add(bfCoupletNo);
                }

            }
            param.setCoupletNo(newCoupletNoList);
        }

        String description = param.getDescription();
        if (!StringUtil.isBlank(description)) {
            try {
                description = String.valueOf(Integer.valueOf(description));
            } catch (Exception e) {
                log.warn("取餐号格式化失败:{}", description);
                return true;
            }
            if (param.getOrderType() != null) {
                String prefix = "B"; // 早餐F 午餐B
                if (param.getOrderType().equals(OrderTypeEnum.BREAKFAST.getCode())) {
                    prefix = "F";
                }
                //取餐号格式 F0002
                description = buildCoupleNo(description, prefix);
                param.setDescription(description);
            } else {
                List<String> descriptionList = new ArrayList<>();
                param.setDescription(null);
                String bfDescription = description;
                // 午餐
                String prefix = "B";
                description = buildCoupleNo(description, prefix);
                descriptionList.add(description);
                // 早餐
                prefix = "F";
                bfDescription = buildCoupleNo(bfDescription, prefix);
                descriptionList.add(bfDescription);

                param.setDescriptionList(descriptionList);
            }

        }
        return false;
    }

    private String buildCoupleNo(String bfCoupletNo, String prefix) {
        String bfSrtFormat = prefix.concat("%04d");
        bfCoupletNo = String.format(bfSrtFormat, Integer.valueOf(bfCoupletNo));
        return bfCoupletNo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult setOutOfStock(@RequestBody List<OrdeUserInfoParam> userInfoParams) {
        if (userInfoParams.isEmpty()) {
            return ResponseResult.buildSuccessResponse();
        }
        String redisKey = "out_of_stock_phones";
        List<String> redisOrderNoList = RedisCache.lRange(redisKey, 0, -1);
        // 数据去重，set对象去重需重写equals和hashcode方法
        Set<OrdeUserInfoParam> orderNoSet = new HashSet<>(userInfoParams);
        List<String> orderNoList = orderNoSet.stream().map(OrdeUserInfoParam::getOrderNo).collect(Collectors.toList());
        if (orderNoList.isEmpty()) {
            return ResponseResult.buildSuccessResponse();
        }
        orderMapper.batchUpdateStockStatus(orderNoList);
        // 已经发送过的用户去重，并根据业务类型分组
        Map<Byte, List<OrdeUserInfoParam>> collect = orderNoSet.stream().filter(orderSet -> !redisOrderNoList.contains(orderSet.getUserInfo() + "_" + orderSet.getOrderType())).collect(Collectors.groupingBy(OrdeUserInfoParam::getOrderType));
        List<OrdeUserInfoParam> bfOrder = collect.get(OrderTypeEnum.BREAKFAST.getCode());
        List<OrdeUserInfoParam> lunchOrder = collect.get(OrderTypeEnum.LUNCH.getCode());
        if (null != bfOrder && !bfOrder.isEmpty()) {
            cacheOrder(redisKey, bfOrder, OrderTypeEnum.BREAKFAST.getCode());
        }
        if (null != lunchOrder && !lunchOrder.isEmpty()) {
            cacheOrder(redisKey, lunchOrder, OrderTypeEnum.LUNCH.getCode());
        }

        return ResponseResult.buildSuccessResponse();
    }

    private boolean cacheOrder(String redisKey, List<OrdeUserInfoParam> userInfoList, Byte orderType) {

        Set<String> userInfoSet = userInfoList.stream().map(OrdeUserInfoParam::getUserInfo).collect(Collectors.toSet());

        List<String> phones = userInfoSet.stream().map(user -> {
            int index = user.indexOf("_") + 1;
            return user.substring(index);
        }).collect(Collectors.toList());

        // 将这批手机存进redis
        userInfoSet.stream().forEach(userinfo -> {
            RedisCache.lLeftPush(redisKey, userinfo + "_" + orderType);
            RedisCache.expire(redisKey, 8, TimeUnit.HOURS);
        });

        // 判断是否到0730
        if (!checkTime()) {
            String waitPhonesKey = "wait_out_of_stock_phones";
            RedisCache.lLeftPushAll(waitPhonesKey, phones);
            RedisCache.expire(waitPhonesKey, 2, TimeUnit.HOURS);
            return true;
        }

        // signNo :午餐2 早餐3
        String signNo = "3";
        if (OrderTypeEnum.LUNCH.getCode().equals(orderType)) {
            signNo = "2";
        }
        smsTemplate.sendMsg(phones, "OUT_OF_STOCK", signNo, outOfStockMsg);
        return false;
    }

    @Override
    public void sendOutOfStockMsgTask() {
        String waitPhonesKey = "wait_out_of_stock_phones";
        List<String> phoneList = RedisCache.lRange(waitPhonesKey, 0, -1);
        if (null != phoneList && !phoneList.isEmpty()) {
            log.info("开始发送缺货通知短信");
            // 七点半发送的短信一般都是早餐 所以通道直接写死成早餐通道
            smsTemplate.sendMsg(phoneList, "OUT_OF_STOCK", "3", outOfStockMsg);
        }
        RedisCache.delete(waitPhonesKey);
    }

    @Override
    public ResponseResult<List<ShopGoodsCount>> getShopGoodsCount(@RequestParam(name = "shopCode", required = false)String shopCode,@RequestParam(name = "companyId", required = false) Long companyId) {
        log.info("查询门店公司商品已售量:{}公司ID{}",shopCode,companyId);
        if(!StringUtils.isEmpty(shopCode) && null != companyId){
            List<ShopGoodsCount> listResult = orderMapper.getShopGoodsCount(shopCode,companyId);
            log.info("查询返回值：{}",JSON.toJSONString(listResult));
            if (CollectionUtils.isEmpty(listResult)){
                return ResponseResult.buildFailResponse();
            }
            return ResponseResult.buildSuccessResponse(listResult);
        }
        return ResponseResult.buildFailResponse();
    }

    private List<OrderSearchResultPos> listSearchOrderPos(OrderSearchParamPos param) {
        List<OrderSearchResultPos> resultList = orderMapper.selectOrderPagePos(param);
        if (resultList == null || resultList.isEmpty()) {
            return new ArrayList<>();
        }
        ResponseResult<List<ShopBossDto>> shopResp = shopService.getAllShopList();
        if (null == shopResp || !shopResp.isSuccess()) {
            log.warn("调用门店查询结果失败：{}", JSONObject.toJSONString(shopResp));
            throw new RuntimeException("查询所有门店信息失败");
        }

        List<String> orderNoList = resultList.stream().map(OrderSearchResultPos::getOrderNo).collect(Collectors.toList());
//        OrderGoodsExample example = new OrderGoodsExample();
//        example.createCriteria().andOrderNoIn(orderNoList);
//        List<OrderGoods> orderDetails = orderGoodsMapper.selectByExample(example);
        List<OrderGoods> orderDetails = orderGoodsMapper.batchSearchByOrderNo(orderNoList);
        List<OrderGoodsResult> orderGoodsResults = new ArrayList<>();
        for (OrderGoods orderDetail : orderDetails) {
            // 判断是否套餐
//            if (orderDetail.getIsCombo()) {
//                OrderGoodsComboExample goodsComboExample = new OrderGoodsComboExample();
//                goodsComboExample.createCriteria().andOrderNoEqualTo(orderDetail.getOrderNo());
//                List<OrderGoodsCombo> orderGoodsCombos = orderGoodsComboMapper.selectByExample(goodsComboExample);
//                for (OrderGoodsCombo orderGoodsCombo : orderGoodsCombos) {
//                    OrderGoodsResult orderGoodsResult = new OrderGoodsResult();
//                    orderGoodsResult.setGoodsName(orderGoodsCombo.getGoodsName());
//                    orderGoodsResult.setGoodsNumber(orderGoodsCombo.getGoodsCount());
//                    orderGoodsResult.setOrderNo(orderGoodsCombo.getOrderNo());
//                    orderGoodsResults.add(orderGoodsResult);
//                }
//            } else {
                OrderGoodsResult orderGoodsResult = new OrderGoodsResult();
                orderGoodsResult.setGoodsName(orderDetail.getGoodsName());
                orderGoodsResult.setGoodsNumber(orderDetail.getGoodsCount());
                orderGoodsResult.setOrderNo(orderDetail.getOrderNo());
                orderGoodsResults.add(orderGoodsResult);
//            }
        }
        resultList.stream().forEach(order -> {
            List<OrderGoodsResult> orderGoodsCollect = orderGoodsResults.stream().filter(orderGoods -> orderGoods.getOrderNo().equals(order.getOrderNo())).collect(Collectors.toList());
            order.setDeliveryStatusStr(LogisticsDeliveryStatus.getNameByValue(order.getDeliveryStatus()));
            order.setGoodsResultList(orderGoodsCollect);
        });
        return resultList;
    }

    // 判断是否到了七点三十分 发送短信的时间
    private Boolean checkTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Date targetTime;
        try {
            targetTime = simpleDateFormat.parse(sendMsgTime);
        } catch (Exception e) {
            log.error("日期转换失败", e);
            return false;
        }
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = targetTime.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);
        LocalTime targetLocalTime = localDateTime.toLocalTime().withNano(0).withSecond(0);
        LocalTime localTime = LocalTime.now().withNano(0).withSecond(0);
        return localTime.isAfter(targetLocalTime);

    }

    @Override
    public ResponseResult<Integer> getOrderGoodsCount(@RequestBody OrderGoodsSumParam param) {
        // 根据门店ID获取公司ID
        log.info("根据门店获取已售量：{}", JSON.toJSONString(param));
	/*	ResponseResult<List<CompanyPolymerizationDto>> listCompany = companyService.queryByShopId(param.getShopId());
		//组装查询参数
		if (listCompany.isSuccess()) {

			if (listCompany.getData() != null && listCompany.getData().size() > 0) {
	                */
        //  List<CompanyPolymerizationDto> listCompanyPloymerization = listCompany.getData();
        //  List<Long> companyId = listCompanyPloymerization.stream().map(CompanyPolymerizationDto::getId).collect(Collectors.toList());
        param.setCompanyId(null);
        OrderGoodsCompanySqlParam paramSql = BeanMapper.map(param, OrderGoodsCompanySqlParam.class);
        paramSql.setBeginDeliveryDate(DateUtil.getDayBeginTime(param.getDeliveryDate()));
        paramSql.setEndDeliveryDate(DateUtil.getDayBeginTime(param.getDeliveryDate(), 1));
        log.info("查询入参：{}", JSON.toJSONString(paramSql));
        Integer goodsSum = orderMapper.selectOrderGoodsCountCompany(paramSql);
        log.info("返回值：{}", goodsSum);
        return ResponseResult.buildSuccessResponse(goodsSum);
		/*	}
		}*/
        //log.error("根据门店ID获取公司ID失败：{}",JSON.toJSONString(param));
        //return ResponseResult.buildSuccessResponse(0);
    }
}

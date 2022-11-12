package com.milisong.ecm.common.user.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.db.IdGenerator;
import com.farmland.core.util.BeanMapper;
import com.google.common.collect.Lists;
import com.milisong.ecm.common.enums.RestEnum;
import com.milisong.ecm.common.notify.api.NotifyService;
import com.milisong.ecm.common.notify.constant.BusinessLineEnum;
import com.milisong.ecm.common.notify.dto.FeedbackInvestigateSmsDto;
import com.milisong.ecm.common.user.api.UserEvaluationService;
import com.milisong.ecm.common.user.domain.UserEvaluation;
import com.milisong.ecm.common.user.dto.UserEvaluationDisplayDto;
import com.milisong.ecm.common.user.dto.UserEvaluationInfoDto;
import com.milisong.ecm.common.user.dto.UserEvaluationSendDto;
import com.milisong.ecm.common.user.dto.UserEvaluationSubmitDto;
import com.milisong.ecm.common.user.mapper.UserEvaluationMapper;
import com.milisong.ecm.common.user.properties.UserProperties;
import com.milisong.oms.api.OrderService;
import com.milisong.oms.dto.OrderDeliveryDto;
import com.milisong.oms.dto.OrderDeliveryGoodsDto;
import com.milisong.oms.dto.OrderDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 *    author  : Tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/10/10   18:47
 *    desc    : 问卷调查业务实现
 *    version : v1.0
 * </pre>
 */
@Slf4j
@RestController
public class UserEvaluationServiceImpl implements UserEvaluationService {

    //执行异步任务使用的缓存线程池，60s空闲后会被释放,最大10个
    private ThreadPoolExecutor executor = new ThreadPoolExecutor(0, 10,
            60L, TimeUnit.SECONDS,
            new SynchronousQueue<>());

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserEvaluationMapper userEvaluationMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private NotifyService notifyService;

    @Autowired
    private UserProperties userProperties;

    @Override
    public ResponseResult<Boolean> sendFeedbackMsg(@RequestBody UserEvaluationSendDto dto) {
        executor.execute(()-> {
            log.info("----发送问卷短信入参---{}",JSON.toJSONString(dto));
            //查询今日配送的订单(配送日期为今天的，并且已经配送的)
            //拿到订单号（子订单号ID)
            //TODO
            Date date = new Date();
            ResponseResult<List<OrderDeliveryDto>> orderDtos = orderService.getCompletedDeliveryListByDeliveryDate(date.getTime(),dto.getBusinessLine());
            //生成短网址（把订单id包含到url中）
//        orderService，给个日期，获取当天已完成配送的订单信息(订单id)
            Map<String, List<String>> mobileAndUrl = generateShortUrl(orderDtos.getData(),dto.getBusinessLine());
            //发送短信
            if (mobileAndUrl == null || mobileAndUrl.size() == 0) {
                return ;
            }
            FeedbackInvestigateSmsDto smsDto = new FeedbackInvestigateSmsDto();
            smsDto.setBusinessLine(dto.getBusinessLine());
            smsDto.setMap(mobileAndUrl);
            notifyService.sendFeedbackInvestigateSms(smsDto);
        });
        return ResponseResult.buildSuccessResponse();
    }

    @Override
    public ResponseResult<Boolean> testSendFeedbackMsg(@RequestBody UserEvaluationSendDto dto) {
        Long deliveryId = dto.getDeliveryId();
        ResponseResult<OrderDto> orderByDeliveryId = orderService.getOrderByDeliveryId(deliveryId);
        List<OrderDeliveryDto> deliveries = orderByDeliveryId.getData().getDeliveries();
        List<OrderDeliveryDto> orderDeliveryDtos = new ArrayList<>();
        deliveries.stream().forEach(o->{
            if(o.getId().equals(deliveryId)){
                orderDeliveryDtos.add(o);
            }
        });
        deliveries.stream().forEach(o-> o.setMobileNo(orderByDeliveryId.getData().getMobileNo()));
        Map<String,List<String>> mobileAndUrl = generateShortUrl(orderDeliveryDtos,dto.getBusinessLine());
        //发送短信
        FeedbackInvestigateSmsDto smsDto = new FeedbackInvestigateSmsDto();
        smsDto.setBusinessLine(dto.getBusinessLine());
        smsDto.setMap(mobileAndUrl);
        notifyService.sendFeedbackInvestigateSms(smsDto);
        return ResponseResult.buildSuccessResponse();
    }

    @Override
    public ResponseResult<UserEvaluationDisplayDto> getInvestigateInfo(@RequestBody UserEvaluationInfoDto dto) {
        log.info("---查询入参--{}",JSON.toJSONString(dto));
        Long deliveryId = dto.getDeliveryId();
       /* if(dto.getBusinessLine().equals(BusinessLineEnum.BREAKFAST.getVal())){
            int result = userEvaluationMapper.selectByDeliveryId(deliveryId);
            if(result > 0){
                return ResponseResult.buildSuccessResponse();
            }
        }*/
        UserEvaluationDisplayDto userEvaluationDisplayDto = new UserEvaluationDisplayDto();
        userEvaluationDisplayDto.setDeliveryId(deliveryId);
        log.info("----查询订单服务---{}",deliveryId);
        ResponseResult<OrderDto> orderDtoResponseResult = orderService.getOrderByDeliveryId(deliveryId);
        log.info("-----查询订单信息结果----{}",JSONObject.toJSONString(orderDtoResponseResult));
        OrderDto orderDto = orderDtoResponseResult.getData();
        if(orderDto == null){
            return ResponseResult.buildFailResponse(RestEnum.SYS_ERROR.getCode(),RestEnum.SYS_ERROR.getDesc());
        }
        List<OrderDeliveryDto> deliveries = orderDto.getDeliveries();
        if(deliveries == null || deliveries.size() == 0){
            return ResponseResult.buildFailResponse(RestEnum.SYS_ERROR.getCode(),RestEnum.SYS_ERROR.getDesc());
        }
        OrderDeliveryDto deliveryDto = new OrderDeliveryDto();
        for(OrderDeliveryDto delivery:deliveries){
            if(delivery.getId().equals(deliveryId)){
                deliveryDto = delivery;
                break;
            }
        }
        List<OrderDeliveryGoodsDto> deliveryGoods = deliveryDto.getDeliveryGoods();
        OrderDeliveryGoodsDto orderDeliveryGoodsDto = deliveryGoods.get(0);
        userEvaluationDisplayDto.setDeliveryGoodsId(orderDeliveryGoodsDto.getId());
        userEvaluationDisplayDto.setGoodsName(orderDeliveryGoodsDto.getGoodsName());
        if(dto.getBusinessLine().equals(BusinessLineEnum.BREAKFAST.getVal())){
            ResponseResult<List<OrderDeliveryGoodsDto>> listResponseResult = orderService.getGoodsByDeliveryId(deliveryId);
            log.info("---查询配送单商品明细--{}",JSON.toJSONString(listResponseResult));
            if(listResponseResult.isSuccess() && listResponseResult.getData() != null){
                List<OrderDeliveryGoodsDto> goodsDtos = listResponseResult.getData();
                goodsDtos.removeIf(o->Boolean.TRUE.equals(o.getIsCombo()));
                if(goodsDtos.size() > 0){
                    userEvaluationDisplayDto.setGoodsName(goodsDtos.get(0).getGoodsName());
                }
                if(goodsDtos.size() > 1){
                    userEvaluationDisplayDto.setGoodsNameSecond(goodsDtos.get(1).getGoodsName());
                }
                if(goodsDtos.size() > 2){
                    userEvaluationDisplayDto.setGoodsNameThird(goodsDtos.get(2).getGoodsName());
                }
            }
        }
        userEvaluationDisplayDto.setUserId(orderDto.getUserId());
        userEvaluationDisplayDto.setOrderId(orderDto.getId());
        userEvaluationDisplayDto.setMobileNo(orderDto.getMobileNo());
        return ResponseResult.buildSuccessResponse(userEvaluationDisplayDto);
    }

    /**
     * 保存问卷调查信息
     * @param dto,提交orderId，userId,orderNo,mobileNo，goodsName
     * @return
     */
    @Override
    @Transactional
    public ResponseResult<Boolean> saveInvestigateInfo(@RequestBody UserEvaluationSubmitDto dto) {
        UserEvaluation userEvaluation = BeanMapper.map(dto, UserEvaluation.class);
        userEvaluation.setId(IdGenerator.nextId());
        if(dto.getBusinessLine().equals(BusinessLineEnum.BREAKFAST.getVal())
                &&dto.getGoodsNames()!= null){
            userEvaluation.setGoodsName(StringUtils.join(dto.getGoodsNames(),","));
        }
        userEvaluationMapper.insertSelective(userEvaluation);
        return ResponseResult.buildSuccessResponse(true);
    }

    private Map<String, List<String>> generateShortUrl(List<OrderDeliveryDto> orderDtos, Byte businessLine) {
        Map<String,Long>  map = new HashMap<>();
        if(orderDtos == null || orderDtos.size() == 0){
            return null;
        }
        orderDtos.stream().forEach(o-> map.put(o.getMobileNo(),o.getId()));
        Set<String> mobiles = map.keySet();
        //存放手机号和url
        Map<String,List<String>> urlMap = new HashMap<>();

        mobiles.stream().forEach(o->{
            List<String> list = new ArrayList<>();
            String url = getShortUrl(map.get(o));
            if(businessLine.equals(BusinessLineEnum.LUNCH.getVal())){
                list.add(getOrderGoodsName(map.get(o)));
            }
            list.add(url);
            urlMap.put(o,list);
        });
        return urlMap;
    }

    private String getOrderGoodsName(Long deliveryId) {
        ResponseResult<OrderDto> orderDtoResponseResult = orderService.getOrderByDeliveryId(deliveryId);
        OrderDto orderDto = orderDtoResponseResult.getData();
        List<OrderDeliveryDto> deliveries = orderDto.getDeliveries();
        OrderDeliveryDto deliveryDto = new OrderDeliveryDto();
        for(OrderDeliveryDto delivery:deliveries){
            if(delivery.getId().equals(deliveryId)) {
                deliveryDto = delivery;
                break;
            }
        }
        List<OrderDeliveryGoodsDto> deliveryGoods = deliveryDto.getDeliveryGoods();
        OrderDeliveryGoodsDto orderDeliveryGoodsDto = deliveryGoods.get(0);
        return orderDeliveryGoodsDto.getGoodsName();
    }

    private String getShortUrl(Long orderId){
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        messageConverters.add(new FastJsonHttpMessageConverter());
        JSONObject result = restTemplate.getForObject(userProperties.getShortUrl().concat(userProperties.getEvaluationShowUrl()).concat(orderId.toString()),JSONObject.class);
        JSONArray urls = result.getJSONArray("urls");
        JSONObject jsonObject = urls.getJSONObject(0);
        log.info("【生成短网址】：result={}", JSONObject.toJSONString(result));
        return jsonObject.getString("url_short");
    }

    private ResponseResult<Object> postForUrl(String url,Object dto){
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<Object> entity = new HttpEntity<>(JSON.toJSONString(dto),headers);
        ResponseResult<Object> result = restTemplate.postForObject(url, entity,ResponseResult.class);
        if (result.isSuccess()) {
            return result;
        } else {
            log.error(result.getCode(), result.getMessage());
            return result;
        }
    }

}

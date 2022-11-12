package com.milisong.ecm.common.user.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.db.IdGenerator;
import com.farmland.core.util.BeanMapper;
import com.milisong.ecm.common.notify.api.SmsService;
import com.milisong.ecm.common.notify.dto.SmsSendDto;
import com.milisong.ecm.common.user.api.FeedbackService;
import com.milisong.ecm.common.user.domain.UserFeedback;
import com.milisong.ecm.common.user.domain.UserFeedbackType;
import com.milisong.ecm.common.user.dto.FeedbackTypeDto;
import com.milisong.ecm.common.user.dto.UserFeedbackDto;
import com.milisong.ecm.common.user.dto.UserFeedbackTypeDto;
import com.milisong.ecm.common.user.mapper.UserFeedbackMapper;
import com.milisong.ecm.common.user.mapper.UserFeedbackTypeMapper;
import com.milisong.ucs.api.UserDeliveryAddressService;
import com.milisong.ucs.dto.UserDeliveryAddressDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static com.milisong.ecm.common.user.enums.FeedbackResponseCode.*;


/**
 * @author sailor wang
 * @date 2018/9/5 下午4:30
 * @description
 */
@Slf4j
@RestController
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private UserFeedbackTypeMapper userFeedbackTypeMapper;

    @Autowired
    private UserFeedbackMapper userFeedbackMapper;

    @Autowired
    private UserDeliveryAddressService userDeliveryAddressService;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SmsService smsService;

    @Value("${user.feedback.mobiles}")
    private String MOBILENOS;

    /**
     * 获取反馈类型
     * @return
     */
    @Override
    public ResponseResult<List<UserFeedbackTypeDto>> feedbackType(@RequestBody FeedbackTypeDto feedbackTypeDto) {
        try {
            Byte buzLine = feedbackTypeDto.getBusinessLine();
            List<UserFeedbackTypeDto> feedbackTypes;
            if(feedbackTypeDto.getTransit()!= null && feedbackTypeDto.getTransit()){
                feedbackTypes = userFeedbackTypeMapper.feedbackTypeTransit(buzLine);
            }else{
                feedbackTypes = userFeedbackTypeMapper.feedbackType(buzLine);
            }
            return ResponseResult.buildSuccessResponse(feedbackTypes);
        } catch (Exception e) {
            log.error("",e);
        }
        return ResponseResult.buildFailResponse(QUERY_FEEDBACK_TYPE_EXCEPTION.code(),QUERY_FEEDBACK_TYPE_EXCEPTION.message());
    }

    /**
     * 用户反馈
     * @param userFeedbackDto
     * @return
     */
    @Override
    public ResponseResult<Boolean> saveFeedback(@RequestBody UserFeedbackDto userFeedbackDto) {
        if (userFeedbackDto == null || userFeedbackDto.getBusinessLine() == null){
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(),REQUEST_PARAM_EMPTY.message());
        }
        try {
            log.info("保存用户缓存 -> {}", JSONObject.toJSONString(userFeedbackDto));
            UserFeedback userFeedback = BeanMapper.map(userFeedbackDto,UserFeedback.class);
            userFeedback.setId(IdGenerator.nextId());
            userFeedback.setCreateTime(new Date());
            userFeedback.setUpdateTime(new Date());
            int row = userFeedbackMapper.insertSelective(userFeedback);
            //TODO 发送短信通知运营人员
            String[] mobiles = MOBILENOS.replaceAll(" ", "").split(",");
            Map<Integer,Object> map = new HashMap<>();
            map.put(0,userFeedback.getNickName());
            map.put(1,userFeedback.getMobileNo());
            map.put(2,getFeedBackType(userFeedback.getFeedbackType()));
            map.put(3,userFeedback.getFeedbackOpinion());
            String address = "未知";
            ResponseResult<UserDeliveryAddressDto> deliveryAddrResult = userDeliveryAddressService.queryDeliveryAddrByMobileAndId(userFeedbackDto.getUserId(),userFeedback.getMobileNo());
            if(deliveryAddrResult!= null && deliveryAddrResult.getData()!= null){
                UserDeliveryAddressDto data = deliveryAddrResult.getData();
                address = data.getDeliveryAddress()+" "+data.getDeliveryFloor()+"层 "+data.getDeliveryCompany();
            }
            map.put(4,address);
            sendNotifySms(Arrays.asList(mobiles),map);
            return ResponseResult.buildSuccessResponse(row > 0);
        } catch (Exception e) {
            log.error("",e);
        }
        return ResponseResult.buildFailResponse(SAVE_FEEDBACK_TYPE_EXCEPTION.code(),SAVE_FEEDBACK_TYPE_EXCEPTION.message());
    }

    /**
     * 1有效,0无效
     * @param feedbackType
     * @return
     */
    private String getFeedBackType(long feedbackType) {
        UserFeedbackType dto = userFeedbackTypeMapper.selectByPrimaryKey(feedbackType);
        if(dto!=null){
            return dto.getName();
        }
        return "未定义";
    }

    private boolean sendNotifySms(List<String> mobiles,Map<Integer,Object> map){
        TreeMap treeMap = new TreeMap(Comparator.comparingInt(o -> (int)o));
        treeMap.putAll(map);
        SmsSendDto smsSendDto = new SmsSendDto();
        smsSendDto.setMobile(mobiles);
        smsSendDto.setParams(new ArrayList<>(treeMap.values()));
        smsSendDto.setTemplateCode("MILISONG_USER_FEEDBACK");
        ResponseResult<String> result = smsService.sendMsg(smsSendDto);
        return result.isSuccess();
    }

    private ResponseResult<?> sendPost(Object obj,String url){
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        String result = "{}";
        if(obj != null){
            result = JSON.toJSONString(obj);
        }
        HttpEntity<String> formEntity = new HttpEntity<String>(result,headers);
        ResponseResult responseResult = restTemplate.postForObject(url, formEntity, ResponseResult.class);
        if(!responseResult.isSuccess()){
            return ResponseResult.buildFailResponse(responseResult.getCode(),responseResult.getMessage());
        }else{
            return responseResult;
        }
    }
}
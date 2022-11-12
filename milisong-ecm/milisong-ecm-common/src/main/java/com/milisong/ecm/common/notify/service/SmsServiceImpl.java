package com.milisong.ecm.common.notify.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.milisong.ecm.common.notify.api.SmsService;
import com.milisong.ecm.common.notify.dto.SmsSendDto;
import com.milisong.ecm.common.notify.properties.NotifyProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@RestController
public class SmsServiceImpl implements SmsService {

    @Autowired
    private NotifyProperties notifyProperties;

    private static final String businessId = "MILISONG";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseResult<String> sendMsg(@RequestBody SmsSendDto dto){
        String templateCode = dto.getTemplateCode();
        List<String> mobiles = dto.getMobile();
        List<String> params = dto.getParams();
        if (StringUtils.isBlank(templateCode) || CollectionUtils.isEmpty(mobiles)) {
            return ResponseResult.buildFailResponse("9999","参数校验失败，缺少模版或者手机");
        }
        // 发送短信
        SmsServiceImpl.SmsDto smsDto = new SmsServiceImpl.SmsDto();
        List<String> mobileList = mobiles;
        smsDto.setTelephones(mobileList);
        smsDto.setTemplateCode(templateCode);
        if(params!=null && params.size() >0){
            smsDto.setParameters(params);
        }
        if(dto.getAdvFlag()){
            smsDto.setAdvFlag(true);
        }
        smsDto.setIp("127.0.0.1");
        smsDto.setSignNo(notifyProperties.getSignNo());
        smsDto.setBusinessLine(businessId);
        try {
            log.info("【发送短信】：smsDto={}", JSONObject.toJSONString(smsDto));
            HttpHeaders headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(type);
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            HttpEntity<Object> entity = new HttpEntity<>(JSON.toJSONString(smsDto),headers);
            ResponseResult<String> result = restTemplate.postForObject(notifyProperties.getSendMsgPath(), entity,ResponseResult.class);
            if (result.isSuccess()) {
                return result;
            } else {
                log.error(result.getCode(), result.getMessage());
                return result;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseResult.buildFailResponse("9999","发送短信失败");
        }
    }

    @Getter
    @Setter
    static final class SmsDto {
        private String templateCode;
        private List<String> telephones;
        private List<String> parameters;
        private String businessLine;
        private String ip;
        private String signNo;
        private boolean advFlag = false;
    }

}

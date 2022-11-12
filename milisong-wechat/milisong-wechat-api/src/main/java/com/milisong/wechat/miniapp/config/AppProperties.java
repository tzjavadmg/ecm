package com.milisong.wechat.miniapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.milisong.wechat.miniapp.enums.BusinessLine.BREAKFAST;

/**
 * @author sailor wang
 * @date 2018/12/3 2:50 PM
 * @description
 */
@Component
public class AppProperties {

    @Autowired
    LanuchAppProperties lanuchAppProperties;

    @Autowired
    BreakfastAppProperties breakfastAppProperties;

    public String getReceiveTemplateId(Byte buzLine) {
        if (BREAKFAST.getCode().equals(buzLine)){
            return breakfastAppProperties.getReceiveTemplateId();
        }
        // 默认返回午餐配置
        return lanuchAppProperties.getReceiveTemplateId();
    }

    public String getPayFailTemplateId(Byte buzLine) {
        if (BREAKFAST.getCode().equals(buzLine)){
            return breakfastAppProperties.getPayFailTemplateId();
        }
        return lanuchAppProperties.getPayFailTemplateId();
    }

    public String getMiniAppid(Byte buzLine) {
        if (BREAKFAST.getCode().equals(buzLine)){
            return breakfastAppProperties.getMiniAppid();
        }
        return lanuchAppProperties.getMiniAppid();
    }

    public String getMiniSecret(Byte buzLine) {
        if (BREAKFAST.getCode().equals(buzLine)){
            return breakfastAppProperties.getMiniSecret();
        }
        return lanuchAppProperties.getMiniSecret();
    }

    public String getPlanId() {
        return lanuchAppProperties.getPlanId();
    }

    public String getMchid(Byte buzLine) {
        if (BREAKFAST.getCode().equals(buzLine)){
            return breakfastAppProperties.getMchid();
        }
        return lanuchAppProperties.getMchid();
    }

    public String getApikey(Byte buzLine) {
        if (BREAKFAST.getCode().equals(buzLine)){
            return breakfastAppProperties.getApikey();
        }
        return lanuchAppProperties.getApikey();
    }

    public String getSigntype(Byte buzLine) {
        if (BREAKFAST.getCode().equals(buzLine)){
            return breakfastAppProperties.getSigntype();
        }
        return lanuchAppProperties.getSigntype();
    }

    public String getPappayapplyCallbackUrl() {
        return lanuchAppProperties.getPappayapplyCallbackUrl();
    }

    public String getUnifiedOrderCallbackUrl(Byte buzLine) {
        if (BREAKFAST.getCode().equals(buzLine)){
            return breakfastAppProperties.getUnifiedOrderCallbackUrl();
        }
        return lanuchAppProperties.getUnifiedOrderCallbackUrl();
    }

    public String getContractCallbackUrl(Byte buzLine) {
        if (BREAKFAST.getCode().equals(buzLine)){
            return breakfastAppProperties.getContractCallbackUrl();
        }
        return lanuchAppProperties.getContractCallbackUrl();
    }

    public String getRefundCallbackUrl(Byte buzLine) {
        if (BREAKFAST.getCode().equals(buzLine)){
            return breakfastAppProperties.getRefundCallbackUrl();
        }
        return lanuchAppProperties.getRefundCallbackUrl();
    }

}
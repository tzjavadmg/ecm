package com.milisong.wechat.miniapp.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "breakfast")
@Component
@Setter
@Getter
public class BreakfastAppProperties {

    public String receiveTemplateId;//支付成功通知模板
    public String payFailTemplateId;   // 支付失败通知模版
    private String miniAppid;   //小程序appid
    private String miniSecret;  //小程序secret
    private String mchid;   //商户id
    private String apikey;  //
    private String signtype;    //加密类型
    private String unifiedOrderCallbackUrl; // 主动支付回调
    private String contractCallbackUrl; //签约回调
    private String refundCallbackUrl;//退款回调
}

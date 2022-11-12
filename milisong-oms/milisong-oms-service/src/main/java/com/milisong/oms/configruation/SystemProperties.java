package com.milisong.oms.configruation;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @ Description：系统配置属性
 * @ Author     ：zengyuekang.
 * @ Date       ：2018/12/26 13:49
 */
@Component
@ConfigurationProperties(
        prefix = "oms.sys"
)
@Getter
@Setter
@RefreshScope
public class SystemProperties {

    private String scmBaseUrl;
    //延迟队列配置
    private DelayQueue delayQueue = new DelayQueue();
    //本地二级缓存配置
    private LocalCache localCache = new LocalCache();

    private WechatPay wechatPay = new WechatPay();
    //积分比例
    private Integer pointRatio = 50;

    private Integer nodeHour = 2;

    @Getter
    @Setter
    public static class DelayQueue {

        private String baseUrl;

        private String orderCallbackUrl;

        private String virtualOrderCallbackUrl;

        private String groupBuyOrderCallbackUrl;
    }

    @Getter
    @Setter
    public static class LocalCache {

        private Long expireTime = 1L;
    }

    @Getter
    @Setter
    public static class WechatPay {
        private String defaultPayCallbackUrl;
        private String groupBuyPayCallbackUrl;
        private String defaultRefundCallbackUrl;
        private String groupBuyRefundCallbackUrl;
        private Long groupBuyExpiredTime = 60L;

    }
}

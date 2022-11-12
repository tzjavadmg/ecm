package com.milisong.ecm.lunch.mq;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;


public interface MessageSink {

    /**
     * 全局属性、单个门店属性配置消息input
     */
    String MESSAGE_CONFIG_INPUT = "config_input";
    
    /**
     * 门店截单配置消息input
     * @return
     */
    String MESSAGE_CONFIG_INTERCEPT_INPUT = "config_intercept_input";
    
    /**
     * Banner图配置消息input
     * @return
     */
    String MESSAGE_CONFIG_BANNER_INPUT = "config_banner_input";

    /**
     * 商品基本信息
     */
    String MESSAGE_GOODS_BASIC = "goods_basic_input";
    
    /**
     * 可售商品消息消费通道
     * @return
     */
    String MESSAGE_SALABLE_GOODS = "salable_goods_input";
    
    /**
     * 档期消息消费通道
     * @return
     */
    String MESSAGE_SCHEDULE_INPUT = "schedule_input";

    /**
     * 消费公司信息
     */
    String MESSAGE_COMPANY_INPUT = "company_input";
    
    @Input(MessageSink.MESSAGE_CONFIG_INPUT)
    SubscribableChannel messageConfigInput();
    
    @Input(MessageSink.MESSAGE_CONFIG_INTERCEPT_INPUT)
    SubscribableChannel messageConfigInterceptInput();
    
    @Input(MessageSink.MESSAGE_CONFIG_BANNER_INPUT)
    SubscribableChannel messageConfigBannerInput();
    
    @Input(MESSAGE_GOODS_BASIC)
    SubscribableChannel messageGoodsBasicInput();


    @Input(MESSAGE_SALABLE_GOODS)
    SubscribableChannel messageSalableGoodsInput();
    
    @Input(MESSAGE_SCHEDULE_INPUT)
    SubscribableChannel messageMovieInput();

    @Input(MESSAGE_COMPANY_INPUT)
    SubscribableChannel messageCompanyInput();
 
}

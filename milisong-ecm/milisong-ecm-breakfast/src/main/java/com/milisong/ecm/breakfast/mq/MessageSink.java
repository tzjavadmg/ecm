package com.milisong.ecm.breakfast.mq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface MessageSink {
    /**
     * 公司消息消费通道
     */
    String MESSAGE_COMPANY_INPUT = "company_input";
    
    /**
     * 分类消息消费通道
     * @return
     */
    String MESSAGE_CATALOG_INPUT = "catalog_input";
    
    /**
     * 档期消息消费通道
     * @return
     */
    String MESSAGE_MOVIE_INPUT = "movie_input";
    
    /**
     * 可售商品消息消费通道
     * @return
     */
    String MESSAGE_SALABLE_GOODS = "salable_goods_input";
 
    
    /**
     * 商品基本信息
     */
    String MESSAGE_GOODS_BASIC = "goods_basic_input";
    
    
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
    
    @Input(MESSAGE_COMPANY_INPUT)
    SubscribableChannel messageCompanyInput();

    @Input(MESSAGE_CATALOG_INPUT)
    SubscribableChannel messageCatalogInput();
    
    @Input(MESSAGE_MOVIE_INPUT)
    SubscribableChannel messageMovieInput();

    @Input(MESSAGE_SALABLE_GOODS)
    SubscribableChannel messageSalableGoodsInput();
    
    @Input(MESSAGE_GOODS_BASIC)
    SubscribableChannel messageGoodsBasicInput();
    
    @Input(MessageSink.MESSAGE_CONFIG_INPUT)
    SubscribableChannel messageConfigInput();
    
    @Input(MessageSink.MESSAGE_CONFIG_INTERCEPT_INPUT)
    SubscribableChannel messageConfigInterceptInput();
    
    @Input(MessageSink.MESSAGE_CONFIG_BANNER_INPUT)
    SubscribableChannel messageConfigBannerInput();

 
}

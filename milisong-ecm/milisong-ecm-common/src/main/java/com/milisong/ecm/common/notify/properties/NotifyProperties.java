package com.milisong.ecm.common.notify.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;


/**
 * <pre>
 *    author  : tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/12/4   14:46
 *    desc    : 通知体系的一些模板配置
 *    version : v1.0
 * </pre>
 */
@Configuration
@Data
@RefreshScope
public class NotifyProperties {

    //调用SCM，发送IVR电话执行请求
    @Value("${ivr.send-url:@null}")
    private String ivrUrl;
    //调用SCM，发送IVR电话模板
    @Value("${ivr.template:@null}")
    private String ivrTemplate;

    //取餐通知微信模板id
    @Value("${miniapp.takeFoodTemplateId:@null}")
    private String takeFoodTemplateId;
    @Value("${miniapp.receiveTemplateId:@null}")
    private String receiveTemplateId;
    @Value("${miniapp.payFailTemplateId:@null}")
    private String payFailTemplateId;

    //早餐取餐通知微信模板id
    @Value("${breakfast.takeFoodTemplateId:@null}")
    private String bfTakeFoodTemplateId;
    @Value("${breakfast.receiveTemplateId:@null}")
    private String bfReceiveTemplateId;
    @Value("${breakfast.payFailTemplateId:@null}")
    private String bfPayFailTemplateId;

    /**
     * 【发团 & 参团成功】：拼团进度通知模版id
     */
    @Value("${miniapp.joinGroupBuyTemplateId:@null}")
    private String joinGroupBuyTemplateId;

    /**
     * 【发团 & 参团成功】：拼团进度通知模版id
     */
    @Value("${miniapp.joinGroupBuyFailedTemplateId:@null}")
    private String joinGroupBuyFailedTemplateId;

    /**
     * 【拼团时间提醒（在剩余30min的时候提醒）进度提示】：拼团待成团提醒模版id
     */
    @Value("${miniapp.joinGroupBuyTimeRemindTemplateId:@null}")
    private String joinGroupBuyTimeRemindTemplateId;

    /**
     * 【仅提示团长第一个参团和倒数第二个参团进度】：拼团进度通知
     */
    @Value("${miniapp.joinGroupBuyScheduleTemplateId:@null}")
    private String joinGroupBuyScheduleTemplateId;

    /**
     * 拼团成功通知
     */
    @Value("${miniapp.joinGroupBuySuccessTemplateId:@null}")
    private String joinGroupBuySuccessTemplateId;

    //联系电话
    @Value("${sms.contact-phone}")
    private String contactPhone;
    //获取集单中详单
    @Value("${scm.order.union-order-url}")
    private String scmUnionOrderUrl;

    @Value("${sms.send-url}")
    private String sendMsgPath;

    @Value("${sms.sign-no}")
    private String signNo;

    @Value("${business-line}")
    private Byte businessLine;
}

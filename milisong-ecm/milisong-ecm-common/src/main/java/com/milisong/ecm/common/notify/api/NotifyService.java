package com.milisong.ecm.common.notify.api;

import com.farmland.core.api.ResponseResult;
import com.milisong.ecm.common.notify.dto.*;
import com.milisong.pms.prom.dto.groupbuy.GroupBuyWechatMsgMessage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <pre>
 *    author  : Tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/9/20   22:50
 *    desc    : 通知业务
 *    version : v1.0
 * </pre>
 */
public interface NotifyService {

    /**
     * 顺丰送达后，通知我方处理通知业务
     * @param
     * @return
     */
    @PostMapping(value = "/v1/NotifyService/notifyCustomer")
    ResponseResult<Boolean> notifyCustomer(@RequestBody NotifyArriveDto dto);

    /**
     * 发送短信通知
     * @param dto
     * @return
     */
    @PostMapping(value = "/v1/NotifyService/sendNotifySms")
    boolean sendNotifySms(@RequestBody NotifySmsDto dto);

    /**
     * 打电话通知
     * @param dto
     * @return
     */
    @PostMapping(value = "/v1/NotifyService/sendNotifyIvr")
    boolean sendNotifyIvr(@RequestBody NotifyIvrDto dto);

    /**
     * 再次打电话通知 入参:orderNoKey,mobile,value
     * @param dto
     * @return
     */
    @PostMapping(value = "/v1/NotifyService/sendNotifyRetryIvr")
    boolean sendNotifyRetryIvr(@RequestBody NotifyIvrDto dto);

    /**
     * 更新电话拨打结果到redis缓存中,入参： uuid, mobile, success
     * @param dto
     * @return
     */
    @PostMapping(value = "/v1/NotifyService/updateIvrResult")
    boolean updateIvrResult(@RequestBody NotifyIvrResponseDto dto);

    /**
     * 发送微信取餐通知,入参:map
     * @param dto
     * @return
     */
    @PostMapping(value = "/v1/NotifyService/sendWeiXinDeliveryMsg")
    boolean sendWeiXinDeliveryMsg(@RequestBody WechatArriveNotifyDto dto);

    /**
     * 发送微信支付通知
     * @param dto
     */
    @PostMapping(value = "/v1/NotifyService/sendWeiXinPayMsg")
    void sendWeiXinPayMsg(@RequestBody WechatPayNotifyDto dto);

    /**
     * 提醒用户今日订单
     * @return
     */
    @PostMapping(value = "/v1/NotifyService/notifyTodayOrderTask")
    ResponseResult<String> notifyTodayOrderTask(@RequestBody NotifyTodayOrderTaskDto dto);

    /**
     * 发送待支付微信通知,入参:orderId
     * @param dto
     */
    @PostMapping(value = "/v1/NotifyService/notifyWaitPay")
    void notifyWaitPay(@RequestBody WechatPayNotifyDto dto);

    /**
     * 检查并处理重试拨号
     */
    @PostMapping(value = "/v1/NotifyService/dealRetryIvr")
    void dealRetryIvr();

    /**
     * 发送用户反馈信息通知公司人员短信
     * @param dto
     * @return
     */
    @PostMapping(value = "/v1/NotifyService/sendFeedbackNotifySms")
    boolean sendFeedbackNotifySms(@RequestBody SendFeedbackNotifyDto dto);

    /**
     * 发送用户反馈调查短信
     * @param dto
     * @return
     */
    @PostMapping(value = "/v1/NotifyService/sendFeedbackInvestigateSms")
    boolean sendFeedbackInvestigateSms(@RequestBody FeedbackInvestigateSmsDto dto);

    /**
     * 发送推广红包通知短信
     * @param dto
     * @return
     */
    @PostMapping(value = "/v1/NotifyService/sendAdvertiseSms")
    void sendAdvertiseSms(@RequestBody AdvertiseSmsDto dto);


    /**
     * 【发团 & 参团成功】：拼团进度通知
     *
     * @return
     */
    Boolean sendWeiXinJoinGroupBuy(GroupBuyWechatMsgMessage groupBuyWechatMsgMessage);


    /**
     * 【拼团超时，失败退款】：拼团失败通知
     *
     * @return
     */
    Boolean sendWeiXinJoinGroupBuyFailed(GroupBuyWechatMsgMessage groupBuyWechatMsgMessage);


    /**
     * 【拼团时间提醒（在剩余30min的时候提醒）进度提示】：拼团待成团提醒
     *
     * @return
     */
    Boolean sendWeiXinJoinGroupBuyTimeRemind(GroupBuyWechatMsgMessage groupBuyWechatMsgMessage);


    /**
     * 【仅提示团长第一个参团和倒数第二个参团进度】：拼团进度通知
     *
     * @return
     */
    Boolean sendWeiXinJoinGroupBuySchedule(GroupBuyWechatMsgMessage groupBuyWechatMsgMessage);



    /**
     * 拼团成功通知
     *
     * @return
     */
    Boolean sendWeiXinJoinGroupBuySuccess(GroupBuyWechatMsgMessage groupBuyWechatMsgMessage);

}

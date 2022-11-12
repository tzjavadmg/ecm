package com.milisong.pms.prom.api;

import com.farmland.core.api.ResponseResult;
import com.milisong.pms.prom.dto.DelayQueueMessage;
import com.milisong.pms.prom.dto.groupbuy.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 早餐拼团
 *
 * @author sailor wang
 * @date 2019/5/17 3:27 PM
 * @description
 */
@FeignClient("milisong-pms-service")
public interface GroupBuyService {

    /**
     * 获取拼团详情
     *
     * @param groupBuyRequest
     * @return
     */
    @PostMapping("/v1/GroupBuyService/groupBuyEntry")
    ResponseResult<GroupBuyEntryResponse> groupBuyEntry(@RequestBody GroupBuyRequest groupBuyRequest);


    /**
     * 点击分享的的参团信息
     *
     * @return
     */
    @PostMapping("/v1/GroupBuyService/queryOtherJoinedGroupBuy")
    ResponseResult<GroupBuyResponse> queryOtherJoinedGroupBuy(@RequestBody GroupBuyRequest groupBuyRequest);


    /**
     * 查询自己参与的拼团信息
     *
     * @param groupBuyRequest
     * @return
     */
    @PostMapping("/v1/GroupBuyService/querySelfJoinedGroupBuy")
    ResponseResult<GroupBuyResponse> querySelfJoinedGroupBuy(@RequestBody GroupBuyRequest groupBuyRequest);

    /**
     * 预支付订单，创建开团
     *
     * @param groupBuyRequest
     * @return
     */
    @PostMapping("/v1/GroupBuyService/createGroupBuyForPay")
    ResponseResult<GroupBuyResponse> createGroupBuyForPay(@RequestBody GroupBuyRequest groupBuyRequest);

    /**
     * 释放拼团锁
     *
     * @param groupBuyRequest
     * @return
     */
    @PostMapping("/v1/GroupBuyService/releaseGroupBuyLock")
    ResponseResult<Boolean> releaseGroupBuyLock(@RequestBody GroupBuyRequest groupBuyRequest);

    /**
     * 定时取消超时的拼团
     *
     * @return
     */
    @PostMapping("/v1/GroupBuyService/refundSigleGroupBuy")
    ResponseResult<Boolean> refundSigleGroupBuy(@RequestBody DelayQueueMessage message);

    /**
     * 定时取消超时的拼团
     *
     * @return
     */
    @PostMapping("/v1/GroupBuyService/refundGroupBuy")
    ResponseResult<Boolean> refundGroupBuy();

    /**
     * 剩余xx时间，拼团待成团提醒
     *
     * @return
     */
    @PostMapping("/v1/GroupBuyService/leftTimeNotifyGroupBuy")
    ResponseResult<Boolean> leftTimeNotifyGroupBuy(@RequestBody DelayQueueMessage message);


    /**
     * 查询拼团活动详情
     *
     * @param groupBuyRequest
     * @return
     */
    @PostMapping("/v1/GroupBuyService/queryActivityGroupBuyInfo")
    ResponseResult<ActivityGroupBuyDto> queryActivityGroupBuyInfo(@RequestBody GroupBuyRequest groupBuyRequest);

    /**
     * 预支付订单，参团
     *
     * @param groupBuyRequest
     * @return
     */
    @PostMapping("/v1/GroupBuyService/joinGroupBuyForPay")
    ResponseResult<GroupBuyResponse> joinGroupBuyForPay(@RequestBody GroupBuyRequest groupBuyRequest);

    /**
     * 拼团支付成功
     * @param payRequest
     * @return
     */
    @PostMapping("/v1/GroupBuyService/groupBuyPaySuccess")
    ResponseResult<Boolean> groupBuyPaySuccess(@RequestBody GroupBuyPayRequest payRequest);





}
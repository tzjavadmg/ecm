package com.milisong.pms.breakfast.web.lc;

import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.milisong.pms.breakfast.service.lc.LCRestPromotionService;
import com.milisong.pms.prom.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author sailor wang
 * @date 2018/9/14 下午7:32
 * @description
 */
@Slf4j
@RestController
@RequestMapping("/lc/v1/promotion/")
public class LCPromotionRest {

    @Autowired
    private LCRestPromotionService promotionService;

    /**
     * 活动砍红包发起入口
     * @return
     */
//    @PostMapping("create-cutredpacket")
//    ResponseResult<UserRedPacketResponse> createUserCutRedPacket(@RequestBody UserRequest userRequest){
//        return promotionService.createUserCutRedPacket(userRequest);
//    }

    /**
     * 砍活动红包
     * @return
     */
//    @PostMapping("hack-cutredpacket/{userRedPacketId}")
//    ResponseResult<UserRedPacketResponse> hackUserCutRedPacket(@PathVariable("userRedPacketId") Long userRedPacketId, @RequestBody UserRequest userRequest){
//        return promotionService.hackUserCutRedPacket(userRedPacketId,userRequest);
//    }

    /**
     * 发起多人分享红包
     * @return
     */
    @GetMapping("create-multishareredpacket/{orderid}")
    ResponseResult<UserRedPacketResponse> createMultiShareRedPacket(@PathVariable("orderid") Long orderid){
        return promotionService.createMultiShareRedPacket(orderid);
    }

    /**
     * 领取多人分享红包
     * @return
     */
    @GetMapping("receive-multishareredpacket/{orderid}")
    ResponseResult<LCUserRedPacketResponse> recevieMultiShareRedPacket(@PathVariable("orderid") Long orderid){
        return promotionService.recevieMultiShareRedPacket(orderid);
    }

    /**
     * 可用红包数量
     * @return
     */
    @GetMapping("usable-redpacket-count")
    ResponseResult<Integer> usableRedpacketCount(){
        return promotionService.usableRedpacketCount();
    }

    /**
     * 可用红包列表
     * @return
     */
    @GetMapping("usable-redpackets")
    ResponseResult<UserRedPacketResponse> usableRedpackets(){
        return promotionService.usableRedpackets();
    }

    /**
     * 多人分享红包详情
     * @return
     */
    @GetMapping("multi-redpacket-info")
    ResponseResult<ActivityRedPacketDto> multiRedpacketInfo(){
        return promotionService.multiRedpacketInfo();
    }

    /**
     * 个人中心:我的红包列表
     * @return
     */
    @PostMapping("my-redpacket")
    ResponseResult<Pagination<UserRedPacketDto>> myRedpacket(@RequestBody ActivityQueryParam queryParam) {
        return promotionService.myRedpacket(queryParam);
    }

    /**
     * 红包引导弹层
     * @return
     */
    @GetMapping("redpacket-toast")
    ResponseResult<UserRedPacketGuide> redPacketToast(){
        return promotionService.redPacketToast();
    }

    /**
     * 发送红包
     *
     * @return
     */
    @GetMapping("send-redpacket")
    ResponseResult<Boolean> sendRedPacket(){
        return promotionService.sendRedPacket();
    }

    /**
     * 批量发送红包
     *
     * @param sendRedPacketParam
     * @return
     */
    @PostMapping("batch-send-redpacket")
    ResponseResult<String> batchSendRedPacket(@RequestBody BatchSendRedPacketParam sendRedPacketParam){
        return promotionService.batchSendRedPacket(sendRedPacketParam);
    }

    /**
     * 满减
     * @return
     */
    @GetMapping("full-reduce")
    ResponseResult<FullReduce> fullReduce(){
        return promotionService.fullReduce();
    }


}
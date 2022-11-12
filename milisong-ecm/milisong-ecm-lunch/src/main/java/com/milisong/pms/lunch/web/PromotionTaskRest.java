package com.milisong.pms.lunch.web;

import com.milisong.pms.lunch.service.RestPromotionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author sailor wang
 * @date 2018/11/23 10:41 AM
 * @description
 */
@Slf4j
@RestController
@RequestMapping("/v1/promotion/task/")
public class PromotionTaskRest {

    @Resource
    RestPromotionService promotionService;


    /**
     * 活动任务，每分钟执行一次
     */
//    @Scheduled(cron = "0 0/1 * * * ?")
    @GetMapping("scan-redpacket-activity-status")
    public void scanRedPacketActivityStatus() {
        log.info("scanRedPacketActivityStatus定时器");
        promotionService.scanRedPacketActivityStatus();
    }

    /**
     * 用户活动状态，每分钟执行一次
     */
//    @Scheduled(cron = "0 0/1 * * * ?")
    @GetMapping("scan-user-redpacket")
    public void scanUserRedPacket() {
        log.info("scanUserRedPacket定时器");
        promotionService.scanUserRedPacket();
    }
}
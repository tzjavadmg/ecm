package com.milisong.dms.task;

import com.milisong.dms.api.ShunfengOrderTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhaozhonghui
 * @date 2018-10-26
 */
@Slf4j
@RequestMapping("/shunfeng")
@RestController
@RefreshScope
public class ShunfengProcessTask {
    @Autowired
    private ShunfengOrderTaskService shunfengOrderTaskService;
    // 早餐派单的延迟时间
    @Value("${bf.orderset.delay:10}")
    private Integer bfDelayMinute;

    @GetMapping("/task/check-delivery-log")
    public void checkDeliveryLog() {

        try {
            shunfengOrderTaskService.checkSfOrder();
        } catch (Exception e) {
            log.info("扫描请求失败的顺丰订单失败!", e);
        }
    }

    @GetMapping("/task/update-order-status")
    public void updateOrderStatus() {
        try {
            shunfengOrderTaskService.updateOrderStatus2Finish();
        } catch (Exception e) {
            log.info("处理前一天未完成的顺丰订单失败!", e);
        }
    }

    @GetMapping("/task/check-unconfirmed-order")
    public void checkUnConfirmedOrder() {
        try {
            shunfengOrderTaskService.checkUnconfirmedSfOrder();
        } catch (Exception e) {
            log.info("处理未接单的顺丰订单失败!", e);
        }
    }

    @GetMapping("/task/check-overtime-order")
    public void checkOverTimeSfOrder() {
        try {
            shunfengOrderTaskService.checkOverTimeSfOrder();
        } catch (Exception e) {
            log.info("处理配送超时的顺丰订单失败!", e);
        }
    }

//    /**
//     * 早餐延迟派单任务
//     * @return
//     */
//    @GetMapping("/task/bf-delay")
//    public ResponseResult<Object> bfOrderSetDelayTask() {
//    	this.shunfengOrderTaskService.runDelayTask(bfDelayMinute);
//    	return ResponseResult.buildSuccessResponse();
//    }
}

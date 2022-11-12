package com.milisong.ecm.lunch.web.task;

import com.farmland.core.api.ResponseResult;
import com.milisong.ecm.common.notify.api.NotifyService;
import com.milisong.ecm.common.notify.constant.BusinessLineEnum;
import com.milisong.ecm.common.notify.dto.NotifyTodayOrderTaskDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>
 *    author  : Tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/10/11   11:06
 *    desc    : 用户评估调查rest层
 *    version : v1.0
 * </pre>
 */
@Slf4j
@RestController
@RequestMapping("/v1/notify/task/")
public class NofityTaskRest {

    @Autowired
    private NotifyService notifyService;

    /**
     * 定时查询ivr
     * @param
     * @return
     */
    @GetMapping("check-ivr")
    ResponseResult<Boolean> checkIvr(){
        log.info("----检查ivr----");
        notifyService.dealRetryIvr();
        return ResponseResult.buildSuccessResponse();
    }

    /**
     * 定时发送9点提醒短信
     */
    @GetMapping(value = "notify-today-order")
    public void notifyTodayOrder() {
        log.info("-----发送9点提醒短信-----");
        NotifyTodayOrderTaskDto orderTaskDto = new NotifyTodayOrderTaskDto();
        orderTaskDto.setBusinessLine(BusinessLineEnum.LUNCH.getVal());
        ResponseResult<String> stringResponseResult = notifyService.notifyTodayOrderTask(orderTaskDto);
    }


}

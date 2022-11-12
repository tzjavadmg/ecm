package com.milisong.ecm.lunch.web.task;

import com.farmland.core.api.ResponseResult;
import com.milisong.ecm.common.user.api.UserEvaluationService;
import com.milisong.ecm.common.user.dto.UserEvaluationSendDto;
import com.milisong.ecm.common.notify.api.NotifyService;
import com.milisong.ecm.common.notify.constant.BusinessLineEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>
 *    author  : Administrator
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/10/31   14:17
 *    desc    : 问卷调查
 *    version : v1.0
 * </pre>
 */
@Slf4j
@RestController
@RequestMapping("/v1/evaluation/task/")
public class EvaluationTaskRest {

    @Autowired
    private UserEvaluationService userEvaluationService;

    @GetMapping("send-evaluation-sms")
    ResponseResult<Boolean> sendEvaluationSms(){
        UserEvaluationSendDto sendDto = new UserEvaluationSendDto();
        sendDto.setBusinessLine(BusinessLineEnum.LUNCH.getVal());
        ResponseResult<Boolean> booleanResponseResult = userEvaluationService.sendFeedbackMsg(sendDto);
        return booleanResponseResult;
    }

}

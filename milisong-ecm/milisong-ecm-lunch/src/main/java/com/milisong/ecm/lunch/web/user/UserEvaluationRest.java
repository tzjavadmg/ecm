package com.milisong.ecm.lunch.web.user;

import com.farmland.core.api.ResponseResult;
import com.milisong.ecm.common.user.api.UserEvaluationService;
import com.milisong.ecm.common.user.dto.UserEvaluationDisplayDto;
import com.milisong.ecm.common.user.dto.UserEvaluationInfoDto;
import com.milisong.ecm.common.user.dto.UserEvaluationSendDto;
import com.milisong.ecm.common.user.dto.UserEvaluationSubmitDto;
import com.milisong.ecm.common.notify.api.NotifyService;
import com.milisong.ecm.common.notify.constant.BusinessLineEnum;
import com.milisong.ecm.common.notify.dto.FeedbackInvestigateSmsDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


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
@RequestMapping("/v1/evaluation/")
public class UserEvaluationRest {

    @Autowired
    private UserEvaluationService userEvaluationService;

    /**
     * 展示反馈页信息
     * @param
     * @return
     */
    @GetMapping("show-evaluation-info/{orderId}")
    ResponseResult<UserEvaluationDisplayDto> showEvaluationInfo(@PathVariable("orderId") Long orderId){
        UserEvaluationInfoDto userEvaluationInfoDto = new UserEvaluationInfoDto();
        userEvaluationInfoDto.setDeliveryId(orderId);
        userEvaluationInfoDto.setBusinessLine(BusinessLineEnum.LUNCH.getVal());
        ResponseResult<UserEvaluationDisplayDto> investigateInfo = userEvaluationService.getInvestigateInfo(userEvaluationInfoDto);
        return investigateInfo;
    }

    /**
     * 保存用户反馈信息 提交字段orderId,orderNo,userId,mobileNo,
     * @param
     * @return
     */
    @PostMapping("save")
    ResponseResult<Boolean> save(@RequestBody UserEvaluationSubmitDto submitDto){
        submitDto.setBusinessLine(BusinessLineEnum.LUNCH.getVal());
        return userEvaluationService.saveInvestigateInfo(submitDto);
    }

    @GetMapping("test/{deliveryId}")
    ResponseResult<Boolean> test(@PathVariable("deliveryId") Long deliveryId){
        try {
            UserEvaluationSendDto sendDto = new UserEvaluationSendDto();
            sendDto.setBusinessLine(BusinessLineEnum.LUNCH.getVal());
            if (deliveryId.equals(0L)) {
                ResponseResult<Boolean> booleanResponseResult = userEvaluationService.sendFeedbackMsg(sendDto);
                return booleanResponseResult;
            }
            sendDto.setDeliveryId(deliveryId);
            return userEvaluationService.testSendFeedbackMsg(sendDto);
        }catch (Exception e){
            log.error("---访问异常---",e);
            return ResponseResult.buildSuccessResponse();
        }
    }

}

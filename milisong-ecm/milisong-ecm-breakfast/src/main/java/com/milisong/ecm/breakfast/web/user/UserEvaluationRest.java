package com.milisong.ecm.breakfast.web.user;

import com.farmland.core.api.ResponseResult;
import com.milisong.ecm.common.enums.RestEnum;
import com.milisong.ecm.common.notify.constant.BusinessLineEnum;
import com.milisong.ecm.common.user.api.UserEvaluationService;
import com.milisong.ecm.common.user.dto.UserEvaluationDisplayDto;
import com.milisong.ecm.common.user.dto.UserEvaluationInfoDto;
import com.milisong.ecm.common.user.dto.UserEvaluationSendDto;
import com.milisong.ecm.common.user.dto.UserEvaluationSubmitDto;
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
        UserEvaluationInfoDto infoDto = new UserEvaluationInfoDto();
        infoDto.setBusinessLine(BusinessLineEnum.BREAKFAST.getVal());
        infoDto.setDeliveryId(orderId);
        ResponseResult<UserEvaluationDisplayDto> result = userEvaluationService.getInvestigateInfo(infoDto);
        if(!result.isSuccess()){
            return ResponseResult.buildFailResponse(RestEnum.SYS_ERROR.getCode(),RestEnum.SYS_ERROR.getDesc());
        }
        return result;
    }

    /**
     * 保存用户反馈信息 提交字段orderId,orderNo,userId,mobileNo,
     * @param
     * @return
     */
    @PostMapping("save")
    ResponseResult<Boolean> save(@RequestBody UserEvaluationSubmitDto submitDto){
        submitDto.setBusinessLine(BusinessLineEnum.BREAKFAST.getVal());
        ResponseResult<Boolean> result = userEvaluationService.saveInvestigateInfo(submitDto);
        if(!result.isSuccess()){
            return ResponseResult.buildFailResponse(RestEnum.SYS_ERROR.getCode(),RestEnum.SYS_ERROR.getDesc());
        }
        return result;
    }

    @GetMapping("test/{deliveryId}")
    ResponseResult<Boolean> test(@PathVariable("deliveryId") Long deliveryId){
        try {
            UserEvaluationSendDto sendDto = new UserEvaluationSendDto();
            sendDto.setBusinessLine(BusinessLineEnum.BREAKFAST.getVal());
            if (deliveryId.equals(0L)) {
                return userEvaluationService.sendFeedbackMsg(sendDto);
            }
            sendDto.setDeliveryId(deliveryId);
            return userEvaluationService.testSendFeedbackMsg(sendDto);
        }catch (Exception e){
            log.error("---访问异常---",e);
            return ResponseResult.buildSuccessResponse();
        }
    }

}

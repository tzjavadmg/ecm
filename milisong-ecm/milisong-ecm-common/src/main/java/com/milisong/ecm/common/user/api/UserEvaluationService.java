package com.milisong.ecm.common.user.api;

import com.farmland.core.api.ResponseResult;
import com.milisong.ecm.common.user.dto.UserEvaluationDisplayDto;
import com.milisong.ecm.common.user.dto.UserEvaluationInfoDto;
import com.milisong.ecm.common.user.dto.UserEvaluationSendDto;
import com.milisong.ecm.common.user.dto.UserEvaluationSubmitDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author田海波
 * @date 2018/10/10 下午4:29
 * @description
 */
public interface UserEvaluationService {

    /**
     * 问卷调查定时任务执行业务,businessLine
     * @return
     */
	@PostMapping("/v1/UserEvaluationService/sendFeedbackMsg")
    ResponseResult<Boolean> sendFeedbackMsg(@RequestBody UserEvaluationSendDto dto);

    /**
     * 测试问卷调查定时任务执行业务, deliveryId,businessLine
     * @return
     */
	@PostMapping("/v1/UserEvaluationService/testSendFeedbackMsg")
    ResponseResult<Boolean> testSendFeedbackMsg(@RequestBody UserEvaluationSendDto dto);

    /**
     * 查询问卷显示内容 businessLine,deliveryId
     * @param dto
     * @return
     */
	@PostMapping("/v1/UserEvaluationService/getInvestigateInfo")
    ResponseResult<UserEvaluationDisplayDto> getInvestigateInfo(@RequestBody UserEvaluationInfoDto dto);

	@PostMapping("/v1/UserEvaluationService/saveInvestigateInfo")
    ResponseResult<Boolean> saveInvestigateInfo(@RequestBody UserEvaluationSubmitDto dto);
}
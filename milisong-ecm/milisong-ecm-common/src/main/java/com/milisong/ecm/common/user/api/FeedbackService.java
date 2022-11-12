package com.milisong.ecm.common.user.api;

import com.farmland.core.api.ResponseResult;
import com.milisong.ecm.common.user.dto.FeedbackTypeDto;
import com.milisong.ecm.common.user.dto.UserFeedbackDto;
import com.milisong.ecm.common.user.dto.UserFeedbackTypeDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author sailor wang
 * @date 2018/9/5 下午4:29
 * @description
 */
public interface FeedbackService {
    /**
     * 获取反馈类型
     * @return
     */
	@PostMapping(value = "/v1/FeedbackService/feedbackType")
    ResponseResult<List<UserFeedbackTypeDto>> feedbackType(@RequestBody FeedbackTypeDto feedbackTypeDto);

    /**
     * 用户反馈
     * @param userFeedback
     * @return
     */
	@PostMapping(value = "/v1/FeedbackService/saveFeedback")
    ResponseResult<Boolean> saveFeedback(@RequestBody UserFeedbackDto userFeedback);
}
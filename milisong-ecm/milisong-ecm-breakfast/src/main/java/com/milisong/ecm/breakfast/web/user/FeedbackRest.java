package com.milisong.ecm.breakfast.web.user;

import com.farmland.core.api.ResponseResult;
import com.milisong.ecm.breakfast.service.RestFeedbackService;
import com.milisong.ecm.common.user.dto.UserFeedbackDto;
import com.milisong.ecm.common.user.dto.UserFeedbackTypeDto;
import com.milisong.ecm.common.notify.constant.BusinessLineEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户反馈
 * @author sailor wang
 * @date 2018/9/5 下午3:58
 * @description
 */
@Slf4j
@RestController
@RequestMapping("/v1/feedback/")
public class FeedbackRest {

    @Autowired
    private RestFeedbackService feedbackService;

    /**
     * 获取反馈类型
     * @return
     */
    @GetMapping("types")
    ResponseResult<List<UserFeedbackTypeDto>> feedbackType(@RequestParam(value = "transit",required = false)Boolean transit){
        return feedbackService.feedbackType(transit);
    }

    /**
     * 用户反馈
     * @param userFeedback
     * @return
     */
    @PostMapping("save-feedback")
    ResponseResult<Boolean> saveFeedback(@RequestBody UserFeedbackDto userFeedback){
        return feedbackService.saveFeedback(userFeedback);
    }
}
package com.milisong.ecm.lunch.service;

import com.farmland.core.api.ResponseResult;
import com.milisong.ecm.common.user.api.FeedbackService;
import com.milisong.ecm.common.user.dto.FeedbackTypeDto;
import com.milisong.ecm.common.user.dto.UserFeedbackDto;
import com.milisong.ecm.common.user.dto.UserFeedbackTypeDto;
import com.milisong.oms.constant.OrderType;
import com.milisong.ucs.sdk.security.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author sailor wang
 * @date 2018/9/5 下午4:00
 * @description
 */
@Slf4j
@Service
public class RestFeedbackService {

    @Autowired
    private FeedbackService feedbackService;
    /**
     * 获取反馈分类
     * @return
     */
    public ResponseResult<List<UserFeedbackTypeDto>> feedbackType() {
        FeedbackTypeDto feedbackTypeDto = new FeedbackTypeDto();
        feedbackTypeDto.setBusinessLine(OrderType.LUNCH.getValue());
        return feedbackService.feedbackType(feedbackTypeDto);
    }

    /**
     * 用户发反馈
     * @param userFeedback
     * @return
     */
    public ResponseResult<Boolean> saveFeedback(UserFeedbackDto userFeedback) {
        if (userFeedback != null){
            userFeedback.setBusinessLine(OrderType.LUNCH.getValue());
            userFeedback.setUserId(UserContext.getCurrentUser().getId());
            userFeedback.setMobileNo(UserContext.getCurrentUser().getMobileNo());
            userFeedback.setNickName(UserContext.getCurrentUser().getNickName());
        }
        return feedbackService.saveFeedback(userFeedback);
    }
}
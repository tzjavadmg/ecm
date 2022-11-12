package com.milisong.pms.prom.service;

import com.farmland.core.api.ResponseResult;
import com.farmland.core.db.IdGenerator;
import com.farmland.core.util.BeanMapper;
import com.google.common.collect.Lists;
import com.milisong.pms.prom.api.UserInviteRecordService;
import com.milisong.pms.prom.domain.UserInviteRecord;
import com.milisong.pms.prom.dto.invite.UserInviteRecordDto;
import com.milisong.pms.prom.mapper.UserInviteRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

import static com.milisong.pms.prom.enums.PromotionResponseCode.REQUEST_PARAM_EMPTY;
import static com.milisong.pms.prom.enums.UserInviteEnum.INVITE_RECORD_STATUS.*;

/**
 * @author sailor wang
 * @date 2019/3/25 5:11 PM
 * @description
 */
@Slf4j
@Service
@RestController
public class UserInviteRecordServiceImpl implements UserInviteRecordService {

    @Autowired
    private UserInviteRecordMapper userInviteRecordMapper;

    /**
     * 查询老拉新获取邀请数据 通过邀请者id
     *
     * @param originateUserId
     * @return
     */
    @Override
    public ResponseResult<List<UserInviteRecordDto>> queryInviteRecords(@RequestParam("originateUserId") Long originateUserId) {
        List<UserInviteRecordDto> userInviteRecord = userInviteRecordMapper.queryByOriginateUserId(originateUserId);
        return ResponseResult.buildSuccessResponse(userInviteRecord);
    }

    /**
     * 查询老拉新获取邀请数据，根据活动id来查询
     * @param activityInviteId
     * @return
     */
    @Override
    public ResponseResult<List<UserInviteRecordDto>> queryInviteRecordsByActivityId(@RequestParam("activityInviteId") Long activityInviteId) {
        List<UserInviteRecordDto> result = Lists.newArrayList();
        List<UserInviteRecord> userInviteRecord = userInviteRecordMapper.queryByActivityId(activityInviteId);
        if(userInviteRecord != null && userInviteRecord.size() > 0){
            result = BeanMapper.mapList(userInviteRecord, UserInviteRecordDto.class);
            //TODO 如有必要，按需排序
        }
        return ResponseResult.buildSuccessResponse(result);
    }

    /**
     * 新用户助力老拉新活动
     *
     * @param userInviteRecord
     * @return
     */
    @Override
    public ResponseResult<Boolean> createInviteRecords(@RequestBody UserInviteRecordDto userInviteRecord) {
        if (userInviteRecord.getActivityInviteId() == null || userInviteRecord.getOriginateUserId() == null
                || userInviteRecord.getReceiveUserId() == null || userInviteRecord.getBusinessLine() == null
                || userInviteRecord.getNickName() == null || userInviteRecord.getHeadPortraitUrl() == null
                || userInviteRecord.getGoodsCode() == null){
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        UserInviteRecord inviteRecord = new UserInviteRecord();
        BeanMapper.copy(userInviteRecord,inviteRecord);
        inviteRecord.setId(IdGenerator.nextId());
        inviteRecord.setStatus(NOT_PLACE_ORDER.getValue());
        inviteRecord.setRemark(NOT_PLACE_ORDER.getDesc());
        inviteRecord.setCreateTime(new Date());
        inviteRecord.setUpdateTime(new Date());
        int row = userInviteRecordMapper.insertSelective(inviteRecord);
        return ResponseResult.buildSuccessResponse(row > 0);
    }
}
package com.milisong.pms.prom.service;

import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.farmland.core.db.IdGenerator;
import com.farmland.core.util.BeanMapper;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.milisong.pms.prom.api.LunchRedPacketService;
import com.milisong.pms.prom.domain.RedPacket;
import com.milisong.pms.prom.domain.SendRedpacketRecord;
import com.milisong.pms.prom.domain.SendRedpacketWater;
import com.milisong.pms.prom.domain.UserRedPacket;
import com.milisong.pms.prom.dto.Pagenation;
import com.milisong.pms.prom.dto.RedPacketDto;
import com.milisong.pms.prom.dto.RedPacketQueryDto;
import com.milisong.pms.prom.dto.SendLunchRedPacketRequest;
import com.milisong.pms.prom.enums.RedPacketEnum;
import com.milisong.pms.prom.mapper.RedPacketMapper;
import com.milisong.pms.prom.mapper.SendRedpacketRecordMapper;
import com.milisong.pms.prom.mapper.SendRedpacketWaterMapper;
import com.milisong.pms.prom.mapper.UserRedPacketMapper;
import com.milisong.ucs.api.UserService;
import com.milisong.ucs.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.milisong.pms.prom.enums.PromotionResponseCode.*;
import static com.milisong.ucs.enums.BusinessLineEnum.LUNCH;

/**
 * @author sailor wang
 * @date 2019/2/18 3:38 PM
 * @description
 */
@Slf4j
@Service
@RestController
public class LunchRedPacketServiceImpl implements LunchRedPacketService {
    @Autowired
    RedPacketMapper redPacketMapper;

    @Autowired
    UserService userService;

    @Autowired
    CommonService commonService;

    @Autowired
    UserRedPacketMapper userRedpacketMapper;

    @Autowired
    SendRedpacketRecordMapper sendRedpacketRecordMapper;

    @Autowired
    SendRedpacketWaterMapper sendRedpacketWaterMapper;

    Integer fetchSize = 500;

    /**
     * 新增or修改红包
     *
     * @param redPacketDto
     * @return
     */
    @Override
    public ResponseResult<Long> saveOrUpdate(@RequestBody RedPacketDto redPacketDto) {
        Long id = redPacketDto.getId();
        if (redPacketDto.getId() != null) {
            RedPacket redPacket = BeanMapper.map(redPacketDto, RedPacket.class);
            redPacket.setUpdateTime(new Date());
            redPacketMapper.updateByPrimaryKeySelective(redPacket);
        } else {
            id = IdGenerator.nextId();
            RedPacket redPacket = BeanMapper.map(redPacketDto, RedPacket.class);
            redPacket.setLimitDays(redPacket.getLimitDays()==null?0:redPacket.getLimitDays());
            redPacket.setId(id);
            Date now = new Date();
            redPacket.setCreateTime(now);
            redPacket.setUpdateTime(now);
            redPacketMapper.insertSelective(redPacket);
        }
        return ResponseResult.buildSuccessResponse(id);
    }

    /**
     * 红包列表
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult<Pagenation<RedPacketDto>> queryRedpacket(@RequestBody RedPacketQueryDto dto) {
        if (dto.getBusinessLine() == null) {
            return ResponseResult.buildFailResponse(BUSINESS_LINE_IS_EMPTY.code(), BUSINESS_LINE_IS_EMPTY.message());
        }
        if (dto.getPageNo() < 0) {
            dto.setPageNo(1);
        }
        if (dto.getPageSize() < 0) {
            dto.setPageSize(10);
        }
        if (dto.getPageSize() > 500) {
            dto.setPageSize(500);
        }
        Pagenation<RedPacketDto> pagenation = new Pagenation<>();
        pagenation.setRowCount(0);
        pagenation.setList(Collections.EMPTY_LIST);
        int rowCount = redPacketMapper.countRedpacket(dto);
        if (rowCount > 0) {
            List<RedPacketDto> list = redPacketMapper.queryRedpacket(dto);
            pagenation.setRowCount(rowCount);
            pagenation.setList(list);
        }
        return ResponseResult.buildSuccessResponse(pagenation);
    }

    @Override
    public ResponseResult<RedPacketDto> queryById(@RequestParam("id") Long id) {
        RedPacket redPacket = redPacketMapper.selectByPrimaryKey(id);
        if (redPacket != null) {
            return ResponseResult.buildSuccessResponse(BeanMapper.map(redPacket, RedPacketDto.class));
        }
        return ResponseResult.buildSuccessResponse();
    }

    @Override
    public ResponseResult<Boolean> offlineRedpacket(@RequestParam("id") Long id) {
        RedPacket redPacket = new RedPacket();
        redPacket.setId(id);
        redPacket.setStatus(RedPacketEnum.STATUS.OFFLINE.getCode());
        redPacket.setUpdateTime(new Date());
        int row = redPacketMapper.updateByPrimaryKeySelective(redPacket);
        return ResponseResult.buildSuccessResponse(row > 0);
    }

    /**
     * 批量发放红包
     *
     * @param sendLunchRedPacketRequest
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @Async("asyncServiceExecutor")
    public ResponseResult<Boolean> sendRedPacket(@RequestBody SendLunchRedPacketRequest sendLunchRedPacketRequest) {
        log.info("批量发送红包 sendLunchRedPacket -> {}", sendLunchRedPacketRequest);
        if (CollectionUtils.isEmpty(sendLunchRedPacketRequest.getRedpacketids()) || sendLunchRedPacketRequest.getOperatorId() == null
                || StringUtils.isBlank(sendLunchRedPacketRequest.getOperatorName()) || sendLunchRedPacketRequest.getBusinessLine() == null) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        Date now = new Date();
        if (sendLunchRedPacketRequest.getSendTime() == null) {
            sendLunchRedPacketRequest.setSendTime(now);
        }
        // 根据条件过滤查询用户数据
        sendLunchRedPacketRequest.setPageNo(1);
        sendLunchRedPacketRequest.setPageSize(fetchSize);

        // 根据红包ids查询红包数据
        List<Long> redpacketids = sendLunchRedPacketRequest.getRedpacketids();
        List<RedPacketDto> redpacketList = redPacketMapper.queryByIds(redpacketids);
        if (CollectionUtils.isNotEmpty(redpacketList)) {
            // 校验知否存在过期红包
            ResponseResult<Boolean> result = isExistRedpacket(sendLunchRedPacketRequest, redpacketList);
            if (!result.isSuccess()) {
                return result;
            }

            Long sendRedpacketRecordId = sendLunchRedPacketRequest.getId() == null ? IdGenerator.nextId() : sendLunchRedPacketRequest.getId();
            // 判断时间是否立即发送
            if (sendLunchRedPacketRequest.getSendTime().compareTo(now) > 0) {
                log.info("稍后定时发送。。。。。");
                saveSendRedpacketRecord(redpacketList, sendRedpacketRecordId, sendLunchRedPacketRequest, 0, 0, RedPacketEnum.SendStatus.WAIT_FOR_SEND.getCode());
                return ResponseResult.buildSuccessResponse();
            }
            String key = "send_lunch_redpacket:" + sendRedpacketRecordId;
            Boolean lockSuccess = RedisCache.setIfAbsent(key, "1");
            // 防重发
            if (lockSuccess){
                try {
                    Boolean isSendAll = sendLunchRedPacketRequest.getSendAllUser();
                    Date sendTime = sendLunchRedPacketRequest.getSendTime();
                    String smsMsg = sendLunchRedPacketRequest.getSmsMsg();
                    List<Long> excludeUserIds = sendLunchRedPacketRequest.getExcludeUserIds();

                    if (Boolean.TRUE.equals(isSendAll)) {// 发送所有用户
                        com.milisong.ucs.dto.Pagenation<UserDto> pagenation = commonService.fetchUsers(null, fetchSize);
                        if (CollectionUtils.isNotEmpty(pagenation.getList())) {
                            int pageCount = pagenation.getPageCount();
                            List<UserDto> userList = pagenation.getList();

                            // 发放红包并记录发放流水
                            int sendRow = sendAndRecordRedpacketWater(sendRedpacketRecordId, redpacketList, userList, excludeUserIds, sendTime, smsMsg);
                            if (pageCount > 1) {
                                for (Integer pageNum = 2; pageNum <= pageCount; pageNum++) {

                                    pagenation = commonService.fetchUsers(userList.get(userList.size() - 1).getId(), fetchSize);
                                    userList = pagenation.getList();
                                    //发送红包
                                    if (CollectionUtils.isNotEmpty(userList)) {
                                        // 发放红包并记录发放流水
                                        int row = sendAndRecordRedpacketWater(sendRedpacketRecordId, redpacketList, userList,excludeUserIds, sendTime, smsMsg);
                                        sendRow += row;
                                    }
                                }
                            }
                            saveSendRedpacketRecord(redpacketList, sendRedpacketRecordId, sendLunchRedPacketRequest, sendRow, sendRow, RedPacketEnum.SendStatus.SEND_SUCCESS.getCode());
                            return ResponseResult.buildSuccessResponse();
                        }
                    } else if (CollectionUtils.isNotEmpty(sendLunchRedPacketRequest.getMobiles())) {
                        UserDto userDto = new UserDto();
                        userDto.setBusinessLine(LUNCH.getCode());
                        userDto.setMobileNos(sendLunchRedPacketRequest.getMobiles());
                        ResponseResult<List<UserDto>> responseResult = userService.fetchUserInfoByMobiles(userDto);
                        if (responseResult.isSuccess() && CollectionUtils.isNotEmpty(responseResult.getData())) {
                            int row = sendAndRecordRedpacketWater(sendRedpacketRecordId, redpacketList, responseResult.getData(),excludeUserIds, sendTime, smsMsg);
                            saveSendRedpacketRecord(redpacketList, sendRedpacketRecordId, sendLunchRedPacketRequest, row, row, RedPacketEnum.SendStatus.SEND_SUCCESS.getCode());
                            return ResponseResult.buildSuccessResponse();
                        }
                    }
                } catch (Exception ex) {
                    log.error("", ex);
                    throw ex;
                } finally {
                    RedisCache.expire(key,5,TimeUnit.MINUTES);
                }
            }else {
                log.info("发送红包任务正在执行。。。");
            }
            return ResponseResult.buildFailResponse(NOT_FOUND_USERS.code(), NOT_FOUND_USERS.message());
        }
        // 查不到红包数据
        return notFindRedpackets(sendLunchRedPacketRequest, redpacketids);
    }

    /**
     * 红包数据不存在
     *
     * @param sendLunchRedPacketRequest
     * @param redpacketids
     * @return
     */
    private ResponseResult<Boolean> notFindRedpackets(SendLunchRedPacketRequest sendLunchRedPacketRequest, List<Long> redpacketids) {
        SendRedpacketRecord sendRedpacketRecord = new SendRedpacketRecord();
        sendRedpacketRecord.setContent("");
        if (sendRedpacketRecord.getSendTime() == null) {
            sendRedpacketRecord.setSendTime(new Date());
        }
        sendRedpacketRecord.setOperatorId(sendLunchRedPacketRequest.getOperatorId());
        sendRedpacketRecord.setOperatorName(sendLunchRedPacketRequest.getOperatorName());
        sendRedpacketRecord.setContent("");
        sendRedpacketRecord.setRedpacketids(Joiner.on(",").join(redpacketids));
        sendRedpacketRecord.setFilterCondition(JSONObject.toJSONString(sendLunchRedPacketRequest));
        sendRedpacketRecord.setShouldSendNum(0);
        sendRedpacketRecord.setActualSendNum(0);
        sendRedpacketRecord.setRemark("查不到红包数据");
        sendRedpacketRecord.setStatus(RedPacketEnum.SendStatus.SEND_FAILED.getCode());
        sendRedpacketRecord.setUpdateTime(new Date());

        if (sendLunchRedPacketRequest.getId() != null) {
            sendRedpacketRecord.setId(sendLunchRedPacketRequest.getId());
            sendRedpacketRecordMapper.updateByPrimaryKeySelective(sendRedpacketRecord);
        } else {
            sendRedpacketRecord.setId(IdGenerator.nextId());
            sendRedpacketRecord.setBusinessLine(LUNCH.getCode());
            sendRedpacketRecord.setCreateTime(new Date());
            sendRedpacketRecordMapper.insertSelective(sendRedpacketRecord);
        }
        return ResponseResult.buildFailResponse(RED_PACKET_NOT_EXIST.code(), RED_PACKET_NOT_EXIST.message());
    }

    /**
     * 是否存在过期红包
     *
     * @param redpacketList
     * @return
     */
    private ResponseResult<Boolean> isExistRedpacket(SendLunchRedPacketRequest sendLunchRedPacket, List<RedPacketDto> redpacketList) {
        List<String> remarkList = Lists.newArrayList();
        List<String> nameList = Lists.newArrayList();
        for (RedPacketDto redpacket : redpacketList) {
            String name = redpacket.getName() + "_" + redpacket.getRemark();
            if (RedPacketEnum.STATUS.OFFLINE.getCode().equals(redpacket.getStatus())) {
                remarkList.add(name + " (已过期)");
            }
            nameList.add(name);
        }
        if (CollectionUtils.isNotEmpty(remarkList)) {
            SendRedpacketRecord sendRedpacketRecord = new SendRedpacketRecord();
            sendRedpacketRecord.setOperatorId(sendLunchRedPacket.getOperatorId());
            sendRedpacketRecord.setOperatorName(sendLunchRedPacket.getOperatorName());
            sendRedpacketRecord.setContent(Joiner.on(",").join(nameList));
            sendRedpacketRecord.setSendTime(sendLunchRedPacket.getSendTime());
            sendRedpacketRecord.setRedpacketids(Joiner.on(",").join(redpacketList.stream().map(RedPacketDto::getId).collect(Collectors.toList())));
            sendRedpacketRecord.setFilterCondition(JSONObject.toJSONString(sendLunchRedPacket));
            sendRedpacketRecord.setShouldSendNum(0);
            sendRedpacketRecord.setActualSendNum(0);
            sendRedpacketRecord.setRemark(Joiner.on(",").join(remarkList));
            sendRedpacketRecord.setStatus(RedPacketEnum.SendStatus.SEND_FAILED.getCode());
            Date now = new Date();
            sendRedpacketRecord.setCreateTime(now);
            sendRedpacketRecord.setUpdateTime(now);

            if (sendLunchRedPacket.getId() != null) {
                sendRedpacketRecord.setId(sendLunchRedPacket.getId());
                sendRedpacketRecordMapper.updateByPrimaryKeySelective(sendRedpacketRecord);
            } else {
                sendRedpacketRecord.setId(IdGenerator.nextId());
                sendRedpacketRecord.setBusinessLine(LUNCH.getCode());
                sendRedpacketRecord.setCreateTime(new Date());
                sendRedpacketRecordMapper.insertSelective(sendRedpacketRecord);
            }
            return ResponseResult.buildFailResponse(RED_PACKET_OFFLINE.code(), RED_PACKET_OFFLINE.message());
        }
        return ResponseResult.buildSuccessResponse();
    }

    private void saveSendRedpacketRecord(List<RedPacketDto> redpacketList, Long sendRedpacketRecordId, SendLunchRedPacketRequest sendLunchRedPacketRequest,
                                         Integer shouldSendNum, Integer actualSendNum, Byte status) {
        String content = Joiner.on(",").join(Lists.transform(redpacketList, new Function<RedPacketDto, String>() {
            @Nullable
            @Override
            public String apply(@Nullable RedPacketDto input) {
                return input.getName() + "_" + input.getRemark();
            }
        }));
        SendRedpacketRecord sendRedpacketRecord = new SendRedpacketRecord();
        sendRedpacketRecord.setId(sendRedpacketRecordId);
        sendRedpacketRecord.setOperatorId(sendLunchRedPacketRequest.getOperatorId());
        sendRedpacketRecord.setOperatorName(sendLunchRedPacketRequest.getOperatorName());
        sendRedpacketRecord.setContent(content);
        sendRedpacketRecord.setFilterCondition(JSONObject.toJSONString(sendLunchRedPacketRequest));
        sendRedpacketRecord.setStatus(status);
        sendRedpacketRecord.setSendTime(sendLunchRedPacketRequest.getSendTime());
        sendRedpacketRecord.setRedpacketids(Joiner.on(",").join(redpacketList.stream().map(RedPacketDto::getId).collect(Collectors.toList())));
        sendRedpacketRecord.setShouldSendNum(shouldSendNum);
        sendRedpacketRecord.setActualSendNum(actualSendNum);
        sendRedpacketRecord.setRemark("");
        sendRedpacketRecord.setUpdateTime(new Date());

        if (sendLunchRedPacketRequest.getId() != null) {
            sendRedpacketRecord.setId(sendLunchRedPacketRequest.getId());
            sendRedpacketRecordMapper.updateByPrimaryKeySelective(sendRedpacketRecord);
        } else {
            sendRedpacketRecord.setStatus(status);
            sendRedpacketRecord.setBusinessLine(LUNCH.getCode());
            sendRedpacketRecord.setCreateTime(new Date());
            sendRedpacketRecordMapper.insertSelective(sendRedpacketRecord);
        }
    }

    private Integer sendAndRecordRedpacketWater(Long sendRedpacketRecordId, List<RedPacketDto> redPackets, List<UserDto> userList,List<Long> excludeUserIds, Date sendTime, String smsMsg) {
        if (CollectionUtils.isEmpty(userList)) {
            return 0;
        }
        List<UserRedPacket> userRedPacketList = Lists.newArrayList();
        Map<String, Object> sms = Maps.newHashMap();

        List<SendRedpacketWater> sendRedPacketWaters = Lists.newArrayList();

        for (UserDto userDto : userList) {
            if (CollectionUtils.isNotEmpty(excludeUserIds) && excludeUserIds.contains(userDto.getId())){
                continue;
            }
            sms.put(userDto.getMobileNo(), smsMsg);
            if (CollectionUtils.isNotEmpty(redPackets)) {
                DateTime now = new DateTime();
                for (RedPacketDto redPacketDto : redPackets) {
                    DateTime validDate = now.plusDays(redPacketDto.getValidDays());
                    Date expireTime = new DateTime(validDate.getYear(), validDate.getMonthOfYear(), validDate.getDayOfMonth(), 23, 59, 59).toDate();

                    UserRedPacket userRedPacket = new UserRedPacket();
                    userRedPacket.setId(IdGenerator.nextId());
                    userRedPacket.setName(redPacketDto.getName());
                    userRedPacket.setType(redPacketDto.getType());
                    userRedPacket.setUserId(userDto.getId());
                    userRedPacket.setAmount(redPacketDto.getAmount());
                    userRedPacket.setExpireTime(expireTime);
                    userRedPacket.setIsShare((byte) 0);//默认不同享
                    userRedPacket.setIsValid((byte) 1);
                    userRedPacket.setIsUsed((byte) 0);
                    userRedPacket.setBusinessLine(LUNCH.getCode());
                    userRedPacket.setCreateTime(new Date());
                    userRedPacket.setUpdateTime(new Date());

                    userRedPacketList.add(userRedPacket);
                }
                sendRedPacketWaters.add(initSendRedpacketWater(sendRedpacketRecordId, userDto, sendTime));
            }
        }
        int row = userRedpacketMapper.insertBatch(userRedPacketList);
        if (row > 0) {
            row = sendRedpacketWaterMapper.insertBatch(sendRedPacketWaters);
            if (row > 0) {
                if (StringUtils.isNotBlank(smsMsg)) {
                    //发送短信
                    commonService.sendLunchSms(sms);
                }
            }
        }
        return row;
    }

    private SendRedpacketWater initSendRedpacketWater(Long sendRedpacketRecordId, UserDto userDto, Date sendTime) {
        SendRedpacketWater sendRedpacketWater = new SendRedpacketWater();
        sendRedpacketWater.setId(IdGenerator.nextId());
        sendRedpacketWater.setSendRedpacketRecordId(sendRedpacketRecordId);
        sendRedpacketWater.setUserId(userDto.getId());
        sendRedpacketWater.setUserName(userDto.getRealName());
        sendRedpacketWater.setMobileNo(userDto.getMobileNo());
        sendRedpacketWater.setSex(userDto.getSex() == null ? null : userDto.getSex().byteValue());
        sendRedpacketWater.setSendTime(sendTime);
        sendRedpacketWater.setBusinessLine(LUNCH.getCode());
        sendRedpacketWater.setCreateTime(new Date());
        sendRedpacketWater.setUpdateTime(new Date());
        return sendRedpacketWater;
    }

    @Async("asyncServiceExecutor")
    public void asyncTest() throws InterruptedException {
        Thread.sleep(1000L);
    }
}
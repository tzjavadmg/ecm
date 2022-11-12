package com.milisong.pms.prom.service;
import java.math.BigDecimal;

import com.farmland.core.api.ResponseResult;
import com.farmland.core.util.BeanMapper;
import com.milisong.pms.prom.domain.RedPacket;
import com.milisong.pms.prom.dto.*;
import com.milisong.pms.prom.enums.RedPacketEnum;
import com.milisong.pms.prom.enums.RedPacketType;
import com.milisong.ucs.enums.BusinessLineEnum;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * @author sailor wang
 * @date 2019/1/14 6:01 PM
 * @description
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
public class SendRedPacketTest {

    @Autowired
    LunchRedPacketServiceImpl lunchRedPacketService;


    @Autowired
    SendLunchRedPacketRecordServiceImpl sendLunchRedPacketRecordService;


    @Test
    public void saveOrUpdate(){
        RedPacketDto redPacketDto = new RedPacketDto();
        redPacketDto.setId(2352197960990720L);
        redPacketDto.setName("专享红包");
        redPacketDto.setType(RedPacketType.ACTIVIE_REDPACKET.getCode());
        redPacketDto.setLimitDays(0);
        redPacketDto.setAmount(new BigDecimal("4"));
        redPacketDto.setIsShare((byte)0);
        redPacketDto.setValidDays(7);
        redPacketDto.setBusinessLine(BusinessLineEnum.LUNCH.getCode());
        redPacketDto.setStatus((byte)1);
        redPacketDto.setRemark("五元红包备注");
        ResponseResult<Long> result = lunchRedPacketService.saveOrUpdate(redPacketDto);
        Assert.assertNotNull(result.getData());
    }

    @Test
    public void queryRedpacket(){
        RedPacketQueryDto dto = new RedPacketQueryDto();
        dto.setBusinessLine(BusinessLineEnum.LUNCH.getCode());
        ResponseResult<Pagenation<RedPacketDto>> result = lunchRedPacketService.queryRedpacket(dto);
        Assert.assertNotNull(result.getData().getList());
    }

    @Test
    public void queryById() {
        Long id = 2352197960990720L;
        ResponseResult<RedPacketDto> result = lunchRedPacketService.queryById(id);
        Assert.assertNotNull(result.getData());
    }

    @Test
    public void offlineRedpacket() {
        Long id = 2352197960990720L;
        ResponseResult<Boolean> result = lunchRedPacketService.offlineRedpacket(id);
        Assert.assertNotNull(result.getData());
    }

    @Test
    public void querySendRedPacketRecord(){
        SendLunchRedPacketRequest sendLunchRedPacket = new SendLunchRedPacketRequest();
        sendLunchRedPacket.setBusinessLine(BusinessLineEnum.LUNCH.getCode());
        ResponseResult<Pagenation<SendRedpacketRecordDto>> result = sendLunchRedPacketRecordService.querySendRedPacketRecord(sendLunchRedPacket);
        Assert.assertNotNull(result.getData().getList());
    }

    @Test
    public void querySendRedPacketWater(){
        SendRedPacketWaterRequest waterRequest = new SendRedPacketWaterRequest();
        waterRequest.setSendRedPacketRecordId(1L);
        waterRequest.setBusinessLine(BusinessLineEnum.LUNCH.getCode());
        ResponseResult<Pagenation<SendRedpacketWaterDto>> result = sendLunchRedPacketRecordService.querySendRedPacketWater(waterRequest);
        Assert.assertNotNull(result.getData().getList());
    }

    @Test
    public void asyncTest() throws InterruptedException {
        long timeMillisStart = System.currentTimeMillis();
        System.out.println("----start----");
        lunchRedPacketService.asyncTest();
        long timeMillisEnd = System.currentTimeMillis();
        System.out.println("----end----，cost time "+(timeMillisEnd-timeMillisStart));
    }










}

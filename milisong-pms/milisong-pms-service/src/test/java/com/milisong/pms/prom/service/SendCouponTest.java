package com.milisong.pms.prom.service;

import com.farmland.core.api.ResponseResult;
import com.milisong.pms.prom.dto.*;
import com.milisong.ucs.enums.BusinessLineEnum;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

/**
 * @author sailor wang
 * @date 2019/1/14 6:01 PM
 * @description
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
public class SendCouponTest {

    @Autowired
    private BreakfastCouponServiceImpl breakfastCouponService;

    @Autowired
    SendCouponRecordServiceImpl sendCouponRecordService;


    @Test
    public void batchSendBreakfastCoupon(){
        SendBreakCouponRequest sendBreakCouponRequest = new SendBreakCouponRequest();
        sendBreakCouponRequest.setOperatorId(1L);
        sendBreakCouponRequest.setOperatorName("zhangsan");
        sendBreakCouponRequest.setSendAllUser(true);
        sendBreakCouponRequest.setCouponids(Lists.newArrayList(2137655815634944L));
        sendBreakCouponRequest.setBusinessLine(BusinessLineEnum.BREAKFAST.getCode());
        breakfastCouponService.batchSendBreakfastCoupon(sendBreakCouponRequest);
    }

    @Test
    public void querySendCouponRecord(){
        SendCouponRecordRequest recordRequest = new SendCouponRecordRequest();
        recordRequest.setCouponName("测试");
        recordRequest.setSendTime(new Date());
        recordRequest.setBusinessLine((byte) 0);
        recordRequest.setPageNo(1);
        recordRequest.setPageSize(10);
        ResponseResult<Pagenation<SendCouponRecordDto>> result = sendCouponRecordService.querySendCouponRecord(recordRequest);
        log.info("size -> {}",result.getData().getList().size());
        Assert.assertNotNull(result.getData().getList());
    }


    @Test
    public void querySendCouponWater(){
        SendCouponRecordWaterRequest recordWaterRequest = new SendCouponRecordWaterRequest();
        recordWaterRequest.setSendCouponRecordId(2144235091009536L);
        recordWaterRequest.setMobileNo("13122272813");
        recordWaterRequest.setBusinessLine((byte)0);
        recordWaterRequest.setPageNo(1);
        recordWaterRequest.setPageSize(10);
        ResponseResult<Pagenation<SendCouponWaterDto>> result = sendCouponRecordService.querySendCouponWater(recordWaterRequest);
        log.info("size -> {}",result.getData().getList().size());
        Assert.assertNotNull(result.getData().getList());
    }






}

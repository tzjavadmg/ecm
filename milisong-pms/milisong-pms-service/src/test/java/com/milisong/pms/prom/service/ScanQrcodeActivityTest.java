package com.milisong.pms.prom.service;

import com.farmland.core.api.ResponseResult;
import com.milisong.pms.prom.dto.MarketingQrcodeDto;
import com.milisong.pms.prom.dto.scanqrcode.ScanQrcodeRequest;
import com.milisong.pms.prom.dto.scanqrcode.ScanQrcodeResponse;
import com.milisong.pms.prom.enums.QrcodeType;
import com.milisong.ucs.enums.BusinessLineEnum;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author sailor wang
 * @date 2019/5/10 9:33 PM
 * @description
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
public class ScanQrcodeActivityTest {

    @Autowired
    private MarketingQrcodeServiceImpl marketingQrcodeService;

    @Autowired
    private ActivityScanQrcodeServiceImpl activityScanQrcodeService;

    /**
     * 创建二维码
     */
    @Test
    public void saveOrUpdate(){
        MarketingQrcodeDto marketingQrcode = new MarketingQrcodeDto();
        marketingQrcode.setName("扫码领券活动");
        marketingQrcode.setPagePath("pages/breakfast/receive-coupon/index");
        marketingQrcode.setType(QrcodeType.PROMOTION.getCode());
        marketingQrcode.setUserName("王伟冲");
        marketingQrcode.setBusinessLine(BusinessLineEnum.BREAKFAST.getCode());
        ResponseResult<Boolean> result = marketingQrcodeService.createQrcode(marketingQrcode);
        Assert.assertTrue(result.getData());//PASS
    }

    /**
     * 创建扫码活动
     */
    @Test
    public void createScanQrcodeActivity(){
        ScanQrcodeRequest scanQrcodeRequest = new ScanQrcodeRequest();
        scanQrcodeRequest.setBusinessLine(BusinessLineEnum.BREAKFAST.getCode());
        scanQrcodeRequest.setActivityName("扫码领券活动");
        scanQrcodeRequest.setGiftIds("2788266697564160,2188297322635264");
        scanQrcodeRequest.setBgImg("http://www.baidu.com");
        DateTime date = DateTime.now();
        scanQrcodeRequest.setStartDate(date.toDate());
        scanQrcodeRequest.setEndDate(date.plusDays(10).toDate());
        ResponseResult<Boolean> result = activityScanQrcodeService.createScanQrcodeActivity(scanQrcodeRequest);
        Assert.assertTrue(result.getData());//PASS
    }

    /**
     * 绑定二维码和活动
     */
    @Test
    public void bindQrcodeAndScanQrcodeActivity(){
        ScanQrcodeRequest scanQrcodeRequest = new ScanQrcodeRequest();
        scanQrcodeRequest.setBusinessLine(BusinessLineEnum.BREAKFAST.getCode());
        scanQrcodeRequest.setActivityId(2802356397801472L);
        scanQrcodeRequest.setQrcode("31905101");
        ResponseResult<Boolean> result = activityScanQrcodeService.bindQrcodeAndScanQrcodeActivity(scanQrcodeRequest);
        Assert.assertTrue(result.getData());//PASS
    }

    @Test
    public void queryScanQrcodeActivityByQrcode(){
        ScanQrcodeRequest scanQrcodeRequest = new ScanQrcodeRequest();
        scanQrcodeRequest.setQrcode("31905101");
        ResponseResult<ScanQrcodeResponse> result = activityScanQrcodeService.queryScanQrcodeActivityByQrcode(scanQrcodeRequest);
        Assert.assertNotNull(result.getData());//PASS
    }

    @Test
    public void receiveCouponByQrcode(){
        ScanQrcodeRequest scanQrcodeRequest = ScanQrcodeRequest.builder().qrcode("31905101").userId(2721863869210624L).build();
        ResponseResult<ScanQrcodeResponse> result = activityScanQrcodeService.receiveCouponByQrcode(scanQrcodeRequest);
        Assert.assertNotNull(result.getData());//PASS
    }



}

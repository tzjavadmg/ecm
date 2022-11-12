package com.milisong.pms.breakfast.service;

import com.farmland.core.api.ResponseResult;
import com.milisong.pms.prom.api.ActivityScanQrcodeService;
import com.milisong.pms.prom.dto.scanqrcode.ScanQrcodeRequest;
import com.milisong.pms.prom.dto.scanqrcode.ScanQrcodeResponse;
import com.milisong.ucs.sdk.security.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author sailor wang
 * @date 2019/3/25 5:28 PM
 * @description
 */
@Slf4j
@Service
public class RestScanQrcodeService {

    @Autowired
    private ActivityScanQrcodeService activityScanQrcodeService;

    /**
     * 根据二维码查询活动详情
     *
     * @param qrcode
     * @return
     */
    public ResponseResult<ScanQrcodeResponse> queryScanQrcodeActivityByQrcode(String qrcode) {
        ScanQrcodeRequest scanQrcodeRequest = ScanQrcodeRequest.builder().qrcode(qrcode).build();
        return activityScanQrcodeService.queryScanQrcodeActivityByQrcode(scanQrcodeRequest);
    }

    /**
     * 领取扫码活动券
     *
     * @return
     */
    public ResponseResult<ScanQrcodeResponse> receiveCouponByQrcode(@RequestParam("qrcode") String qrcode){
        Long userId = UserContext.getCurrentUser().getId();
        Byte isNew = UserContext.getCurrentUser().getIsNew();
        ScanQrcodeRequest scanQrcodeRequest = ScanQrcodeRequest.builder().qrcode(qrcode).userId(userId).isNew(isNew).build();
        return activityScanQrcodeService.receiveCouponByQrcode(scanQrcodeRequest);
    }
}
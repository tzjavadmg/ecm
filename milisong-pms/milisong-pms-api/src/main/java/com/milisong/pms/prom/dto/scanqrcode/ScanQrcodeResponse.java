package com.milisong.pms.prom.dto.scanqrcode;

import com.milisong.pms.prom.dto.UserCouponDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sailor wang
 * @date 2019/5/10 1:48 PM
 * @description
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScanQrcodeResponse {

    private ActivityScanQrcodeDto activityScanQrcode;

    //领取的券
    private UserCouponDto receiveCoupon;

    //是否领取过
    private Boolean received = Boolean.FALSE;

    //活动是否结束
    private Boolean isOver = Boolean.FALSE;

    //没资格领取
    private Boolean receiveNotAllowed = Boolean.FALSE;

    //备注
    private String remark;
}
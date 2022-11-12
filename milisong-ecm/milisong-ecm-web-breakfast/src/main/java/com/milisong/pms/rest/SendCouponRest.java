package com.milisong.pms.rest;

import com.farmland.core.api.ResponseResult;
import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.milisong.pms.breakfast.enums.BusinessLine;
import com.milisong.pms.prom.api.BreakfastCouponService;
import com.milisong.pms.prom.api.SendCouponRecordService;
import com.milisong.pms.prom.dto.*;
import com.milisong.ucs.api.UserDeliveryAddressService;
import com.milisong.ucs.dto.UserDeliveryAddressDto;
import com.milisong.upms.dto.UserInfoDto;
import com.milisong.upms.utils.UserInfoUtil;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author sailor wang
 * @date 2019/1/13 5:19 PM
 * @description
 */
@Slf4j
@RestController
@RequestMapping("/v1/sendcoupon/")
public class SendCouponRest {

    @Autowired
    private BreakfastCouponService breakfastCouponService;

    @Autowired
    UserDeliveryAddressService userDeliveryAddressService;

    @Autowired
    SendCouponRecordService sendCouponRecordService;

    /**
     * 查询公司的用户列表
     *
     * @param sendBreakCouponRequest
     * @return
     */
    @PostMapping("query-delivery-user")
    ResponseResult<com.milisong.ucs.dto.Pagenation<UserDeliveryAddressDto>> queryUserByDevlieryAddr(@RequestBody com.milisong.ucs.dto.SendBreakCouponRequest sendBreakCouponRequest) {
        sendBreakCouponRequest.setBusinessLine(BusinessLine.BREAKFAST.getCode());
        return userDeliveryAddressService.queryUserByDevlieryAddr(sendBreakCouponRequest);
    }

    /**
     * 根据id获取数据
     *
     * @param sendCouponRecordId
     * @return
     */
    @GetMapping("get-by-id")
    ResponseResult<SendCouponSearchConditionDto> getById(@RequestParam("id") Long sendCouponRecordId) {
        return sendCouponRecordService.getById(sendCouponRecordId);
    }

    /**
     * 新增或更新
     *
     * @param sendBreakCouponRequest
     * @return
     */
    @PostMapping("save-or-update")
    ResponseResult<Boolean> saveOrUpdate(@RequestBody SendBreakCouponRequest sendBreakCouponRequest) {
        UserInfoDto userInfo = UserInfoUtil.getCurrentUserInfo();
        sendBreakCouponRequest.setBusinessLine(BusinessLine.BREAKFAST.getCode());
        sendBreakCouponRequest.setOperatorId(userInfo.getUserNo());
        sendBreakCouponRequest.setOperatorName(userInfo.getUserName());
        String mobileText = sendBreakCouponRequest.getMobileText();
        if (StringUtil.isNotBlank(mobileText)){
            sendBreakCouponRequest.setMobiles(Lists.newArrayList(Splitter.on(CharMatcher.breakingWhitespace()).split(mobileText)));
            sendBreakCouponRequest.setMobileText(null);
        }
        breakfastCouponService.batchSendBreakfastCoupon(sendBreakCouponRequest);
        return ResponseResult.buildSuccessResponse(Boolean.TRUE);
    }

    /**
     * 查询公司列表
     *
     * @param companyName
     * @return
     */
    @GetMapping("query-delivery-company")
    ResponseResult<List<UserDeliveryAddressDto>> queryDeliveryCompany(@RequestParam("companyName") String companyName) {
        return userDeliveryAddressService.queryDeliveryCompany(companyName,BusinessLine.BREAKFAST.getCode());
    }

    /**
     * 查询发送券记录
     *
     * @param recordRequest
     * @return
     */
    @PostMapping("query-send-coupon-record")
    ResponseResult<Pagenation<SendCouponRecordDto>> querySendCouponRecord(@RequestBody SendCouponRecordRequest recordRequest) {
        recordRequest.setBusinessLine(BusinessLine.BREAKFAST.getCode());
        return sendCouponRecordService.querySendCouponRecord(recordRequest);
    }

    /**
     * 查询发送流水
     *
     * @param recordWaterRequest
     * @return
     */
    @PostMapping("query-send-coupon-water")
    ResponseResult<Pagenation<SendCouponWaterDto>> querySendCouponWater(@RequestBody SendCouponRecordWaterRequest recordWaterRequest) {
        recordWaterRequest.setBusinessLine(BusinessLine.BREAKFAST.getCode());
        return sendCouponRecordService.querySendCouponWater(recordWaterRequest);
    }

}

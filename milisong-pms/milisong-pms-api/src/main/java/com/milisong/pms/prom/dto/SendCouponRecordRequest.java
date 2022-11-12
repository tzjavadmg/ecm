package com.milisong.pms.prom.dto;

import com.farmland.core.api.PageParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author sailor wang
 * @date 2019/1/13 2:37 PM
 * @description
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendCouponRecordRequest extends PageParam {
    private String couponName;

    private Date sendTime;

    private Byte businessLine;
}
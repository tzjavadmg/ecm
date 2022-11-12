package com.milisong.pms.prom.dto;

import com.milisong.pms.prom.enums.CouponEnum;
import com.milisong.pms.prom.enums.RedPacketType;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sailor wang
 * @date 2018/9/16 下午7:10
 * @description
 */
@Data
public class ActivityQueryParam extends BaseDto implements Serializable {

    private Integer pageNo;

    private Integer pageSize;

    private Long userId;

    private Boolean usable;

    private RedPacketType type;

    private CouponEnum.TYPE couponType;
}
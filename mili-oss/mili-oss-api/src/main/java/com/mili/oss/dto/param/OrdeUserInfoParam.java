package com.mili.oss.dto.param;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author zhaozhonghui
 * @Description 订单用户信息
 * @date 2019-03-23
 */
@Data
@EqualsAndHashCode
public class OrdeUserInfoParam implements Serializable {
    private static final long serialVersionUID = 4205749193693033041L;
    /** 订单号 */
    private String orderNo;
    /** 用户信息 */
    private String userInfo;
    /** 业务类型 早餐0 午餐1*/
    private Byte orderType;
}

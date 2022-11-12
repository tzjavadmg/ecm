package com.milisong.wechat.miniapp.dto;

import com.milisong.wechat.miniapp.annotation.Required;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 小程序支付请求（主动支付、代扣）
 * @author sailor wang
 * @date 2018/9/3 上午11:11
 * @description
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MiniPaymentRequest extends BaseDto implements Serializable {
    private static final long serialVersionUID = -5916576903992189959L;

    @Required
    private String openid;

    @Required
    private String orderNo;

    @Required
    private BigDecimal totalAmount;

    @Required
    private String contractNo;

    @Required
    private String ip;


}
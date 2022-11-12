package com.milisong.wechat.miniapp.dto;

import com.milisong.wechat.miniapp.annotation.Required;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.*;

/**
 * @author sailor wang
 * @date 2018/10/12 上午11:06
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XStreamAlias("xml")
public class MiniBillRequest extends BaseMiniPayRequest {

    /**
     *
     */
    private String env;

    /**
     * 下载对账单的日期，格式：20140603
     */
    @Required
    @XStreamAlias("bill_date")
    private String billDate;

    /**
     * ALL，返回当日所有订单信息，默认值
     *
     * SUCCESS，返回当日成功支付的订单
     *
     * REFUND，返回当日退款订单
     *
     * RECHARGE_REFUND，返回当日充值退款订单
     */
    @Required
    @XStreamAlias("bill_type")
    private String billType;

    /**
     * 非必传参数，固定值：GZIP，返回格式为.gzip的压缩包账单。不传则默认为数据流形式。
     */
    @XStreamAlias("tar_type")
    private String tarType;

    @Required
    private Byte businessLine;

    /**
     * 检查约束情况.
     */
    @Override
    protected String checkConstraints() {
        return null;
    }
}
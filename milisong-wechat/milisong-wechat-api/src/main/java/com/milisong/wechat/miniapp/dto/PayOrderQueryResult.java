package com.milisong.wechat.miniapp.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.Setter;

/**
 * @author sailor wang
 * @date 2018/10/31 5:55 PM
 * @description
 */
@Setter
@Getter
@XStreamAlias("xml")
public class PayOrderQueryResult extends BasicPayDto {

    /**
     * <pre>用户标识
     * openid
     * 是
     * String(128)
     * oUpF8uMuAJO_M2pxb1Q9zNjWeS6o
     * 用户在商户appid下的唯一标识
     * </pre>
     */
    @XStreamAlias("openid")
    private String openid;

    /**
     * <pre>是否关注公众账号
     * is_subscribe
     * 否
     * String(1)
     * Y
     * 用户是否关注公众账号，Y-关注，N-未关注，仅在公众账号类型支付有效
     * </pre>
     */
    @XStreamAlias("is_subscribe")
    private String isSubscribe;

    /**
     * <pre>交易类型
     * trade_type
     * 是
     * String(16)
     * JSAPI
     * 调用接口提交的交易类型，取值如下：JSAPI，NATIVE，APP，MICROPAY，详细说明见参数规定
     * </pre>
     */
    @XStreamAlias("trade_type")
    private String tradeType;

    /**
     * <pre>交易状态
     * trade_state
     * 是
     * String(32)
     * SUCCESS
     * SUCCESS—支付成功,REFUND—转入退款,NOTPAY—未支付,CLOSED—已关闭,REVOKED—已撤销（刷卡支付）,USERPAYING--用户支付中,PAYERROR--支付失败(其他原因，如银行返回失败)
     * </pre>
     */
    @XStreamAlias("trade_state")
    private String tradeState;

    /**
     * <pre>付款银行
     * bank_type
     * 是
     * String(16)
     * CMC
     * 银行类型，采用字符串类型的银行标识
     * </pre>
     */
    @XStreamAlias("bank_type")
    private String bankType;

    /**
     * <pre>订单金额
     * total_fee
     * 是
     * Int
     * 100
     * 订单总金额，单位为分
     * </pre>
     */
    @XStreamAlias("total_fee")
    private Integer totalFee;

    /**
     * <pre>货币种类
     * fee_type
     * 否
     * String(8)
     * CNY
     * 货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
     * </pre>
     */
    @XStreamAlias("fee_type")
    private String feeType;

    /**
     * <pre>现金支付金额
     * cash_fee
     * 是
     * Int
     * 100
     * 现金支付金额订单现金支付金额，详见支付金额
     * </pre>
     */
    @XStreamAlias("cash_fee")
    private Integer cashFee;

    /**
     * <pre>微信支付订单号
     * transaction_id
     * 是
     * String(32)
     * 1009660380201506130728806387
     * 微信支付订单号
     * </pre>
     */
    @XStreamAlias("transaction_id")
    private String transactionId;
    /**
     * <pre>商户订单号
     * out_trade_no
     * 是
     * String(32)
     * 20150806125346
     * 商户系统的订单号，与请求一致。
     * </pre>
     */
    @XStreamAlias("out_trade_no")
    private String outTradeNo;
    /**
     * <pre>附加数据
     * attach
     * 否
     * String(128)
     * 深圳分店
     * 附加数据，原样返回
     * </pre>
     */
    @XStreamAlias("attach")
    private String attach;
    /**
     * <pre>支付完成时间
     * time_end
     * 是
     * String(14)
     * 20141030133525
     * 订单支付时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则
     * </pre>
     */
    @XStreamAlias("time_end")
    private String timeEnd;
    /**
     * <pre>交易状态描述
     * trade_state_desc
     * 是
     * String(256)
     * 支付失败，请重新下单支付
     * 对当前查询订单状态的描述和下一步操作的指引
     * </pre>
     */
    @XStreamAlias("trade_state_desc")
    private String tradeStateDesc;


}

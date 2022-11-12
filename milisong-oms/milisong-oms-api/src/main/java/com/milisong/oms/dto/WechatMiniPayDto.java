package com.milisong.oms.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/9/19 14:07
 */
@Getter
@Setter
public class WechatMiniPayDto {
    /**
     * 时间戳从1970年1月1日00:00:00至今的秒数,即当前的时间
     */
    private String timeStamp;
    /**
     * 随机字符串，长度为32个字符以下
     */
    private String nonceStr;
    /**
     * 统一下单接口返回的 prepay_id 参数值，提交格式如：prepay_id=*
     */
    private String dataPackage;
    /**
     * 签名类型，默认为MD5，支持HMAC-SHA256和MD5。注意此处需与统一下单的签名类型一致
     */
    private String signType;
    /**
     * 签名
     */
    private String paySign;
    /**
     * 订单ID
     */
    private Long orderId;
}

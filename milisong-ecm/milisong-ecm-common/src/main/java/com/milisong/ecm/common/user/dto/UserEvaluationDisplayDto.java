package com.milisong.ecm.common.user.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <pre>
 *    author  : Tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/10/10   18:26
 *    desc    : 问卷展示信息dto
 *    version : v1.0
 * </pre>
 */
@Data
public class UserEvaluationDisplayDto extends BaseDto implements Serializable {
    private static final long serialVersionUID = -569468845610477138L;

    /**
     * 配送单id
     */
    private Long deliveryId;

    /**
     * 配送单商品id
     */
    private Long deliveryGoodsId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 订单编号
     */
    private Long orderId;

    /**
     * 商品名称1
     */
    private String goodsName;

    /**
     * 商品名称2
     */
    private String goodsNameSecond;

    /**
     * 商品名称3
     */
    private String goodsNameThird;

    private String mobileNo;

}

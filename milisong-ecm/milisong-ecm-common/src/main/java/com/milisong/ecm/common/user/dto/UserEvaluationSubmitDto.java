package com.milisong.ecm.common.user.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <pre>
 *    author  : Tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/10/10   18:26
 *    desc    : 问卷提交信息dto
 *    version : v1.0
 * </pre>
 */
@Data
public class UserEvaluationSubmitDto extends BaseDto implements Serializable {
    private static final long serialVersionUID = -569468845610477138L;
    private Long id;


    /**
     * 子订单id
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
     * 订单编号（子单号）
     */
    private Long orderId;

    /**
     * 用户手机号
     */
    private String mobileNo;

    /**
     * 商品name
     */
    private String goodsName;

    /**
     * 选项描述，逗号分隔
     */
    private List<String> goodsNames;

    /**
     * 选项1结果1,2,3
     */
    private Byte optionOneResult;

    /**
     * 选项2结果1,2,3
     */
    private Byte optionTwoResult;

    /**
     * 选项3结果1,2,3
     */
    private Byte optionThreeResult;

    /**
     * 选项4结果1,2,3
     */
    private Byte optionFourResult;

    /**
     * 文本结果
     */
    private String content;

    private Byte returnCall;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}

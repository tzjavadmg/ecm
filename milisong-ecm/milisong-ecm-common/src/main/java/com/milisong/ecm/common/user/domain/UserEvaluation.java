package com.milisong.ecm.common.user.domain;

import lombok.Data;

import java.util.Date;

/**
 * @description:user_evaluation表的实体类
 * @version  1.0
 * @author:  tianhaibo
 * @createTime: 2018-10-22 14:33:55
 */
@Data
public class UserEvaluation {
    private Long id;

    /**
     * 业务线:0-早饭,1-午饭,2-下午茶,3-晚餐,4-夜宵
     */
    private Byte businessLine;

    /**
     * 订单id
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
     * 商品名称
     */
    private String goodsName;

    /**
     * 订单编号
     */
    private String orderId;

    /**
     * 用户手机号
     */
    private String mobileNo;

    /**
     * 选项1[菜品口味]结果0-难吃,1-一般,2-好吃
     */
    private Byte optionOneResult;

    /**
     * 选项1[菜品分量]结果0-偏少,1-适合,2-偏多
     */
    private Byte optionTwoResult;

    /**
     * 选项1[主食分量]结果0-偏少,1-适合,2-偏多
     */
    private Byte optionThreeResult;

    /**
     * 选项1[配送服务]结果0-不满意,1-还行,2-满意
     */
    private Byte optionFourResult;

    /**
     * 文本结果
     */
    private String content;

    /**
     * 是否接受电话回访 0-否,1-是
     */
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
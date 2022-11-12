package com.mili.oss.domain;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * 集单商品明细表-早餐
 */
@Getter
@Setter
public class OrderSetGoods {
    /**
     * 
     */
    private Long id;

    /**
     * 子集单单号
     */
    private String detailSetNo;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 订单联的编号
     */
    private String coupletNo;

    /**
     * 订餐用户id
     */
    private Long userId;

    /**
     * 订餐用户名字
     */
    private String userName;

    /**
     * 订餐用户手机号
     */
    private String userPhone;

    /**
     * 商品编号
     */
    private String goodsCode;

    /**
     * 商品名字
     */
    private String goodsName;

    /**
     * 数量
     */
    private Integer goodsNumber;

    /**
     * 单件商品实付金额
     */
    private BigDecimal actualPayAmount;

    /**
     * 是否组合商品 1是 0否
     */
    private Boolean isCombo;

    /**
     * 是否为小菜
     */
    private Byte type;

    /**
     * 组合商品code
     */
    private String comboGoodsCode;

    /**
     * 组合商品名称
     */
    private String comboGoodsName;

    /**
     * 组合商品数量
     */
    private Integer comboGoodsCount;

    /**
     * 是否删除 1是 0否
     */
    private Boolean isDeleted;

    /**
     * 创建人  账号_名字组合
     */
    private String createBy;

    /**
     * 最后修改人 账号_名字组合
     */
    private String updateBy;
}
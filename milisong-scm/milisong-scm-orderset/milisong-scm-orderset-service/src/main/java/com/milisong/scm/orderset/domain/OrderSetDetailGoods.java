package com.milisong.scm.orderset.domain;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * 子集单商品明细表
 */
@Getter
@Setter
public class OrderSetDetailGoods {
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
     * 是否删除 1是 0否
     */
    private Boolean isDeleted;

    /**
     * 创建人  账号_名字组合
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最后修改人 账号_名字组合
     */
    private String updateBy;

    /**
     * 最后修改时间
     */
    private Date updateTime;
}
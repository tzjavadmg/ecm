package com.milisong.scm.goods.domain;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * 商品表
 */
@Getter
@Setter
public class Goods {
    /**
     * 主键
     */
    private Long id;

    /**
     * 商品code/sku
     */
    private String code;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 类目code
     */
    private String categoryCode;

    /**
     * 类目名称
     */
    private String categoryName;

    /**
     * 商品详情
     */
    private String describe;

    /**
     * 建议零售价
     */
    private BigDecimal advisePrice;

    /**
     * 优惠价
     */
    private BigDecimal preferentialPrice;

    /**
     * 商品进价
     */
    private BigDecimal buyingPrice;

    /**
     * 包装进价
     */
    private BigDecimal packagingPrice;

    /**
     * 折扣
     */
    private BigDecimal discount;

    /**
     * 小图片
     */
    private String picture;

    /**
     * 大图片
     */
    private String bigPicture;

    /**
     * 辣度 0不辣 1微辣 2中辣 3重辣 4变态辣
     */
    private Byte spicy;

    /**
     * 保质期(天)
     */
    private Byte shelfLife;

    /**
     * 口味
     */
    private String taste;

    /**
     * 排序号(权重)
     */
    private Byte weight;

    /**
     * 0=主餐、1、小菜
     */
    private Byte type;

    /**
     * 限定开始时间
     */
    private Date beginDate;

    /**
     * 限定结束时间
     */
    private Date endDate;

    /**
     * 是否限定时间1是 0否
     */
    private Byte isLimitedTime;

    /**
     * 税率
     */
    private Double rate;

    /**
     * 商品状态产品要求排序规则为：1使用中、2已停用
     */
    private Byte status;

    /**
     * 商品子状态  2使用中  5已停用
     */
    private Byte detailStatus;

    /**
     * 是否删除 1是 0否
     */
    private Boolean isDeleted;

    /**
     * 创建人:账号_名字组合
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最后修改人:账号_名字组合
     */
    private String updateBy;

    /**
     * 最后修改时间:初始值为创建时间，最终值为最后修改的时间
     */
    private Date updateTime;
}
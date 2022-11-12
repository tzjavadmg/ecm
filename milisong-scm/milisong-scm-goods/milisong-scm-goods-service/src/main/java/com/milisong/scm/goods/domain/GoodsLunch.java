package com.milisong.scm.goods.domain;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * 午餐-商品表
 */
@Getter
@Setter
public class GoodsLunch {
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
     * 实际售价
     */
    private BigDecimal preferentialPrice;

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
     * 口味
     */
    private String taste;

    /**
     * 排序号(权重)
     */
    private Byte weight;

    /**
     * 商品状态 1使用中 2已停用
     */
    private Byte status;

    /**
     * 是否删除 1是 0否
     */
    private Boolean isDeleted;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 修改时间
     */
    private Date updateTime;
}
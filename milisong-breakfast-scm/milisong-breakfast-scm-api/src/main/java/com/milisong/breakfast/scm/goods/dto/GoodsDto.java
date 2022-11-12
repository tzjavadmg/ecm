package com.milisong.breakfast.scm.goods.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商品信息dto
 *
 * @author youxia 2018年9月2日
 */
@Data
public class GoodsDto implements Serializable {

    private static final long serialVersionUID = -6859505414716743460L;

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
     * 商品数量
     */
    private Integer goodsCount;

    /**
     * 小图片
     */
    private String picture;

    /**
     * 大图片
     */
    private String bigPicture;

    /**
     * 小图片
     */
    private String pictureShow;

    /**
     * 大图片
     */
    private String bigPictureShow;

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
     * 商品状态 2使用中 5已停用
     */
    private Byte status;

    private String statusStr;


    /**
     * 是否为组合套餐（1是、0否）
     */
    private Boolean isCombo;

    /**
     * 子商品
     */
    private List<GoodsCombinationDto> listGoodsCombination;

    private Boolean isNewGoods = true;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date newGoodsExpiredTime;

    private BigDecimal purchasePrice;

}

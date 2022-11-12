package com.milisong.breakfast.scm.goods.domain;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * 组合商品明细表
 */
@Getter
@Setter
public class GoodsCombinationRef {
    /**
     * 
     */
    private Long id;

    /**
     * 组合商品code
     */
    private String combinationCode;

    /**
     * 单品商品code
     */
    private String singleCode;

    /**
     * 单品数量
     */
    private Integer goodsCount;

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
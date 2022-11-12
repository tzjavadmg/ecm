package com.milisong.breakfast.scm.goods.dto;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * 早餐-类目表
 */
@Getter
@Setter
public class GoodsCategoryInfoDto {
    /**
     * 主键
     */
    private Long id;

    /**
     * 类目code
     */
    private String code;

    /**
     * 类目名称
     */
    private String name;

    /**
     * 商品图片
     */
    private String picture;
    /**
     * 商品图片
     */
    private String pictureView;

    /**
     * 类目简介
     */
    private String describe;

    /**
     * 排序号(权重)
     */
    private Byte weight;

    /**
     * 父类目id
     */
    private Long pid;

    /**
     * 状态1有效 0无效
     */
    private Byte status;
    /**
     * 状态1有效 0无效
     */
    private String statusStr;
    
    /**
     * 分类类型(1单品，2套餐)
     */
    private Byte type;
    
    /**
     * 分类类型(1单品，2套餐)
     */
    private String typeStr;

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
package com.milisong.ecm.breakfast.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 早餐-类目表
 */
@Getter
@Setter
public class CatalogMqDto {
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
  
}
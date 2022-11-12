package com.milisong.scm.goods.domain;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * 类目表
 */
@Getter
@Setter
public class GoodsCatalog {
    /**
     * 主键
     */
    private Integer id;

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
     * 父类目id
     */
    private Long pid;

    /**
     * 税率
     */
    private Double rate;

    /**
     * 状态1有效 0无效
     */
    private Byte status;

    /**
     * 是否删除 1是 0否
     */
    private Boolean isDeleted;

    /**
     * 创建人账号_名字组合
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最后修改人账号_名字组合
     */
    private String updateBy;

    /**
     * 最后修改时间:初始值为创建时间，最终值为最后修改的时间
     */
    private Date updateTime;
}
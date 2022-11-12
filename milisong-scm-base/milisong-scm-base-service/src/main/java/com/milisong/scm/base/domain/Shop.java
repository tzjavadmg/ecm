package com.milisong.scm.base.domain;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * 门店表
 */
@Getter
@Setter
public class Shop {
    /**
     * 主键
     */
    private Long id;

    /**
     * 门店code
     */
    private String code;

    /**
     * 门店名称
     */
    private String name;

    /**
     * 门店状态:1营业中 2停业
     */
    private Byte status;

    /**
     * 门店地址
     */
    private String address;

    /**
     * 删除标识:1是 0否
     */
    private Boolean isDelete;

    /**
     * 创建人:账号_名字组合
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最后修改人
     */
    private String updateBy;

    /**
     * 最后修改时间
     */
    private Date updateTime;
}
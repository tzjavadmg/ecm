package com.milisong.breakfast.scm.configuration.dto;

import lombok.Data;

import java.util.Date;

/**
 * @description:t_global_config2表的实体类
 * @version  1.0
 * @author:  tianhaibo
 * @createTime: 2018-10-25 18:30:25
 */
@Data
public class GlobalConfigDto {
    private Long id;

    /**
     * 业务线：1-ecm，2-scm，3-pos，0-common
     */
    private String[] serviceLines;

    /**
     * 配置key
     */
    private String configKey;

    /**
     * 配置key描述
     */
    private String configDescribe;

    /**
     * 配置value
     */
    private String configValue;

    /**
     * 查看图片url
     */
    private String actualPicUrl;

    /**
     * 配置值的类型 1字符串 2整数 3小数 4日期 5时间 6日期+时间
     */
    private Byte configValueType;

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
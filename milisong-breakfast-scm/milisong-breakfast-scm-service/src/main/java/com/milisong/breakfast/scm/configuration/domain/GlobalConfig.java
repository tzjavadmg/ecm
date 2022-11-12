package com.milisong.breakfast.scm.configuration.domain;

import java.util.Date;
import lombok.Data;

/**
 * @description:global_config表的实体类
 * @version:  1.0
 * @author:  admin
 * @createTime: 2018-12-01 14:18:57
 */
@Data
public class GlobalConfig {
    private Long id;

    /**
     * 业务线：1-ecm，2-scm，3-pos，0-common
     */
    private String serviceLine;

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
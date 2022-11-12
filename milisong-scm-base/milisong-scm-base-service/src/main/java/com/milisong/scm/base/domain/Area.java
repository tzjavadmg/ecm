package com.milisong.scm.base.domain;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * BOSS基础数据 - 区域
 */
@Getter
@Setter
public class Area {
    /**
     * id
     */
    private Long id;

    /**
     * 区域编码
     */
    private String code;

    /**
     * 区域名称
     */
    private String name;

    /**
     * 父区域code
     */
    private String pCode;

    /**
     * 区域级别 0国家 1省 2市 3县 4镇 5村
     */
    private String level;

    /**
     * 拼音首字母
     */
    private String pyFirst;

    /**
     * 排序号
     */
    private Integer orderNo;

    /**
     * 状态 1有效 0无效
     */
    private Byte status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人 userNo_userName
     */
    private String createBy;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 最后人 userNo_userName
     */
    private String updateBy;
}
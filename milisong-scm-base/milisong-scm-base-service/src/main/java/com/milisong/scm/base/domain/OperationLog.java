package com.milisong.scm.base.domain;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * 操作日志
 */
@Getter
@Setter
public class OperationLog {
    /**
     * 
     */
    private Long id;

    /**
     * 模块
     */
    private String modular;

    /**
     * 业务数据id或编码
     */
    private String bussinessId;

    /**
     * 操作类型 add/update/delete
     */
    private String operationType;

    /**
     * 操作人 id_名字 组合
     */
    private String operationUser;

    /**
     * 操作时间
     */
    private Date operationTime;

    /**
     * 操作简述，例如: 加热/打包/交配送/
     */
    private String operationResume;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;

    /**
     * 操作导致数据的表更
     */
    private String diffData;
}
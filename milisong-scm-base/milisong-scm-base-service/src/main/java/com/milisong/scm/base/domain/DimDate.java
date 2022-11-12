package com.milisong.scm.base.domain;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * 日期信息表
 */
@Getter
@Setter
public class DimDate {
    /**
     * 日期
     */
    private Date date;

    /**
     * 所在周属于哪一年
     */
    private String weekYear;

    /**
     * 一年中的第几周
     */
    private String weekOfYear;

    /**
     * 一周中的星期几：星期一=1，星期日=7
     */
    private Integer dayOfWeek;

    /**
     * 节假日信息：工作日=0,正常休息日=1,节假日=2,已包含2017-2018法定假日
     */
    private Integer status;
}
package com.milisong.oms.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/23 18:39
 */
@Getter
@Setter
public class TimezoneDto {

    private Long id;
    /**
     * 是否默认值
     */
    private Boolean isDefault;
    /**
     * 门店最大产量
     */
    private Integer maxCapacity;
    /**
     * 截单时间字符串 11:00-12:00
     */
    private String cutoffTime;
    /**
     * 配送开始时间字符串 HH:mm
     */
    private String startTime;
    /**
     * 配送结束时间字符串 HH:mm
     */
    private String endTime;
    /**
     * 是否可用
     */
    private boolean available;
}

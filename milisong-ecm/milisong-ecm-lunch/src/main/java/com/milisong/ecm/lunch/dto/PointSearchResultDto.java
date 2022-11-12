package com.milisong.ecm.lunch.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <pre>
 *    author  : tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/12/6   11:10
 *    desc    : 距离搜索dto
 *    version : v1.0
 * </pre>
 */
@Data
public class PointSearchResultDto implements Serializable {

    private static final long serialVersionUID = -2779286122716899328L;
    // 业务系统id
    private String businessId;
    // 距离
    private double distance;
    // 距离单位 km/m
    private String metrics;

}

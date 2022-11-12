package com.milisong.ecm.breakfast.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <pre>
 *    author  : tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/12/6   11:10
 *    desc    : 位置点附近搜索入参dto
 *    version : v1.0
 * </pre>
 */
@Data
public class PointSearchParamDto implements Serializable {
    private static final long serialVersionUID = 5275754179159990371L;

    // 经度
    private double lon;
    // 纬度
    private double lat;
    /**
     * 查询多少条
     */
    private int limit;
    /**
     * 单位 @link MetricsEnum
     */
    private String metrics;
    /**
     * 距离
     */
    private double distance;
    /**
     * 坐标系类型 @link CoordinateSystemEnum
     */
    private String type;

}

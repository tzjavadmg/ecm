package com.milisong.ecm.lunch.goods.dto;

import lombok.Data;

import java.util.Date;

/**
 * 楼宇信息dto
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/9/2 16:43
 */
@Data
public class BuildingDto {

    private Long id;
    /**
     * 楼宇名称
     */
    private String name;
    /**
     * 楼宇名称简称
     */
    private String abbreviation;
    /**
     * 楼宇所在城市code
     */
    private String cityCode;
    /**
     * 楼宇所在城市名称
     */
    private String cityName;
    /**
     * 楼宇所在区县code
     */
    private String regionCode;
    /**
     * 楼宇所在区县name
     */
    private String regionName;
    /**
     * 不需要包含城市和区县的楼宇详细地址
     */
    private String detailAddress;
    /**
     * 开通时间
     */
    private Date openTime;
    /**
     * 楼宇状态0未开通 2待开通 3关闭 9已开通
     */
    private Short status;
    /**
     * 纬度
     */
    private Double lat;
    /**
     * 经度
     */
    private Double lon;
    /**
     * 楼层数
     */
    private Integer floor;
    /**
     * 门店编码
     */
    private String shopCode;
    /**
     * 门店名称
     */
    private String shopName;
    
    /**
     * 排序
     */
    private Integer weight;

}

package com.milisong.breakfast.scm.goods.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
*@author    created by benny
*@date  2018年12月3日---下午7:50:09
*
*/
@Getter
@Setter
public class GoodsScheduleDetailDto {

	 /**
     * 
     */
    private Long id;

    /**
     * 门店d
     */
    private Long shopId;

    /**
     * 门店code
     */
    private String shopCode;

    /**
     * 门店名称
     */
    private String shopName;

    /**
     * 年份
     */
    private Integer year;

    /**
     * W多少周
     */
    private Integer weekOfYear;

    /**
     * 商品编码
     */
    private String goodsCode;

    /**
     * 商品名字
     */
    private String goodsName;

    /**
     * 第1天是否可售 1可售 0不可售
     */
    private Boolean flag1;

    /**
     * 第2天是否可售 1可售 0不可售
     */
    private Boolean flag2;

    /**
     * 第3天是否可售 1可售 0不可售
     */
    private Boolean flag3;

    /**
     * 第4天是否可售 1可售 0不可售
     */
    private Boolean flag4;

    /**
     * 第5天是否可售 1可售 0不可售
     */
    private Boolean flag5;

    /**
     * 第6天是否可售 1可售 0不可售
     */
    private Boolean flag6;

    /**
     * 第7天是否可售 1可售 0不可售
     */
    private Boolean flag7;

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

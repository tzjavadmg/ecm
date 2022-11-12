package com.milisong.scm.printer.request;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

/**
 * 警告！！！警告！！！警告！！！  未经允许、不能修改此类。 请继承此类进行操作
 * @author benny
 *
 */
@Getter
@Setter
public class OrderSetDetailDto implements Serializable{
    /**
	 * 警告！！！警告！！！警告！！！  未经允许、不能修改此类。 请继承此类进行操作
	 */
	private static final long serialVersionUID = -7963260122838506025L;

	/**
     * 警告！！！警告！！！警告！！！  未经允许、不能修改此类。 请继承此类进行操作
     */
    private Long id;

    /**
     * 主集单(楼宇)单号
     */
    private String setNo;

    /**
     * 子集单单号
     */
    private String detailSetNo;

    /**
     * 子集单号描述
     */
    private String detailSetNoDescription;

    /**
     * 配送日期
     */
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date detailDeliveryDate;
    
    /**
     * 配送开始时间
     */
    @JsonFormat(pattern="HH:mm:ss",timezone = "GMT+8")
    private Date deliveryStartTime;

    /**
     * 配送结束时间
     */
    @JsonFormat(pattern="HH:mm:ss",timezone = "GMT+8")
    private Date deliveryEndTime;

    /**
     * 订单详细配送地址
     */
    private String detailDeliveryAddress;

    /**
     * 门店id
     */
    private Long shopId;

    /**
     * 楼宇id
     */
    private Long buildingId;

    /**
     * 楼宇简称
     */
    private String buildingAbbreviation;

    /**
     * 楼层
     */
    private String deliveryFloor;

    /**
     * 公司id
     */
    private Long companyId;

    /**
     * 公司简称/房间号
     */
    private String companyAbbreviation;
    
    /**
     * 取餐点
     */
    private String mealAddress;

    /**
     * 子集单中商品总数量
     */
    private Integer goodsSum;

    /**
     * 实付总金额
     */
    private BigDecimal actualPayAmount;

    /**
     * 是否已打印 1是 0否
     */
    private Boolean isPrint;

    /**
     * 状态 1待打包 2已打包 3配送中 4已通知
     */
    private Byte status;

    /**
     * 配送状态 -1初始化 0已派单 1已接单 2已到店 3已取件 4已送达
     */
    private Byte distributionStatus;

    /**
     * 是否已发MQ 1是 0否
     */
    private Boolean isPush;

    /**
     * 是否删除 1是 0否
     */
    private Boolean isDeleted;

    /**
     * 创建人  账号_名字组合
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date createTime;

    /**
     * 最后修改人 账号_名字组合
     */
    private String updateBy;

    /**
     * 最后修改时间
     */
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date updateTime;
    
}
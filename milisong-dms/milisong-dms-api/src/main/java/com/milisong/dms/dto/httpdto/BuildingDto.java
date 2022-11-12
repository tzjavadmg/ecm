package com.milisong.dms.dto.httpdto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 楼宇信息dto
 * @author youxia 2018年9月2日
 */
@Data
@JsonInclude(Include.NON_NULL)
public class BuildingDto implements Serializable {
	
	private static final long serialVersionUID = 1130013098360369874L;
	
	private Long id;
	
	private String name;
	
	private String abbreviation; // 楼宇名称简称
	
	private String cityCode; // 楼宇所在城市code
	
	private String cityName; // 楼宇所在城市名称
	
	private String regionCode; // 楼宇所在区县code
	
	private String regionName; // 楼宇所在区县name
	
	private String detailAddress; // 不需要包含城市和区县的楼宇详细地址
	
	private Date openTime; // 开通时间
	
	private Byte status; // 楼宇状态0未开通 1已开通 2待开通 3关闭
	
	private BigDecimal lat ; // 纬度
	
	private BigDecimal lon ; // 经度
	/** 楼层数 */
	private Integer floor;
	/**
	 * 门店id
	 */
	private Long shopId;
	/** 门店编码 */
	private String shopCode;
	/** 门店名称 */
	private String shopName;
	
	/** 排序 */
	private Integer weight;

}

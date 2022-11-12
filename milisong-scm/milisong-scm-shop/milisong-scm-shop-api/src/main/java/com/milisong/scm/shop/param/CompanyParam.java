package com.milisong.scm.shop.param;

import java.io.Serializable;

import com.farmland.core.api.PageParam;

import lombok.Getter;
import lombok.Setter;

/**
 * 公司信息查询条件
 * @author yangzhilong
 *
 */
@Getter
@Setter
public class CompanyParam extends PageParam implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 884284206356264024L;
	/**
	 * 公司名称
	 */
	private String companyAbbreviation;
	/**
	 * 楼宇id
	 */
	private Long buildingId;
	/**
	 * 状态
	 */
	private Boolean isSameName;
	/**
	 * 楼层
	 */
	private String floor;
}

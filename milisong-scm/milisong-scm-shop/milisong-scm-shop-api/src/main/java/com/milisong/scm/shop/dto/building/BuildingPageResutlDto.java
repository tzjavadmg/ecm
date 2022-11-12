package com.milisong.scm.shop.dto.building;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

/**
 * 楼宇信息分页查询结果dto
 * @author youxia 2018年9月2日
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class BuildingPageResutlDto extends BuildingDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 372761886123090841L;
	// 公司数量
	private Integer companyCount;
}

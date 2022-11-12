package com.milisong.scm.shop.dto.building;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * 楼宇申请的dto
 * @author yangzhilong
 *
 */
@Getter
@Setter
public class BuildingApplyDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9073534121119588433L;

	private Long buildingId;

    private Long userId;

    private String nikeName;

    private String mobile;

    private Date applyTime;
}
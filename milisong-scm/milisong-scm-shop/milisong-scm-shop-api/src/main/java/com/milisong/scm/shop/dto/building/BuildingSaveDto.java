package com.milisong.scm.shop.dto.building;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 楼宇保存的dto
 * @author yangzhilong
 *
 */
@Getter
@Setter
public class BuildingSaveDto extends BuildingDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1473748724866836859L;
	// 操作人
	private String updateUser;
}

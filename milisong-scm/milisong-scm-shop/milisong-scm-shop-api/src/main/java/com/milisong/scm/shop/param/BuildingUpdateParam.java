package com.milisong.scm.shop.param;

import java.io.Serializable;

import lombok.Data;

/**
*@author    created by benny
*@date  2018年10月9日---下午4:52:55
*
*/
@Data
public class BuildingUpdateParam implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2115022457856452207L;
	private Long id;
	private Integer weight;
}

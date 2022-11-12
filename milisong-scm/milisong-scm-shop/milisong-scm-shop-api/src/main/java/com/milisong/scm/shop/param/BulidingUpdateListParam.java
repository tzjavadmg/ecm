package com.milisong.scm.shop.param;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
*@author    created by benny
*@date  2018年10月10日---下午6:12:09
*
*/
@Data
public class BulidingUpdateListParam implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7453990337650261321L;
	List<BuildingUpdateParam> list;
}

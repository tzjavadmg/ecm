package com.milisong.scm.orderset.dto.param;

import java.io.Serializable;
import java.util.List;

import com.farmland.core.api.PageParam;

import lombok.Getter;
import lombok.Setter;

/**
 * 集单查询的查询参数
 * @author yangzhilong
 *
 */
@Getter
@Setter
public class OrderSetSearchParam extends PageParam implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3275483883051793189L;
	
	// 查询日期 yyyy-mm-dd格式
	private String date;
	// 门店id，不传则查所有
	private Long shopId;
	// 配送单号(目前仅仅用于查询下面的list集合)
	private String distributionNo;
	// 集单编号list
	private List<String> orderSetNo;
}

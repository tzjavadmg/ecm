package com.milisong.scm.orderset.dto.param;

import java.io.Serializable;

import com.farmland.core.api.PageParam;

import lombok.Getter;
import lombok.Setter;

/**
 * 配送单查询的查询参数
 * @author yangzhilong
 *
 */
@Getter
@Setter
public class DistributionSearchParam extends PageParam implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3275483883051793189L;
	
	// 查询日期 yyyy-mm-dd格式
	private String beginDate;
	// 查询日期 yyyy-mm-dd格式
	private String endDate;
	// 门店id，不传则查所有
	private Long shopId;
	// 是否打印了拣货单
	private Boolean isPrintPickList;
	// 是否打印了配送单
    private Boolean isPrintDistribution;
    // 配送单(C0001)
    private String distributionDescription; 
    // 配送单号（C201809140003）
    private String distributionNo;
}

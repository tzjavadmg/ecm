package com.mili.oss.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
*@author    created by benny
*@date  2018年10月10日---下午2:04:05
*
*/
@Data
public class DistributionOrdersetInfoResult implements Serializable{

	  /**
	 * 
	 */
	private static final long serialVersionUID = -3674629418810311820L;

	private String distributionNo;

	  private String distributionDescription;
	    
	  private String detailSetNo;

	  private String detailSetNoDescription;
	  
	  private String coupletNo;
	  
	  private String goodsName;
	  
	  private Integer goodsNumber;
	 
	  private List<OrderSetDetailGoodsDto> goodsInfo;
}

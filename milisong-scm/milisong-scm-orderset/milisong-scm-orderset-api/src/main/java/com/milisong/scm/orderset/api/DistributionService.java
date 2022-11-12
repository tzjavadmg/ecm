package com.milisong.scm.orderset.api;

import java.util.List;

import com.farmland.core.api.Pagination;
import com.milisong.scm.orderset.dto.param.DistributionSearchParam;
import com.milisong.scm.orderset.dto.param.OrderSearch4OrderSetParam;
import com.milisong.scm.orderset.dto.param.OrderSetReqDto;
import com.milisong.scm.orderset.dto.result.DistributionOrdersetInfoResult;
import com.milisong.scm.orderset.dto.result.DistributionOrdersetResult;
import com.milisong.scm.orderset.dto.result.DistributionQueryResult;
import com.milisong.scm.orderset.dto.result.OrderSetDetailResultDto;
import com.milisong.scm.orderset.dto.result.PickOrderPrintQueryResult;

/**
 * 配送单service
 * @author yangzhilong
 *
 */
public interface DistributionService {
	
	/**
	 * 分页查询配送单信息
	 * @param param
	 * @return
	 */
	public Pagination<DistributionQueryResult> pageSearch(DistributionSearchParam param); 
	
	/**
	 * 根据配送单号查询里面的集单明细
	 * @param distributionNo
	 * @return
	 */
	public List<DistributionOrdersetResult> listOrderSetNoByDistributionNo(String distributionNo);
	
	public PickOrderPrintQueryResult getPickOrderByPrint(String distributionNo);
	
	public List<DistributionOrdersetInfoResult> getCustomerOrderByDistributionNo(String distributionNo);
	
	/**
	 * 根据配送单号查询集单号
	 * @param distributionNo
	 * @return
	 */
	public List<String> getOrdersetNoByDistributionNo(String distributionNo);

	public List<Long> getOrdersetIdByDistributionNo(String distributionNo);

	public Pagination<OrderSetDetailResultDto> pageSearchOrderSet(OrderSetReqDto dto);

	public List<DistributionOrdersetInfoResult> getCustomerOrderByOrderSetNo(String setNo);
}

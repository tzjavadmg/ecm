package com.milisong.scm.orderset.api;

import java.util.List;

import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.milisong.scm.orderset.dto.mq.SfOrderMqDto;
import com.milisong.scm.orderset.dto.param.InterceptConfigDto;
import com.milisong.scm.orderset.dto.param.OrderSetSearchParam;
import com.milisong.scm.orderset.dto.param.UpdateOrderSetStatusParam;
import com.milisong.scm.orderset.dto.result.NotifyOrderSetQueryResult;
import com.milisong.scm.orderset.dto.result.OrderSetDetailDto;
import com.milisong.scm.orderset.dto.result.OrderSetSearchResult;
import com.milisong.scm.orderset.dto.result.OrderSetStatusQueryResult;

/**
 * 集单相关的服务
 * @author yangzhilong
 *
 */
public interface OrderSetService {
	/**
	 * 处理门店的集单信息
	 * @param shopId
	 */
	public void executeShopSet(Long shopId, InterceptConfigDto config);
	
	/**
	 * 根据订单号查询集单的处理状态
	 * @param orderNo
	 */
	public OrderSetStatusQueryResult queryStatusByOrderNo(String orderNo);
	
	/**
	 * 分页查询集单信息
	 * @param param
	 * @return
	 */
	public Pagination<OrderSetSearchResult> pageSearch(OrderSetSearchParam param);
	
	/**
	 * 更改集单的状态
	 * @param param
	 */
	public ResponseResult<Object> updateStatus(UpdateOrderSetStatusParam param);
	
	/**
	 * 批量更改集单的状态
	 * @param detailIds
	 * @return
	 */
	public ResponseResult<Object> updateStatusByIds(List<Long> detailIds,String updateBy,Integer status);
	
	/**
	 * 根据子集单号查询集单号里的详细信息
	 * @param detailSetNo
	 */
	public NotifyOrderSetQueryResult getOrderSetInfoByDetailSetNo(String detailSetNo, Long detailSetId);
	
	/**
	 * 更改集单的打印状态
	 * @param param
	 */
	public ResponseResult<Object> updatePrintStatus(UpdateOrderSetStatusParam param);
	
	/**
	 * 更新集单的配送状态
	 * @param param
	 */
	void updateDistributionStatus(SfOrderMqDto param);
	
	/**
	 * 发送门店的集单信息给POS系统进行生产
	 * @param shopId
	 * @param isCompensate 是否补偿
	 */
	void sendOrderSetMq(Long shopId, boolean isCompensate);

	/**
	 * 根据配送订单编号查询集单信息
	 * @return
	 */
	List<OrderSetDetailDto> getOrderSetByOrderNo(String orderNo);
}

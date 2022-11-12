package com.milisong.breakfast.scm.orderset.api;

import java.util.List;

import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.milisong.breakfast.scm.configuration.dto.ShopInterceptConfigDto;
import com.milisong.breakfast.scm.orderset.dto.param.OrderSetSearchParam;
import com.milisong.breakfast.scm.orderset.dto.param.UpdateOrderSetStatusParam;
import com.milisong.breakfast.scm.orderset.dto.result.NotifyOrderSetQueryResult;
import com.milisong.breakfast.scm.orderset.dto.result.OrderSetDetailDto;
import com.milisong.breakfast.scm.orderset.dto.result.OrderSetDetailResultDto;
import com.milisong.breakfast.scm.orderset.dto.result.OrderSetProductionMsgByPrint;
import com.milisong.breakfast.scm.orderset.dto.result.OrdersetInfoResult;
import com.milisong.dms.dto.shunfeng.DeliveryOrderMqDto;

/**
 * 集单相关的服务
 * @author yangzhilong
 *
 */
public interface OrderSetService {
	/**
	 * 分页查询集单列表
	 * @param param
	 * @return
	 */
	public Pagination<OrderSetDetailResultDto> pageSearch(OrderSetSearchParam param);
	
	/**
	 * 根据集单号查询集单的详情
	 * @param setNo
	 * @return
	 */
	public List<OrdersetInfoResult> getDetailByOrderSetNo(String setNo);
	
	/**
	 * 更新集单的配送状态（顺丰回调MQ调用一级入口）
	 * @param param
	 */
	void updateDistributionStatus(DeliveryOrderMqDto param);
		
	/**
	 * 更改集单的状态（顺丰回调MQ调用二级入口）
	 * @param param
	 */
	public ResponseResult<Object> updateStatus(UpdateOrderSetStatusParam param);
	
	/**
	 * 根据子集单号查询集单号里的详细信息（ECM通知用户用）
	 * @param detailSetNo
	 */
	public NotifyOrderSetQueryResult getOrderSetInfoByDetailSetNo(String detailSetNo, Long detailSetId);
		
	/**
	 * 发送门店的集单信息给POS系统进行生产
	 * @param shopId
	 * @param isCompensate 是否补偿
	 */
	void sendOrderSetMq(Long shopId, boolean isCompensate);

	/**
	 * 根据配送订单编号查询集单信息(C端根据订单查询配送信息用)
	 * @return
	 */
	List<OrderSetDetailDto> getOrderSetByOrderNo(String orderNo);

	public OrderSetProductionMsgByPrint getOrderSetMqBySetNo(String setNo);

	/**
	 * 推送顺丰订单
	 * @param configDto
	 */
	void pushSfOrderSetMq(ShopInterceptConfigDto configDto);
}

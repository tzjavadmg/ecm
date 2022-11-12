package com.milisong.pos.production.api;

import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.milisong.pos.production.dto.result.OrderSetInfoResult;
import com.milisong.pos.production.param.PosProductionParam;

/**
*@author    created by benny
*@date  2018年10月25日---下午9:01:34
* 门店订单生产服务
*/
public interface PosProductionService {

	/**
	 * 保存平台推送过来的集单信息
	 * @param msg
	 */
	public Long saveOrderSetInfo(String msg);
	
	/**
	 * 校验集单的顺序
	 * @param msg
	 */
	public boolean checkOrderSetSequence(String msg);
	
	/**
	 * 通知进行生产
	 */
	public void notifyProduction(Long shopId);
	
	/**
	 * 倒计时
	 * @param msg
	 */
	public void countdown(String msg);
	
	/**
	 * 根据集单号修改集单状态
	 */
	public ResponseResult<String> updateOrderSetStatusByNo(Long shopId,String orderSetNo,Byte orderStatus,String updateBy);
	
	/**
	 * 根据门店查询集单状态
	 * @param shopId
	 * @param orderStatus
	 */
	public Pagination<OrderSetInfoResult> getOrderSetListByStatus(PosProductionParam posProductionParam);

	/**
	 * 获取当前门店生产状态
	 * @param shopId
	 * @return
	 */
	public Integer getShopProductionStatus(Long shopId);

	
	/**
	 * 暂停或者恢复
	 * @param shopId
	 * @param opUser 操作人
	 */
	void pauseOrRestart(Long shopId, String opUser);
}

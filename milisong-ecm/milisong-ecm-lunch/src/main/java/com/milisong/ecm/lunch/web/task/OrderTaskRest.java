package com.milisong.ecm.lunch.web.task;

import com.esotericsoftware.minlog.Log;
import com.farmland.core.api.ResponseResult;
import com.milisong.oms.api.OrderService;
import com.milisong.oms.api.RefundOrderService;
import com.milisong.oms.api.SalesService;
import com.milisong.oms.api.VirtualOrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/v1/order/task")
public class OrderTaskRest {

	@Resource
	private OrderService orderService;
	
	@Resource
	private VirtualOrderService virtualOrderService;
    
    @Resource
    private SalesService salesService;
    
    @Resource
    private RefundOrderService refundOrderService;
	
	@GetMapping(value = "/expired-cancel-order")
	public void expiredCancelOrder() {
		ResponseResult<?> responseResult = orderService.cancelToPayOrder();
		if (!responseResult.isSuccess()) {
			Log.warn("取消待支付订单失败");
		}
	}

	@GetMapping(value = "/stat-lately29-days-sales")
	public void statLately29DaysSales() {
		salesService.statLately29DaysSales();
	}
	
	@GetMapping(value = "/expired-cancel-virtual-order")
	public void expiredCancelVirtualOrder() {
		ResponseResult<?> responseResult = virtualOrderService.cancelUnPayVirtualOrder();
		if (!responseResult.isSuccess()) {
			Log.warn("取消待生成虚拟订单失败");
		}
	}
	
		
	/**
	 * 查询待支付订单
	 */
	@GetMapping(value = "/get-unpay-order")
	public void getUnPayOrder() {
		orderService.getUnPayOrder();
	}
	
	/**
	 * 查询退款中订单
	 */
	@GetMapping(value = "get-refunding-order")
	public void getRefundingOrder() {
		refundOrderService.getRefundingOrder();
	}
}

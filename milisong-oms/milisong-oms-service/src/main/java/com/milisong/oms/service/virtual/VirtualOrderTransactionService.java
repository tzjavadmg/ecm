package com.milisong.oms.service.virtual;

import com.farmland.core.api.ResponseResult;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/11/8 18:39
 */
public interface VirtualOrderTransactionService {
    ResponseResult<?> cancelVirtualOrder(Long orderId);
}

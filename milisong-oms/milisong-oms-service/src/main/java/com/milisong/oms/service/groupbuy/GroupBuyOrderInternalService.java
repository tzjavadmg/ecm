package com.milisong.oms.service.groupbuy;

import com.farmland.core.api.ResponseResult;
import com.milisong.oms.domain.GroupBuyOrder;
import com.milisong.oms.param.PaymentResultParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.SortedMap;

/**
 * @description: TODO
 * @program: milisong-workspace
 * @author: codyzeng@163.com
 * @date: 2019/5/20 19:42
 */
public interface GroupBuyOrderInternalService {

    void refundOrder(GroupBuyOrder groupBuyOrder);

    void refundCallback( SortedMap<String, String> map);

    void payCallback(PaymentResultParam paymentResultParam);

    void expiredCancelOrder(Long orderId);
}

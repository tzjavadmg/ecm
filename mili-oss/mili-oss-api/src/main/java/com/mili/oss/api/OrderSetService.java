package com.mili.oss.api;

import java.util.List;

import com.mili.oss.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;

/**
 * @author zhaozhonghui
 * @Description 集单接口
 * @date 2019-02-25
 */
@FeignClient("mili-oss-service")
public interface OrderSetService {
    /**
     * 分页查询集单
     * @param param
     * @return
     */
    @PostMapping("/orderset/page")
    ResponseResult<Pagination<OrderSetDetailResultDto>> pageSearchOrderSet(OrderSetReqDto param);

    /**
     * 根据集单编号获取详情
     * @param setNo
     * @return
     */
    @GetMapping("/orderset/get-customer-order-by-set-no")
    ResponseResult<List<DistributionOrdersetInfoResult>> getCustomerOrderByOrderSetNo(String setNo);

    /**
     * 根据订单号获取集单信息
     * @param orderNo
     * @return
     */
    @GetMapping("/orderset/get-order-set-by-order-no")
    ResponseResult<List<OrderSetDetailDto>> getOrderSetByOrderNo(String orderNo);

    /**
     * 根据集单编号、id获取集单信息
     * @param detailSetNo
     * @param detailSetId
     * @return
     */
    @GetMapping("/orderset/query-detail-order-info")
    ResponseResult<NotifyOrderSetQueryResult> getDetailSetInfoByDetailNo(String detailSetNo, Long detailSetId);

    @GetMapping("/orderset/get-orderset-info")
    NotifyOrderSetQueryResult  getOrderSetMqBySetNo(String setNo);

    @GetMapping("/orderset/get-bf-orderset-info")
    OrderSetProductionMsgByPrint  getBFOrderSetMqBySetNo(String setNo);


}

package com.mili.oss.api;

import java.util.List;

import com.mili.oss.dto.result.ShopGoodsCount;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.mili.oss.dto.OrderReserveSearchResultDto;
import com.mili.oss.dto.OrderSearchResult;
import com.mili.oss.dto.OrderSearchResultPos;
import com.mili.oss.dto.param.OrderGoodsSumParam;
import com.mili.oss.dto.param.OrdeUserInfoParam;
import com.mili.oss.dto.param.OrderReserveSearchParamDto;
import com.mili.oss.dto.param.OrderSearchParam;
import com.mili.oss.dto.param.OrderSearchParamPos;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zhaozhonghui
 * @Description ${Description}
 * @date 2019-02-27
 */
@FeignClient("mili-oss-service")
public interface OrderInsideService {

    /**
     * 分页查询订单
     *
     * @param orderSearchParam
     * @return
     */
    @PostMapping("/order/page")
    ResponseResult<Pagination<OrderSearchResult>> pageSearchOrder(OrderSearchParam orderSearchParam);

    @PostMapping("/order/page-reserve-group-list")
    ResponseResult<Pagination<OrderReserveSearchResultDto>> pageSearchReserveGroupInfoList(OrderReserveSearchParamDto dto);

    @GetMapping("/order/check")
    ResponseResult<List<String>> getTodayOrderList();

    /**
     * 查询pos订单详情
     * @param param
     * @return
     */
    @PostMapping("/order/pos/page")
    ResponseResult<Pagination<OrderSearchResultPos>> getOrderSetPage(OrderSearchParamPos param);
   
    /**
     * 查询订单商品已售量
     * @param param
     * @return
     */
    @PostMapping("/order/goods-count")
    ResponseResult<Integer> getOrderGoodsCount(OrderGoodsSumParam param);

    /***
     * 设置缺货
     * @param orderNo
     * @return
     */
    @PostMapping("/order/pos/out-of-stock")
    ResponseResult setOutOfStock(List<OrdeUserInfoParam> userInfoParams);

    /**
     * 07：30发送之前未发送的缺货通知短信
     */
    @PostMapping("/order/pos/send-out-of-msg")
    void sendOutOfStockMsgTask();

    /**
     * 根据门店code和公司id查询商品数量
     * @param shopCode
     * @param companyId
     * @return
     */
    @GetMapping("/order/shop-goods-count")
    ResponseResult<List<ShopGoodsCount>> getShopGoodsCount(@RequestParam("shopCode")String shopCode,@RequestParam("companyId") Long companyId);
}

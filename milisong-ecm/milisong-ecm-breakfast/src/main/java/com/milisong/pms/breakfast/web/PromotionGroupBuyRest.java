package com.milisong.pms.breakfast.web;

import com.farmland.core.api.ResponseResult;
import com.milisong.pms.breakfast.service.RestGroupBuyService;
import com.milisong.pms.prom.dto.groupbuy.GroupBuyEntryResponse;
import com.milisong.pms.prom.dto.groupbuy.GroupBuyRequest;
import com.milisong.pms.prom.dto.groupbuy.GroupBuyResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 拼团接口
 *
 * @author sailor wang
 * @date 2018/9/14 下午7:32
 * @description
 */
@Slf4j
@RestController
@RequestMapping("/v1/breakfast/")
public class PromotionGroupBuyRest {

    @Autowired
    RestGroupBuyService groupBuyService;

    /**
     * 拼团入口
     * @return
     */
    @PostMapping("query-groupbuy-entry")
    public ResponseResult<GroupBuyEntryResponse> queryGroupBuyEntry(@RequestBody GroupBuyRequest request){
        return groupBuyService.queryGroupBuyEntry(request);
    }

    /**
     * 点击拼团入口，查询自己的拼团详情
     *
     * @param request
     * @return
     */
    @PostMapping("query-self-groupbuy")
    public ResponseResult<GroupBuyResponse> querySelfGroupbuy(@RequestBody GroupBuyRequest request){
        return groupBuyService.querySelfJoinedGroupBuy(request);
    }

    /**
     * 点击分享拼团入口，查询拼团详情
     *
     * @param request
     * @return
     */
    @PostMapping("query-shared-groupbuy")
    public ResponseResult<GroupBuyResponse> querySharedgroupbuy(@RequestBody GroupBuyRequest request){
        return groupBuyService.querySharedGroupBuy(request);
    }

}
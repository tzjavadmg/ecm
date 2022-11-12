package com.milisong.breakfast.pos.web;

import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.milisong.breakfast.pos.api.GoodsSummaryService;
import com.milisong.breakfast.pos.dto.result.GoodsSummaryResult;
import com.milisong.breakfast.pos.param.GoodsSummaryParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhaozhonghui
 * @Description 商品汇总rest
 * @date 2018-12-04
 */
@Api(tags = "POS平台-商品汇总")
@RestController
@Slf4j
@RequestMapping("/goods-summary")
public class GoodsSummaryRest {

    @Autowired
    private GoodsSummaryService goodsSummaryService;

    @ApiOperation("分页查询商品汇总")
    @PostMapping("/page")
    public ResponseResult<Pagination<GoodsSummaryResult>> searchPage(@RequestBody GoodsSummaryParam param){
        return goodsSummaryService.searchPage(param);
    }

    @ApiOperation(("获取当天配送时间"))
    @PostMapping("/get-delivery-time")
    public ResponseResult<List<String>> getDeliveryTimeGroup(@RequestBody GoodsSummaryParam param){
        return goodsSummaryService.getDeliveryTimeGroup(param);
    }
}

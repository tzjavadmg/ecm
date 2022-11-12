package com.milisong.breakfast.pos.service;

import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.milisong.breakfast.pos.api.GoodsSummaryService;
import com.milisong.breakfast.pos.dto.result.GoodsSummaryResult;
import com.milisong.breakfast.pos.mapper.GoodsSummaryMapper;
import com.milisong.breakfast.pos.param.GoodsSummaryParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhaozhonghui
 * @Description
 * @date 2018-12-04
 */
@Service
@Slf4j
public class GoodsSummaryServiceImpl implements GoodsSummaryService {

    @Resource
    private GoodsSummaryMapper goodsSummaryMapper;

    @Override
    public ResponseResult<Pagination<GoodsSummaryResult>> searchPage(GoodsSummaryParam param) {
        List<GoodsSummaryResult> list = goodsSummaryMapper.selectPage(param);
        Long count = goodsSummaryMapper.selectCount(param);
        Pagination<GoodsSummaryResult> pagination = new Pagination<>();
        pagination.setTotalCount(count);
        pagination.setDataList(list);
        return ResponseResult.buildSuccessResponse(pagination);
    }

    @Override
    public ResponseResult<List<String>> getDeliveryTimeGroup(GoodsSummaryParam param) {
        List<String> deliveryTimes = goodsSummaryMapper.selectDeliveyTimeGroup(param);
        return ResponseResult.buildSuccessResponse(deliveryTimes);
    }
}

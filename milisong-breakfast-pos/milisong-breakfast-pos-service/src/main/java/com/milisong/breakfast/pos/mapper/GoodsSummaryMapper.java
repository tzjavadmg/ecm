package com.milisong.breakfast.pos.mapper;


import com.milisong.breakfast.pos.dto.result.GoodsSummaryResult;
import com.milisong.breakfast.pos.param.GoodsSummaryParam;

import java.util.List;

public interface GoodsSummaryMapper extends BaseGoodsSummaryMapper{

    List<GoodsSummaryResult> selectPage(GoodsSummaryParam param);

    Long selectCount(GoodsSummaryParam param);

    List<String> selectDeliveyTimeGroup(GoodsSummaryParam param);
}
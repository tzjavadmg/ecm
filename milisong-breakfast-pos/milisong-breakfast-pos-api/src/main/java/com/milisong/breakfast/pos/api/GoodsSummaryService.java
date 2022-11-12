package com.milisong.breakfast.pos.api;

import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.milisong.breakfast.pos.dto.result.GoodsSummaryResult;
import com.milisong.breakfast.pos.param.GoodsSummaryParam;

import javax.xml.ws.Response;
import java.util.List;

/**
 * @author zhaozhonghui
 * @Description ${Description}
 * @date 2018-12-04
 */
public interface GoodsSummaryService {

    ResponseResult<Pagination<GoodsSummaryResult>> searchPage(GoodsSummaryParam param);

    ResponseResult<List<String>> getDeliveryTimeGroup(GoodsSummaryParam param);
}

package com.milisong.ecm.breakfast.goods.api;

import com.farmland.core.api.ResponseResult;
import com.milisong.ecm.breakfast.dto.GoodsDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <pre>
 *    author  : Tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/9/3   11:37
 *    desc    : 门店业务接口
 *    version : v1.0
 * </pre>
 */
public interface ShopService {

    /**
     * 查询门店商品的价格
     * @param shopCode
     * @param goodsCode
     * @return
     */
	@PostMapping("/v1/ShopService/getGoodsInfo")
    ResponseResult<GoodsDto> getGoodsInfo(@RequestParam("goodsCode")String goodsCode);

}

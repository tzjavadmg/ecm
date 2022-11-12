package com.milisong.ecm.lunch.web.home;

import com.farmland.core.api.ResponseResult;
import com.farmland.core.util.BeanMapper;
import com.milisong.ecm.common.api.ShopConfigService;
import com.milisong.ecm.lunch.goods.api.ShopDisplayDto;
import com.milisong.ecm.lunch.service.LunchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>
 *    author  : tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2019/4/12   16:36
 *    desc    :
 *    version : v1.0
 * </pre>
 */
@Slf4j
@RestController
@RequestMapping("/lc")
public class HomeRestLC {

    @Autowired
    private LunchService lunchService;

    @Autowired
    private ShopConfigService shopConfigService;

    /**从GoodsRest移过来，url暂时不该，后面再改
     * 获取门店信息，首页展示（banner图，地址等信息）
     *
     * @param buildId
     * @return
     */
    @GetMapping("/v1/goods/shop-info")
    public ResponseResult<ShopDisplayDto> shopInfo(@RequestParam(value = "buildId", required = false) Long buildId) {
        log.info("获取公司及配置信息,companyId={}", buildId);
        ResponseResult<?> responseResult = lunchService.shopInfo(buildId);

        ResponseResult<ShopDisplayDto> result = ResponseResult.build(responseResult.isSuccess(),responseResult.getCode(),responseResult.getMessage());
        if (responseResult.getData() != null){
            result.setData(BeanMapper.map(responseResult.getData(),ShopDisplayDto.class));
            result.getData().setPictureUrl(shopConfigService.getSharePicture());
            result.getData().setTitle(shopConfigService.getShareTitle());
        }
        return result;
    }

}

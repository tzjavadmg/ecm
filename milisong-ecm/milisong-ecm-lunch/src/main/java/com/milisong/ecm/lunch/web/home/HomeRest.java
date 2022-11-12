package com.milisong.ecm.lunch.web.home;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.farmland.core.util.BeanMapper;
import com.milisong.ecm.common.api.ShopConfigService;
import com.milisong.ecm.common.dto.BannerDto;
import com.milisong.ecm.common.dto.ShopConfigDto;
import com.milisong.ecm.common.enums.StatusConstant;
import com.milisong.ecm.common.util.RedisKeyUtils;
import com.milisong.ecm.lunch.dto.CompanyDto;
import com.milisong.ecm.lunch.goods.constants.BuildingConstant;
import com.milisong.ecm.lunch.goods.dto.ShopDisplayDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

/**
 * <pre>
 *    author  : tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2019/2/21   13:54
 *    desc    : 主页信息接口
 *    version : v1.0
 * </pre>
 */
@Slf4j
@RestController
public class HomeRest {

    @Autowired
    private ShopConfigService shopConfigService;

    /**从GoodsRest移过来，url暂时不该，后面再改
     * 获取门店信息，首页展示（banner图，地址等信息）
     *
     * @param buildId
     * @return
     */
    @GetMapping("/v1/goods/shop-info")
    ResponseResult<ShopDisplayDto> shopInfo(@RequestParam(value = "buildId", required = false) Long buildId) {
        log.info("获取公司及配置信息,companyId={}", buildId);
        CompanyDto companyDto = null;
        String shopCode = null;
        Short buildingStatus = null;
        ShopDisplayDto displayDto = new ShopDisplayDto();
        if(buildId != null){
            String companyKey = RedisKeyUtils.getCompanyKey(buildId);
            String companyStr = RedisCache.get(companyKey);
            companyDto = JSONObject.parseObject(companyStr, CompanyDto.class);
            if (companyDto != null) {
                shopCode = companyDto.getShopCode();
                buildingStatus = dealStatusForTemp(companyDto.getOpenStatus());//公司状态
                displayDto.setName(companyDto.getName()); //公司名称
                displayDto.setCode(shopCode);//门店code
            }
        }else{
            //用户没有对应的门店信息时，展示默认门店
            shopCode = RedisKeyUtils.getDefaultLunchShopCode();
            buildingStatus = Short.valueOf(Integer.toString(BuildingConstant.BuildingStatusEnum.OPENED.getValue()));
            displayDto.setDefaultShopCode(shopCode);
        }
        List<BannerDto> bannerDtos = shopConfigService.getBanner(shopCode);
        List<BannerDto> bannerList = BeanMapper.mapList(bannerDtos, BannerDto.class);
        bannerList.sort(Comparator.comparingInt(BannerDto::getWeight).reversed());
        displayDto.setBuildingStatus(buildingStatus);
        ShopConfigDto.DeliveryTimezone  deliveryTimezone = shopConfigService.getLastDeliveryTimezone(shopCode);
        displayDto.setInterceptingTime(deliveryTimezone.getCutoffTime());//截单时间
        displayDto.setBannerList(bannerList);//banner图片地址，链接URL
        displayDto.setDeliveryCostAmount(shopConfigService.getDeliveryOriginalPrice(shopCode));//配送费
        displayDto.setDeliveryCostDiscountAmount(shopConfigService.getDeliveryDiscountPrice(shopCode));//配送费优惠价
        displayDto.setPictureUrl(shopConfigService.getSharePicture());//小程序分享图片
        displayDto.setTitle(shopConfigService.getShareTitle());//小程序分享文案
        displayDto.setPackageAmount(shopConfigService.getPackageOriginalPrice(shopCode));//包装费
        displayDto.setPackageDiscountAmount(shopConfigService.getPackageDiscountPrice(shopCode));//包装费优惠价
        displayDto.setGoodsPosterUrl(shopConfigService.getGoodsPosterUrl());//餐品海报
        log.info("门店配置信息：{}", JSON.toJSONString(displayDto));
        return ResponseResult.buildSuccessResponse(displayDto);
    }

    /**
     * 临时处理将公司的状态对应到午餐原楼宇状态上
     * 早餐公司：开通状态 1开通 0关闭
     * 午餐原：楼宇状态0未计划 2待开通 3关闭 9已开通
     * @param companyStatus
     * @return
     */
    private Short dealStatusForTemp(Byte companyStatus) {
        Integer status = null;
        if(companyStatus!=null
                && companyStatus.intValue() == StatusConstant.CompanyStatusEnum.OPENED.getValue()){
            status = BuildingConstant.BuildingStatusEnum.OPENED.getValue();
        }else{
            status = BuildingConstant.BuildingStatusEnum.PREPARE_OPEN.getValue();
        }
        return status.shortValue();
    }

}

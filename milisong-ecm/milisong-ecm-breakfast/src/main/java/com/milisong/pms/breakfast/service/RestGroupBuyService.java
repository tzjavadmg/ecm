package com.milisong.pms.breakfast.service;

import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.milisong.ecm.breakfast.dto.*;
import com.milisong.ecm.breakfast.service.RestUserService;
import com.milisong.ecm.common.notify.constant.BusinessLineEnum;
import com.milisong.ecm.common.util.RedisKeyUtils;
import com.milisong.oms.api.ShopStockService;
import com.milisong.pms.prom.api.GroupBuyService;
import com.milisong.pms.prom.dto.groupbuy.*;
import com.milisong.ucs.sdk.security.UserContext;
import com.milisong.wechat.miniapp.enums.BusinessLine;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

import static com.milisong.pms.prom.enums.PromotionResponseCode.REQUEST_PARAM_EMPTY;

/**
 * @author sailor wang
 * @date 2019/5/17 7:17 PM
 * @description
 */
@Slf4j
@Service
public class RestGroupBuyService {

    @Autowired
    private GroupBuyService groupBuyService;

    @Resource
    private ShopStockService shopStockService;

    @Autowired
    private RestUserService restUserService;

    /**
     * 查询拼团入口
     *
     * @return
     */
    public ResponseResult<GroupBuyEntryResponse> queryGroupBuyEntry(GroupBuyRequest request) {
        if (StringUtils.isBlank(request.getShopCode())) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        request.setUserId(UserContext.getCurrentUser().getId());

        return groupBuyService.groupBuyEntry(request);
    }

    /**
     * 点击拼团入口，查询自己的拼团详情
     *
     * @param request
     * @return
     */
    public ResponseResult<GroupBuyResponse> querySelfJoinedGroupBuy(GroupBuyRequest request) {
        //缓存formId，用于拼团通知
        if (StringUtils.isNotBlank(request.getFormId())) {
            log.info("缓存formId，用于拼团通知,formId={}", request.getFormId());
            RedisKeyUtils.setJoinGroupBuyFormIdValue(UserContext.getCurrentUser().getOpenId(), request.getFormId());
        }

        if (request.getActivityGroupBuyId() == null || StringUtils.isBlank(request.getShopCode())) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        request.setUserId(UserContext.getCurrentUser().getId());
        ResponseResult<GroupBuyResponse> response = groupBuyService.querySelfJoinedGroupBuy(request);
        GroupBuyResponse groupBuyResponse = response.getData();
        if (response.isSuccess() && groupBuyResponse != null && groupBuyResponse.getUserGroupBuyDetail() != null) {
            String goodsCode = groupBuyResponse.getUserGroupBuyDetail().getGoodsCode();
            if (StringUtils.isNotBlank(goodsCode)) {
                //添加商品信息
                fillGoodsData(groupBuyResponse.getUserGroupBuyDetail(), request.getShopCode(), goodsCode);
            }
        }
        return response;
    }


    /**
     * 查询分享的拼团详情
     *
     * @param request
     * @return
     */
    public ResponseResult<GroupBuyResponse> querySharedGroupBuy(GroupBuyRequest request) {
        if (request.getUserGroupBuyId() == null){
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(),REQUEST_PARAM_EMPTY.message());
        }
        String shopCode = getShopCode(null);
        Long userId = UserContext.getCurrentUser().getId();
        request.setUserId(userId);
        request.setShopCode(shopCode);

        ResponseResult<GroupBuyResponse> response = groupBuyService.queryOtherJoinedGroupBuy(request);
        GroupBuyResponse groupBuyEntry = response.getData();
        if (response.isSuccess() && groupBuyEntry != null && groupBuyEntry.getUserGroupBuyDetail() != null) {
            String goodsCode = groupBuyEntry.getUserGroupBuyDetail().getGoodsCode();
            if (StringUtils.isNotBlank(goodsCode)) {
                //添加商品信息
                fillGoodsData(groupBuyEntry.getUserGroupBuyDetail(), shopCode, goodsCode);
            }
            //添加公司信息
            fillCompanyData(groupBuyEntry);
        }
        return response;
    }

    /**
     * 取消拼团并退款
     */
    public void refundGroupBuy() {
        groupBuyService.refundGroupBuy();
    }

    /**
     * 填充商品信息
     *
     * @param data
     * @param shopCode
     * @param goodsCode
     */
    private void fillGoodsData(Object data, String shopCode, String goodsCode) {
        try {
            String goodsBasicKey = RedisKeyUtils.getGoodsBasicKey(goodsCode);
            String goodsBasic = RedisCache.get(goodsBasicKey);
            if (StringUtils.isBlank(goodsBasic)) {
                log.info("fillGoodsData 商品为空, shopCode -> {}, goodsCode -> {}", shopCode, goodsCode);
                return;
            }
            log.info("商品信息 goodsBasic -> {}", goodsBasic);
            //商品
            GoodsDto goodsDto = JSONObject.parseObject(goodsBasic, GoodsDto.class);


            if (data instanceof ActivityGroupBuyDto) {
                ActivityGroupBuyDto activityGroupBuy = (ActivityGroupBuyDto) data;
                if (StringUtils.isNoneBlank(goodsBasic)) {
                    //商品库存
                    Integer stock = goodsStock(goodsDto, shopCode, goodsCode, activityGroupBuy.getDeliveryDate());

                    activityGroupBuy.setLeftStock(stock != null && stock <= 0 ? 0 : stock);
                    activityGroupBuy.setGoodsPic(goodsDto.getPicture());
                    activityGroupBuy.setGoodsDesc(goodsDto.getDescribe());
                    activityGroupBuy.setGoodsName(goodsDto.getName());
                    activityGroupBuy.setGoodsPrice(goodsDto.getPreferentialPrice()!=null?goodsDto.getPreferentialPrice():goodsDto.getAdvisePrice());
                }
            } else if (data instanceof UserGroupBuyDto) {
                UserGroupBuyDto userGroupBuy = (UserGroupBuyDto) data;
                log.info("拼团入口 goodsBasic -> {}", goodsBasic);
                if (StringUtils.isNotBlank(goodsBasic)) {
                    //商品库存
                    Integer stock = goodsStock(goodsDto, shopCode, goodsCode, userGroupBuy.getDeliveryDate());

                    userGroupBuy.setLeftStock(stock != null && stock <= 0 ? 0 : stock);
                    userGroupBuy.setGoodsPic(goodsDto.getPicture());
                    userGroupBuy.setGoodsDesc(goodsDto.getDescribe());
                    userGroupBuy.setGoodsName(goodsDto.getName());
                    userGroupBuy.setGoodsPrice(goodsDto.getPreferentialPrice()!=null?goodsDto.getPreferentialPrice():goodsDto.getAdvisePrice());
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    private void fillCompanyData(GroupBuyResponse groupBuyResponse) {
        try {
            Long companyId = groupBuyResponse.getCompanyId();
            CompanyBucketDto companyBucket = companyInfo(companyId);
            if (companyBucket != null) {
                groupBuyResponse.setCompanyName(companyBucket.getName());
            }
            String shopCode = getShopCode(companyId);
            groupBuyResponse.setShopCode(shopCode);

            ResponseResult<BfUserAddressDto> result = restUserService.getDeliveryAddr(null, BusinessLine.BREAKFAST.getCode());
            if (result.isSuccess() && result.getData() != null) {
                groupBuyResponse.setUserCompanyId(result.getData().getDeliveryOfficeBuildingId());
                groupBuyResponse.setUserCompanyName(result.getData().getDeliveryCompany());
                shopCode = getShopCode(result.getData().getDeliveryOfficeBuildingId());
                groupBuyResponse.setUserShopCode(shopCode);
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    private Integer goodsStock(GoodsDto goodsDto, String shopCode, String goodsCode, Date deliveryDate) {
        Integer stock = null;
        if (Boolean.TRUE.equals(goodsDto.getIsCombo())) {
            if (CollectionUtils.isNotEmpty(goodsDto.getListGoodsCombinationRefMqDto())) {
                for (GoodsCombinationRefDto goodsCombinationRef : goodsDto.getListGoodsCombinationRefMqDto()) {
                    //库存
                    Integer singleStock = shopStockService.getGoodsStock(shopCode, deliveryDate.getTime(), goodsCombinationRef.getSingleCode());
                    log.info("单拼库存 singleCode -> {}, singleStock -> {}", goodsCombinationRef.getSingleCode(), singleStock);
                    if (stock == null) {//取单拼最小值
                        stock = singleStock;
                    } else if (stock > singleStock) {
                        stock = singleStock;
                    }
                }
            }
        } else {
            //库存
            stock = shopStockService.getGoodsStock(shopCode, deliveryDate.getTime(), goodsCode);
        }
        return stock;
    }

    private CompanyBucketDto companyInfo(Long companyId) {
        log.info("获取公司信息,companyId=", companyId);
        try {
            String companyKey = RedisKeyUtils.getCompanyKey(companyId);
            String companyStr = RedisCache.get(companyKey);
            CompanyBucketDto companyBucketDto = JSONObject.parseObject(companyStr, CompanyBucketDto.class);
            //查询用户最后一次下单，如果是这个公司的话，那就取它的配送时间
            String deliveryAddressKey = RedisKeyUtils.getLatestDeliveryAddressKey(UserContext.getCurrentUser().getId());
            //用户进入首页从redis获取最后一次下单收货地址
            String deliveryAddress = RedisCache.get(deliveryAddressKey);
            if (StringUtils.isBlank(deliveryAddress)) {
                return companyBucketDto;
            }
            return companyBucketDto;
        } catch (Exception e) {
            log.error("获取公司信息异常", e);
        }
        return null;
    }

    private String getShopCode(Long companyId) {
        try {
            ResponseResult<BfUserAddressDto> result = restUserService.getDeliveryAddr(companyId, BusinessLineEnum.BREAKFAST.getVal());
            if (companyId == null) {
                if (result.isSuccess() && result.getData() != null) {
                    companyId = result.getData().getDeliveryOfficeBuildingId();
                }
            }
            if (companyId != null) {
                String companyKey = RedisKeyUtils.getCompanyKey(companyId);
                String companyStr = RedisCache.get(companyKey);
                CompanyDto companyDto = JSONObject.parseObject(companyStr, CompanyDto.class);
                if (companyDto != null) {
                    return companyDto.getShopCode();
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return RedisKeyUtils.getDefaultShopCode();
    }

}
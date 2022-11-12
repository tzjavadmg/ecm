package com.milisong.dms.api;

import com.milisong.dms.dto.httpdto.ShopDto;

import java.util.List;

/**
 * @author zhaozhonghui
 * @Description 门店接口
 * @date 2018-12-25
 */
public interface ShopService {
    /**
     * 根据id查询门店
     *
     * @param shopId
     * @return
     */
    ShopDto getShopById(String shopId);

    /**
     * 查询所有门店
     *
     * @return
     */
    List<ShopDto> queryShopList();

    /**
     * 通过MQ更新redis中的门店信息
     *
     * @param msg
     */
    void syncShopInfo(ShopDto shopDto);
}

package com.milisong.scm.shop.dto.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @description:全门店Banner配置响应Dto
 * @version  1.0
 * @author:  tianhaibo
 * @createTime: 2018-10-23 17:19:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopBannerParam extends BaseParam{

    private Long id;

    private Long shopId;

    public ShopBannerParam(Long shopId) {
        this.shopId = shopId;
    }
}
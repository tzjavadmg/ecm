package com.milisong.scm.shop.dto.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @description:单个门店属性配置查询Dto
 * @version  1.0
 * @author:  tianhaibo
 * @createTime: 2018-10-23 17:19:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopSingleConfigParam extends BaseParam{

    private Long id;

    private Long shopId;

    public ShopSingleConfigParam(Long shopId) {
        this.shopId = shopId;
    }

}
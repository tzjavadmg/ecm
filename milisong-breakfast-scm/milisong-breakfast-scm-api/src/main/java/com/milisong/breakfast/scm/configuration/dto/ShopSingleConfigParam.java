package com.milisong.breakfast.scm.configuration.dto;

import com.farmland.core.api.PageParam;
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
public class ShopSingleConfigParam extends PageParam {

    private Long id;

    private Long shopId;

    public ShopSingleConfigParam(Long shopId) {
        this.shopId = shopId;
    }

}
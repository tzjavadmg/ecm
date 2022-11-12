package com.milisong.breakfast.scm.configuration.dto;

import com.farmland.core.api.PageParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <pre>
 *    author  : Tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/10/25   11:37
 *    desc    : 门店截单查询dto
 *    version : v1.0
 * </pre>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopInterceptConfigParam extends PageParam {

    private Long id;

    private Long shopId;

    public ShopInterceptConfigParam(Long shopId) {
        this.shopId = shopId;
    }
}

package com.milisong.scm.base.dto;

import java.util.List;

import com.farmland.core.api.PageParam;

import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 *    author  : Administrator
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/11/7   18:36
 *    desc    : 门店信息条件查询dto
 *    version : v1.0
 * </pre>
 */
@Getter
@Setter
public class ShopParam extends PageParam {

    private List<Long> ids;

    private Byte status;

    private String name;

}

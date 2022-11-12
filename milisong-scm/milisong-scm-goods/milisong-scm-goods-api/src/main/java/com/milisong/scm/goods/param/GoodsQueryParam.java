package com.milisong.scm.goods.param;

import java.io.Serializable;

import com.farmland.core.api.PageParam;

import lombok.Getter;
import lombok.Setter;

/**
 * 商品列表查询条件
 * @author youxia 2018年9月6日
 */
@Getter
@Setter
public class GoodsQueryParam extends PageParam implements Serializable {

	private static final long serialVersionUID = 8242214387139874476L;
	
	private String name; // 商品名称
	
	private String code; // 商品编码

}

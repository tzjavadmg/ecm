package com.milisong.scm.goods.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.milisong.scm.goods.domain.GoodsCatalog;
import com.milisong.scm.goods.mapper.base.BaseGoodsCatalogMapper;

public interface GoodsCatalogExtMapper extends BaseGoodsCatalogMapper{

	GoodsCatalog getGoodsCatalog();
	
}
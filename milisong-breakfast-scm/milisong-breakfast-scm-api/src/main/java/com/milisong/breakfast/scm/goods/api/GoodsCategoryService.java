package com.milisong.breakfast.scm.goods.api;

import java.util.List;

import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.milisong.breakfast.scm.goods.dto.GoodsCategoryInfoDto;
import com.milisong.breakfast.scm.goods.param.GoodsCategoryParam;
import com.milisong.breakfast.scm.goods.param.GoodsCategoryQueryParam;

/**
*@author    created by benny
*@date  2018年12月7日---上午12:28:42
*
*/
public interface GoodsCategoryService {

	/**
	 * 查询所有类目
	 * @param type
	 * @return
	 */
	public ResponseResult<List<GoodsCategoryInfoDto>> getAll(byte type);
	
	/**
	 * 新增编辑类目
	 * @param goodsCategoryParam
	 * @return
	 */
	public ResponseResult<String> saveGoodsCategory(GoodsCategoryParam goodsCategoryParam);
	
	/**
	 * 分页查询类目
	 * @param param
	 * @return
	 */
	public ResponseResult<Pagination<GoodsCategoryInfoDto>> getGoodsCategoryPageInfo(GoodsCategoryQueryParam param); 

	/**
	 * 编辑、根据查询ID
	 * @param id
	 * @return
	 */
	public ResponseResult<GoodsCategoryInfoDto> getGoodsCategoryById(Long id);
}

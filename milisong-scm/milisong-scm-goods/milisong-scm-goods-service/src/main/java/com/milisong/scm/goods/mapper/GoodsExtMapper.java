package com.milisong.scm.goods.mapper;

import com.milisong.scm.goods.domain.Goods;
import com.milisong.scm.goods.dto.GoodsDto;
import com.milisong.scm.goods.mapper.base.BaseGoodsMapper;
import com.milisong.scm.goods.param.GoodsQueryParam;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GoodsExtMapper extends BaseGoodsMapper{
    
    /**
     * 统计商品列表数量
     * @return
     * youxia 2018年9月3日
     */
    int getGoodsListCount(GoodsQueryParam param);
    
    
    /**
     * 分页查询商品列表信息
     * @param pageSize
     * @param startRow
     * @return
     * youxia 2018年9月3日
     */
    List<GoodsDto> getGoodsList(GoodsQueryParam param);
    
    /**
     * 批量更新商品状态
     * @param list
     * @return
     * youxia 2018年9月4日
     */
    int updateGoodsStatusByBatch(List<Goods> list);
}
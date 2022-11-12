package com.milisong.oms.mapper.base;

import com.milisong.oms.domain.VirtualOrder;

public interface BaseVirtualOrderMapper {
    /**
     *
     * @mbg.generated 2019-05-21
     */
    int insert(VirtualOrder record);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    int insertSelective(VirtualOrder record);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    VirtualOrder selectByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    int updateByPrimaryKeySelective(VirtualOrder record);

    /**
     *
     * @mbg.generated 2019-05-21
     */
    int updateByPrimaryKey(VirtualOrder record);
}
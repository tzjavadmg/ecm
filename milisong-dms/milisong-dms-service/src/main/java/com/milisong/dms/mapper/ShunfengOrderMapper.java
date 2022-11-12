/* https://github.com/orange1438 */
package com.milisong.dms.mapper;

import com.milisong.dms.domain.ShunfengOrder;
import com.milisong.dms.domain.ShunfengOrderExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ShunfengOrderMapper {
    long countByExample(ShunfengOrderExample example);

    int deleteByExample(ShunfengOrderExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ShunfengOrder record);

    int insertSelective(ShunfengOrder record);

    List<ShunfengOrder> selectByExample(ShunfengOrderExample example);

    ShunfengOrder selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ShunfengOrder record, @Param("example") ShunfengOrderExample example);

    int updateByExample(@Param("record") ShunfengOrder record, @Param("example") ShunfengOrderExample example);

    int updateByPrimaryKeySelective(ShunfengOrder record);

    int updateByPrimaryKey(ShunfengOrder record);
}
/* https://github.com/orange1438 */
package com.milisong.dms.mapper;

import com.milisong.dms.domain.ShunfengOrderset;
import com.milisong.dms.domain.ShunfengOrdersetExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ShunfengOrdersetMapper {
    long countByExample(ShunfengOrdersetExample example);

    int deleteByExample(ShunfengOrdersetExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ShunfengOrderset record);

    int insertSelective(ShunfengOrderset record);

    List<ShunfengOrderset> selectByExample(ShunfengOrdersetExample example);

    ShunfengOrderset selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ShunfengOrderset record, @Param("example") ShunfengOrdersetExample example);

    int updateByExample(@Param("record") ShunfengOrderset record, @Param("example") ShunfengOrdersetExample example);

    int updateByPrimaryKeySelective(ShunfengOrderset record);

    int updateByPrimaryKey(ShunfengOrderset record);
}
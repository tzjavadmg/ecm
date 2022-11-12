/* https://github.com/orange1438 */
package com.milisong.dms.mapper;

import com.milisong.dms.domain.ShunfengDeliveryLog;
import com.milisong.dms.domain.ShunfengDeliveryLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ShunfengDeliveryLogMapper {
    long countByExample(ShunfengDeliveryLogExample example);

    int deleteByExample(ShunfengDeliveryLogExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ShunfengDeliveryLog record);

    int insertSelective(ShunfengDeliveryLog record);

    List<ShunfengDeliveryLog> selectByExample(ShunfengDeliveryLogExample example);

    ShunfengDeliveryLog selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ShunfengDeliveryLog record, @Param("example") ShunfengDeliveryLogExample example);

    int updateByExample(@Param("record") ShunfengDeliveryLog record, @Param("example") ShunfengDeliveryLogExample example);

    int updateByPrimaryKeySelective(ShunfengDeliveryLog record);

    int updateByPrimaryKey(ShunfengDeliveryLog record);
}
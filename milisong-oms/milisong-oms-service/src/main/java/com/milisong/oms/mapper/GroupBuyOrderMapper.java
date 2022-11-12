package com.milisong.oms.mapper;

import com.milisong.oms.domain.GroupBuyOrder;
import com.milisong.oms.mapper.base.BaseGroupBuyOrderMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @description: TODO
 * @program: milisong-workspace
 * @author: codyzeng@163.com
 * @date: 2019/5/20 16:19
 */
@Mapper
public interface GroupBuyOrderMapper extends BaseGroupBuyOrderMapper {

    @Select({"SELECT *",
            "FROM group_buy_order",
            "WHERE group_buy_id = #{groupBuyId} and status=2"})
    List<GroupBuyOrder> findPaidOrderListByGroupBuyId(Long groupBuyId);

    @Select({"SELECT *",
            "FROM group_buy_order",
            "WHERE group_buy_id = #{groupBuyId} and status in (0,2)"})
    List<GroupBuyOrder> findCancelableOrderListByGroupBuyId(Long groupBuyId);
}

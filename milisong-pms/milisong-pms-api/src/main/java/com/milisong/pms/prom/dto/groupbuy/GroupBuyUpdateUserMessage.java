package com.milisong.pms.prom.dto.groupbuy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 拼团消息通知
 *
 * @author sailor wang
 * @date 2019/5/22 2:11 PM
 * @description
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupBuyUpdateUserMessage implements Serializable {

    private static final long serialVersionUID = -5391163019732044065L;

    private List<Long> userIdList;

}
package com.milisong.pms.prom.dto.groupbuy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author sailor wang
 * @date 2019/5/22 1:08 PM
 * @description
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserGroupBuyRecordDto implements Serializable {
    private static final long serialVersionUID = 8509905821737383170L;

    private Long id;

    private Long userGroupBuyId;

    private Long userId;

    private String openId;

    private Long orderId;

    private Byte status;
}
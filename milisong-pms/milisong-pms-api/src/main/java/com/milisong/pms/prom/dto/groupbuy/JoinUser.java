package com.milisong.pms.prom.dto.groupbuy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author sailor wang
 * @date 2019/5/19 4:52 PM
 * @description
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JoinUser implements Serializable {
    private static final long serialVersionUID = 6097679438742174262L;
    private Long userId;
    private String openId;
    private String nickName;
    private String headPortraitUrl;
    private Long orderId;

}